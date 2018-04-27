package test;

import structures.SimpleBST;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestSimpleBST {

	@Test
	public void testRandom() {
		SimpleBST<Integer> bst = new SimpleBST<Integer>();
		
		assertEquals("()", bst.toString());
		assertEquals(null, bst.find(4));
		assertEquals(null, bst.getMin());
		assertEquals(null, bst.getMax());
		
		bst.insert(4);
		assertEquals("(4,(),())", bst.toString());
		assertEquals(new Integer(4), bst.find(4));
		assertEquals(null, bst.find(7));
		assertEquals(new Integer(4), bst.getMin());
		assertEquals(new Integer(4), bst.getMax());
		
		bst.insert(7);
		assertEquals("(4,(),(7,(),()))", bst.toString());
		assertEquals(new Integer(4), bst.find(4));
		assertEquals(new Integer(7), bst.find(7));
		assertEquals(new Integer(4), bst.getMin());
		assertEquals(new Integer(7), bst.getMax());
		
		bst.insert(10);
		assertEquals("(4,(),(7,(),(10,(),())))", bst.toString());
		
		bst.insert(6);
		assertEquals("(4,(),(7,(6,(),()),(10,(),())))", bst.toString());
		
		bst.insert(2);
		assertEquals("(4,(2,(),()),(7,(6,(),()),(10,(),())))", bst.toString());
		
		bst.insert(5);
		assertEquals("(4,(2,(),()),(7,(6,(5,(),()),()),(10,(),())))", bst.toString());
		assertEquals(new Integer(6), bst.find(6));
		assertEquals(new Integer(2), bst.find(2));
		assertEquals(new Integer(7), bst.find(7));
		assertEquals(null, bst.find(9));
		assertEquals(new Integer(2), bst.getMin());
		assertEquals(new Integer(10), bst.getMax());
		
		bst.remove(7);
		assertEquals("(4,(2,(),()),(6,(5,(),()),(10,(),())))", bst.toString());
		assertEquals(new Integer(6), bst.find(6));
		assertEquals(new Integer(2), bst.find(2));
		assertEquals(null, bst.find(7));
		
		bst.remove(2);
		assertEquals("(4,(),(6,(5,(),()),(10,(),())))", bst.toString());
		assertEquals(new Integer(6), bst.find(6));
		assertEquals(null, bst.find(2));
		assertEquals(null, bst.find(7));
		assertEquals(new Integer(4), bst.getMin());
		assertEquals(new Integer(10), bst.getMax());
		
		System.out.println(bst.toString());
		
		bst.dump("test.txt");
	}
	
	@Test
	public void testAscending() {
		SimpleBST<Integer> bst = new SimpleBST<Integer>();
		
		bst.insert(1);
		assertEquals("(1,(),())", bst.toString());
		
		bst.insert(2);
		assertEquals("(1,(),(2,(),()))", bst.toString());
		
		bst.insert(3);
		assertEquals("(1,(),(2,(),(3,(),())))", bst.toString());
		
		bst.insert(4);
		assertEquals("(1,(),(2,(),(3,(),(4,(),()))))", bst.toString());
		
		bst.insert(6);
		assertEquals("(1,(),(2,(),(3,(),(4,(),(6,(),())))))", bst.toString());
		assertEquals(new Integer(1), bst.getMin());
		assertEquals(new Integer(6), bst.getMax());
		
		System.out.println(bst.toString());
		
		bst.dump("test.txt");
	}

}
