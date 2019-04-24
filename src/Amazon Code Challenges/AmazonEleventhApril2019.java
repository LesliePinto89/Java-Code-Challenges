import java.util.Scanner;

public class AmazonEleventhApril2019 {

	/**
	 * Given a string, determine whether any permutation of it is a palindrome.
	 * For example, carrace should return true, since it can be rearranged to
	 * form racecar, which is a palindrome. daily should return false, since
	 * there's no rearrangement that can form a palindrome.
	 */

	public static void main(String[] args) {

		System.out.print("Type in string: ");
		Scanner test = new Scanner(System.in);
		String potential = test.nextLine();
		String removedSpace = potential.replaceAll("\\s+", "");

		boolean isPalindrome = false;
		int reverseIndex = removedSpace.length();

		// Back to front check
		for (char aChar : removedSpace.toCharArray()) {
			if (aChar == removedSpace.charAt(reverseIndex - 1)) {
				reverseIndex--;
				if (reverseIndex == 0) {
					isPalindrome = true;
					break;
				}
				continue;
			} else {
				break;
			}
		}

		if (isPalindrome) {
			System.out.println(potential + " is a palindrome");
		}

		else {
			System.out.println(potential + " is not a palindrome");
		}

	}

}
