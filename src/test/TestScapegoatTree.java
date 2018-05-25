package test;

import static org.junit.Assert.assertEquals;
import java.util.ArrayDeque;

import org.junit.Test;

import structures.ScapegoatTree;

public class TestScapegoatTree {

	@Test
	public void testInsertRebalanceAll() {
		ScapegoatTree<Integer> sc = new ScapegoatTree<Integer>(0.57);

		assertEquals("()", sc.toString());
		assertEquals(null, sc.find(4));
		assertEquals(null, sc.getMin());
		assertEquals(null, sc.getMax());

		sc.insert(8);
		checkParentsAreConsistent(sc);
		assertEquals("(8,(),())", sc.toString());
		assertEquals(new Integer(8), sc.find(8));
		assertEquals(null, sc.find(7));
		assertEquals(new Integer(8), sc.getMin());
		assertEquals(new Integer(8), sc.getMax());

		sc.insert(1);
		checkParentsAreConsistent(sc);
		assertEquals("(8,(1,(),()),())", sc.toString());

		sc.insert(13);
		checkParentsAreConsistent(sc);
		assertEquals("(8,(1,(),()),(13,(),()))", sc.toString());

		sc.insert(10);
		checkParentsAreConsistent(sc);
		assertEquals("(8,(1,(),()),(13,(10,(),()),()))", sc.toString());

		sc.insert(20);
		checkParentsAreConsistent(sc);
		assertEquals("(8,(1,(),()),(13,(10,(),()),(20,(),())))", sc.toString());

		sc.insert(19);
		checkParentsAreConsistent(sc);
		assertEquals("(8,(1,(),()),(13,(10,(),()),(20,(19,(),()),())))", sc.toString());

		sc.insert(22);
		checkParentsAreConsistent(sc);
		assertEquals("(8,(1,(),()),(13,(10,(),()),(20,(19,(),()),(22,(),()))))", sc.toString());

		//The tree gets rebalanced here
		sc.insert(29);
		checkParentsAreConsistent(sc);
		assertEquals("(19,(10,(8,(1,(),()),()),(13,(),())),(22,(20,(),()),(29,(),())))", sc.toString());

		sc.insert(29);
		checkParentsAreConsistent(sc);
		assertEquals("(19,(10,(8,(1,(),()),()),(13,(),())),(22,(20,(),()),(29,(),())))", sc.toString());
	}

	@Test
	public void testInsertRebalancePart() {
		ScapegoatTree<Integer> sc = new ScapegoatTree<Integer>(0.57);

		checkParentsAreConsistent(sc);
		
		assertEquals("()", sc.toString());
		assertEquals(null, sc.find(4));
		assertEquals(null, sc.getMin());
		assertEquals(null, sc.getMax());

		sc.insert(5);
		checkParentsAreConsistent(sc);
		assertEquals("(5,(),())", sc.toString());

		sc.insert(1);
		checkParentsAreConsistent(sc);
		assertEquals("(5,(1,(),()),())", sc.toString());

		sc.insert(7);
		checkParentsAreConsistent(sc);
		assertEquals("(5,(1,(),()),(7,(),()))", sc.toString());

		sc.insert(6);
		checkParentsAreConsistent(sc);
		assertEquals("(5,(1,(),()),(7,(6,(),()),()))", sc.toString());

		sc.insert(9);
		checkParentsAreConsistent(sc);
		assertEquals("(5,(1,(),()),(7,(6,(),()),(9,(),())))", sc.toString());

		sc.insert(10);
		checkParentsAreConsistent(sc);
		assertEquals("(5,(1,(),()),(7,(6,(),()),(9,(),(10,(),()))))", sc.toString());

		//The tree gets rebalanced here
		sc.insert(12);
		checkParentsAreConsistent(sc);
		assertEquals("(5,(1,(),()),(7,(6,(),()),(10,(9,(),()),(12,(),()))))", sc.toString());
	}

	@Test
	public void testRemove() {
		ScapegoatTree<Integer> sc = new ScapegoatTree<Integer>(0.57);

		sc.remove(5);
		checkParentsAreConsistent(sc);
		sc.insert(5);
		checkParentsAreConsistent(sc);
		sc.remove(5);
		checkParentsAreConsistent(sc);
		sc.insert(5);
		checkParentsAreConsistent(sc);
		sc.insert(1);
		checkParentsAreConsistent(sc);
		sc.remove(5);
		checkParentsAreConsistent(sc);
		sc.remove(1);
		checkParentsAreConsistent(sc);
		sc.insert(5);
		checkParentsAreConsistent(sc);
		sc.insert(1);
		checkParentsAreConsistent(sc);
		sc.insert(13);
		checkParentsAreConsistent(sc);
		sc.insert(10);
		checkParentsAreConsistent(sc);
		sc.insert(19);
		checkParentsAreConsistent(sc);
		sc.insert(9);
		checkParentsAreConsistent(sc);
		sc.insert(16);
		checkParentsAreConsistent(sc);
		sc.insert(22);
		checkParentsAreConsistent(sc);
		assertEquals("(5,(1,(),()),(13,(10,(9,(),()),()),(19,(16,(),()),(22,(),()))))", sc.toString());

		sc.remove(9);
		checkParentsAreConsistent(sc);
		assertEquals("(5,(1,(),()),(13,(10,(),()),(19,(16,(),()),(22,(),()))))", sc.toString());

		sc.remove(10);
		checkParentsAreConsistent(sc);
		assertEquals("(5,(1,(),()),(13,(),(19,(16,(),()),(22,(),()))))", sc.toString());

		sc.remove(16);
		checkParentsAreConsistent(sc);
		assertEquals("(5,(1,(),()),(13,(),(19,(),(22,(),()))))", sc.toString());

		sc.remove(1);
		checkParentsAreConsistent(sc);
		assertEquals("(19,(13,(5,(),()),()),(22,(),()))", sc.toString());

		sc.remove(1);
		checkParentsAreConsistent(sc);
		assertEquals("(19,(13,(5,(),()),()),(22,(),()))", sc.toString());

		sc.remove(10);
		checkParentsAreConsistent(sc);
		assertEquals("(19,(13,(5,(),()),()),(22,(),()))", sc.toString());
	}

	public void checkParentsAreConsistent(ScapegoatTree<Integer> sc) {
		ArrayDeque<ScapegoatTree<Integer>.Node> queue = new ArrayDeque<>();

		if (sc.getRoot() != null) {
			assertEquals(null, sc.getRoot().getParent());
			queue.add(sc.getRoot());
		}

		while(!queue.isEmpty()) {
			ScapegoatTree<Integer>.Node top = queue.poll();
			ScapegoatTree<Integer>.Node left = top.getLeft();
			ScapegoatTree<Integer>.Node right = top.getRight();
			
			if(left != null) {
				assertEquals(top.getKey(), left.getParent().getKey());
				queue.add(left);
			}
			if(right != null) {
				assertEquals(top.getKey(), right.getParent().getKey());
				queue.add(right);
			}
		}
	}
}
