package capsule;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Map;
import org.junit.Test;
import com.capsule.model.Petridish;
import com.capsule.visuals.GridView;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import static org.junit.Assert.*;

public class UnitTesting {

	@Test
	public void multiplicationOfZeroIntegersShouldReturnZero() {
		Petridish tester = new Petridish();

		/* Test 1 - Ensure an initial state Dish is successfully populated by
		 pairing values bases on x,y values as index values*/
		Multimap<Integer, Integer> testPairings = ArrayListMultimap.create();
		testPairings.put(1, 2);
		testPairings.put(2, 2);
		testPairings.put(3, 2);

		String[][] filledTestDish = new String[3][4];
		for (Map.Entry<Integer, Integer> entry : testPairings.entries()) {
			filledTestDish[(Integer) entry.getValue()][Integer.parseInt(entry.getKey().toString())] = "X | ";
		}
		String[][] emptyTestDish = new String[3][4];
		assertArrayEquals(filledTestDish, tester.addDishBacteria(testPairings, emptyTestDish));
		

		/* Test 2 - Ensure each of a cell's adjacent neighbours
		 can be checked for live bacteria cells with edge case errors*/
		String[][] testNeighbourDish = new String[3][4];
		for (String[] row : testNeighbourDish) {
			Arrays.fill(row, "  | ");
		}
		for (Map.Entry<Integer, Integer> entry : testPairings.entries()) {
			testNeighbourDish[(Integer) entry.getValue()][Integer.parseInt(entry.getKey().toString())] = "X | ";
		}
		assertEquals(3, tester.checkNeighbours(1, 2, testNeighbourDish, 3, 4));

		/*Test 3 - Compare higher values against old individual values from
		 each new line to determine minimum rows and columns to support */
		String result = tester.dynamicGridDimensions("2,2", "1", "1");
		assertEquals("2,2", result);
		

		/* Test 4 - Check that user can end the simulation after a new
		  generation successfully passes*/
		assertEquals(true, tester.nextIteration("N"));
		

		/* Test 5 - Check a given dish/matrix is populated with grid like
		 empty cells to later be filled with pair values.
		 Compares a finite matrix with a soon to be populated one.*/
		String[][] testDish = new String[3][3];
		for (String[] row : testDish) {
			Arrays.fill(row, "  | ");
		}
		String[][] toBeFilledDish = new String[3][3];
		assertArrayEquals(testDish, GridView.prepareDish(toBeFilledDish));
	}
}