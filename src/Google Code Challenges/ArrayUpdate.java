import java.io.IOException;

public class ArrayUpdate {

	/** Increments array number sequence by one and adjusts size as needed */
	public static void main(String[] args) throws IOException {

		int carriedOver = 1;
		int[] originalArray = { 9, 9, 9 };

		// Used as first array with no size change and second for size
		int[] newOrginArray = new int[originalArray.length];

		// Start from end of array and work back to 0 index
		for (int i = originalArray.length - 1; i >= 0; i--) {
			int sum = originalArray[i] + carriedOver;
			if (sum == 10) {
				carriedOver = 1;

				// End carried over 1 for rest of loop
			} else {
				carriedOver = 0;
				// Update first instance by 1, add others without increment
				newOrginArray[i] = sum % 10;
			}
		}

		// If all numbers sum over 10, make bigger array, add 1 at index 0
		if (carriedOver == 1) {
			newOrginArray = new int[originalArray.length + 1];
			newOrginArray[0] = carriedOver;
		}

		for (int i = 0; i < newOrginArray.length; i++) {
			System.out.print(newOrginArray[i]);
		}
	}
}