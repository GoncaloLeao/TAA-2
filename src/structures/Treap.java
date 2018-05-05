package structures;

import java.util.Random;

/**
 * A binary search tree in which every node has both a search key and a
 * priority, where the inorder sequence of search keys is sorted and each node's
 * priority is smaller than the priority of its children
 * 
 * @author Matheus Rosa
 *
 */
public class Treap<K extends Comparable<K>> implements DynamicSet<K> {
	private Random randGenerator = new Random();
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
			this.priority = randGenerator.nextInt(LIMIT);
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

	public Treap() {
		this.size = 0;
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

	@Override
	public void dump(String filename) {
		// TODO Auto-generated method stub
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
