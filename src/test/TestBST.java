package test;

import structures.AVLTree;
import structures.BST;
import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.Test;

public class TestBST {

	
	@Test
	public void test() {
		BST<Integer,Integer> bst = new BST<Integer,Integer>();
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
		
		bst.insert(4, null);
		bst.insert(7, null);
		bst.insert(10, null);
		bst.insert(6, null);
		bst.insert(2, null);
		bst.insert(5, null);
		bst.delete(7);
		bst.delete(2);
		
		PrintStream p = new PrintStream(System.out);
		bst.print(p);
		p.println();
		
		bst.dump("test.txt");
	}

}
