package test;

import structures.SimpleBST;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestSimpleBST {

	@Test
	public void test() {
		SimpleBST<Integer> bst = new SimpleBST<Integer>();
		
		assertEquals("()", bst.toString());
		
		bst.insert(4);
		bst.insert(7);
		bst.insert(10);
		bst.insert(6);
		bst.insert(2);
		bst.insert(5);
		bst.remove(7);
		bst.remove(2);
		
		System.out.println(bst.toString());
		
		bst.dump("test.txt");
	}

}
