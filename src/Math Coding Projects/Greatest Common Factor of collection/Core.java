package amazon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Core {

	private static Integer[][] holdFactors;

	public static void main(String[] args) {
		List<Integer> sample = Arrays.asList(56, 98, 42, 84,70);
		prepDataSample(sample);
		assert prepDataSample(sample) == 14;

		List<Integer> sampleTwo = Arrays.asList(175, 55, 225);
		prepDataSample(sampleTwo);
		assert prepDataSample(sampleTwo) == 5;
		
		List<Integer> sampleThree = Arrays.asList(81,909,45,450);
		prepDataSample(sampleThree);
		assert prepDataSample(sampleThree) == 9;
	}

	/**
	 * Central point of computing HCF.
	 * 
	 * @param List<Integer> currentSample : The current collection of values to find
	 *                      their HCF
	 * @return the found HCF
	 */
	public static int prepDataSample(List<Integer> currentSample) {
		Collections.sort(currentSample); // Re-order to smallest values first

		// Prepare entries of each value's prime factors
		holdFactors = new Integer[currentSample.size()][];

		// Make as Integer to easier allow conversion to string
		Integer[] targetArray = currentSample.toArray(new Integer[currentSample.size()]);

		// find all prime factors each number
		oneToValueFactorsApproach(targetArray);
		int highestCommonFactor = foundHighestCommonFactor();
		String values = Arrays.toString(targetArray).replace("[", " ").replace("]", " ").replace(",", " ");
		System.out.println("The Highest Common Factor from the values (" + values + ") is: " + highestCommonFactor);
		return highestCommonFactor;
	}

	/**
	 * This approach to finding factors works like this: Loop an index 
	 * from 1 to the given value, and if remainder of value from index is
	 * 0, add index to value's respective array for later.
	 * 
	 * @param Integer[] sample : The current collection of values to find their HCF
	 */
	public static void oneToValueFactorsApproach(Integer[] sample) {
		List<Integer> tempFactors;
		int holdFactorsIndex = 0;
		for (int value : sample) {
			tempFactors = new ArrayList<Integer>();
			for (int i = 1; i < value; i++) {
				if (value % i == 0) {
					tempFactors.add(i);
				}
			}
			Integer[] tempFactorsArray = tempFactors.toArray(new Integer[tempFactors.size()]);
			holdFactors[holdFactorsIndex] = tempFactorsArray;
			holdFactorsIndex++;
		}
	}

	/**
	 * Against holdFactors[0]'s elements, compare against other arrays' values and
	 * update HCF until end of holdFactors[0] length.
	 * 
	 * @return the found HCF
	 */
	public static int foundHighestCommonFactor() {
		int x = 0;
		int highestCommonFactor = 0;

		for (int i = 1; i < holdFactors.length; i++) {
			if (x == holdFactors[0].length) {
				break;
			}
			for (int j = 0; j < holdFactors[i].length; j++) {
				
				// True when current x in smallest array, i.e holdFactors[0]
				// is found in next array in iteration.
				if (holdFactors[0][x] == holdFactors[i][j]) {

					// Found at least once in all arrays,
					// if true then add to holdFactors array index, 
					// leave this loop, reset i back to 0,
					// and move to next value i.e. x
					if (i + 1 == holdFactors.length) {
						highestCommonFactor = holdFactors[0][x];
						break;
					}
					//Else move onto next array : Made j =-1 as loop 
					// increments it back to 0
					i++;
					j = -1;
				} 
				//If current holdFactors[0][x] is not found by end
				//of current i, then its not a common factor, so
				//leave loop, reset i back to 0,
				// and move to next value i.e. x
				else if (j == holdFactors[i].length - 1) {
					break;
				}
			}
			// Not found a given factor in smallest array,
			// move to next one
			i = 0;
			x++;
			continue;
		}
		return highestCommonFactor;
	}
	
	 /**Kept this method to show comparison of finding factors from 1 to number approach,
	 * and divide value by increments (2,3,5,7 and add 2 continually until value is 1) approach.*/
	/*  public static void divideToFindFactorsApproach(Integer [] currentSample) {
		int increment = 2;
		List<Integer> tempFactors;
		int holdFactorsIndex = 0;
		for (int a : currentSample) {
			tempFactors = new ArrayList<Integer>();
			while (a != 1) {
				if (a % 2 == 0) {
					tempFactors.add(2);
					a /= 2;
				} else if (a % 3 == 0) {
					increment = 3;
					tempFactors.add(3);
					a /= increment;
				} else if (a % 5 == 0) {
					increment = 5;
					tempFactors.add(5);
					a /= increment;
				} else if (a % increment == 0) {
					tempFactors.add(increment);
					a /= increment;
				}
				else if (a % 7 == 0) {
					increment = 7;
					tempFactors.add(7);
					a /= increment;
				} 
				
				else {
					increment += 2;
				}
			}
			increment =2;	
			Integer[] tempFactorsArray = tempFactors.toArray(new Integer[tempFactors.size()]);
			holdFactors[holdFactorsIndex] = tempFactorsArray;
			holdFactorsIndex++;	
		}
	}*/
}
