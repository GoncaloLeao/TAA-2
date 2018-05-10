package structures;

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
			deleteProcess(v, u);
		}
	}

	private Node remove(Node node, K key) {
		if (node.getKey() == null) {
			u = DBLACK;
			v = DBLACK;
			return null;
		} else if (key.compareTo(node.getKey()) < 0) {
			return remove(node.getLeft(), key);
		} else if (key.compareTo(node.getKey()) > 0) {
			return remove(node.getRight(), key);
		} else {
			if (node.getLeft().getKey() == null && node.getRight().getKey() == null) {
				v = node;
				u = DBLACK;
				return node;
			} else if (node.getLeft().getKey() == null) {
				v = node;
				u = node.getRight();
				return node;
			} else if (node.getRight().getKey() == null) {
				v = node;
				u = node.getLeft();
				return node;
			} else {
				Node largestLeftNode = getMax(node.getLeft());
				node.setKey(largestLeftNode.getKey());
				remove(node.getLeft(), largestLeftNode.getKey());
				return node;
			}
		}
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
		else if (node.getLeft() == LEAF || node.getLeft() == DBLACK)
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
		else if (node.getRight() == LEAF || node.getRight() == DBLACK)
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
		if (node.getKey() == null)
			return 0;
		return (node.getColor() == BLACK ? 1 : 0) + countBlacks(node.getLeft());
	}

	public Node getRoot() {
		return this.root;
	}

	/**
	 * Receives a node that is the last inserted node and check the properties of
	 * the Red Black Tree after this. If necessary we call the method again with a
	 * new node. The more trick case is when the father of the inserted node is red,
	 * because it violates the red red property.
	 * 
	 * There are three main cases:
	 * 
	 * <ul>
	 * <li>The node is the root</li>
	 * <li>The father is red (RED RED problem) and uncle is black There are four
	 * possible case, but two are symmetric, therefore we have only two real cases.
	 * <ul>
	 * <li>Case a - Uncle is a black node and the inserted node is a left child</li>
	 * <li>Case b - Uncle is a black node and the inserted node is a right
	 * child</li>
	 * </ul>
	 * </li>
	 * <li>The father is red (RED RED problem) and uncle is red</li>
	 * </ul>
	 * 
	 * @param node
	 *            - that was inserted or the node from the new call
	 */
	private void checkInvariant(Node node) {
		if (node == null)
			return;
		if (node == root) {
			root.setColor(BLACK);
			return;
		}

		if (node.getParent().getColor() == RED) {
			if (uncle(node).color == BLACK) {
				if (isLeftChild(node.getParent())) {
					if (isLeftChild(node)) {
						case1A(node);
					} else {
						case1B(node);
					}
				} else {
					if (!isLeftChild(node)) {
						case1C(node);
					} else {
						case1D(node);
					}
				}
			} else {
				uncle(node).setColor(BLACK);
				node.getParent().setColor(BLACK);
				grandparent(node).setColor(RED);
				checkInvariant(grandparent(node));
			}
		}
	}

	private void case1A(Node node) {
		grandparent(node).setColor(RED);
		node.getParent().setColor(BLACK);
		rotateRightUpdating(grandparent(node));
	}

	private void case1B(Node node) {
		rotateLeftUpdating(node.getParent());
		case1A(node.left);
	}

	private void case1C(Node node) {
		grandparent(node).setColor(RED);
		node.getParent().setColor(BLACK);
		rotateLeftUpdating(grandparent(node));
	}

	private void case1D(Node node) {
		rotateRightUpdating(node.getParent());
		case1C(node.right);
	}

	/**
	 * Given a node to be deleted process the current properties of tree and choose
	 * the actions to delete and update the nodes.
	 * 
	 * There are 3 main cases.
	 * 
	 * <ul>
	 * <li>Root case - The node to be deleted is the root.</li>
	 * <li>Simple case - One of the nodes u, v are red.</li>
	 * <li>Double Black case - Both nodes u, v are black</li>
	 * </ul>
	 * 
	 * @param v
	 *            - to be deleted
	 * @param u
	 *            - to replace v
	 */
	private void deleteProcess(Node v, Node u) {
		if (v == root) {
			u.setColor(BLACK);
			root = u;
		} else if (v.getColor() == RED || u.getColor() == RED) {
			u.setParent(v.getParent());
			u.setColor(BLACK);
			if (isLeftChild(v))
				v.getParent().setLeft(u);
			else
				v.getParent().setRight(u);
			v = null;
		} else {
			u.setParent(v.getParent());
			if (isLeftChild(v))
				v.getParent().setLeft(u);
			else
				v.getParent().setRight(u);
			v = LEAF;
			deleteBlackBlack(u);
			u = LEAF;
		}
	}

	/**
	 * Given a double black node, analyze the properties and do the transformations,
	 * if necessary, call the method again with the next double black node.
	 * 
	 * There are 8 cases but we can just replicate the symmetric cases, therefore
	 * are 4 four cases. For the double black left child we have:
	 * 
	 * <ul>
	 * <li>Case 1 - The sibling node is red.</li>
	 * <li>Case 2 - The sibling is black and its two children are black.</li>
	 * <li>Case 3 - The sibling is black and the left child is red.</li>
	 * <li>Case 4 - The sibling is black and the right child is red.</li>
	 * </ul>
	 * 
	 * For the double black right child is the same, but symmetric.<br>
	 * <br>
	 * 
	 * @param x
	 *            - the double black node
	 */
	private void deleteBlackBlack(Node x) {
		if (x == root || x.getColor() == RED) {
			x.setColor(BLACK);
			return;
		}

		Node next = root;

		if (isLeftChild(x)) {

			Node w1 = sibling(x);

			if (w1.getColor() == RED) {
				x.getParent().setColor(RED);
				w1.setColor(BLACK);
				rotateLeftUpdating(x.getParent());
			}

			Node w2 = sibling(x);

			if (w2.getLeft().getColor() == BLACK && w2.getRight().getColor() == BLACK) {
				w2.setColor(RED);
				next = x.getParent();
			} else {
				if (w2.getRight().getColor() == BLACK) {
					w2.getLeft().setColor(BLACK);
					w2.setColor(RED);
					rotateRightUpdating(w2);
				}

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
				x.getParent().setColor(RED);
				w1.setColor(BLACK);
				rotateRightUpdating(x.getParent());
			}

			Node w2 = sibling(x);
			if (w2.getRight().getColor() == BLACK && w2.getLeft().getColor() == BLACK) {
				w2.setColor(RED);
				next = x.getParent();
			} else {
				if (w2.getLeft().getColor() == BLACK) {
					w2.getRight().setColor(BLACK);
					w2.setColor(RED);
					rotateLeftUpdating(w2);
				}

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

	private boolean isLeftChild(Node node) {
		Node parent = node.getParent();
		if (parent.getLeft() == node)
			return true;
		return false;
	}

	private Node grandparent(Node node) {
		Node parent = node.getParent();
		if (parent == null)
			return null;
		return parent.getParent();
	}

	private Node sibling(Node node) {
		Node parent = node.getParent();
		if (parent == null)
			return null;

		if (node == parent.getLeft())
			return parent.getRight();
		return parent.getLeft();
	}

	private Node uncle(Node node) {
		Node parent = node.getParent();
		Node grand = grandparent(node);
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
