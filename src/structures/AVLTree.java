package structures;

public class AVLTree<K extends Comparable<K> ,V> extends BST<K,V> {
    
    protected int height(Node node) {
    	if(node == null) return 0;
    	else return node.getHeight();
    }
    
    protected int balance(Node node) {
		return height(node.left) - height(node.right);
	}
    
    @Override
    protected Node insert(Node node, K key, V value) { 
    	//Node is empty
    	if (node == null) return new Node(key,value);
    	//Tree has the key on its root
        else if (key.equals(node.getKey())) {
        	node.setValue(value);
        	return node;
        }
    	//Add to the left subtree
    	else if (key.compareTo(node.getKey()) < 0) node.setLeft(insert(node.getLeft(), key, value));
    	//Add to the right subtree
    	else node.setRight(insert(node.getRight(), key, value));
    	
    	node.updateHeight();
    	
    	int balance = balance(node);
    	
    	//Left Left Case
        if (balance > 1 && key.compareTo(node.getLeft().getKey()) < 0) return rotateRight(node);
        //Right Right Case
        else if (balance < -1 && key.compareTo(node.getRight().getKey()) > 0) return rotateLeft(node);
        //Left Right Case
        else if (balance > 1 && key.compareTo(node.getLeft().getKey()) > 0) {
            node.setLeft(rotateLeft(node.getLeft()));
            return rotateRight(node);
        }
        //Right Left Case
        else if (balance < -1 && key.compareTo(node.getRight().getKey()) < 0) {
            node.setRight(rotateRight(node.getRight()));
            return rotateLeft(node);
        }
        //Node is balanced
        else return node;
    }
    
    @Override
    protected Node delete(Node node, K key) {
    	//Node is empty
    	if (node == null) return null;
    	//Tree has the key on its root
    	else if (key.equals(node.getKey())) {
    		//The current node is a leaf (0 children).
    		if (node.getLeft() == null && node.getRight() == null) return null;
    		//The current node only has a right child
    		else if (node.getLeft() == null) node = node.getRight();
    		//The current node only has a left child
    		else if (node.getRight() == null) node = node.getLeft();
    		//The current node has two children
    		else {
    			Node smallestRightNode = (BST<K, V>.Node) getMin();
    			node.setKey(smallestRightNode.getKey());
    			node.setValue(smallestRightNode.getValue());
    			node.setRight(delete(node.getRight(), smallestRightNode.getKey()));
    		}
        }
    	//Delete on the left subtree
    	else if (key.compareTo(node.getKey()) < 0) node.setLeft(delete(node.getLeft(), key));
    	//Delete on the right subtree
        else node.setRight(delete(node.getRight(), key));
    	
    	node.updateHeight();
    	
    	int balance = balance(node);
    	
    	//Left Left Case
        if (balance > 1 && balance(root.getLeft()) >= 0) return rotateRight(node);
        //Right Right Case
        else if (balance < -1 && balance(root.getRight()) <= 0) return rotateLeft(node);
        //Left Right Case
        else if (balance > 1 && balance(root.getLeft()) < 0) {
            node.setLeft(rotateLeft(node.getLeft()));
            return rotateRight(node);
        }
        //Right Left Case
        else if (balance < -1 && balance(root.getRight()) > 0) {
            node.setRight(rotateRight(node.getRight()));
            return rotateLeft(node);
        }
        //Node is balanced
        else return node;
    }
}
