import java.io.IOException;

public class ArrayUpdate {

	/** Increments array number sequence by one and adjusts size as needed */
	private static void testUpdateArray(int[] originalArr, int[] newArr, int carried) {
		// Start from end of array and work back to 0 index
		for (int i = originalArr.length - 1; i >= 0; i--) {
			int sum = originalArr[i] + carried;
			if (sum == 10) {
				carried = 1;

				// End carried over 1 for rest of loop
			} else {
				carried = 0;

				// update first instance by 1, add others without increment
				newArr[i] = sum % 10;
			}
		}

		// If all numbers sum over 10, make bigger array, add 1 at index 0
		if (carried == 1) {
			newArr = new int[originalArr.length + 1];
			newArr[0] = carried;

		}
		for (int i = 0; i < newArr.length; i++) {
			System.out.print(newArr[i]);
		}
		System.out.println("");
	}

	private static void addUpdateArray() {
		int carriedOver = 1;
		int[] originalArray = { 9, 9, 9 };

		// Used as array with size change
		int[] newArr = new int[originalArray.length];
		testUpdateArray(originalArray, newArr, carriedOver);

		originalArray = new int[] { 1, 2, 5, 7 };
		// Also used as array for no size change
		newArr = new int[originalArray.length];
		testUpdateArray(originalArray, newArr, carriedOver);
	}

	public static void main(String[] args) throws IOException {
		addUpdateArray();
	}
}
