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
		//assertEquals(null, splay.getMin());
		//assertEquals(null, splay.getMax());
		
		splay.insert(4);
		assertEquals("(4,(),())", splay.toString());
		
		assertEquals(new Integer(4), splay.find(4));
		assertEquals(null, splay.find(7));
		assertEquals("(4,(),())", splay.toString());
		
		assertEquals(new Integer(4), splay.getMin());
		assertEquals("(4,(),())", splay.toString());
		assertEquals(new Integer(4), splay.getMax());
		assertEquals("(4,(),())", splay.toString());
		
		splay.insert(7);
		assertEquals("(7,(4,(),()),())", splay.toString());
		
		assertEquals(new Integer(4), splay.find(4));
		assertEquals("(4,(),(7,(),()))", splay.toString());
		
		assertEquals(new Integer(7), splay.find(7));
		assertEquals("(7,(4,(),()),())", splay.toString());
	
		assertEquals(new Integer(7), splay.getMax());
		assertEquals("(7,(4,(),()),())", splay.toString());
		assertEquals(new Integer(4), splay.getMin());
		assertEquals("(4,(),(7,(),()))", splay.toString());
		
		splay.insert(7);
		assertEquals("(7,(4,(),()),())", splay.toString());
		
		assertEquals(new Integer(4), splay.find(4));
		assertEquals("(4,(),(7,(),()))", splay.toString());
		
		assertEquals(new Integer(7), splay.find(7));
		assertEquals("(7,(4,(),()),())", splay.toString());
		
		splay.insert(10);
		assertEquals("(10,(7,(4,(),()),()),())", splay.toString());
		
		splay.insert(6);
		assertEquals("(6,(4,(),()),(7,(),(10,(),())))", splay.toString());
		
		splay.insert(2);
		assertEquals("(2,(),(4,(),(6,(),(7,(),(10,(),())))))", splay.toString());
		
		splay.insert(5);
		assertEquals("(5,(4,(2,(),()),()),(6,(),(7,(),(10,(),()))))", splay.toString());
		
		assertEquals(new Integer(6), splay.find(6));
		assertEquals("(6,(5,(4,(2,(),()),()),()),(7,(),(10,(),())))", splay.toString());
		
		assertEquals(new Integer(2), splay.find(2));
		assertEquals("(2,(),(5,(4,(),()),(6,(),(7,(),(10,(),())))))", splay.toString());
		
		assertEquals(new Integer(7), splay.find(7));
		assertEquals("(7,(5,(2,(),(4,(),())),(6,(),())),(10,(),()))", splay.toString());
		
		assertEquals(null, splay.find(9));
		assertEquals("(10,(7,(5,(2,(),(4,(),())),(6,(),())),()),())", splay.toString());
		
		assertEquals(new Integer(10), splay.getMax());
		assertEquals("(10,(7,(5,(2,(),(4,(),())),(6,(),())),()),())", splay.toString());
		
		splay.dump("test.txt");

		splay.remove(10);
		assertEquals("(7,(5,(2,(),(4,(),())),(6,(),())),())", splay.toString());
		
		splay.remove(7);
		assertEquals("(6,(5,(2,(),(4,(),())),()),())", splay.toString());
		assertEquals(null, splay.find(7));
		assertEquals("(6,(5,(2,(),(4,(),())),()),())", splay.toString());
		
		splay.remove(4);
		assertEquals("(2,(),(5,(),(6,(),())))", splay.toString());
		
		splay.remove(2);
		assertEquals("(5,(),(6,(),()))", splay.toString());
	
		splay.remove(5);
		assertEquals("(6,(),())", splay.toString());
		
		splay.remove(6);
		assertEquals("()", splay.toString());
		assertEquals(null, splay.find(6));
		assertEquals(null, splay.find(4));
		assertEquals(null, splay.find(7));
		assertEquals(null, splay.getMin());
		assertEquals(null, splay.getMax());
		assertEquals("()", splay.toString());
		
		splay.remove(6);
		assertEquals("()", splay.toString());
		assertEquals(null, splay.find(6));
		assertEquals(null, splay.find(4));
		assertEquals(null, splay.find(7));
		assertEquals(null, splay.getMin());
		assertEquals(null, splay.getMax());
		assertEquals("()", splay.toString());
	}
	
	@Test
	public void testAscending() {
		SplayTree<Integer> splay = new SplayTree<Integer>();
		
		splay.insert(1);
		assertEquals("(1,(),())", splay.toString());
		
		splay.insert(2);
		assertEquals("(2,(1,(),()),())", splay.toString());
		
		splay.insert(3);
		assertEquals("(3,(2,(1,(),()),()),())", splay.toString());
		
		splay.insert(4);
		assertEquals("(4,(3,(2,(1,(),()),()),()),())", splay.toString());
		
		splay.insert(5);
		assertEquals("(5,(4,(3,(2,(1,(),()),()),()),()),())", splay.toString());
		
		splay.insert(6);
		assertEquals("(6,(5,(4,(3,(2,(1,(),()),()),()),()),()),())", splay.toString());
		
		splay.insert(7);
		assertEquals("(7,(6,(5,(4,(3,(2,(1,(),()),()),()),()),()),()),())", splay.toString());
		assertEquals(new Integer(1), splay.getMin());
		assertEquals("(1,(),(6,(4,(2,(),(3,(),())),(5,(),())),(7,(),())))", splay.toString());
		assertEquals(new Integer(7), splay.getMax());
		assertEquals("(7,(6,(1,(),(4,(2,(),(3,(),())),(5,(),()))),()),())", splay.toString());
		
		splay.remove(8);
		assertEquals("(7,(6,(1,(),(4,(2,(),(3,(),())),(5,(),()))),()),())", splay.toString());
		
		splay.remove(6);
		assertEquals("(5,(4,(1,(),(2,(),(3,(),()))),()),(7,(),()))", splay.toString());
		
		System.out.println(splay.toString());
		
		splay.dump("test.txt");
	}
	
	@Test
	public void testString() {
		SplayTree<String> splay = new SplayTree<String>();
		
		assertEquals("()", splay.toString());
		assertEquals(null, splay.find("goncalo"));
		assertEquals(null, splay.getMin());
		assertEquals(null, splay.getMax());
		
		splay.insert("goncalo");
		assertEquals("(goncalo,(),())", splay.toString());
		assertEquals("goncalo", splay.find("goncalo"));
		assertEquals(null, splay.find("matheus"));
		assertEquals("goncalo", splay.getMin());
		assertEquals("goncalo", splay.getMax());
		
		splay.insert("pedro");
		assertEquals("(pedro,(goncalo,(),()),())", splay.toString());
		assertEquals("goncalo", splay.find("goncalo"));
		assertEquals("(goncalo,(),(pedro,(),()))", splay.toString());
		assertEquals("pedro", splay.find("pedro"));
		assertEquals("(pedro,(goncalo,(),()),())", splay.toString());
		assertEquals("goncalo", splay.getMin());
		assertEquals("(goncalo,(),(pedro,(),()))", splay.toString());
		assertEquals("pedro", splay.getMax());
		assertEquals("(pedro,(goncalo,(),()),())", splay.toString());
		
		splay.insert("matheus");
		assertEquals("(matheus,(goncalo,(),()),(pedro,(),()))", splay.toString());

		splay.remove("matheus");
		assertEquals("(goncalo,(),(pedro,(),()))", splay.toString());
		assertEquals("goncalo", splay.find("goncalo"));
		assertEquals("(goncalo,(),(pedro,(),()))", splay.toString());
		assertEquals("pedro", splay.find("pedro"));
		assertEquals("(pedro,(goncalo,(),()),())", splay.toString());
		assertEquals(null, splay.find("matheus"));
		assertEquals("(goncalo,(),(pedro,(),()))", splay.toString());
		
		splay.remove("pedro");
		assertEquals("(goncalo,(),())", splay.toString());
		assertEquals("goncalo", splay.find("goncalo"));
		assertEquals(null, splay.find("matheus"));
		assertEquals(null, splay.find("pedro"));
		
		splay.remove("goncalo");
		assertEquals("()", splay.toString());
		assertEquals(null, splay.find("goncalo"));
		assertEquals(null, splay.find("matheus"));
		assertEquals(null, splay.find("pedro"));
		
		splay.remove("goncalo");
		assertEquals("()", splay.toString());
		assertEquals(null, splay.find("goncalo"));
		assertEquals(null, splay.find("matheus"));
		assertEquals(null, splay.find("pedro"));
		
		System.out.println(splay.toString());
		
		splay.dump("test.txt");
	}
}
