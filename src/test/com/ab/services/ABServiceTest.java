package com.ab.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;

import com.google.common.collect.MinMaxPriorityQueue;

public class ABFindUnitTest {
	
	@Test
	public void test_hasDuplicates() {
		String q_twice = "qwertyuiopasdfghjklq";
		assertTrue(ABFind.hasDuplicates(q_twice));
		String no_dup = "123456qwert";
		assertFalse(ABFind.hasDuplicates(no_dup));
	}
	
	@Test
	public void test_isPallindrome() {
		String odd_pal = "abcba";
		String even_pal = "abccba";
		String odd_not_pal = "asdqw";
		String even_not_pal = "qwesqr";
		
		assertTrue(ABFind.isPallindrome(odd_pal));
		assertTrue(ABFind.isPallindrome(even_pal));
		assertFalse(ABFind.isPallindrome(odd_not_pal));
		assertFalse(ABFind.isPallindrome(even_not_pal));
		
	}
	
	@Test
	public void test_fibonacciLoop() {
		assertEquals(BigInteger.ZERO, ABFind.fibonacciLoop(0));
		assertEquals(BigInteger.ONE, ABFind.fibonacciLoop(1));
		assertEquals(BigInteger.ONE, ABFind.fibonacciLoop(2));
		assertEquals(new BigInteger("2"), ABFind.fibonacciLoop(3));
		assertEquals(new BigInteger("21"), ABFind.fibonacciLoop(8));
		assertEquals(new BigInteger("218922995834555169026"), ABFind.fibonacciLoop(99));
	}

	@Test
	public void test_fibonacciRecursive() {
		assertEquals(BigInteger.ZERO, ABFind.fibonacciRecursive(0));
		assertEquals(BigInteger.ONE, ABFind.fibonacciRecursive(1));
		assertEquals(BigInteger.ONE, ABFind.fibonacciRecursive(2));
		assertEquals(new BigInteger("2"), ABFind.fibonacciRecursive(3));
		assertEquals(new BigInteger("21"), ABFind.fibonacciRecursive(8));
		assertEquals(new BigInteger("218922995834555169026"), ABFind.fibonacciRecursive(99));
	}
	
	@Test
	public void test_quickSort() {
		int[] a1 = { 3, 1 };
		int[] ac1 = a1.clone();
		ABFind.quickSort(a1);
		test_euqality(ac1, a1);
		int a11[] = { 1, 3 };
		ABFind.quickSort(a11);
		test_euqality(ac1, a11);
		
		int[] a2 = { 2, 3, 8, 9, 3, 5 };
		int[] ac2 = a2.clone();
		ABFind.quickSort(a2);
		test_euqality(ac2, a2);
	}
	
	@Test
	public void test_MinMaxPriorityQueue() {
		MinMaxPriorityQueue<String> heap = MinMaxPriorityQueue.create();
		heap.add("d");
		heap.add("a");
		heap.add("c");
		heap.add("b");
		
		System.out.println("peek : " + heap.peek());
		System.out.println("peak first : " + heap.peekFirst());
		System.out.println("peak last : " + heap.peekLast());
		while (!heap.isEmpty()) {
			System.out.println("polling : " + heap.pollFirst());	
		}
	}
	
	@Test
	public void test_TreeMap() {
		TreeMap<String, Integer> t = new TreeMap<String, Integer>();
		t.put("Tito", 30);
		t.put("Ayan", 87);
		t.put("Zara", 3);
		t.put("John", 50);
		
		for (Entry<String, Integer> m : t.entrySet()) {
			System.out.println(m.getKey() + " : " + m.getValue());
		}
		t.remove("Ayan");
		System.out.println("After removing head");
		for (Entry<String, Integer> m : t.entrySet()) {
			System.out.println(m.getKey() + " : " + m.getValue());
		}
		
	}
	
	
	private void test_euqality(int[] ac1, int[] a1) {
		Arrays.sort(ac1);
		for (int x = 0; x < a1.length; x++) {
			assertEquals(ac1[x], a1[x]);
		}		
	}

}