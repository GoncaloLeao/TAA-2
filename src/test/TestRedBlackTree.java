package test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import structures.RedBlackTree;

public class TestRedBlackTree {

	public void checkRedBlackInvariant(RedBlackTree<Integer> rb) {		
		TreeMap<Integer, Integer> node2count = new TreeMap<>();
		Integer numberBlacks = rb.countBlacks();
		ArrayDeque<RedBlackTree<Integer>.Node> queue = new ArrayDeque<>();
		queue.add(rb.getRoot());
		node2count.put(rb.getRoot().hashCode(), 1);
				
		while(!queue.isEmpty()) {
			RedBlackTree<Integer>.Node top = queue.poll();
			RedBlackTree<Integer>.Node left = top.getLeft();
			RedBlackTree<Integer>.Node right = top.getRight();

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
	
	@Test
	public void TestRandom() {
		int nElements = 300;
		int maxRand = 20;
		RedBlackTree<Integer> rb = new RedBlackTree<Integer>();
		Random rand = new Random();
		TreeSet<Integer> set = new TreeSet<Integer>();
		
		// test empty red black
		assertNull(rb.find(1));
		assertNull(rb.getMax());
		assertNull(rb.getMin());
		
		rb.insert(1);
		System.out.println(rb.toString());
		rb.insert(15);
		System.out.println(rb.toString());
		rb.insert(4);
		System.out.println(rb.toString());
		rb.insert(19);
		System.out.println(rb.toString());
		rb.insert(9);
		System.out.println(rb.toString());
		rb.insert(13);
		System.out.println(rb.toString());
		rb.insert(5);
		System.out.println(rb.toString());
		rb.insert(8);
		System.out.println(rb.toString());
		rb.insert(11);
		System.out.println(rb.toString());
		rb.insert(19);
		System.out.println(rb.toString());
		rb.insert(5);
		System.out.println(rb.toString());
		rb.insert(7);

		System.out.println(rb.toString());
		System.out.println(rb.find(7));
		
		// test random insertion of n elements
		/*for (int i = 0; i < nElements; i++) {
			int newElement = rand.nextInt(maxRand);
			System.out.println(newElement);
			set.add(newElement);
			rb.insert(newElement);
			
			assertEquals(new Integer(newElement), rb.find(newElement));
			
			// testing root
			assertEquals(false, rb.getRoot().getColor());
			
			checkRedBlackInvariant(rb);
			
			assertEquals(set.first(), rb.getMin());
			assertEquals(set.last(), rb.getMax());
		}*/
	}
	
}
