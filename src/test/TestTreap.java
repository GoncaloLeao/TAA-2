package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import structures.Treap;

public class TestTreap {
	
	private static int nElements;
	private static int maxRand;
	public static Random rand;
	Treap<Integer> treap;
	TreeSet<Integer> set;
	
	@BeforeClass
	public static void setUp() {
		nElements = 8192;
		maxRand = 10000;
	}
	
	@Before
	public void setUpTest() {
		treap = new Treap<Integer>();
		rand = new Random();
		set = new TreeSet<Integer>();
	}
	
	@Test
	public void TestRandomInsertionDeletion() {
		assertNull(treap.find(1));
		assertNull(treap.getMax());
		assertNull(treap.getMin());
		
		for (int i = 0; i < nElements; i++) {
			int newElement = rand.nextInt(maxRand);
			set.add(newElement);
			treap.insert(newElement);
			
			assertEquals(new Integer(newElement), treap.find(newElement));
			checkHeapProperty(treap.getRoot());
			checkBSTProperty(treap.getRoot());
			
			assertEquals(set.size(), treap.getSize());
			assertEquals(set.first(), treap.getMin());
			assertEquals(set.last(), treap.getMax());
		}
		
		while (treap.getRoot() != null) {
			int newElement = rand.nextInt(maxRand);
			
			set.remove(newElement);
			treap.remove(newElement);
			
			assertNull(treap.find(newElement));
			checkHeapProperty(treap.getRoot());
			checkBSTProperty(treap.getRoot());
			
			assertEquals(set.size(), treap.getSize());
			if (set.size() > 0 && treap.getSize() > 0) {
				assertEquals(set.first(), treap.getMin());
				assertEquals(set.last(), treap.getMax());
			}		
		}
		
		assertNull(treap.find(1));
	}
	
	@Test
	public void TestOrderedInsertionDeletion() {
		
		for (int i = 0; i < nElements; i++) {
			int newElement = i;
			treap.insert(newElement);
			assertEquals(new Integer(newElement), treap.find(newElement));
			checkHeapProperty(treap.getRoot());
			checkBSTProperty(treap.getRoot());
		}
		
		for (int i = 0; i < nElements; i++) {
			int newElement = i;
			treap.remove(newElement);
			assertNull(treap.find(newElement));
			checkHeapProperty(treap.getRoot());
			checkBSTProperty(treap.getRoot());
		}
		
		assertNull(treap.find(1));
	}
	
	private void checkHeapProperty(Treap<Integer>.Node node) {
		Queue<Treap<Integer>.Node> queue = new ArrayDeque<>();
		
		if (node != null) queue.add(node);
		
		while (!queue.isEmpty()) {
			Treap<Integer>.Node top = queue.poll();
			if (top.getLeft() != null) {
				assertEquals(false, top.getLeft().getPriority() < top.getPriority());
				queue.add(top.getLeft());
			}
			if (top.getRight() != null) {
				assertEquals(false, top.getRight().getPriority() < top.getPriority());
				queue.add(top.getRight());
			}
		}
	}
	
	private void checkBSTProperty(Treap<Integer>.Node node) {
		Queue<Treap<Integer>.Node> queue = new ArrayDeque<>();
		
		if (node != null) queue.add(node);
		
		while (!queue.isEmpty()) {
			Treap<Integer>.Node top = queue.poll();
			if (top.getLeft() != null) {
				assertNotEquals(-1, top.getKey().compareTo(top.getLeft().getKey()));
				queue.add(top.getLeft());
			}
			if (top.getRight() != null) {
				assertEquals(-1, top.getKey().compareTo(top.getRight().getKey()));
				queue.add(top.getRight());
			}
		}
	}
}
