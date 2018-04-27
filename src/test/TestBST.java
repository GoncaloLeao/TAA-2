package test;

import structures.SimpleBST;

import org.junit.Test;

public class TestBST {

	
	@Test
	public void test() {
		SimpleBST<Integer> bst = new SimpleBST<Integer>();
		/*bst.insert(1, 2);
		bst.insert(1, 2);
		bst.find(1);
		bst.find(2);
		bst.insert(2, 0);
		
		bst.insert(3, 0);
		bst.insert(-3, 0);
		bst.insert(30, 0);
		bst.insert(50, 0);
		bst.insert(20, 0);
		bst.insert(65, 0);
		
		AVLTree<Integer,Integer>.Node node3 = bst.find(3);
		node3.setRight(bst.rotateLeft(bst.find(30)));
		
		bst.root = bst.rotateRight(bst.root);
		*/
		
		bst.insert(4);
		bst.insert(7);
		bst.insert(10);
		bst.insert(6);
		bst.insert(2);
		bst.insert(5);
		bst.remove(7);
		bst.remove(2);
		
		bst.dump("test.txt");
	}

}
