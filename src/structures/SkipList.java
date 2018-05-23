package structures;

import java.util.ArrayList;
import java.util.Random;

/**
 * A probabilistic alternative to balanced trees. A list with log n complexity
 * of search, insert and delete
 * 
 * Implementation based on William Pugh paper: Skip Lists: A Probabilistic
 * Alternative to Balanced Trees
 * 
 * @author Matheus Rosa
 *
 * @param <K>
 */
public class SkipList<K extends Comparable<K>> implements DynamicSet<K> {

	private int maxLevel;
	private Node head;
	private Node nil;
	private final Random rand;

	public class Node {
		private K key;
		private ArrayList<Node> fowardPointers;

		public Node(K key) {
			this.key = key;
			fowardPointers = new ArrayList<>();
		}

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public ArrayList<Node> getList() {
			return this.fowardPointers;
		}
	}

	public SkipList(long seed) {
		head = new Node(null);
		nil = new Node(null);
		head.getList().add(nil);
		maxLevel = 0;
		rand = new Random(seed);
	}

	public SkipList() {
		this(System.currentTimeMillis());
	}

	@Override
	public K find(K key) {
		Node x = this.head;

		// loop invariant: x->key < search key
		for (int i = maxLevel; i >= 0; i--) {
			while (x.getList().get(i) != nil && x.getList().get(i).getKey().compareTo(key) < 0) {
				x = x.getList().get(i);
			}
		}

		x = x.getList().get(0);

		if (x != nil && x.getKey().compareTo(key) == 0) {
			return x.getKey();
		}
		return null;
	}

	@Override
	public void insert(K key) {
		ArrayList<Node> update = createArray(maxLevel+1);
		Node x = head;

		// search for the first node before key
		for (int i = maxLevel; i >= 0; i--) {
			while (x.getList().get(i) != nil && 
					x.getList().get(i).getKey().compareTo(key) < 0) {
				x = x.getList().get(i);
			}
			update.set(i, x);
		}

		x = x.getList().get(0);

		if (x != nil && x.getKey().compareTo(key) == 0)
			return;

		int v = 0; 						// number of levels for the new element
		while (rand.nextBoolean()) v++;

		v = Math.min(v, maxLevel+1);

		if (v > maxLevel) {
			head.getList().add(nil);
			update.add(head);
			maxLevel = v;
		}

		Node newNode = new Node(key);

		for (int i = 0; i <= v; i++) {
			newNode.getList().add(update.get(i).getList().get(i));
			update.get(i).getList().set(i, newNode);
		}
	}

	@Override
	public void remove(K key) {
		ArrayList<Node> update = createArray(maxLevel+1);

		Node x = head;
		// search for the first node before key
		for (int i = maxLevel; i >= 0; i--) {
			while (x.getList().get(i) != nil && 
					x.getList().get(i).getKey().compareTo(key) < 0) {
				x = x.getList().get(i);
			}
			update.set(i, x);
		}

		x = x.getList().get(0);
		if (x != nil && x.getKey().compareTo(key) == 0) {
			for (int i = 0; i <= maxLevel; i++) {
				if (update.get(i).getList().get(i) != x)
					break;
				update.get(i).getList().set(i, x.getList().get(i));
			}
			x = null;

			if (maxLevel > 0 && head.getList().get(maxLevel) == nil) {
				head.getList().remove(maxLevel);
				maxLevel--;
			}
		}
	}

	@Override
	public K getMin() {
		return head.getList().get(0).getKey();
	}

	@Override
	public K getMax() {
		Node x = head;
		for (int i = maxLevel; i >= 0; i--) {
			while (x.getList().get(i) != nil) {
				x = x.getList().get(i);
			}
		}
		return x.getKey();
	}

	//https://courses.e-ce.uth.gr/CE210/doku.php?id=skiplist
	@Override
	public String toDotString() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("digraph {");
		stringBuilder.append("\n");

		stringBuilder.append("graph [rankdir=LR];");
		stringBuilder.append("\n");
		stringBuilder.append("node [shape=record,width=.1,height=.1];");
		stringBuilder.append("\n");

		stringBuilder.append("end [label = \"");
		for(int i=maxLevel; i>=0; i--) {
			stringBuilder.append(" <f"+i+"> ");
			if( i>0 ) stringBuilder.append("|");
			else stringBuilder.append(" end ");
		}
		stringBuilder.append("\"];");
		stringBuilder.append("\n");

		stringBuilder.append(toDotString(head));

		stringBuilder.append("}");
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}

	private String toDotString(Node node) {
		String itemName;    
		if(node.getKey() == null) itemName = "\"-oo\"";
		else itemName = node.getKey().toString();

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(itemName + " [label = \"");

		int level = node.getList().size() - 1;
		for(int i=level; i>=0; i--) {
			stringBuilder.append(" <f"+i+"> ");
			stringBuilder.append(itemName.equals("\"-oo\"") ? "-oo" : itemName);
			if( i>0 ) stringBuilder.append("|");
		}
		stringBuilder.append("\"];\n");

		ArrayList<Node> next = node.getList();
		for(int i=level; i>=0; i--) {
			if( next.get(i).getKey() != null ) {
				stringBuilder.append(itemName+":f"+i+" -> "+ next.get(i).getKey().toString()+":f"+i+";\n");
			}
			else stringBuilder.append(itemName+":f"+i+" -> end:f"+i+";\n");
		}
		if( next.get(0).getKey() != null ) {
			stringBuilder.append(toDotString(next.get(0)));
		}
		return stringBuilder.toString();
	}

	@Override
	public String toString() {

		StringBuilder string = new StringBuilder();

		Node x = head;

		for (int i = maxLevel; i >= 0; i--) {
			string.append("HEAD ");
			while (x.getList().get(0) != nil) {
				x = x.getList().get(0);
				if (x.getList().size() >= i) {
					string.append("-> "+x.getKey()+" ");
				} else {
					string.append("------");
				}
			}
			string.append("-> NIL\n");
			x = head;
		}

		return string.toString();
	}

	private ArrayList<Node> createArray(int size) {
		ArrayList<Node> newArray = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			newArray.add(null);
		}
		return newArray;
	} 

}
