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
		assertEquals("([1]4,(),())", avl.toString());
		assertEquals(new Integer(4), avl.find(4));
		assertEquals(null, avl.find(7));
		assertEquals(new Integer(4), avl.getMin());
		assertEquals(new Integer(4), avl.getMax());
		
		avl.insert(7);
		assertEquals("([2]4,(),([1]7,(),()))", avl.toString());
		assertEquals(new Integer(4), avl.find(4));
		assertEquals(new Integer(7), avl.find(7));
		assertEquals(new Integer(4), avl.getMin());
		assertEquals(new Integer(7), avl.getMax());
		
		avl.insert(7);
		assertEquals("([2]4,(),([1]7,(),()))", avl.toString());
		assertEquals(new Integer(4), avl.find(4));
		assertEquals(new Integer(7), avl.find(7));
		assertEquals(new Integer(4), avl.getMin());
		assertEquals(new Integer(7), avl.getMax());
		
		avl.insert(10);
		assertEquals("([2]7,([1]4,(),()),([1]10,(),()))", avl.toString());
		
		avl.insert(6);
		assertEquals("([3]7,([2]4,(),([1]6,(),())),([1]10,(),()))", avl.toString());
		
		avl.insert(2);
		assertEquals("([3]7,"
				+ 		"([2]4,([1]2,(),()),([1]6,(),())),"
				+ 		"([1]10,(),())"
				+ 		")", avl.toString());
		
		avl.insert(5);
		assertEquals("([3]6,"
				+ 		"([2]4,([1]2,(),()),([1]5,(),())),"
				+ 		"([2]7,(),([1]10,(),()))"
				+		")",
				avl.toString());
		assertEquals(new Integer(6), avl.find(6));
		assertEquals(new Integer(2), avl.find(2));
		assertEquals(new Integer(7), avl.find(7));
		assertEquals(null, avl.find(9));
		assertEquals(new Integer(2), avl.getMin());
		assertEquals(new Integer(10), avl.getMax());
		
		avl.dump("test.txt");
		
		avl.remove(7);
		assertEquals("([3]6,"
				+ 		"([2]4,([1]2,(),()),([1]5,(),())),"
				+ 		"([1]10,(),())"
				+		")",
				avl.toString());
		assertEquals(new Integer(6), avl.find(6));
		assertEquals(new Integer(2), avl.find(2));
		assertEquals(null, avl.find(7));
		
		avl.remove(10);
		assertEquals("([3]4,"
				+ 		"([1]2,(),()),"
				+ 		"([2]6,([1]5,(),()),())"
				+		")",
				avl.toString());
		assertEquals(new Integer(6), avl.find(6));
		assertEquals(new Integer(2), avl.find(2));
		assertEquals(null, avl.find(7));
		
		avl.remove(4);
		assertEquals("([2]5,"
				+ 		"([1]2,(),()),"
				+ 		"([1]6,(),())"
				+		")",
				avl.toString());
		assertEquals(new Integer(6), avl.find(6));
		assertEquals(null, avl.find(4));
		assertEquals(null, avl.find(7));
		assertEquals(new Integer(2), avl.getMin());
		assertEquals(new Integer(6), avl.getMax());
		
		avl.remove(2);
		assertEquals("([2]5,(),([1]6,(),()))", avl.toString());
		assertEquals(new Integer(6), avl.find(6));
		assertEquals(null, avl.find(4));
		assertEquals(null, avl.find(7));
		assertEquals(new Integer(5), avl.getMin());
		assertEquals(new Integer(6), avl.getMax());
		
		avl.remove(5);
		assertEquals("([1]6,(),())", avl.toString());
		assertEquals(new Integer(6), avl.find(6));
		assertEquals(null, avl.find(4));
		assertEquals(null, avl.find(7));
		assertEquals(new Integer(6), avl.getMin());
		assertEquals(new Integer(6), avl.getMax());
		
		avl.remove(6);
		assertEquals("()", avl.toString());
		assertEquals(null, avl.find(6));
		assertEquals(null, avl.find(4));
		assertEquals(null, avl.find(7));
		assertEquals(null, avl.getMin());
		assertEquals(null, avl.getMax());
		
		avl.remove(6);
		assertEquals("()", avl.toString());
		assertEquals(null, avl.find(6));
		assertEquals(null, avl.find(4));
		assertEquals(null, avl.find(7));
		assertEquals(null, avl.getMin());
		assertEquals(null, avl.getMax());	
	}
	
	@Test
	public void testAscending() {
		AVLTree<Integer> avl = new AVLTree<Integer>();
		
		avl.insert(1);
		assertEquals("([1]1,(),())", avl.toString());
		
		avl.insert(2);
		assertEquals("([2]1,(),([1]2,(),()))", avl.toString());
		
		avl.insert(3);
		assertEquals("([2]2,([1]1,(),()),([1]3,(),()))", avl.toString());
		
		avl.insert(4);
		assertEquals("([3]2,([1]1,(),()),([2]3,(),([1]4,(),())))", avl.toString());
		
		avl.insert(5);
		assertEquals("([3]2,([1]1,(),()),([2]4,([1]3,(),()),([1]5,(),())))", avl.toString());
		
		avl.insert(6);
		assertEquals("([3]4,([2]2,([1]1,(),()),([1]3,(),())),([2]5,(),([1]6,(),())))", avl.toString());
		
		avl.insert(7);
		assertEquals("([3]4,([2]2,([1]1,(),()),([1]3,(),())),([2]6,([1]5,(),()),([1]7,(),())))", avl.toString());
		assertEquals(new Integer(1), avl.getMin());
		assertEquals(new Integer(7), avl.getMax());
		
		System.out.println(avl.toString());
		
		avl.dump("test.txt");
	}
	
	@Test
	public void testString() {
		AVLTree<String> avl = new AVLTree<String>();
		
		assertEquals("()", avl.toString());
		assertEquals(null, avl.find("goncalo"));
		assertEquals(null, avl.getMin());
		assertEquals(null, avl.getMax());
		
		avl.insert("goncalo");
		assertEquals("([1]goncalo,(),())", avl.toString());
		assertEquals("goncalo", avl.find("goncalo"));
		assertEquals(null, avl.find("matheus"));
		assertEquals("goncalo", avl.getMin());
		assertEquals("goncalo", avl.getMax());
		
		avl.insert("pedro");
		assertEquals("([2]goncalo,(),([1]pedro,(),()))", avl.toString());
		assertEquals("goncalo", avl.find("goncalo"));
		assertEquals("pedro", avl.find("pedro"));
		assertEquals("goncalo", avl.getMin());
		assertEquals("pedro", avl.getMax());
		
		avl.insert("matheus");
		assertEquals("([2]matheus,([1]goncalo,(),()),([1]pedro,(),()))", avl.toString());
		assertEquals("goncalo", avl.getMin());
		assertEquals("pedro", avl.getMax());

		avl.remove("matheus");
		assertEquals("([2]goncalo,(),([1]pedro,(),()))", avl.toString());
		assertEquals("goncalo", avl.find("goncalo"));
		assertEquals("pedro", avl.find("pedro"));
		assertEquals(null, avl.find("matheus"));
		
		avl.remove("pedro");
		assertEquals("([1]goncalo,(),())", avl.toString());
		assertEquals("goncalo", avl.find("goncalo"));
		assertEquals(null, avl.find("matheus"));
		assertEquals(null, avl.find("pedro"));
		
		avl.remove("goncalo");
		assertEquals("()", avl.toString());
		assertEquals(null, avl.find("goncalo"));
		assertEquals(null, avl.find("matheus"));
		assertEquals(null, avl.find("pedro"));
		
		avl.remove("goncalo");
		assertEquals("()", avl.toString());
		assertEquals(null, avl.find("goncalo"));
		assertEquals(null, avl.find("matheus"));
		assertEquals(null, avl.find("pedro"));
		
		System.out.println(avl.toString());
		
		avl.dump("test.txt");
	}

}
