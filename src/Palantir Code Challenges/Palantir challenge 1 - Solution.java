package palantir;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PalantirBase {

	/**Palantir Coding Challenge 1
	 * ---------------------------
	 * Write a program that checks whether an integer is a palindrome. For example,
	 * 121 is a palindrome, as well as 888. 678 is not a palindrome. Do not convert
	 * the integer into a string.
	 * 
	 * Added assert test with different samples
	 */

	public static void main(String[] args) {
		Integer sampleOne = 8898; // not palindrome
		checkIfPalindrome(sampleOne);
		assert checkIfPalindrome(sampleOne) : false; 
		
		Integer sampleTwo = 12021; // is palindrome
		checkIfPalindrome(sampleTwo);
		assert checkIfPalindrome(sampleTwo) : true; 
		
		Integer sampleThree = 212303212; // is palindrome
		checkIfPalindrome(sampleThree);
		assert checkIfPalindrome(sampleThree) : true;
	}
	
	public static boolean checkIfPalindrome(int input) {
		int original = input;
		List<Integer> digits = new ArrayList<Integer>();
		int decrement = 1;
		int digitLength = (int) (Math.log10(input) + 1);
        boolean isPalindrome = false;
		if (input == 0) {
			System.out.print("Integer not a palidrome");
		}
		
		else {
			while (input > 0) {
				digits.add(input % 10);
				input /= 10;
			}
			Collections.reverse(digits);

			// Use either list size or digitLength variable
			for (int i = 0; i < digitLength; i++) {
				if (digits.get(i) == digits.get(digitLength - decrement)) {
					decrement++;
					if (i + 1 == digits.size() / 2) {
						isPalindrome = true;
						System.out.println("Integer: "+original +" is a palindrome");
						break;
					}
				} else {
					isPalindrome =false;
					System.out.println("Integer: "+original+" is not a palindrome");
					break;
				}
			}
		}
		return isPalindrome;
	}
}
