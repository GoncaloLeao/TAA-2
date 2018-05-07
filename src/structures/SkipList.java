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
	private final Random rand = new Random();

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

	public SkipList() {
		head = new Node(null);
		nil = new Node(null);
		head.getList().add(nil);
		maxLevel = 0;
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

		int v = rand.nextInt(maxLevel + 2);
		
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

	@Override
	public void dump(String filename) {
		// TODO Auto-generated method stub

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
