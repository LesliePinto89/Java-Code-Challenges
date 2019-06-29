package googleTaskOne;

public class Base  {

public static void main(String[] args) {
		
	  PrefixMapSum mapsum = new PrefixMapSum();
	  mapsum.insert("columnar", 3);
	  assert mapsum.sum("col") == 3;
	  
	  mapsum.insert("column", 2);
	  assert mapsum.sum("col") == 5;
	}
}
