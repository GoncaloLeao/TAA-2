package structures;

import java.net.Socket;

import structures.Treap.Node;

public class RedBlackTree<K extends Comparable<K>> implements DynamicSet<K> {

	private final boolean BLACK = false;
	private final boolean RED = true;
	private final Node LEAF = new Node(null, BLACK, null);
	private Node root = LEAF;
	private Node tmpNewNode;
	
	public class Node {
		private K key;
		private Node left;
		private Node right;
		private Node parent;
		private boolean color;

		protected Node(K key, boolean color, Node parent) {
			this.key = key;
			this.color = color;
			this.parent = parent;
			this.setLeft(LEAF);
			this.setRight(LEAF);
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
		
		public Node getParent() {
			return parent;
		}

		public boolean getColor() {
			return color;
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
		
		public void setParent(Node parent) {
			this.parent = parent;
		}
		
		public void setColor(boolean color) {
			this.color = color;
		}
	}
	
	/**
	 * The implementation is the usual one for Binary Trees.
	 */
	@Override
	public K find(K key) {
		Node node = find(root, key);
		if (node != null)
			return node.getKey();
		else
			return null;
	}

	private Node find(Node node, K key) {
		if (node == LEAF)
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
		// the tree is empty
		if (root == LEAF) {
			root = new Node(key, BLACK, null); 
		} else if (find(key) == null){
			root = insert(root, key, root);
			/*if (tmpNewNode.getParent() != null && tmpNewNode.getParent().getParent() != null &&
					tmpNewNode.getParent().getParent().getParent() != null) {
				System.out.println("Cara: "+tmpNewNode.getParent().getParent().getParent().getKey());
			}*/
			checkInvariant(tmpNewNode);
		}
	}

	private Node insert(Node node, K key, Node parent) {
		if (node == LEAF)
			return tmpNewNode = new Node(key, RED, parent);
		else if (key.compareTo(node.getKey()) < 0)
			node.setLeft(insert(node.getLeft(), key, node));
		else if (key.compareTo(node.getKey()) > 0)
			node.setRight(insert(node.getRight(), key, node));
		
		return node;
	}
	
	@Override
	public void remove(K key) {
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
			
		}

		return node;
	}
	
	/**
	 * The implementation is the same as SimpleBST.
	 */
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
		else if (node.getLeft() == LEAF)
			return node;
		else
			return getMin(node.getLeft());
	}

	/**
	 * The implementation is the same as SimpleBST.
	 */
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
		else if (node.getRight() == LEAF)
			return node;
		else
			return getMax(node.getRight());
	}
	
	@Override
	public void dump(String filename) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String toString() {
		return toString(root);
	}
	
	// print the values in the treap in pre-order (to p)
	private String toString(Node node) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("(");
		// Print the root
		if (node != LEAF) {
			stringBuilder.append("[" + node.getColor() + "]");
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
	
	public int countBlacks() {
		return countBlacks(root);
	}
	
	private int countBlacks(Node node) {
		if (node == LEAF) return 0;
		return (node.getColor() == BLACK ? 1 : 0) + countBlacks(node.getLeft()); 
	}
	
	public Node getRoot() {
		return this.root;
	}
	
	private void checkInvariant(Node node) {
		if (node == null) return;
		if (node == root) {
			root.setColor(BLACK);
			return;
		}
		// if the parent is red we have two consecutive red nodes
		if (node.getParent().getColor() == RED) {
			// Case 1 The uncle is a black node
			if (uncle(node).color == BLACK) {
				if (isLeftChild(node.getParent())) {
					if (isLeftChild(node)) {
						System.out.println("Caso1a");
						case1A(node);
					} else {
						System.out.println("Caso1b");
						case1B(node);
					}
				} else {
					if (!isLeftChild(node)) {
						System.out.println("Caso1c");
						case1C(node);
					} else {
						System.out.println("Caso1d");
						case1D(node);
					}
				}
			} 
			// Case 2 The uncle is a red node
			else {
				System.out.println("Caso2");
				uncle(node).setColor(BLACK);
				node.getParent().setColor(BLACK);
				grandparent(node).setColor(RED);
				checkInvariant(grandparent(node));
			}
		}
	}
	
	private boolean isLeftChild(Node node) {
		Node parent = node.getParent();
		if (parent.getLeft() == node) return true;
		return false;
	}
	
	// Case 1 a - Uncle is a black node and the inserted node is a left child
	private void case1A(Node node) {
		grandparent(node).setColor(RED);
		node.getParent().setColor(BLACK);
		
		Node tmp = grandparent(node.getParent());
		
		if (tmp == null) {
			root = rotateRight(grandparent(node));
		} else {
			if (isLeftChild(grandparent(node))) {
				tmp.setLeft(rotateRight(grandparent(node)));
			} else {
				tmp.setRight(rotateRight(grandparent(node)));
			}
		}
	}
	
	// Case 1 b - Uncle is a black node and the inserted node is a right child
	private void case1B(Node node) {
		grandparent(node).setLeft(rotateLeft(node.getParent()));
		case1A(node.left);
	}

	// Case 1 c - Symmetric of 1 a
	private void case1C(Node node) {
		grandparent(node).setColor(RED);
		node.getParent().setColor(BLACK);
		System.out.println("NOde:"+node.getKey());
		System.out.println("Parent: "+node.getParent().getKey());
		System.out.println("Grand: "+grandparent(node).getKey());
		Node tmp = grandparent(node.getParent());
		if (tmp == null) {
			root = rotateLeft(grandparent(node));
		} else {
			System.out.println("Ances1: "+grandparent(node).getParent().getKey());
			System.out.println("Ances2: "+tmp.getKey());
			if (isLeftChild(grandparent(node))) {
				tmp.setLeft(rotateLeft(grandparent(node)));
			} else {
				tmp.setRight(rotateLeft(grandparent(node)));
			}
		}
	}
	
	// Case 1 d - Symmetric of 1 b
	private void case1D(Node node) {
		grandparent(node).setRight(rotateRight(node.getParent()));
		case1C(node.right);
	}
	
	private Node grandparent(Node node) {
		Node parent = node.getParent();
		// There is no father
		if (parent == null) return null;
		return parent.getParent();
	}
	
	private Node sibling(Node node) {
		Node parent = node.getParent();
		// There is no father, so there is no sibling
		if (parent == null) return null;
		
		// Verify if is the node itself or the sibling 
		if (node == parent.getLeft()) return parent.getRight();
		return parent.getLeft();
	}
	
	private Node uncle(Node node) {
		Node parent = node.getParent();
		Node grand = grandparent(node);
		// There is no grandfather
		if (grand == null) return null;
		return sibling(parent);
	}
	
	private Node rotateLeft(Node x) {
		Node y = x.getRight();
		if (y == null)
			return x;
		Node z = y.getLeft();

		y.setLeft(x);
		x.setRight(z);

		y.setParent(x.getParent());
		x.setParent(y);
		
		return y;
	}

	private Node rotateRight(Node x) {
		Node y = x.getLeft();
		if (y == null)
			return x;
		Node z = y.getRight();

		y.setRight(x);
		x.setLeft(z);
		
		y.setParent(x.getParent());
		x.setParent(y);
		
		return y;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
