package amazon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Core {

	public static int index = 1;
	public static int currentLargestDem = 0;
	public static Integer[][] holdFactors;

	public static void main(String[] args) {
		int [] sample = new int [] {56,42};
		holdFactors = new Integer[sample.length][];
		gatherPrimaryFactors(sample);
		
		int gcf = matchCommonFactors();
		String values = Arrays.toString(sample).replace("[", " ").replace("]"," ").replace(",", " ");
        System.out.println("The GCF from the values ("+values+") is: "+gcf);
		//assert findLargestDenominator(sample) == (Integer) 9;
	}

	public static void findFactors(int i, Integer[] currentFactors) {
		holdFactors[i] = currentFactors;
	}

	public static void gatherPrimaryFactors(int [] currentSample) {
		int increment = 2;
		List<Integer> tempFactors;
		int index = 0;
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
			Integer[] array = tempFactors.toArray(new Integer[tempFactors.size()]);
			findFactors(index, array);
			index++;
		}
	}

	public static int matchCommonFactors() {
		List<Integer> commonFactors = new ArrayList<Integer>();
		int x = 0;
		int j = 0;
		
		for (int i = 1; i < holdFactors.length; i++) {
			for (; j < holdFactors[i].length;) {

				// Found in next array, do this till end
				if (holdFactors[i][j] == holdFactors[0][x]) {

//					// Check all arrays, so add and move to next value
//					if (i + 1 == holdFactors.length) {
//						commonFactors.add(holdFactors[0][x]);
//						break;
//					}
					i++;
					j = -1;
				}
			}
			if (x == holdFactors[0].length - 1) {
				break;
			}
			// Not found a given factor, so move to next one from
			i = 0;
			x++;
			continue;
		}
		Integer[] array = commonFactors.toArray(new Integer[commonFactors.size()]);
		return findLargestDenominator(array);
	}

	public static int findLargestDenominator(Integer [] commonFactors) {
		int sum = 0;
		int temp =0;
		boolean start =false;
		for(int i =0; i <commonFactors.length; i++) {
			if(!start) {
				if(commonFactors.length==1) {
					sum = commonFactors[i];
					break;
				}
				temp = commonFactors[i] * commonFactors[i+1];
				sum += temp;
				start=true;
			}
			else if (i+1 !=commonFactors.length){
				sum *= commonFactors[i+1];}
			}
			
		return sum;
		}
	
}
