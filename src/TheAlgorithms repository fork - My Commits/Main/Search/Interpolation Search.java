package src.main.java.com.search;

public class InterpolationSearch {

	/**
	 * A linear interpolation search algorithm that finds 
	 * the position of a target value in an sorted array 
	 * using its lowest value and highest value.
	 * 
	 * Average time-complexity	O(log log N) - uniformly distributed
	 * Worst-case time-complexity	O(N) - non-uniformly distributed
	 * Worst-case space complexity	O(1)
	 * 
	 * @param <T> This is any comparable type
	 * @param array This is the array where the element should be found
	 * @param Key This is the element to find in the array
	 * @return The index of the key in the array
	 */
	public <T extends Comparable<T>> int findIndex(T array[], T key) {
		return search(array, key, 0, array.length - 1);
	}

	/**
     * @param array The array to make the interpolation search
     * @param key The element to be found 
     * @param start The first and smallest element in the sorted array
     * @param end The last and largest element in the sorted array
     * @return The index location of the key
	 */
	private <T extends Comparable<T>> int search(T arr[], T key, int start, int end) {

		while (start <= end && key.compareTo(arr[start]) > 0
				|| key.compareTo(arr[start]) == 0 && key.compareTo(arr[end]) < 0 || key.compareTo(arr[end]) == 0) {

			String startValueString = arr[start].toString();
			int startValueInt = Integer.parseInt(startValueString);
			String endValueString = arr[end].toString();
			int endValueInt = Integer.parseInt(endValueString);

			String keyValueString = key.toString();
			int keyValueInt = Integer.parseInt(keyValueString);

			int position = start + (((end - start) / (endValueInt - startValueInt) * (keyValueInt - startValueInt)));
			if (arr[position].equals(key))
				return position;

			if (arr[position].compareTo(key) < 0)
				start = position + 1;
			else
				end = position - 1;
		}
		return -1;
	}
}