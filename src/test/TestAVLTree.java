package test;

import structures.AVLTree;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestAVLTree {

	@Test
	public void testRandom() {
		AVLTree<Integer> avl = new AVLTree<Integer>();
		
		assertEquals("()", avl.toString());
		assertEquals(null, avl.find(4));
		assertEquals(null, avl.getMin());
		assertEquals(null, avl.getMax());
		
		avl.insert(4);
		assertEquals("([0]4,(),())", avl.toString());
		assertEquals(new Integer(4), avl.find(4));
		assertEquals(null, avl.find(7));
		assertEquals(new Integer(4), avl.getMin());
		assertEquals(new Integer(4), avl.getMax());
		
		avl.insert(7);
		assertEquals("([-1]4,(),([0]7,(),()))", avl.toString());
		assertEquals(new Integer(4), avl.find(4));
		assertEquals(new Integer(7), avl.find(7));
		assertEquals(new Integer(4), avl.getMin());
		assertEquals(new Integer(7), avl.getMax());
		
		avl.insert(10);
		assertEquals("([0]7,([0]4,(),()),([0]10,(),()))", avl.toString());
		
		avl.insert(6);
		assertEquals("([1]7,([-1]4,(),([0]6,(),())),([0]10,(),()))", avl.toString());
		
		avl.insert(2);
		assertEquals("([1]7,([-1]4,(([0]2,(),())),([0]6,(),())),([0]10,(),()))", avl.toString());
		
		avl.insert(5);
		assertEquals("([0]6,([0]4,([0]2,(),()),([0]5,(),())),([1]7,([-1]7,(),([0]10,(),())))", avl.toString());
		assertEquals(new Integer(6), avl.find(6));
		assertEquals(new Integer(2), avl.find(2));
		assertEquals(new Integer(7), avl.find(7));
		assertEquals(null, avl.find(9));
		assertEquals(new Integer(2), avl.getMin());
		assertEquals(new Integer(10), avl.getMax());
		
		/*
		avl.remove(7);
		assertEquals("(4,(2,(),()),(6,(5,(),()),(10,(),())))", avl.toString());
		assertEquals(new Integer(6), avl.find(6));
		assertEquals(new Integer(2), avl.find(2));
		assertEquals(null, avl.find(7));
		
		avl.remove(2);
		assertEquals("(4,(),(6,(5,(),()),(10,(),())))", avl.toString());
		assertEquals(new Integer(6), avl.find(6));
		assertEquals(null, avl.find(2));
		assertEquals(null, avl.find(7));
		assertEquals(new Integer(4), avl.getMin());
		assertEquals(new Integer(10), avl.getMax());
		*/
		
		System.out.println(avl.toString());
		
		avl.dump("test.txt");
	}
	
	@Test
	public void testAscending() {
		AVLTree<Integer> avl = new AVLTree<Integer>();
		
		avl.insert(1);
		assertEquals("(1,(),())", avl.toString());
		
		avl.insert(2);
		assertEquals("(1,(),(2,(),()))", avl.toString());
		
		avl.insert(3);
		assertEquals("(1,(),(2,(),(3,(),())))", avl.toString());
		
		avl.insert(4);
		assertEquals("(1,(),(2,(),(3,(),(4,(),()))))", avl.toString());
		
		avl.insert(6);
		assertEquals("(1,(),(2,(),(3,(),(4,(),(6,(),())))))", avl.toString());
		assertEquals(new Integer(1), avl.getMin());
		assertEquals(new Integer(6), avl.getMax());
		
		System.out.println(avl.toString());
		
		avl.dump("test.txt");
	}

}
