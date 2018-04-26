package structures;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.LinkedList;

public class BST<K extends Comparable<K>, V> {
   
	public class Node {
    	protected K key;
    	protected V value;
    	protected Node left;
    	protected Node right;
    	//For AVL trees
    	protected int height;
    	 
    	protected Node(K key, V value) {
    		this.key = key;
    	    this.value = value;
    	    
    	    //For AVL trees
    	    this.height = 1;
    	}
    	
    	protected K getKey() { return key; }
    	protected V getValue() { return value; }
    	protected Node getLeft() { return left; }
    	protected Node getRight() { return right; }
    	//For AVL trees
    	protected int getHeight() { return height; }
    	
    	protected void setKey(K key) { this.key = key; }
    	protected void setValue(V value) { this.value = value; }
    	protected void setLeft(Node left) { this.left = left; }
    	public void setRight(Node right) { this.right = right; }
    	//For AVL trees
    	protected void updateHeight() {
    		if(left != null) {
    			if(right != null) this.height = 1 + Math.max(left.getHeight(), right.getHeight());
    			else this.height = 1 + left.getHeight();
    		}
    		else {
    			if(right != null) this.height = 1 + right.getHeight();
    			else this.height = 1;
    		}
    	}
    }
	
	public Node root;
    
    public BST() { this.root = null; }
	
    public Node find(K key) {
    	return find(root, key);
    }
    
    protected Node find(Node node, K key) {
    	//Node is empty
    	if(node == null) return null;
    	//Tree has the key on its root
    	else if (key.equals(node.getKey())) return node;
    	//Search the left subtree
    	else if (key.compareTo(node.getKey()) < 0) return find(node.getLeft(), key);
        //Search the right subtree
        else return find(node.getRight(), key);
    }
    
    public Node getMin() {
    	return getMin(root);
    }
    
    protected final Node getMin(Node node) {
    	//Node is empty
    	if(node == null) return null;
    	//Left child is empty
    	else if(node.getLeft() == null) return node;
    	//Left child is not empty
    	else return getMin(node.getLeft());
    }
    
    public Node getMax() {
    	return getMax(root);
    }

    protected final Node getMax(Node node) {
    	//Node is empty
    	if(node == null) return null;
    	//Right child is empty
    	else if(node.getRight() == null) return node;
    	//Right child is not empty
    	else return getMax(node.getRight());
    }
    
    // Add key to this BST
    public void insert(K key, V value) {
    	root = insert(root, key, value); 
    }
    
    protected Node insert(Node node, K key, V value) { 
    	//Node is empty
    	if (node == null) return new Node(key,value);
    	//Tree has the key on its root
        else if (key.equals(node.getKey())) {
        	node.setValue(value);
        	return node;
        }
    	//Add to the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		node.setLeft(insert(node.getLeft(), key, value));
    		return node;
    	}
    	//Add to the right subtree
    	else {
    		node.setRight(insert(node.getRight(), key, value));
    		return node;
    	}
    }
    
    public void delete(K key) {
    	root = delete(root, key);
    }
     
    protected Node delete(Node node, K key) {
    	//Node is empty
    	if (node == null) return null;
    	//Tree has the key on its root
    	else if (key.equals(node.getKey())) {
    		//The current node is a leaf (0 children).
    		if (node.getLeft() == null && node.getRight() == null) return null;
    		//The current node only has a right child
    		else if (node.getLeft() == null) {
                return node.getRight();
            }
    		//The current node only has a left child
    		else if (node.getRight() == null) {
                return node.getLeft();
            }
    		//The current node has two children
    		else {
    			Node smallestRightNode = (AVLTree<K, V>.Node) getMin();
    			node.setKey(smallestRightNode.getKey());
    			node.setValue(smallestRightNode.getValue());
    			node.setRight(delete(node.getRight(), smallestRightNode.getKey()));
    			return node;
    		}
        }
    	//Delete on the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		node.setLeft(delete(node.getLeft(), key));
    		return node;
    	}
    	//Delete on the right subtree
        else {
        	node.setRight(delete(node.getRight(), key));
        	return node;
        }
    }
    
    public Node rotateLeft(Node x) {
        Node y = x.getRight();
        if(y == null) return x;
        Node z = y.getLeft();
        
        y.setLeft(x);
        x.setRight(z);

        return y;
    }
    
    public Node rotateRight(Node x) {
        Node y = x.getLeft();
        if(y == null) return x;
        Node z = y.getRight();
        
        y.setRight(x);
        x.setLeft(z);

        return y;
    }
    
    public void print(PrintStream p) { 
    	print(root, p);
    }
    
    // print the values in this BST in sorted order (to p)
    protected void print(Node node, PrintStream p) { 
    	//Print the left subtree
    	if(node.getLeft() != null) print(node.getLeft(), p);
    	//Print the root
    	if(node != null) p.append("("+ node.getKey() +","+ node.getValue() +")");
        //Print the right subtree
        if(node.getRight() != null) print(node.getRight(), p);
    }
    
    public void dump(String filename) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileOutputStream(filename, false));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("The specified file " + filename + " does not exist.");
		}

		writer.println("digraph {");

		// Dump all the nodes
		LinkedList<Node> curLevelNodes = new LinkedList<Node>();
		LinkedList<Node> nextLevelNodes = new LinkedList<Node>();
		int nullDotCount = 0;
		
		if(root != null) {
			curLevelNodes.add(root);
			do {
				nextLevelNodes.clear();
				
				//Draw the current level's nodes
				writer.print(" { rank=same; ");
				for(int i = 0; i < curLevelNodes.size(); i++) {
					Node node = curLevelNodes.get(i);
					writer.print(node.getKey() + "; ");
				}
				writer.println("}");
				
				while(!curLevelNodes.isEmpty()) {
					Node node = curLevelNodes.remove();
					writer.println(node.getKey() + " [shape=circle];");
					
					Node left = node.getLeft();
					if(left != null) {
						nextLevelNodes.add(left);
						writer.println(node.getKey() + "->" + left.getKey());
					}
					else {
						writer.println("null" + nullDotCount + " [shape=point];");
						writer.println(node.getKey() + "->" + "null" + nullDotCount);
						nullDotCount++;
					}
					
					Node right = node.getRight();
					if(right != null) {
						nextLevelNodes.add(right);
						writer.println(node.getKey() + "->" + right.getKey());
					}
					else {
						writer.println("null" + nullDotCount + " [shape=point];");
						writer.println(node.getKey() + "->" + "null" + nullDotCount);
						nullDotCount++;
					}
				}
				
				//Prepare the next curLevelNodes array
				curLevelNodes = (LinkedList<BST<K, V>.Node>) nextLevelNodes.clone();
			}
			while (!nextLevelNodes.isEmpty());
		}

		writer.println("}");
		writer.close();
	}
}
