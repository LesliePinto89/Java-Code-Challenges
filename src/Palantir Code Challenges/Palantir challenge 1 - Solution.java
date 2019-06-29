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
	 */

	public static void main(String[] args) {
		Integer sample = 12001;
		int decrement = 1;
		int digitLength = (int) (Math.log10(sample) + 1);
		List<Integer> digits = new ArrayList<Integer>();

		if (sample == 0) {
			System.out.print("Integer not a palidrome");
		} else {
			while (sample > 0) {
				digits.add(sample % 10);
				sample /= 10;
			}
			Collections.reverse(digits);

			// Use either list size or digitLength variable
			for (int i = 0; i < digitLength; i++) {
				if (digits.get(i) == digits.get(digitLength - decrement)) {
					decrement++;
					if (i + 1 == digits.size() / 2) {
						System.out.print("Integer is a palidrome");
						break;
					}
				} else {
					System.out.print("Integer not a palidrome");
					break;
				}
			}
		}
	}
}
