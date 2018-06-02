package structures;

import java.util.LinkedList;

/**
 * A self-adjusting binary search tree where after each operation, the refered node in 
 * that operation is bring to the root of the tree. That operations of bring the node 
 * to the root are called Zig-Zag and are based on simple rotations. This structure is
 * amortized logarithmic.
 * 
 * Our implementation is based on one by Danny Sleator.
 * Available in: http://www.link.cs.cmu.edu/link/ftp-site/splaying/SplayTree.java
 *
 * @author Gonçalo Leão & Matheus Rosa
 *
 */
public class SplayTree<K extends Comparable<K>> implements DynamicSet<K> {
	
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
    	
    	public void setKey(K key) { this.key = key; }
    	public void setLeft(Node left) { this.left = left; }
    	public void setRight(Node right) { this.right = right; }
    }
	
	public Node root;
   
	@Override
	public K find(K key) {
		if(root == null) return null;
		else {
			Node found = find(root, key);
			splay(found.getKey());
			if(root.getKey() == key) return root.getKey();
			else return null;
		}
	}
	
	protected Node find(Node node, K key) {
    	//Search the left subtree
    	if (key.compareTo(node.getKey()) < 0) {
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
	
	@Override
	public void insert(K key) {
		root = insert(root, key);
		splay(key);
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
		if(root != null) {
			//Splay the node to delete to the root
			splay(key);
			//Check if the tree truly contains the node to remove
			if(root.getKey() == key) {
				if(root.left == null) {
					root = root.right;
				}
				else {
					Node right = root.getRight();
					root = root.getLeft();
					//Splay the maximum node of the left subtree
					splay(key); 
					//This works since the left subtree does not contain key
					//and all of its nodes are strictly less than key 
					root.setRight(right);
				}
			}
		}
	}
	
	@Override
	public K getMin() {
		if(root == null) return null;
		else {
			Node min = root;
			while(min.getLeft() != null) min = min.getLeft();
			splay(min.getKey());
			return min.getKey();
		}
	}

	@Override
	public K getMax() {
		if(root == null) return null;
		else {
			Node max = root;
			while(max.getRight() != null) max = max.getRight();
			splay(max.getKey());
			return max.getKey();
		}
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
					stringBuilder.append(node.getKey() + " [shape=doublecircle];");
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
				curLevelNodes = (LinkedList<SplayTree<K>.Node>) nextLevelNodes.clone();
			}
			while (!nextLevelNodes.isEmpty());
		}

		stringBuilder.append("}");
		stringBuilder.append("\n");
		return stringBuilder.toString();
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
	
	/**
	 * Moves a node to the root given its key using top-down splaying.
	 * 
	 * @param key Key of the node to splay.
	 */
	private void splay(K key) {
		Node header = new Node(null); //tree whose left and right subtrees will then be used for the reassembling
		Node l = header; //maximum node on the left side of header
		Node r = header; //mininum node on the right side of header
		Node x = root; //current node being processed
		while(true) {
			if(key.compareTo(x.getKey()) < 0) {
				Node y = x.getLeft();
				if(y == null) break;
				//Check if we have the zig-zig case (if not, it is just a simple rotation, zig)
				if(key.compareTo(y.getKey()) < 0) {
					x = rotateRight(x);
					y = x.getLeft();
					if(y == null) break;
				}
				r.setLeft(x);
				r = x;
				x = x.getLeft();
			}
			else if(key.compareTo(x.getKey()) > 0) {
				Node y = x.getRight();
				if(y == null) break;
				//Check if we have the zag-zag case (if not, it is just a simple rotation, zag)
				if(key.compareTo(y.getKey()) > 0) {
					x = rotateLeft(x);
					y = x.getRight();
					if(y == null) break;
				}
				l.setRight(x);
				l = x;
				x = x.getRight();
			}
			else break;
		}
		//Reassemble the tree
		l.setRight(x.getLeft());
		r.setLeft(x.getRight());
		x.setLeft(header.getRight());
		x.setRight(header.getLeft());
		root = x;
	}
}
