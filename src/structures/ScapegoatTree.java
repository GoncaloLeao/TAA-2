package structures;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class ScapegoatTree<K extends Comparable<K>> implements DynamicSet<K> {
	
	public class Node {
    	private K key;
    	private Node left;
    	private Node right;
    	 
    	protected Node(K key) {
    		this.key = key;
    	}
    	
    	public K getKey() { return key; }
    	public Node getLeft() { return left; }
    	public Node getRight() { return right; }
    	public boolean isLeaf() {
    		return left == null && right == null;
    	}
    	
    	public void setKey(K key) { this.key = key; }
    	public void setLeft(Node left) { this.left = left; }
    	public void setRight(Node right) { this.right = right; }
    }
	
	private Node root;
	private final double ALPHA;
    private int size; //number of nodes in the tree
    private int maxSize; //maximal value of size since the last time the tree was completely rebuilt. 
	
    public ScapegoatTree(double alpha) {
    	//Check if the value for alpha is not valid
    	if(alpha < 0.5 || alpha >= 1) {
    		throw new IllegalArgumentException("Alpha must be in the range [0.5,1[, but was " + alpha + ".");
    	}
    	
    	this.ALPHA = alpha;
    	this.size = 0;
    	this.maxSize = 0;
    }
    
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
		root = insert(root, key).node;		
	}
	
	private class InsertResult {
		public Node node;
		public int nodeSize;
		public int nodeHeight;
		
		public InsertResult(Node node, int nodeSize, int nodeHeight) {
			this.node = node;
			this.nodeSize = nodeSize;
			this.nodeHeight = nodeHeight;
		}
	}

    private InsertResult insert(Node node, K key) { 
    	//Node is empty
    	if (node == null) {
    		this.size++;
    		if(this.size > this.maxSize) this.maxSize = this.size;
    		return new InsertResult(new Node(key), 1, 0);
    	}
    	//Add to the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		InsertResult result = insert(node.getLeft(), key);
    		node.setLeft(result.node);
    		
    		int height = 1 + result.nodeHeight;
    		int size = 1 + result.nodeSize + getSize(node.getRight());
    		//Check if the node is a scapegoat
    		if(height > hAlpha(size)) {
    			//Rebuild the tree
    			node = rebuildTree(node);
    		}
    		return new InsertResult(node, size, height); 
    	}
    	//Add to the right subtree
    	else if (key.compareTo(node.getKey()) > 0) {
    		InsertResult result = insert(node.getRight(), key);
    		node.setRight(result.node);
    		
    		int height = 1 + result.nodeHeight;
    		int size = 1 + result.nodeSize + getSize(node.getLeft());
    		//Check if the node is a scapegoat
    		if(height > hAlpha(size)) {
    			//Rebuild the tree
    			node = rebuildTree(node);
    			//TODO: update maxSize?
    			//TODO: prevent rebuildTree from being called twice for the insert of a same key
    		}
    		return new InsertResult(node, size, height); 
    	}
    	//Tree has the key on its root
    	else return new InsertResult(node, 1, 0);
    }

	@Override
	public void remove(K key) {
		root = remove(root, key);
		
		if(this.size < this.ALPHA * this.maxSize) {
			//Rebuid the whole tree
			root = rebuildTree(root);
			//Set maxSize to size 
			this.maxSize = this.size;
		}
	}
	
	private Node remove(Node node, K key) {
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
    		this.size--;
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
	
	public int getSize() {
		return getSize(root);
	}
	
	private int getSize(Node node) {
		//Node is empty
    	if(node == null) return 0;
    	//Node is not empty
    	else return 1 + getSize(node.getLeft()) + getSize(node.getRight());
	}
	
	private int hAlpha(int n) {
		return (int)Math.floor(-Math.log(n)/Math.log(this.ALPHA));
	}
	
	private Node rebuildTree(Node node) {
		//Retrieve the subtree's list of nodes in order
		ArrayList<Node> list = flatten(node);
		return buildTree(list, 0, list.size() - 1);
	}
	
	private ArrayList<Node> flatten(Node node) {
		ArrayList<Node> list = new ArrayList<Node>();
		if(node != null) {
			Node curNode = node; //next node whose left subtree must be explored first
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
					curNode = list.get(list.size() - 1).getRight();
				}
			}
		}
		
		return list;
	}
	
	private Node buildTree(ArrayList<Node> list, int start, int end) {
		if(start <= end) {
			int middle = (start + end) / 2;
			Node node = list.get(middle);
			node.setLeft(buildTree(list, start, middle - 1));
			node.setRight(buildTree(list, middle + 1, end));
			return node;
		}
		else return null;
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
				curLevelNodes = (LinkedList<ScapegoatTree<K>.Node>) nextLevelNodes.clone();
			}
			while (!nextLevelNodes.isEmpty());
		}

		writer.println("}");
		writer.close();
	}
}
