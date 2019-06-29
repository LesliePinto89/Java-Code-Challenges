package com.capsule.visuals;

import java.util.Arrays;

/** This class handles the view of the application */
public class GridView {

	/**
	 * Prints out current and next generation dish
	 * 
	 * @param dish
	 *            : Current matrix (dish)
	 */
	public static void printGenerationGrid(String[][] dish, int nRows) {
		String axis = "";
		for (int i = 0; i <= nRows + 2; i++) {
			axis += "  " + String.valueOf(i) + "  | ";
		}
		System.out.println(axis + "\n-----------------------------------");

		int i = 0;
		for (String[] row : dish) {
			String toPrint = Arrays.toString(row);
			toPrint = toPrint.replace("[", " ").replace("]", " ").replace(",", " ");
			System.out.println(i++ + "| " + toPrint + "\n-----------------------------------");
		}
	}

	/**
	 * Fills a given empty dish with grid like lines to aid draw visuals of grid
	 * generations. Includes JUnit test.
	 * 
	 * @param dish
	 *            : Current matrix (dish)
	 * @return filled dish with grid lines
	 */
	public static String[][] prepareDish(String[][] dish) {
		for (String[] row : dish) {
			Arrays.fill(row, "  | ");
		}
		return dish;
	}

}
