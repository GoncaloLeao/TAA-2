package structures;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

/**
 * A binary search tree in which every node has both a search key and a
 * priority, where the inorder sequence of search keys is sorted and each node's
 * priority is smaller than the priority of its children
 * 
 * @author Gonçalo Leão & Matheus Rosa
 *
 */
public class Treap<K extends Comparable<K>> implements DynamicSet<K> {
	private Random rand;
	private final int LIMIT = 10000;
	private final int INF = LIMIT + 1;
	private int size;
	private Node root;

	public class Node {
		private K key;
		private Node left;
		private Node right;
		private int priority;

		protected Node(K key) {
			this.key = key;
			this.priority = rand.nextInt(LIMIT);
		}

		public K getKey() {
			return key;
		}

		public Node getLeft() {
			return left;
		}

		public Node getRight() {
			return right;
		}

		public int getPriority() {
			return priority;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public void setLeft(Node left) {
			this.left = left;
		}

		public void setRight(Node right) {
			this.right = right;
		}
	}

	public Treap(long seed) {
		this.size = 0;
		rand = new Random(seed);
	}
	
	public Treap() {
		this(System.currentTimeMillis());
	}

	@Override
	public K find(K key) {
		Node node = find(root, key);
		if (node != null)
			return node.getKey();
		else
			return null;
	}

	private Node find(Node node, K key) {
		if (node == null)
			return null;
		else if (key.compareTo(node.getKey()) < 0)
			return find(node.getLeft(), key);
		else if (key.compareTo(node.getKey()) > 0)
			return find(node.getRight(), key);
		else
			return node;
	}

	@Override
	public void insert(K key) {
		if (this.find(key) == null) size++;
		root = insert(root, key);
	}

	private Node insert(Node node, K key) {
		if (node == null)
			return new Node(key);
		else if (key.compareTo(node.getKey()) < 0)
			node.setLeft(insert(node.getLeft(), key));
		else if (key.compareTo(node.getKey()) > 0)
			node.setRight(insert(node.getRight(), key));
		return checkHeapProperty(node);
	}

	@Override
	public void remove(K key) {
		if (this.find(key) != null) size--;
		root = remove(root, key);
	}

	private Node remove(Node node, K key) {
		if (node == null)
			return null;
		else if (key.compareTo(node.getKey()) < 0)
			node.setLeft(remove(node.getLeft(), key));
		else if (key.compareTo(node.getKey()) > 0)
			node.setRight(remove(node.getRight(), key));
		else {
			Node left = node.getLeft();
			Node right = node.getRight();

			if (left == null && right == null) {
				return null;
			} else {
				// rotate the lowest priority child
				int leftPriority = left != null ? left.getPriority() : INF;
				int rightPriority = right != null ? right.getPriority() : INF;

				if (leftPriority < rightPriority) {
					node = rotateRight(node);
					node.setRight(remove(node.right, key));
				} else {
					node = rotateLeft(node);
					node.setLeft(remove(node.left, key));
				}
			}
		}

		return node;
	}

	private Node checkHeapProperty(Node node) {
		Node left = node.getLeft();
		Node right = node.getRight();
		int leftPriority = left != null ? left.getPriority() : INF;
		int rightPriority = right != null ? right.getPriority() : INF;

		// check if it violates the heap property in any child
		if (node.getPriority() > leftPriority || node.getPriority() > rightPriority) {
			if (leftPriority < rightPriority) {
				return rotateRight(node);
			} else {
				return rotateLeft(node);
			}
		}

		return node;
	}

	@Override
	public K getMin() {
		Node node = getMin(root);
		if (node != null)
			return node.getKey();
		else
			return null;
	}

	protected final Node getMin(Node node) {
		if (node == null)
			return null;
		else if (node.getLeft() == null)
			return node;
		else
			return getMin(node.getLeft());
	}

	@Override
	public K getMax() {
		Node node = getMax(root);
		if (node != null)
			return node.getKey();
		else
			return null;
	}

	protected final Node getMax(Node node) {
		if (node == null)
			return null;
		else if (node.getRight() == null)
			return node;
		else
			return getMax(node.getRight());
	}

	@Override
	public String toString() {
		return toString(root);
	}

	private String toString(Node node) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("(");
		// Print the root
		if (node != null) {
			stringBuilder.append("[" + node.getPriority() + "]");
			stringBuilder.append(node.getKey() + ",");
			// Print the left subtree
			stringBuilder.append(toString(node.getLeft()));
			stringBuilder.append(",");
			// Print the right subtree
			stringBuilder.append(toString(node.getRight()));
		}
		stringBuilder.append(")");

		return stringBuilder.toString();
	}
	
	private Integer getMinPriority() {
		if(root != null) return root.getPriority();
		else return null;
	}
	
	private Integer getMaxPriority() {
		if(root == null) return null;
		else {
			Integer maxPriority = new Integer(-1);
			ArrayList<Node> list = new ArrayList<Node>();
			Node curNode = root; //next node whose left subtree must be explored first
			Stack<Node> stack = new Stack<Node>(); //nodes whose left (but not right) subtree has been explored, and not included in the list yet
			while(curNode != null || !stack.empty()) {
				//Check if we need explore a left subtree first
				if(curNode != null) {
					stack.push(curNode);
					curNode = curNode.getLeft();
				}
				//Check if there we need to add a node to the list and explore its right subtree
				else if (!stack.empty()){
					list.add(stack.pop());
					Node node = list.get(list.size() - 1);
					if(node.getPriority() > maxPriority) maxPriority = node.getPriority();
					curNode = node.getRight();
				}
			}
			
			return maxPriority;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toDotString() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("digraph {");
		stringBuilder.append("\n");
		
		Integer minPriority = getMinPriority();
		Integer maxPriority = getMaxPriority();

		// Dump all the nodes
		LinkedList<Node> curLevelNodes = new LinkedList<Node>();
		LinkedList<Node> nextLevelNodes = new LinkedList<Node>();
		int nullDotCount = 0;
		
		if(root != null) {
			curLevelNodes.add(root);
			do {
				nextLevelNodes.clear();
				
				//Draw the current level's nodes
				stringBuilder.append(" { rank=same; ");
				for(int i = 0; i < curLevelNodes.size(); i++) {
					Node node = curLevelNodes.get(i);
					stringBuilder.append(node.getKey() + "; ");
				}
				stringBuilder.append("}");
				stringBuilder.append("\n");
				
				while(!curLevelNodes.isEmpty()) {
					Node node = curLevelNodes.remove();
					//Compute the color
					double hue = 0;
					if(!minPriority.equals(maxPriority)) hue = node.getPriority() * 0.17 / (maxPriority - minPriority);
					
					stringBuilder.append(node.getKey() + " [shape=circle, style=filled, fillcolor=\"" + hue + " 1.000 1.000\", label=<"+ node.getKey() +"<BR /><FONT POINT-SIZE=\"10\">" + node.getPriority() + "</FONT>>];");
					stringBuilder.append("\n");
					
					Node left = node.getLeft();
					if(left != null) {
						nextLevelNodes.add(left);
						stringBuilder.append(node.getKey() + "->" + left.getKey());
						stringBuilder.append("\n");
					}
					else {
						stringBuilder.append("null" + nullDotCount + " [shape=point];");
						stringBuilder.append("\n");
						stringBuilder.append(node.getKey() + "->" + "null" + nullDotCount);
						stringBuilder.append("\n");
						nullDotCount++;
					}
					
					Node right = node.getRight();
					if(right != null) {
						nextLevelNodes.add(right);
						stringBuilder.append(node.getKey() + "->" + right.getKey());
						stringBuilder.append("\n");
					}
					else {
						stringBuilder.append("null" + nullDotCount + " [shape=point];");
						stringBuilder.append("\n");
						stringBuilder.append(node.getKey() + "->" + "null" + nullDotCount);
						stringBuilder.append("\n");
						nullDotCount++;
					}
				}
				
				//Prepare the next curLevelNodes array
				curLevelNodes = (LinkedList<Treap<K>.Node>) nextLevelNodes.clone();
			}
			while (!nextLevelNodes.isEmpty());
		}

		stringBuilder.append("}");
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}

	private Node rotateLeft(Node x) {
		Node y = x.getRight();
		if (y == null)
			return x;
		Node z = y.getLeft();

		y.setLeft(x);
		x.setRight(z);

		return y;
	}

	private Node rotateRight(Node x) {
		Node y = x.getLeft();
		if (y == null)
			return x;
		Node z = y.getRight();

		y.setRight(x);
		x.setLeft(z);

		return y;
	}

	public void printTreeFormat() {
		System.out.println();
		printTreeFormat(this.root, 0);
		System.out.println();
	}

	private void printTreeFormat(Node node, int deepth) {
		if (node == null)
			return;
		printTreeFormat(node.getLeft(), deepth + 1);
		for (int i = 0; i < deepth; i++)
			System.out.print("\t\t");
		System.out.println(node.getKey() + "(" + node.getPriority() + ")");
		printTreeFormat(node.getRight(), deepth + 1);
	}

	public Node getRoot() {
		return this.root;
	}

	public int getSize() {
		return this.size;
	}
}
