package structures;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @author Gonçalo Leão & Matheus Rosa
 *
 */
public class ScapegoatTree<K extends Comparable<K>> implements DynamicSet<K> {
	
	public class Node {
    	private K key;
    	private Node left;
    	private Node right;
    	private Node parent;
    	 
    	protected Node(K key) {
    		this.key = key;
    	}
    	
    	public K getKey() { return key; }
    	public Node getLeft() { return left; }
    	public Node getRight() { return right; }
    	public Node getParent() { return parent; }
    	public Node getSibling() {
    		//Node is the root of the tree
    		if(this.parent == null) return null;
    		//Node isn't the root of the tree
    		else {
    			//This node is the left child if its parent
    			if(this.key.compareTo(this.parent.key) < 0) return this.parent.right;
    			//This node is the right child if its parent
    			else return this.parent.left;
    		}
    	}
    	
    	public void setKey(K key) { this.key = key; }
    	public void setLeft(Node left) { this.left = left; }
    	public void setRight(Node right) { this.right = right; }
    	public void setParent(Node parent) { this.parent = parent; }
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
    
    public Node getRoot() {
    	return this.root;
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
		InsertResult result = insert(root, key);
		root = result.resultNode;
		int depth = result.depth;
		
		//Check if the inserted node is a deep node
		if(depth > hAlpha(size)) {
			Node scapegoat = findScapegoat(result.insertedNode);
			Node parent = scapegoat.getParent(); //the parent of the scapegoat node
			Node newNode = rebuildTree(scapegoat);
			newNode.setParent(parent);
			//The whole tree was rebalanced (scapegoat was the root)
			if(parent == null) {
				this.maxSize = this.size;
				root = newNode;
			}
			//Only a part of the tree was rebalanced
			else {
				//newNode is the left child if its parent
    			if(newNode.getKey().compareTo(parent.getKey()) < 0) parent.setLeft(newNode);
    			//This node is the right child if its parent
    			else parent.setRight(newNode);
			}
		}
	}
	
	private class InsertResult {
		public Node resultNode;
		public Node insertedNode;
		public int depth;
		
		public InsertResult(Node resultNode, Node insertedNode, int depth) {
			this.resultNode = resultNode;
			this.insertedNode = insertedNode;
			this.depth = depth;
		}
	}

    private InsertResult insert(Node node, K key) { 
    	//Node is empty
    	if (node == null) {
    		this.size++;
    		if(this.size > this.maxSize) this.maxSize = this.size;
    		Node newNode = new Node(key);
    		return new InsertResult(newNode, newNode, 0);
    	}
    	//Add to the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		InsertResult result = insert(node.getLeft(), key);
    		node.setLeft(result.resultNode);
    		result.resultNode.setParent(node);
    		
    		int depth = 1 + result.depth;
    		return new InsertResult(node, result.insertedNode, depth); 
    	}
    	//Add to the right subtree
    	else if (key.compareTo(node.getKey()) > 0) {
    		InsertResult result = insert(node.getRight(), key);
    		node.setRight(result.resultNode);
    		result.resultNode.setParent(node);
    		
    		int depth = 1 + result.depth;
    		return new InsertResult(node, result.insertedNode, depth);
    	}
    	//Tree has the key on its root
    	else return new InsertResult(node, node, 0);
    }
    
    private Node findScapegoat(Node node) {
    	int size = 1;
    	int depth = 0;
    	Node n = node;
    	while(n.getParent() != null) {
    		depth++;
    		int totalSize = 1 + size + getSize(n.getSibling());
    		if(depth > hAlpha(totalSize)) return n.getParent();
    		n = n.getParent();
    		size = totalSize;
    	}
    	throw new RuntimeException("Found a null scapegoat node.");
    }

	@Override
	public void remove(K key) {
		root = remove(root, key);
		
		if(this.size < this.ALPHA * this.maxSize) {
			//Rebuid the whole tree
			root = rebuildTree(root);
			if(root != null) root.setParent(null);
			//Set maxSize to size 
			this.maxSize = this.size;
		}
	}
	
	private Node remove(Node node, K key) {
    	//Node is empty
    	if (node == null) return null;
    	//Delete on the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		Node newChild = remove(node.getLeft(), key);
    		node.setLeft(newChild);
    		if(newChild != null) newChild.setParent(node);
    		return node;
    	}
    	//Delete on the right subtree
        else if (key.compareTo(node.getKey()) > 0) {
        	Node newChild = remove(node.getRight(), key);
    		node.setRight(newChild);
    		if(newChild != null) newChild.setParent(node);
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
    			this.size++; //increment to nufily the decrement from the call to remove in the next line (that always removes a leaf)
    			Node newChild = remove(node.getLeft(), largestLeftNode.getKey());
    			node.setLeft(newChild);
    			if(newChild != null) newChild.setParent(node);
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
			int middle = (int)Math.ceil((start + end) / 2.0);
			Node node = list.get(middle);
			
			Node newLeftChild = buildTree(list, start, middle - 1);
			node.setLeft(newLeftChild);
			if(newLeftChild != null) newLeftChild.setParent(node);
			
			Node newRightChild = buildTree(list, middle + 1, end);
			node.setRight(newRightChild);
			if(newRightChild != null) newRightChild.setParent(node);
			
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
	public String toDotString() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("digraph {");
		stringBuilder.append("\n");

		// Dump all the nodes
		LinkedList<Node> curLevelNodes = new LinkedList<Node>();
		LinkedList<Node> nextLevelNodes = new LinkedList<Node>();
		int nullDotCount = 0;
		
		if(root != null) {
			curLevelNodes.add(root);
			do {
				nextLevelNodes.clear();
				
				//Draw the current level's nodes
				stringBuilder.append(" { rank=same; ");
				for(int i = 0; i < curLevelNodes.size(); i++) {
					Node node = curLevelNodes.get(i);
					stringBuilder.append(node.getKey() + "; ");
				}
				stringBuilder.append("}");
				stringBuilder.append("\n");
				
				while(!curLevelNodes.isEmpty()) {
					Node node = curLevelNodes.remove();
					stringBuilder.append(node.getKey() + " [shape=octagon];");
					stringBuilder.append("\n");
					
					Node left = node.getLeft();
					if(left != null) {
						nextLevelNodes.add(left);
						stringBuilder.append(node.getKey() + "->" + left.getKey());
						stringBuilder.append("\n");
					}
					else {
						stringBuilder.append("null" + nullDotCount + " [shape=point];");
						stringBuilder.append("\n");
						stringBuilder.append(node.getKey() + "->" + "null" + nullDotCount);
						stringBuilder.append("\n");
						nullDotCount++;
					}
					
					Node right = node.getRight();
					if(right != null) {
						nextLevelNodes.add(right);
						stringBuilder.append(node.getKey() + "->" + right.getKey());
						stringBuilder.append("\n");
					}
					else {
						stringBuilder.append("null" + nullDotCount + " [shape=point];");
						stringBuilder.append("\n");
						stringBuilder.append(node.getKey() + "->" + "null" + nullDotCount);
						stringBuilder.append("\n");
						nullDotCount++;
					}
				}
				
				//Prepare the next curLevelNodes array
				curLevelNodes = (LinkedList<ScapegoatTree<K>.Node>) nextLevelNodes.clone();
			}
			while (!nextLevelNodes.isEmpty());
		}

		stringBuilder.append("}");
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}
}
