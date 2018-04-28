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
    		this.heightEqual = true;
    	}
    	
    	protected K getKey() { return key; }
    	protected Node getLeft() { return left; }
    	protected Node getRight() { return right; }
    	protected int getBalance() {
    		if(heightEqual) return 0;
    		else if (heightPlusLeft) return 1;
    		else return -1;
    	}
    	
    	protected void setKey(K key) { this.key = key; }
    	protected void setLeft(Node left) { this.left = left; }
    	public void setRight(Node right) { this.right = right; }
    	public void setBalance(int balance) {
    		if(balance == 0) heightEqual = true;
    		else if(balance == 1) {
    			heightEqual = false;
    			heightPlusLeft = true;
    		}
    		else if(balance == -1) {
    			heightEqual = false;
    			heightPlusLeft = false;
    		}
    	}
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
		root = insert(root, key);
	}

    private Node insert(Node node, K key) { 
    	//Node is empty
    	if (node == null) return new Node(key);
    	//Add to the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		node.setLeft(insert(node.getLeft(), key));
    		
    		// Check if the tree needs to be rebalanced
    		if(node.getBalance() == 1) {
    			Node y = node.getLeft();
    			int balanceY = y.getBalance();
    			//Left left case
    			if(key.compareTo(y.getKey()) < 0) {
    				node.setBalance(Math.abs(balanceY - 1));
    				y.setBalance(balanceY - 1);
    				
    				return rotateRight(node);
    			}
    			//Left right case
    			else if(key.compareTo(y.getKey()) > 0) {
    				Node z = y.getRight();
        			int balanceZ = z.getBalance();
    				
    				z.setBalance(0);
    				// Before the rotations, node has height h+3 and y has height h+2.
    				if(balanceZ == 0) {
    					//Both children of z have height h.
    					y.setBalance(0);
    					node.setBalance(0);
    				}
    				else if(balanceZ == 1) {
    					//Left child of z has height h, right child has height h-1.
    					y.setBalance(0);
    					node.setBalance(-1);
    				}
    				else if(balanceZ == -1) {
    					//Left child of z has height h-1, right child has height h.
    					y.setBalance(1);
    					node.setBalance(0);
    				}
    				
    				node.setLeft(rotateLeft(y));
    	            return rotateRight(node);
    			}
    			else return node;
    		}
    		//Node is balanced
    		else {
    			node.setBalance(node.getBalance() + 1);
    			return node;
    		}
    	}
    	//Add to the right subtree
    	else if (key.compareTo(node.getKey()) > 0) {
    		node.setRight(insert(node.getRight(), key));
    		
    		// Check if the tree needs to be rebalanced
    		if(node.getBalance() == -1) {
    			Node y = node.getRight();
    			int balanceY = y.getBalance();
    			//Right Right case
    			if(key.compareTo(y.getKey()) > 0) {
    				node.setBalance(Math.abs(balanceY + 1));
    				y.setBalance(balanceY + 1);
    				
    				return rotateLeft(node);
    			}
    			//Right left case
    			else if(key.compareTo(y.getKey()) < 0) {
    				Node z = y.getLeft();
        			int balanceZ = z.getBalance();
    				
    				z.setBalance(0);
    				// Before the rotations, node has height h+3 and y has height h+2.
    				if(balanceZ == 0) {
    					//Both children of z have height h.
    					y.setBalance(0);
    					node.setBalance(0);
    				}
    				else if(balanceZ == 1) {
    					//Left child of z has height h, right child has height h-1.
    					node.setBalance(0);
    					y.setBalance(-1);
    				}
    				else if(balanceZ == -1) {
    					//Left child of z has height h-1, right child has height h.
    					node.setBalance(1);
    					y.setBalance(0);
    				}
    				
    				node.setRight(rotateRight(y));
    	            return rotateLeft(node);
    			}
    			else return node;
    		}
    		//Node is balanced
    		else {
    			node.setBalance(node.getBalance() - 1);
    			return node;
    		}
    	}
    	//Tree has the key on its root
        else return node;
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
	public String toString() { 
		return toString(root);
    }
    
    // print the values in this BST in pre-order (to p)
    protected String toString(Node node) {
    	StringBuilder stringBuilder = new StringBuilder();
    	stringBuilder.append("(");
    	//Print the root
    	if(node != null) {
    		stringBuilder.append("[" + node.getBalance() + "]");
    		stringBuilder.append(node.getKey() + ",");
    		//Print the left subtree
    		stringBuilder.append(toString(node.getLeft()));
        	stringBuilder.append(",");
            //Print the right subtree
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
        if(y == null) return x;
        Node z = y.getLeft();
        
        y.setLeft(x);
        x.setRight(z);

        return y;
    }
    
	private Node rotateRight(Node x) {
        Node y = x.getLeft();
        if(y == null) return x;
        Node z = y.getRight();
        
        y.setRight(x);
        x.setLeft(z);

        return y;
    }
}
