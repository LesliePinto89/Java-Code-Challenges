import java.util.Scanner;

public class FacebookFifteenthApril2019 {

	/**Given a 32-bit integer, return the number with its bits reversed.For 
	 * example, given the binary number 1111 0000 1111 0000 1111 0000 1111 0000, 
	 * return 0000 1111 0000 1111 0000 1111 0000 1111*/
	
	public static void main(String[] args) {
	
		//E.G. 61112
		System.out.print("Type in 32 bit integer: ");
		Scanner test = new Scanner(System.in);
		int binaryFromInt = test.nextInt(); 
		String binary = Integer.toBinaryString(binaryFromInt);
		String inOctets = java.util.Arrays.toString(binary.split("(?<=\\G....)"));
		inOctets = inOctets.replace('[', ' ');
		inOctets = inOctets.replace(']', ' ');
		inOctets = inOctets.replace(',', ' ');
		StringBuilder builder = new StringBuilder(32);
		for (char aChar : inOctets.toCharArray()){
			if(aChar == ' '){
				builder.append(" ");	
			}
		else if(aChar == '1'){
				builder.append("0");	
			}
			else if(aChar =='0'){
				builder.append("1");	
			}
		}
		System.out.println("Original binary: "+inOctets);
		System.out.println("Inverse binary: "+builder);
		test.close();
	}
}
