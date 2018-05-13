/*package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Random;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import structures.SkipList;

public class TestSkipList {

	private static Random rand;
	private static int nElements;
	private static int maxRand;
	
	private SkipList<Integer> list;
	private TreeSet<Integer> set;
	
	@BeforeAll
	public static void setUp() {
		nElements = 100;
		maxRand = 10000;
		rand = new Random();
	}
	
	@BeforeEach
	public void setUpTest() {
		list = new SkipList<>();
		set = new TreeSet<>();
	}
	
	@Test
	public void TestBasicInsertion() {
		assertNull(list.find(10));
		
		list.insert(13);
		
		assertEquals(new Integer(13), list.find(13));
		
		list.insert(20);
		
		assertEquals(new Integer(13), list.find(13));
		assertEquals(new Integer(20), list.find(20));
		
		list.insert(15);
		list.insert(25);
		list.insert(50);
		list.insert(62);
		
		assertEquals(new Integer(50), list.find(50));
		
		list.insert(85);
		list.insert(94);
		list.insert(72);
		list.insert(95);
		
		assertNull(list.find(10));
		assertNull(list.find(1000));
		
		assertEquals(new Integer(95), list.getMax());
		assertEquals(new Integer(13), list.getMin());
		assertEquals(new Integer(13), list.find(13));
		assertEquals(new Integer(62), list.find(62));
		assertEquals(new Integer(72), list.find(72));
		assertEquals(new Integer(94), list.find(94));
		assertEquals(new Integer(95), list.find(95));
	}
	
	@Test
	public void TestBasicDeletion() {
		list.insert(10);
		list.insert(40);
		list.insert(30);
		list.remove(40);
		
		assertNull(list.find(40));
		
		list.insert(40);
		list.remove(30);
		list.remove(40);		
		list.remove(10);
		
		assertNull(list.find(30));
		assertNull(list.find(10));
		assertNull(list.find(40));
	}
	
	@Test
	public void TestRandom() {
		for (int i = 0; i < nElements; i++) {
			
			int newElement = 10+rand.nextInt(maxRand);
			if (rand.nextBoolean()) {
				list.insert(newElement);
				set.add(newElement);
			} else {
				list.remove(newElement);
				set.remove(newElement);
			}
			
			if (!set.contains(newElement)) {
				assertNull(list.find(newElement));
			} else {
				assertEquals(new Integer(newElement), list.find(newElement));
			}
			
			if (set.isEmpty()) {
				assertNull(list.getMax());
				assertNull(list.getMin());
			} else {
				assertEquals(set.last(), list.getMax());
				assertEquals(set.first(), list.getMin());
			}
		}
	}
	
	void printStructure() {
		System.out.println(list.toString());
	}
	
}
*/