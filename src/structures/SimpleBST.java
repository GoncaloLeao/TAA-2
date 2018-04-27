package structures;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;

public class SimpleBST<K extends Comparable<K>> implements DynamicSet<K> {

	public class Node {
    	protected K key;
    	protected Node left;
    	protected Node right;
    	 
    	protected Node(K key) {
    		this.key = key;
    	}
    	
    	protected K getKey() { return key; }
    	protected Node getLeft() { return left; }
    	protected Node getRight() { return right; }
    	
    	protected void setKey(K key) { this.key = key; }
    	protected void setLeft(Node left) { this.left = left; }
    	public void setRight(Node right) { this.right = right; }
    }
	
	public Node root;
    
    public SimpleBST() { this.root = null; }
	
	@Override
	public K find(K key) {
		return find(root, key).getKey();
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
	
	protected Node insert(Node node, K key) { 
    	//Node is empty
    	if (node == null) return new Node(key);
    	//Add to the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		node.setLeft(insert(node.getLeft(), key));
    		return node;
    	}
    	//Add to the right subtree
    	else if (key.compareTo(node.getKey()) > 0) {
    		node.setRight(insert(node.getRight(), key));
    		return node;
    	}
    	//Tree has the key on its root
        else return node;
    }

	@Override
	public void remove(K key) {
		root = remove(root, key);
	}
	
	protected Node remove(Node node, K key) {
    	//Node is empty
    	if (node == null) return null;
    	//Delete on the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		node.setLeft(remove(node.getLeft(), key));
    		return node;
    	}
    	//Delete on the right subtree
        else if (key.compareTo(node.getKey()) > 0) {
        	node.setRight(remove(node.getRight(), key));
        	return node;
        }
    	//Tree has the key on its root
    	else {
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
    			Node largestLeftNode = getMax(node.getLeft());
    			node.setKey(largestLeftNode.getKey());
    			node.setLeft(remove(node.getLeft(), largestLeftNode.getKey()));
    			return node;
    		}
        }
    }

	@Override
	public K getMin() {
		return getMin(root).getKey();
	}
	
	protected final Node getMin(Node node) {
    	//Node is empty
    	if(node == null) return null;
    	//Left child is empty
    	else if(node.getLeft() == null) return node;
    	//Left child is not empty
    	else return getMin(node.getLeft());
    }

	@Override
	public K getMax() {
		return getMax(root).getKey();
	}
	
	protected final Node getMax(Node node) {
    	//Node is empty
    	if(node == null) return null;
    	//Right child is empty
    	else if(node.getRight() == null) return node;
    	//Right child is not empty
    	else return getMax(node.getRight());
    }
	
	@SuppressWarnings("unchecked")
	@Override
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
				curLevelNodes = (LinkedList<SimpleBST<K>.Node>) nextLevelNodes.clone();
			}
			while (!nextLevelNodes.isEmpty());
		}

		writer.println("}");
		writer.close();
	}

}
