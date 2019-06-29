package com.capsule.visuals;

import java.util.Arrays;
import java.util.Scanner;

import com.capsule.model.Petridish;

/** This class handles the view of the application */
public class GridView {

	/**
	 * Prints out current and next generation dish
	 * 
	 * @param dish
	 *            : Current matrix (dish)
	 * @param nRows
	 *            : the dish's row count limit
	 */
	public static void printGeneratedGrid(String[][] dish, int nRows) {
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
	 * @return dish with indexes filled with grid lines
	 */
	public static String[][] prepareDishGridLines(String[][] dish) {
		for (String[] row : dish) {
			Arrays.fill(row, "  | ");
		}
		return dish;
	}

	/**
	 * Prints out the output grid on the console and the output results * @param
	 * nextGenerationDish : new dish to store the next generation
	 * 
	 * @param nRows
	 *            : the dish's row count limit
	 * @param carriedScanner
	 *            : the active scanner used for user input
	 * @return a string that contains user input
	 */
	public static String outputResults(String[][] nextGenerationDish, int nRows) {
		StringBuilder output = new StringBuilder();
		System.out.println("\nOutput of next generation grid\n");
		GridView.printGeneratedGrid(nextGenerationDish, nRows);
		System.out.println("Output results");
		System.out.println("------------------------------");
		for (int i = 0; i < nextGenerationDish.length; i++) {
			for (int j = 0; j < nextGenerationDish[i].length; j++) {
				if (nextGenerationDish[i][j].contains("X")) {
					output.append(String.valueOf(j) + "," + String.valueOf(i) + "\n");
					System.out.println(new String(String.valueOf(j) + "," + String.valueOf(i)));
				}
			}
		}
		output.append("end\n");
		System.out.println("end");
		System.out.println("------------------------------");
		System.out.println("1) Start simulation again. 2) proceed into next generation, 3) end simulation? "
				+ "Type \"1\", \"2\" or \"3\":");
		Petridish.storeOutput(output.toString());
		// Left open for now since messes with scanner as is a reference
		String choice = Petridish.currentScanner().next();
		return new String(choice);
	}
}