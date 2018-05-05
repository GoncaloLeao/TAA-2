package test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayDeque;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import structures.RedBlackTree;

public class TestRedBlackTree {

	private static int nElements;
	private static int maxRand;
	private static Random rand;
	private TreeSet<Integer> set;
	private RedBlackTree<Integer> rb;
	
	@BeforeAll
	public static void setUp() {
		nElements = 10000;
		maxRand = 100000;
		rand = new Random();
	} 
	
	@BeforeEach
	public void setUpTest() {
		rb = new RedBlackTree<Integer>();
		set = new TreeSet<Integer>();
	}
	
	@Test
	public void TestRandomInsertion() {
		// test empty red black
		assertNull(rb.find(1));
		assertNull(rb.getMax());
		assertNull(rb.getMin());
		
		// test random insertion of n elements
		for (int i = 0; i < nElements; i++) {
			int newElement = rand.nextInt(maxRand);
			set.add(newElement);
			rb.insert(newElement);
			
			assertEquals(new Integer(newElement), rb.find(newElement));
			
			checkRedBlackInvariant(rb);
			
			assertEquals(set.first(), rb.getMin());
			assertEquals(set.last(), rb.getMax());
		}
	}
	
	@Test
	public void TestRandomInsertionDeletion() {
		// test empty red black
		assertNull(rb.find(1));
		assertNull(rb.getMax());
		assertNull(rb.getMin());
		
		for (int i = 0; i < nElements; i++) {
			int newElement = rand.nextInt(maxRand);
			int sort = rand.nextInt(100);
			
			if (sort > 50) {
				set.add(newElement);
				rb.insert(newElement);
			} else {
				set.remove(newElement);
				rb.remove(newElement);
			}
			
			if (!set.contains(newElement)) {
				assertNull(rb.find(newElement));
			} else {
				assertEquals(new Integer(newElement), rb.find(newElement));
			}
			
			checkRedBlackInvariant(rb);
			
			if (set.size() > 0) {
				assertEquals(set.first(), rb.getMin());
				assertEquals(set.last(), rb.getMax());
			} else {
				assertNull(rb.getMin());
				assertNull(rb.getMin());
			}
		}
	}
	
	@Test
	public void TestDeletionRoot() {
		rb.insert(10);
		rb.insert(5);
		rb.remove(10);
		assertEquals("([false]5,(),())", rb.toString());
		rb.insert(10);
		rb.remove(5);
		assertEquals("([false]10,(),())", rb.toString());
	}
	
	@Test
	public void TestDeletionSimpleCase() {
		rb.insert(10);
		rb.insert(5);
		rb.insert(15);
		rb.insert(2);
		rb.remove(5);
		assertEquals("([false]10,([false]2,(),()),([false]15,(),()))", rb.toString());
		rb.insert(5);
		rb.remove(2);
		assertEquals("([false]10,([false]5,(),()),([false]15,(),()))", rb.toString());
	}

	@Test
	public void TestDeletionCase11() {
		rb.insert(10);
		rb.insert(15);
		rb.insert(5);
		rb.insert(13);
		rb.insert(12);
		rb.insert(11);
		rb.remove(5);
		assertEquals("([false]13,([true]11,([false]10,(),()),([false]12,(),())),([false]15,(),()))", rb.toString());
	}
	
	@Test
	public void TestDeletionCase12() {
		rb.insert(10);
		rb.insert(15);
		rb.insert(5);
		rb.insert(7);
		rb.insert(8);
		rb.insert(9);
		rb.remove(15);
		assertEquals("([false]7,([false]5,(),()),([true]9,([false]8,(),()),([false]10,(),())))", rb.toString());
	}
	
	@Test
	public void TestDeletionCase21() {
		rb.insert(10);
		rb.insert(15);
		rb.insert(5);
		rb.insert(18);
		rb.insert(12);
		rb.remove(12);
		rb.remove(18);
		rb.remove(5);
		assertEquals("([false]10,(),([true]15,(),()))", rb.toString());
	}
	
	@Test
	public void TestDeletionCase22() {
		rb.insert(10);
		rb.insert(15);
		rb.insert(5);
		rb.insert(18);
		rb.insert(12);
		rb.remove(12);
		rb.remove(18);
		rb.remove(15);
		assertEquals("([false]10,([true]5,(),()),())", rb.toString());
	}
	
	@Test
	public void TestDeletionCase31() {
		rb.insert(10);
		rb.insert(5);
		rb.insert(15);
		rb.insert(13);
		rb.remove(5);
		assertEquals("([false]13,([false]10,(),()),([false]15,(),()))", rb.toString());
	}
	
	@Test
	public void TestDeletionCase32() {
		rb.insert(10);
		rb.insert(5);
		rb.insert(15);
		rb.insert(7);
		rb.remove(15);
		assertEquals("([false]7,([false]5,(),()),([false]10,(),()))", rb.toString());
	}
	
	@Test
	public void TestDeletionCase41() {
		rb.insert(10);
		rb.insert(5);
		rb.insert(15);
		rb.insert(18);
		rb.insert(12);
		rb.remove(5);
		assertEquals("([false]15,([false]10,(),([true]12,(),())),([false]18,(),()))", rb.toString());
	}
	
	@Test
	public void TestDeletionCase42() {
		rb.insert(10);
		rb.insert(5);
		rb.insert(15);
		rb.insert(2);
		rb.insert(7);
		rb.remove(15);
		assertEquals("([false]5,([false]2,(),()),([false]10,([true]7,(),()),()))", rb.toString());
	}
	
	public void checkRedBlackInvariant(RedBlackTree<Integer> rb) {		
		TreeMap<Integer, Integer> node2count = new TreeMap<>();
		Integer numberBlacks = rb.countBlacks();
		ArrayDeque<RedBlackTree<Integer>.Node> queue = new ArrayDeque<>();
		
		if (rb.getRoot().getKey() != null)
			queue.add(rb.getRoot());
		node2count.put(rb.getRoot().hashCode(), 1);
		
		// root property
		assertEquals(false, rb.getRoot().getColor());
		
		while(!queue.isEmpty()) {
			RedBlackTree<Integer>.Node top = queue.poll();
			RedBlackTree<Integer>.Node left = top.getLeft();
			RedBlackTree<Integer>.Node right = top.getRight();
			
			// Red Red property
			if (top.getColor() == true) {
				assertFalse(top.getLeft().getColor());
				assertFalse(top.getRight().getColor());
			}
			
			// Number of blacks property
			int key = top.hashCode();
			if (left.getKey() != null) {
				int isBlack = left.getColor() == false ? 1 : 0;
				node2count.put(left.hashCode(), node2count.get(key)+isBlack);
				queue.add(left);
			} else {
				assertEquals(numberBlacks, node2count.get(key));
			}
			if (right.getKey() != null) {
				int isBlack = right.getColor() == false ? 1 : 0;
				node2count.put(right.hashCode(), node2count.get(key)+isBlack);
				queue.add(right);
			} else {
				assertEquals(numberBlacks, node2count.get(key));
			}
		}
	}

	
}
