package test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

import org.junit.jupiter.api.Test;

import structures.Treap;

public class TestTreap {

	private boolean checkHeapProperty(Treap<Integer>.Node node) {
		Queue<Treap<Integer>.Node> queue = new ArrayDeque<>();
		
		if (node != null) queue.add(node);
		
		while (!queue.isEmpty()) {
			Treap<Integer>.Node top = queue.poll();
			if (top.getLeft() != null) {
				if (top.getLeft().getPriority() < top.getPriority()) return false;
				queue.add(top.getLeft());
			}
			if (top.getRight() != null) {
				if (top.getRight().getPriority() < top.getPriority()) return false;
				queue.add(top.getRight());
			}
		}
		
		return true;
	}
	
	private boolean checkBTProperty(Treap<Integer>.Node node) {
		Queue<Treap<Integer>.Node> queue = new ArrayDeque<>();
		
		if (node != null) queue.add(node);
		
		while (!queue.isEmpty()) {
			Treap<Integer>.Node top = queue.poll();
			if (top.getLeft() != null) {
				if (top.getKey().compareTo(top.getLeft().getKey()) < 0) return false;
				queue.add(top.getLeft());
			}
			if (top.getRight() != null) {
				if (top.getKey().compareTo(top.getRight().getKey()) > 0) return false;
				queue.add(top.getRight());
			}
		}
		
		return true;
	}
	
	@Test
	public void TestRamdom() {
		int nElements = 20;
		int maxRand = 1000;
		Treap<Integer> treap = new Treap<Integer>();
		Random rand = new Random();
		
		// test empty treap
		assertNull(treap.find(1));
		
		// test random insertion of n elements
		for (int i = 0; i < nElements; i++) {
			int newElement = rand.nextInt(maxRand);
			//System.out.println(newElement);
			treap.insert(newElement);
			//System.out.println(treap.toString());
			//treap.printTreeFormat();
			assertEquals(new Integer(newElement), treap.find(newElement));
			assertTrue(checkHeapProperty(treap.getRoot()));
			assertTrue(checkBTProperty(treap.getRoot()));
		}
		
		// test random remove until empty treap
		while (treap.getRoot() != null) {
			int newElement = rand.nextInt(maxRand);
			treap.remove(newElement);
			assertNull(treap.find(newElement));
			assertTrue(checkHeapProperty(treap.getRoot()));
			assertTrue(checkBTProperty(treap.getRoot()));
		}
		
		// test empty treap
		assertNull(treap.find(1));
	}
	
	@Test
	public void TestOrdered() {
		int nElements = 20;
		Treap<Integer> treap = new Treap<Integer>();
		
		// test ordered insertion of n elements
		for (int i = 0; i < nElements; i++) {
			int newElement = i;
			//System.out.println(newElement);
			treap.insert(newElement);
			//System.out.println(treap.toString());
			//treap.printTreeFormat();
			assertEquals(new Integer(newElement), treap.find(newElement));
			assertTrue(checkHeapProperty(treap.getRoot()));
			assertTrue(checkBTProperty(treap.getRoot()));
		}
		
		// test ordered remove until empty treap
		for (int i = 0; i < nElements; i++) {
			int newElement = i;
			treap.remove(newElement);
			assertNull(treap.find(newElement));
			assertTrue(checkHeapProperty(treap.getRoot()));
			assertTrue(checkBTProperty(treap.getRoot()));
		}
		
		// test empty treap
		assertNull(treap.find(1));
	}
}
