//Top-down splay tree: http://digital.cs.usu.edu/~allan/DS/Notes/Ch22.pdf
//Codigo util: http://www.link.cs.cmu.edu/link/ftp-site/splaying/SplayTree.java

package test;

import structures.SplayTree;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestSplayTree {

	@Test
	public void testRandom() {
		SplayTree<Integer> splay = new SplayTree<Integer>();
		
		assertEquals("()", splay.toString());
		assertEquals(null, splay.find(4));
		assertEquals(null, splay.getMin());
		assertEquals(null, splay.getMax());
		
		splay.insert(4);
		assertEquals("(4,(),())", splay.toString());
		
		assertEquals(new Integer(4), splay.find(4));
		assertEquals(null, splay.find(7));
		assertEquals("(4,(),())", splay.toString());
		
		//assertEquals(new Integer(4), splay.getMin());
		//assertEquals(new Integer(4), splay.getMax());
		
		splay.insert(7);
		assertEquals("(7,(4,(),()),())", splay.toString());
		
		assertEquals(new Integer(4), splay.find(4));
		assertEquals("(4,(),(7,(),()))", splay.toString());
		
		assertEquals(new Integer(7), splay.find(7));
		assertEquals("(7,(4,(),()),())", splay.toString());
		
		//assertEquals(new Integer(4), splay.getMin());
		//assertEquals(new Integer(7), splay.getMax());
		
		splay.insert(7);
		assertEquals("(7,(4,(),()),())", splay.toString());
		
		assertEquals(new Integer(4), splay.find(4));
		assertEquals("(4,(),(7,(),()))", splay.toString());
		
		assertEquals(new Integer(7), splay.find(7));
		assertEquals("(7,(4,(),()),())", splay.toString());
		
		//assertEquals(new Integer(4), splay.getMin());
		//assertEquals(new Integer(7), splay.getMax());
		
		splay.insert(10);
		assertEquals("(10,(7,(4,(),()),()),())", splay.toString());
		
		SplayTree.Node n10 = splay.root;
		SplayTree.Node n7 = n10.getLeft();
		SplayTree.Node n4 = n7.getLeft();
		
		System.out.println("n10 = " + n10);
		System.out.println("n7 = " + n7);
		System.out.println("n4 = " + n4);
		
		System.out.println("n10 = " + n10);
		System.out.println("n10 = " + n10);
		System.out.println("n10 = " + n10);
		
		/*
		splay.insert(6);
		assertEquals("(6,(4,(),()),(10,(7,(),()),()))", splay.toString());
		
		splay.insert(2);
		assertEquals("(2,(),(4,(),(6,(),(10,(7,(),()),()))))", splay.toString());
		
		splay.insert(5);
		assertEquals("(5,(2,(),(4,(),())),(6,(),(10,(7,(),()),())))", splay.toString());
		
		assertEquals(new Integer(6), splay.find(6));
		assertEquals("(6,(5,(2,(),(4,(),()))),(10,(7,(),()),()))", splay.toString());
		
		assertEquals(new Integer(2), splay.find(2));
		assertEquals("(2,(),(5,(4,(),()),(6,(),(10,(7,(),()),()))))", splay.toString());
		
		assertEquals(new Integer(7), splay.find(7));
		assertEquals("(7,(5,(2,(),(4,(),())),(6,(),())),(10,(),()))", splay.toString());
		
		assertEquals(null, splay.find(9));
		assertEquals("(10,(7,(5,(2,(),(4,(),())),(6,(),())),()),())", splay.toString());
		
		//assertEquals(new Integer(2), splay.getMin());
		//assertEquals(new Integer(10), splay.getMax());
		
		splay.dump("test.txt");
		*/
		/*
		splay.remove(10);
		assertEquals("(4,(2,(),()),(7,(6,(5,(),()),()),()))", splay.toString());
		assertEquals(new Integer(6), splay.find(6));
		assertEquals(new Integer(2), splay.find(2));
		assertEquals(new Integer(7), splay.find(7));
		
		splay.remove(7);
		assertEquals("(4,(2,(),()),(6,(5,(),()),()))", splay.toString());
		assertEquals(new Integer(6), splay.find(6));
		assertEquals(new Integer(2), splay.find(2));
		assertEquals(null, splay.find(7));
		
		splay.remove(4);
		assertEquals("(2,(),(6,(5,(),()),()))", splay.toString());
		assertEquals(new Integer(6), splay.find(6));
		assertEquals(null, splay.find(4));
		assertEquals(null, splay.find(7));
		assertEquals(new Integer(2), splay.getMin());
		assertEquals(new Integer(6), splay.getMax());
		
		splay.remove(2);
		assertEquals("(6,(5,(),()),())", splay.toString());
		assertEquals(new Integer(6), splay.find(6));
		assertEquals(null, splay.find(4));
		assertEquals(null, splay.find(7));
		assertEquals(new Integer(5), splay.getMin());
		assertEquals(new Integer(6), splay.getMax());
		
		splay.remove(5);
		assertEquals("(6,(),())", splay.toString());
		assertEquals(new Integer(6), splay.find(6));
		assertEquals(null, splay.find(4));
		assertEquals(null, splay.find(7));
		assertEquals(new Integer(6), splay.getMin());
		assertEquals(new Integer(6), splay.getMax());
		
		splay.remove(6);
		assertEquals("()", splay.toString());
		assertEquals(null, splay.find(6));
		assertEquals(null, splay.find(4));
		assertEquals(null, splay.find(7));
		assertEquals(null, splay.getMin());
		assertEquals(null, splay.getMax());
		
		splay.remove(6);
		assertEquals("()", splay.toString());
		assertEquals(null, splay.find(6));
		assertEquals(null, splay.find(4));
		assertEquals(null, splay.find(7));
		assertEquals(null, splay.getMin());
		assertEquals(null, splay.getMax());
		*/
	}
	
	/*
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
		
		bst.insert(5);
		assertEquals("(1,(),(2,(),(3,(),(4,(),(5,(),())))))", bst.toString());
		
		bst.insert(6);
		assertEquals("(1,(),(2,(),(3,(),(4,(),(5,(),(6,(),()))))))", bst.toString());
		
		bst.insert(7);
		assertEquals("(1,(),(2,(),(3,(),(4,(),(5,(),(6,(),(7,(),())))))))", bst.toString());
		assertEquals(new Integer(1), bst.getMin());
		assertEquals(new Integer(7), bst.getMax());
		
		bst.remove(8);
		assertEquals("(1,(),(2,(),(3,(),(4,(),(5,(),(6,(),(7,(),())))))))", bst.toString());
		assertEquals(new Integer(1), bst.getMin());
		assertEquals(new Integer(7), bst.getMax());
		
		bst.remove(6);
		assertEquals("(1,(),(2,(),(3,(),(4,(),(5,(),(7,(),()))))))", bst.toString());
		assertEquals(new Integer(1), bst.getMin());
		assertEquals(new Integer(7), bst.getMax());
		
		System.out.println(bst.toString());
		
		bst.dump("test.txt");
	}
	
	@Test
	public void testString() {
		SimpleBST<String> bst = new SimpleBST<String>();
		
		assertEquals("()", bst.toString());
		assertEquals(null, bst.find("goncalo"));
		assertEquals(null, bst.getMin());
		assertEquals(null, bst.getMax());
		
		bst.insert("goncalo");
		assertEquals("(goncalo,(),())", bst.toString());
		assertEquals("goncalo", bst.find("goncalo"));
		assertEquals(null, bst.find("matheus"));
		assertEquals("goncalo", bst.getMin());
		assertEquals("goncalo", bst.getMax());
		
		bst.insert("pedro");
		assertEquals("(goncalo,(),(pedro,(),()))", bst.toString());
		assertEquals("goncalo", bst.find("goncalo"));
		assertEquals("pedro", bst.find("pedro"));
		assertEquals("goncalo", bst.getMin());
		assertEquals("pedro", bst.getMax());
		
		bst.insert("matheus");
		assertEquals("(goncalo,(),(pedro,(matheus,(),()),()))", bst.toString());
		assertEquals("goncalo", bst.getMin());
		assertEquals("pedro", bst.getMax());

		bst.remove("matheus");
		assertEquals("(goncalo,(),(pedro,(),()))", bst.toString());
		assertEquals("goncalo", bst.find("goncalo"));
		assertEquals("pedro", bst.find("pedro"));
		assertEquals(null, bst.find("matheus"));
		
		bst.remove("pedro");
		assertEquals("(goncalo,(),())", bst.toString());
		assertEquals("goncalo", bst.find("goncalo"));
		assertEquals(null, bst.find("matheus"));
		assertEquals(null, bst.find("pedro"));
		
		bst.remove("goncalo");
		assertEquals("()", bst.toString());
		assertEquals(null, bst.find("goncalo"));
		assertEquals(null, bst.find("matheus"));
		assertEquals(null, bst.find("pedro"));
		
		bst.remove("goncalo");
		assertEquals("()", bst.toString());
		assertEquals(null, bst.find("goncalo"));
		assertEquals(null, bst.find("matheus"));
		assertEquals(null, bst.find("pedro"));
		
		System.out.println(bst.toString());
		
		bst.dump("test.txt");
	}
	*/
}
