package com.ab.services;

import java.math.BigInteger;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

public class ABFind {

	/**
	 * Finds if String has duplicates
	 * @param s
	 * @return
	 */
	public static boolean hasDuplicates(String s) {
		if (StringUtils.isEmpty(s)) {
			return false;
		}
		char[] c = s.toCharArray();
		Arrays.sort(c);
		for (int i = 0; i < c.length - 1; i++) {
			if (c[i] == c[i+1]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Find if String is a pallindrome
	 * @param s
	 * @return
	 */
	public static boolean isPallindrome(String s) {
		if (StringUtils.isEmpty(s)) {
			return false;
		}
		return StringUtils.reverse(s).equals(s);
	}
	
	/**
	 * Return the Nth Fibonacci number
	 * (starting from 0th)
	 * @param n
	 * @return
	 */
	public static BigInteger fibonacci(int n) {
		// Use best algorithm to compute		
		return ABFind.fibonacciLoop(n);
	}
	
	/**
	 * Return the Sequence of n Fibonacci numbers
	 * @param n
	 * @return
	 */
	public static BigInteger[] fibonacciSeries(int n) {
		return fibonacciSeriesLoop(n);
	}
	
	/**
	 * Use Iteration to generate a series of n Fibonacci numbers
	 * @param n
	 * @return
	 */
	public static BigInteger[] fibonacciSeriesLoop(int n) {

		return null;
	}
	
	/**
	 * Use Recursion to generate a series of n Fibonacci numbers
	 * @param n
	 * @return
	 */
	public static BigInteger[] fibonacciSeriesRecursive(int n) {
		return null;
	}

	public static BigInteger fibonacciRecursive(int n) {
		if (n == 0) {
			return BigInteger.ZERO;
		} else if (n == 1) {
			return BigInteger.ONE;
		}
		return fibonacciRecursive(n - 2).add(fibonacciRecursive(n - 1));
	}
	
	public static BigInteger fibonacciLoop(int n) {
		if (n == 0) {
			return BigInteger.ZERO;
		}
		BigInteger fibonacciPrev2 = BigInteger.ZERO;
		BigInteger fibonacciPrev1 = BigInteger.ONE;
		BigInteger fibonacci = fibonacciPrev1;
		for (int i = 2; i <= n; i++) {
			fibonacci = fibonacciPrev2.add(fibonacciPrev1);
			fibonacciPrev2 = fibonacciPrev1;
			fibonacciPrev1 = fibonacci;
		}
		return fibonacci;
	}
	
	public static void quickSort(int[] a) {
		quickSort(a, 0, a.length - 1);
	}
	
	private static void quickSort(int[] a, int left, int right) {
		if (a.length <= 1) {
			return;
		}
		if (left >= right) {
			return;
		}
		int pivot = getPivot(left, right);
				
		int i = left;
		int j = right;
		int tmp;
		while (i <= j) {
			while (a[i] < a[pivot]) {
				i++;
			}
			while (a[j] > a[pivot]) {
				j--;
			}
			if (i <= j) {
				// swap now
				tmp = a[j];
				a[j] = a[i];
				a[i] = tmp;
				i++;j--;
			}
		}
		// now sort left partition between left and pivot
		if (left < j ) {
			quickSort(a, left, j);
		}
		// now sort partition between pivot+1 and right
		if (right > i) {
			quickSort(a, i, right);
		}
	}
		
	// Get a random number between left and right (both inclusive)
	private static int getPivot(int left, int right) {
		return (left+right)/2;
	}
	
	
}
