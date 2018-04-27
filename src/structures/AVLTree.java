package structures;

public class AVLTree<K extends Comparable<K>> implements DynamicSet<K> {
	
	public class Node {
    	protected K key;
    	protected Node left;
    	protected Node right;
    	protected boolean heightEqual;
    	protected boolean heightPlusLeft;
    	 
    	protected Node(K key) {
    		this.key = key;
    	}
    	
    	protected K getKey() { return key; }
    	protected Node getLeft() { return left; }
    	protected Node getRight() { return right; }
    	protected boolean isHeightEqual() { return heightEqual; }
    	protected boolean isheightPlusLeft() { return !heightEqual && heightPlusLeft; }
    	protected boolean isheightPlusRight() { return !heightEqual && !heightPlusLeft; }
    	
    	protected void setKey(K key) { this.key = key; }
    	protected void setLeft(Node left) { this.left = left; }
    	public void setRight(Node right) { this.right = right; }
    }
	
	public Node root;
    
	/**
	 * The implementation is the same as SimpleBST.
	 */
	@Override
	public K find(K key) {
		Node node = find(root, key);
		if(node != null) return node.getKey();
		else return null;
	}
	
	protected Node find(Node node, K key) {
    	//Node is empty
    	if(node == null) return null;
    	//Search the left subtree
    	else if (key.compareTo(node.getKey()) < 0) return find(node.getLeft(), key);
    	//Search the right subtree
    	else if (key.compareTo(node.getKey()) > 0) return find(node.getRight(), key);
    	//Tree has the key on its root
    	else return node;
    }

	@Override
	public void insert(K key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(K key) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * The implementation is the same as SimpleBST.
	 */
	@Override
	public K getMin() {
		Node node = getMin(root);
		if(node != null) return node.getKey();
		else return null;
	}
	
	protected final Node getMin(Node node) {
    	//Node is empty
    	if(node == null) return null;
    	//Left child is empty
    	else if(node.getLeft() == null) return node;
    	//Left child is not empty
    	else return getMin(node.getLeft());
    }

	/**
	 * The implementation is the same as SimpleBST.
	 */
	@Override
	public K getMax() {
		Node node = getMax(root);
		if(node != null) return node.getKey();
		else return null;
	}
	
	protected final Node getMax(Node node) {
    	//Node is empty
    	if(node == null) return null;
    	//Right child is empty
    	else if(node.getRight() == null) return node;
    	//Right child is not empty
    	else return getMax(node.getRight());
    }

	@Override
	public void dump(String filename) {
		// TODO Auto-generated method stub
		
	}

}
