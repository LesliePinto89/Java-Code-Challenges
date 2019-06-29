package com.capsule.model;

import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import com.capsule.visuals.GridView;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Capsule Code Challenge : Bacteria. Author: Leslie Pinto
 * 
 * This class creates the simulation of life and death of bacteria based on the
 * set rules.
 */
public class Petridish {

	// Defines 2 dimensional axis as well as x and y value coordinates
	// To create x axis then y axis, using row count as column count
	// and column count for row count.
	private int nRows = 0;
	private int nCols = 0;
	private static Scanner aScanner;
	private String[][] petriDish;
	private String[][] nextGenerationDish;
	private static String output = null;
	private String userChoice = "";

	public static Scanner currentScanner() {
		return aScanner;
	}

	public void createPetridish() {
		Multimap<Integer, Integer> pairings = storePairings();
		prepareDishes();
		addDishBacteria(pairings, petriDish);
		GridView.printGeneratedGrid(petriDish, nRows);
		bacteriaSimulation(pairings);
		userChoice = GridView.outputResults(nextGenerationDish, nRows);
		nextIteration(userChoice);
	}

	/**
	 * Create infinite scaled, dynamic-like sized matrix. Includes JUnit test
	 * 
	 * @param input
	 *            : current x,y pairing as string
	 * @param extractRow
	 *            : x value (nRows) from previous pair
	 * @param extractCols
	 *            : y value (nCols) from previous pair
	 * @return MultiMap with current highest x,y values as dimension values
	 */
	public String dynamicGridDimensions(String input, String extractRow, String extractCol) {
		String tempRowCount = input.substring(0, input.indexOf(","));
		String tempColCount = input.substring(input.indexOf(",") + 1);
		if (Integer.parseInt(extractRow) < Integer.parseInt(tempRowCount)) {
			nRows = Integer.parseInt(tempRowCount);
		}
		if (Integer.parseInt(extractCol) < Integer.parseInt(tempColCount)) {
			nCols = Integer.parseInt(tempColCount);
		}
		return String.valueOf(nRows) + "," + String.valueOf(nCols);
	}

	public Multimap<Integer, Integer> bindPairings(String input) {
		Multimap<Integer, Integer> map = ArrayListMultimap.create();
		String extractRow = "";
		String extractCol = "";
		boolean iteration = false;
		while (input.equals("end") == false) {
			boolean matches = Pattern.matches("\\d+\\,+\\d+", input);
			// These conditions only allow pair format or "end" string
			if (input.equals("End")) {
				System.out.println("Please type in \"end\" to finish input");
				input = aScanner.nextLine();
				continue;
			} else if (!matches) {
				System.out.println("Type in pairing seperated by comma: e.g: 2,3 on current line,"
						+ "and or type \"end \" to finish input");
				input = aScanner.nextLine();
				continue;
			}

			if (iteration) {
				dynamicGridDimensions(input, extractRow, extractCol);
				map.put(nRows, nCols);
			} else if (!iteration) {
				extractRow = input.substring(0, input.indexOf(","));
				extractCol = input.substring(input.indexOf(",") + 1);
				iteration = true;
				nRows = Integer.parseInt(extractRow);
				nCols = Integer.parseInt(extractCol);
				map.put(nRows, nCols);
			}
			input = aScanner.nextLine();
		}
		return map;
	}

	/**
	 * Create custom sized grid columns and rows based input, while storing pair
	 * values from input to later add to grid for first generation
	 * 
	 * @return MultiMap of final sized record of parings to populate dish
	 */
	public Multimap<Integer, Integer> storePairings() {
		String carriedOutput = getOutput();
		Multimap<Integer, Integer> baseMap = ArrayListMultimap.create();
		String input = "";
		System.out.println("Input stage\n" + StringUtils.repeat("-", 15));
		System.out.println("Please type in pairing value on each new" + "line and type \"end\" to finish input");
		System.out.println(StringUtils.repeat("-", 75));
		if (carriedOutput == null || userChoice.equals("1")) {
			aScanner = new Scanner(System.in);
			input = aScanner.nextLine();
			baseMap = bindPairings(input);
		} else if (carriedOutput != null || userChoice.equals("2")) {
			aScanner = new Scanner(carriedOutput);
			input = aScanner.nextLine();
			baseMap = bindPairings(input);
			// reverts input back to console input
			aScanner = new Scanner(System.in);
		}
		System.out.println(StringUtils.repeat("=", 75));
		return baseMap;
	}

	public void prepareDishes() {
		petriDish = new String[nCols + 2][nRows + 2];
		nextGenerationDish = new String[nCols + 2][nRows + 2];
		GridView.prepareDishGridLines(petriDish);
		GridView.prepareDishGridLines(nextGenerationDish);
	}

	/**
	 * Create 2 dimensional grid based on highest x and y values, increased by
	 * one to match values grid to actual x,y values. Includes JUnit test
	 * 
	 * @param pairings
	 *            : Current integer x,y pair for live bacteria.
	 * @param dish
	 *            : Current matrix (dish)
	 */
	public String[][] addDishBacteria(Multimap<Integer, Integer> pairings, String[][] dish) {
		for (Map.Entry<Integer, Integer> entry : pairings.entries()) {
			// Assigns "X" from column to row
			dish[(Integer) entry.getValue()][Integer.parseInt(entry.getKey().toString())] = "X | ";
		}
		return dish;
	}

	/** Check for adjacent 8 neighbours each cell */
	private static final int[][] BACTERIA_NEIGHBOURS = { { -1, -1 }, { -1, 0 }, { -1, +1 }, { 0, -1 }, { 0, +1 },
			{ +1, -1 }, { +1, 0 }, { +1, +1 } };

	/**
	 * Uses current dish's cell values against an offset BACTERIA_NEIGHBOURS
	 * method to check adjacent neighbours for live bacteria as marked x,y
	 * values. Includes JUnit test
	 * 
	 * @param i
	 *            : Current row
	 * @param j
	 *            : Current column
	 * @param dish
	 *            : Current matrix (dish)
	 * @param cols
	 *            : Current matrix's column limit
	 * @param rows
	 *            : Current matrix's rows limit
	 * 
	 * @return count of neighbours that have live cells.
	 */
	public int checkNeighbours(int i, int j, String[][] dish, int cols, int rows) {
		int count = 0;
		for (int[] offset : BACTERIA_NEIGHBOURS) {
			if (i + offset[0] < 0 || j + offset[1] < 0) {
				continue;
			}
			if (i + offset[0] > cols + 1 || j + offset[1] > rows + 1) {
				continue;
			}

			if (dish[i + offset[0]][j + offset[1]].contains("X")) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Start simulation traversal to next generation
	 * 
	 * @param pairings
	 *            : current set of integer x,y pairs for live bacteria
	 */
	public void bacteriaSimulation(Multimap<Integer, Integer> pairings) {
		int neighbourCounter = 0;
		for (int i = 0; i < petriDish.length; i++) {
			for (int j = 0; j < petriDish[i].length; j++) {
				neighbourCounter = checkNeighbours(i, j, petriDish, nCols, nRows);

				/*
				 * Any dead bacteria cell with exactly three live neighbours
				 * becomes a live bacteria cell, as if by reproduction.
				 */
				if (petriDish[i][j].contains("  |") && (neighbourCounter == 3)) {
					nextGenerationDish[i][j] = "X | ";
				}

				/*
				 * Any live bacteria cell with fewer than two live neighbours
				 * dies, as if caused by under-population
				 */
				else if (neighbourCounter < 2) {
					nextGenerationDish[i][j] = "  |";
				}

				/*
				 * Any live bacteria cell with two or three live neighbours
				 * lives on to the next generation.
				 */
				else if (petriDish[i][j].contains("X") && neighbourCounter == 2
						|| petriDish[i][j].contains("X") && neighbourCounter == 3) {
					nextGenerationDish[i][j] = "X | ";
				}

				/*
				 * Any live bacteria cell with more than three live neighbours
				 * dies, as if by overcrowding.
				 */
				else if (neighbourCounter > 3) {
					nextGenerationDish[i][j] = "  |";
				}
			}
		}
	}

	/**
	 * Gives user the choice of ending the simulation or moving to the next
	 * generation using the output results
	 * 
	 * @param choice
	 *            : user key-pressed string parsed by scanner
	 * @return true when the simulation has ended
	 */
	public boolean nextIteration(String choice) {
		while (choice.equals(" ") == false) {
			if (choice.equals("1") || choice.equals("2")) {
				createPetridish();
				break;
			} else if (choice.equals("3")) {
				System.out.println("Finished simulation");
				if (aScanner != null) {
					aScanner.close();
				}
				break;
			}

			else {
				System.out.println("Incorrect input : Type Y or N");
				choice = aScanner.next();
			}
		}
		return true;
	}

	public static void storeOutput(String carriedOutput) {
		output = carriedOutput;
	}

	public static String getOutput() {
		return output;
	}
}