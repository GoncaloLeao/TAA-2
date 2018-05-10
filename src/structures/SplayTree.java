package structures;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;

public class SplayTree<K extends Comparable<K>> implements DynamicSet<K> {
	
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
    	
    	public void setKey(K key) { this.key = key; }
    	public void setLeft(Node left) { 
    		this.left = left;
    		if(left != null) left.parent = this;
    	}
    	public void setRight(Node right) { 
    		this.right = right;
    		if(right != null) right.parent = this;
    	}
    	public void setParent(Node parent) { this.parent = parent; }
    }
	
	public class NodePair {
		private Node first;
		private Node second;
		
		public NodePair(Node first, Node second) {
			this.first = first;
			this.second = second;
		}
		
		public Node getFirst() { return first; }
		public Node getSecond() { return second; }
	}
	
	public Node root;
    
	/**
	 * The implementation is the same as SimpleBST.
	 */
	@Override
	public K find(K key) {
		Node found = find(root, key);
		splay(found);
		if(root != null && root.getKey() == key) return root.getKey();
		else return null;
	}
	
	protected Node find(Node node, K key) {
    	//Node is empty
    	if(node == null) return null;
    	//Search the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		Node left = node.getLeft();
    		if(left == null) return node;
    		else return find(left, key);
    	}
    	//Search the right subtree
    	else if (key.compareTo(node.getKey()) > 0) {
    		Node right = node.getRight();
    		if(right == null) return node;
    		return find(right, key);
    	}
    	//Tree has the key on its root
    	else return node;
    }
	
	/*
	protected Node splay(Node node, K key) {
    	// Node is empty
    	if(node == null) return null;
    	// Search the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		Node left = node.getLeft();
    		// Key is not in the subtree
    		if(left == null) return node;
    		else {
    			// Zig-Zig case (left-left)
    			if(key.compareTo(left.getKey()) < 0) {
    				left.setLeft(find(left.getLeft(), key));
    				node = rotateRight(node);
    			}
    			// Zig-Zag case (left-right)
    			else if (key.compareTo(left.getKey()) > 0) {
    				left.setRight(find(left.getRight(), key));
    				node.setLeft(rotateLeft(left));
    			}
    			// If none if the two previous conditions is true,
    			// then the key is the left child of node
    			
        		return rotateRight(node);
    		}
    	}
    	// Search the right subtree
    	else if (key.compareTo(node.getKey()) > 0) {
    		Node right = node.getRight();
    		// Key is not in the subtree
    		if(right == null) return node;
    		else {
    			// Zag-Zig case (right-left)
    			if(key.compareTo(right.getKey()) < 0) {
    				right.setLeft(find(right.getLeft(), key));
    				node.setRight(rotateRight(right));
    			}
    			// Zag-Zag case (right-right)
    			else if (key.compareTo(right.getKey()) > 0) {
    				right.setRight(find(right.getRight(), key));
    				node = rotateLeft(node);
    			}
    			// If none if the two previous conditions is true,
    			// then the key is the right child of node
    			
        		return rotateLeft(node);
    		}
    	}
    	// Tree has the key on its root
    	else return node;
    }
	*/
	/* protected Node splay(Node node, K key) {
    	//Node is empty
    	if(node == null) return null;
    	//Search the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		node.setLeft(splay(node.getLeft(), key));
    		return rotateRight(node);
    	}
    	//Search the right subtree
    	else if (key.compareTo(node.getKey()) > 0) {
    		node.setRight(splay(node.getRight(), key));
    		return rotateLeft(node);
    	}
    	//Tree has the key on its root
    	else return node;
    } */
	
	@Override
	public void insert(K key) {
		NodePair pair = insert(root, key);
		root = pair.getFirst();
		splay(pair.getSecond());
	}

    private NodePair insert(Node node, K key) { 
    	// Node is empty
    	if(node == null) {
    		Node newNode = new Node(key);
    		return new NodePair(newNode, newNode);
    	}
    	// Add to the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		NodePair pair = insert(node.getLeft(), key);
    		node.setLeft(pair.getFirst());
    		return new NodePair(node, pair.getSecond());
    	}
    	// Add to the right subtree
    	else if (key.compareTo(node.getKey()) > 0) {
    		NodePair pair = insert(node.getRight(), key);
    		node.setRight(pair.getFirst());
    		return new NodePair(node, pair.getSecond());
    	}
    	// Tree has the key on its root
    	else return new NodePair(node, node);
    }
    
    /* private Node insert(Node node, K key) { 
    	//Tree is empty
    	if (node == null) return new Node(key);
    	else {
    		//Splay the tree
    		node = splay(node, key);
    		//Tree doesn't contain the key and key is smaller than the root's key
        	if (key.compareTo(node.getKey()) < 0) {
        		Node newRoot = new Node(key);
        		newRoot.setRight(node);
        		newRoot.setLeft(node.getLeft());
        		node.setLeft(null);
        		return newRoot;
        	}
        	//Tree doesn't contain the key and key is larger than the root's key
        	else if (key.compareTo(node.getKey()) > 0) {
        		Node newRoot = new Node(key);
        		newRoot.setLeft(node);
        		newRoot.setRight(node.getRight());
        		node.setRight(null);
        		return newRoot;
        	}
        	//Tree already contains the key (it is in the root due to the splay)
        	else return node;
    	}
    } */

	@Override
	public void remove(K key) {
		root = null; //remove(root, key);
	}
	/*
	private Node remove(Node node, K key) {
		//Node is empty
    	if (node == null) return null;
    	//Delete on the left subtree
    	else if (key.compareTo(node.getKey()) < 0) node.setLeft(remove(node.getLeft(), key));
    	//Delete on the right subtree
        else if (key.compareTo(node.getKey()) > 0) node.setRight(remove(node.getRight(), key));
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
    		}
        }
    		
    	node.updateHeight();
    	
    	//Rebalance if needed
    	return rebalance(node);
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
				curLevelNodes = (LinkedList<SplayTree<K>.Node>) nextLevelNodes.clone();
			}
			while (!nextLevelNodes.isEmpty());
		}

		writer.println("}");
		writer.close();
	}

	private Node rotateLeft(Node x) {
        Node y = x.getRight();
        if(y == null) return x;
        Node z = y.getLeft();

        y.setParent(x.getParent());
        
        y.setLeft(x);
        x.setRight(z);

        return y;
    }
    
	private Node rotateRight(Node x) {
        Node y = x.getLeft();
        if(y == null) return x;
        Node z = y.getRight();
        
        y.setParent(x.getParent());
        
        y.setRight(x);
        x.setLeft(z);

        return y;
    }
	
	private void splay(Node node) {
		if(node == null) return;
		
		while(node.getParent() != null) {
			Node parent = node.getParent();
			Node grandParent = parent.getParent();
			//Do a simple rotation (this is done at most once at the end)
			if(grandParent == null) {
				//Zig (left)
				if(node.getKey().compareTo(parent.getKey()) < 0) parent = rotateRight(parent);
				//Zag (right)
				else parent = rotateLeft(parent);
			}
			//Do a double-rotation
			else {	
				if(parent.getKey().compareTo(grandParent.getKey()) < 0) {
					//Zig-zig (left-left)
					if(node.getKey().compareTo(parent.getKey()) < 0) grandParent = rotateRight(grandParent);
					//Zig-zag (left-right)
					else parent = rotateLeft(parent);
					grandParent = rotateRight(grandParent);
				}
				else {
					//Zag-zig (right-left)
					if(node.getKey().compareTo(parent.getKey()) < 0) parent = rotateRight(parent);
					//Zag-zag (right-right)
					else grandParent = rotateLeft(grandParent);
					grandParent = rotateLeft(grandParent);
				}
			}
		}
		root = node;
	}
}

/*public class SplayTree<K extends Comparable<K> ,V> extends BST<K,V> {
    @Override
    protected Node find(Node node, K key) {
    	//Node is empty
    	if(node == null) return null;
    	//Tree has the key on its root
    	else if (key.equals(node.getKey())) return node;
    	//Search the left subtree
    	else if (key.compareTo(node.getKey()) < 0) {
    		//Key is not in the tree
    		if(root.getLeft() == null) return null;
    		// Zig-Zig (Left Left)
    		else if(key < root.getLeft().getKey()) {
    			root.setLeft(root.getLeft());
    		}
    		
    		return find(node.getLeft(), key);
    	}
        //Search the right subtree
        else return find(node.getRight(), key);
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
}*/
