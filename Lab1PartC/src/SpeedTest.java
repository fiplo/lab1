/*
 * SpeedTest.java
 * Copyright (C) 2018 fiplo <fiplo@weebtop>
 *
 * Distributed under terms of the MIT license.
 */

import studijosKTU.*;
import java.util.*;


    public class SpeedTest{
	private Random rng = new Random();

	private int maxLength;
	private int minLength;

	private int testToPerform;
    public SpeedTest(int minLength, int maxLength, int testToPerform){
		this.maxLength = maxLength;
		this.minLength = minLength;
		this.testToPerform = testToPerform;
	}

	
	public static void main(String[] args){
		if(args.length != 3){
			System.err.println("Three arguments exactly are required.");
			return;
		}
		int maxLength = 0;
		int minLength = 0;
		int testToPerform = 0;
		try {
			minLength = Integer.parseInt(args[0]);
			maxLength = Integer.parseInt(args[1]);
			testToPerform = Integer.parseInt(args[2]);
		} catch (NumberFormatException ex) {
			System.err.println("Couldn't parse arguments.");
		}
		if(maxLength < minLength || testToPerform < 1){
			System.err.println("Maximum cannot be greater than minimum and "
					+ "number of retreivals must be positive.");
			return;
		}
		new SpeedTest(minLength, maxLength, testToPerform).run();
	}

	public void run(){
		for(int j = 0; j < 5; j++){
			ArrayList<Integer> testArr = generateArray();
			LinkedList<Integer> testList = generateList();
			long beginTime = System.nanoTime();
			long endTime;
			//Testing get(int i);
			int i = 0;
			//Testing ArrayList
			for(i = 0; i < testToPerform; i++){
				testArr.get(Math.abs(rng.nextInt() % testArr.size()));
			}
			endTime = System.nanoTime();
			System.out.println("It took " + Long.toString(endTime - beginTime) 
					+ " nanoseconds to perform " 
					+ Integer.toString(testToPerform)
					+ " random retreivals from ArrayList.");
			//Testing LinkedList
			beginTime = System.nanoTime();
			for(i = 0; i < testToPerform; i++){
				testList.get(Math.abs(rng.nextInt() % testList.size()));
			}
			endTime = System.nanoTime();
			System.out.println("It took " + Long.toString(endTime - beginTime) 
					+ " nanoseconds to perform the same operation with a linked list.");
			//Testing cbrt vs sqrt 
			beginTime = System.nanoTime();
			//Test cbrt
			for(i = 0; i < testToPerform; i++){
				Math.cbrt(rng.nextDouble());
			}
			endTime = System.nanoTime();
			System.out.println("It took " + Long.toString(endTime - beginTime)
					+ " nanoseconds to pull " + Integer.toString(testToPerform) 
					+ " cube roots with cbrt.");
			//Testing sqrt 
			beginTime = System.nanoTime();
			for(i = 0; i < testToPerform; i++){
				Math.sqrt(rng.nextDouble());
			}
			endTime = System.nanoTime();
			System.out.println("It took " + Long.toString(endTime - beginTime) 
					+ " nanoseconds to do sqrt(x)");
			System.out.println();
		}
	}
	private ArrayList<Integer> generateArray(){
		int length = Math.abs(rng.nextInt()) % (maxLength - minLength + 1) + minLength;
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for(int i = 0; i < length; i++){
			ret.add(rng.nextInt());
		}
		return ret;
	}
	private LinkedList<Integer> generateList(){
		int length = Math.abs(rng.nextInt()) % (maxLength - minLength + 1) + minLength;
		LinkedList<Integer> ret = new LinkedList<>();
		for(int i = 0; i < length; i++){
			ret.add(rng.nextInt());
		}
		return ret;
	}
}
