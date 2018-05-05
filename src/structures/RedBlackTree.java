package structures;

import structures.SimpleBST.Node;

/**
 * A self-balanced binary search tree where each node has an extra bit and that
 * bit is interpreted as the color (red or black). These color bits are used to
 * ensure the tree remains approximately balanced during insertions and
 * deletions.
 * 
 * @author Matheus Rosa
 *
 */
public class RedBlackTree<K extends Comparable<K>> implements DynamicSet<K> {

	private final boolean BLACK = false;
	private final boolean RED = true;
	private final Node LEAF = new Node(null, BLACK, null);
	private final Node DBLACK = new Node(null, BLACK, null);

	private Node root;
	private Node tmpNewNode;
	private Node u, v;

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

	public RedBlackTree() {
		root = LEAF;
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
		if (node.getKey() == null)
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
		if (root.getKey() == null) {
			root = new Node(key, BLACK, null);
		} else if (find(key) == null) {
			root = insert(root, key, root);
			// tmpNewNode is a global node to store the last insertion
			checkInvariant(tmpNewNode);
		}
	}

	private Node insert(Node node, K key, Node parent) {
		if (node.getKey() == null)
			return tmpNewNode = new Node(key, RED, parent);
		else if (key.compareTo(node.getKey()) < 0)
			node.setLeft(insert(node.getLeft(), key, node));
		else if (key.compareTo(node.getKey()) > 0)
			node.setRight(insert(node.getRight(), key, node));

		return node;
	}

	@Override
	public void remove(K key) {
		if (remove(root, key) != null) {
			System.out.println();
			deleteProcess(v, u);
		}
	}
	
	private Node remove(Node node, K key) {
    	//Node is empty
    	if (node.getKey() == null) {
    		u = DBLACK;
    		v = DBLACK;
    		return null;
    	}
    	//Delete on the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		return remove(node.getLeft(), key);
    	}
    	//Delete on the right subtree
        else if (key.compareTo(node.getKey()) > 0) {
        	return remove(node.getRight(), key);
        }
    	//Tree has the key on its root
    	else {
    		//The current node is a leaf (0 children).
    		if (node.getLeft().getKey() == null && node.getRight().getKey() == null) {
    			v = node;
    			u = DBLACK;
    			return node;
    		}
    		//The current node only has a right child
    		else if (node.getLeft().getKey() == null) {
                v = node;
    			u = node.getRight();
    			return node;
            }
    		//The current node only has a left child
    		else if (node.getRight().getKey() == null) {
                v = node;
    			u = node.getLeft();
    			return node;
            }
    		//The current node has two children
    		else {
    			Node largestLeftNode = getMax(node.getLeft());
    			node.setKey(largestLeftNode.getKey());
    			remove(node.getLeft(), largestLeftNode.getKey());
    			return node;
    		}
        }
	}
	
	private void deleteProcess(Node v, Node u) {
		if (v == root) {
			u.setColor(BLACK);
			root = u;
			return;
		}
		// Simple case: one of the nodes u, v are red
		if (v.getColor() == RED || u.getColor() == RED) {
			u.setParent(v.getParent());
			u.setColor(BLACK);
			if (isLeftChild(v)) v.getParent().setLeft(u);
			else v.getParent().setRight(u);
			v = null;
		}
		// Both nodes u, v are black
		else {
			u.setParent(v.getParent());
			if (isLeftChild(v)) v.getParent().setLeft(u);
			else v.getParent().setRight(u);
			v = LEAF;
			deleteBlackBlack(u);
			u = LEAF;
		}
	}
	
	private void deleteBlackBlack(Node x) {
		if (x == root || x.getColor() == RED) {
			x.setColor(BLACK);
			return;
		}
		
		Node next = root;
		
		if (isLeftChild(x)) {
		
			Node w1 = sibling(x);
			
			if (w1.getColor() == RED) {
				//System.out.println("Caso 11");
				x.getParent().setColor(RED);
				w1.setColor(BLACK);
				rotateLeftUpdating(x.getParent());
			}
			
			Node w2 = sibling(x);
			
			if (w2.getLeft().getColor() == BLACK && w2.getRight().getColor() == BLACK) {
				//System.out.println("Case 21");
				w2.setColor(RED);
				next = x.getParent();
			} else {
				if (w2.getRight().getColor() == BLACK) {
					//System.out.println("Case 31");
					w2.getLeft().setColor(BLACK);
					w2.setColor(RED);
					rotateRightUpdating(w2);
				}
				
				//System.out.println("Case 41");
				Node w3 = sibling(x);
				
				w3.setColor(x.getParent().getColor());
				x.getParent().setColor(BLACK);
				w3.getRight().setColor(BLACK);
				rotateLeftUpdating(x.getParent());
				next = root;
			}
		} else {
			Node w1 = sibling(x);
			
			if (w1.getColor() == RED) {
				//System.out.println("Caso 12");
				x.getParent().setColor(RED);
				w1.setColor(BLACK);
				rotateRightUpdating(x.getParent());
			}
			
			Node w2 = sibling(x);
			
			if (w2.getRight().getColor() == BLACK && w2.getLeft().getColor() == BLACK) {
				//System.out.println("Case 22");
				w2.setColor(RED);
				next = x.getParent();
			} else {
				if (w2.getLeft().getColor() == BLACK) {
					//System.out.println("Case 32");
					w2.getRight().setColor(BLACK);
					w2.setColor(RED);
					rotateLeftUpdating(w2);
				}
				
				//System.out.println("Case 42");
				Node w3 = sibling(x);
				
				w3.setColor(x.getParent().getColor());
				x.getParent().setColor(BLACK);
				w3.getLeft().setColor(BLACK);
				rotateRightUpdating(x.getParent());
				next = root;
			}
		}
		
		deleteBlackBlack(next);
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
		if (node != LEAF && node != DBLACK) {
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
		if (node == LEAF)
			return 0;
		return (node.getColor() == BLACK ? 1 : 0) + countBlacks(node.getLeft());
	}

	public Node getRoot() {
		return this.root;
	}

	/**
	 * Receives a node that is the last inserted node and check the properties of
	 * the Red Black Tree after this.
	 * 
	 * @param node
	 */
	private void checkInvariant(Node node) {
		if (node == null)
			return;
		if (node == root) {
			root.setColor(BLACK);
			return;
		}
		// If the parent is red we have two consecutive red nodes
		if (node.getParent().getColor() == RED) {
			// Case 1 The uncle is a black node
			if (uncle(node).color == BLACK) {
				if (isLeftChild(node.getParent())) {
					if (isLeftChild(node)) {
						// System.out.println("Caso1a");
						case1A(node);
					} else {
						// System.out.println("Caso1b");
						case1B(node);
					}
				} else {
					if (!isLeftChild(node)) {
						// System.out.println("Caso1c");
						case1C(node);
					} else {
						// System.out.println("Caso1d");
						case1D(node);
					}
				}
			}
			// Case 2 The uncle is a red node
			else {
				// System.out.println("Caso2");
				uncle(node).setColor(BLACK);
				node.getParent().setColor(BLACK);
				grandparent(node).setColor(RED);
				checkInvariant(grandparent(node));
			}
		}
	}

	private boolean isLeftChild(Node node) {
		Node parent = node.getParent();
		if (parent.getLeft() == node)
			return true;
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
		Node tmp = grandparent(node.getParent());
		if (tmp == null) {
			root = rotateLeft(grandparent(node));
		} else {
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
		if (parent == null)
			return null;
		return parent.getParent();
	}

	private Node sibling(Node node) {
		Node parent = node.getParent();
		// There is no father, so there is no sibling
		if (parent == null)
			return null;

		// Verify if it is the node itself or the sibling
		if (node == parent.getLeft())
			return parent.getRight();
		return parent.getLeft();
	}

	private Node uncle(Node node) {
		Node parent = node.getParent();
		Node grand = grandparent(node);
		// There is no grandfather
		if (grand == null)
			return null;
		return sibling(parent);
	}

	private Node rotateLeft(Node x) {
		Node y = x.getRight();
		if (y == null)
			return x;
		Node z = y.getLeft();

		y.setLeft(x);
		x.setRight(z);

		z.setParent(x);
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

		z.setParent(x);
		y.setParent(x.getParent());
		x.setParent(y);

		return y;
	}

	private void rotateRightUpdating(Node node) {
		Node tmp = node.getParent();
		
		if (tmp == null) {
			root = rotateRight(node);
		} else {
			if (isLeftChild(node)) {
				tmp.setLeft(rotateRight(node));
			} else {
				tmp.setRight(rotateRight(node));
			}
		}
	}
	
	private void rotateLeftUpdating(Node node) {
		Node tmp = node.getParent();
		if (tmp == null) {
			root = rotateLeft(node);
		} else {
			if (isLeftChild(node)) {
				tmp.setLeft(rotateLeft(node));
			} else {
				tmp.setRight(rotateLeft(node));
			}
		}
	}
	
}
