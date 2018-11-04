package patriots.basic.menu;

import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.util.Scanner;

import patriots.basic.datareader.ClimateDataReader;
import patriots.basic.search.ListForMonthYear;
import patriots.basic.search.ListOrderByConditions;
import patriots.basic.search.MinMaxSearch;
import patriots.basic.userinteraction.Presentation;

public class ClimateMenu {

	/**
	 * Introduction: A climate station database application developed by our
	 * group "Patriots". Project completes LJMU module - 5117COMP: Data
	 * Structures and Algorithms.
	 */

	// Variable to allow non static access to data reader class
	private ClimateDataReader dataReader;

	/**
	 * Utilises above variable to get climate data from memory
	 * 
	 * @param dataReader
	 *            - non-static reference to the ClimateDataReader.class
	 * @throws FileNotFoundException
	 * @throws AWTException
	 **/
	public ClimateMenu(ClimateDataReader dataReader) throws FileNotFoundException, AWTException {
		this.dataReader = dataReader;
	}

	/**
	 * Provides a user various search options for station data in memory
	 * 
	 * @param minMaxSearch
	 * @param minMaxMenus
	 * @param listForMonthYear
	 * @param listOrderByConditions
	 * @param userInput
	 * @param dataReader
	 * @param bar
	 *            - cosmetic use rather than functional
	 * @param confirmInput
	 *            - String used with userInput Scanner
	 * @throws AWTException
	 **/
	public void welcomeMenu() throws AWTException {
		MinMaxSearch minMaxSearch = new MinMaxSearch(dataReader);
		MinMaxMenus minMaxMenus = new MinMaxMenus(minMaxSearch);
		ListForMonthYear listForMonthYear = new ListForMonthYear(dataReader);
		ListOrderByConditions listOrderByConditions = new ListOrderByConditions(dataReader);
		Scanner userInput = new Scanner(System.in);
		String confirmInput = null;
		do {

			System.out.println("Welcome to the Patriots Climate Station database. \n" + Presentation.bar);
			System.out.println("Type 1 to list all station locations that hold data.");
			System.out.print("Type 2 to list all station observation data for specific location. \n");
			System.out.print("Type 3 to list observations for a given month / year across all locations. \n");
			System.out.print("Type 4 to list min and max temperatures for specified location. \n");
			System.out.print("Type 5 to list the location with the min measurements. \n");
			System.out.print("Type 6 to list the location with the max measurements. \n");
			System.out.print("Type 7 to list locations chronologically of their first weather measurement \n\n");
			System.out.print("Type Q to quit.\n\nOption: ");
			confirmInput = userInput.next();

			switch (confirmInput) {

			case "1":
				Presentation.clearConsoleDisplay();
				listOrderByConditions.searchAllValidLocations(userInput);
				break;
			case "2":
				Presentation.clearConsoleDisplay();
				System.out
						.print("Feature set B: List all weather observations for a given location, in chronological order. (Basic)\n"
								+ Presentation.bar + "\nLocation name: ");
				userInput.nextLine(); // clears buffer
				String locationChoice = userInput.nextLine().toUpperCase();
				listOrderByConditions.searchForGivenLocations(userInput, locationChoice);
				break;
			case "3":
				Presentation.clearConsoleDisplay();
				listForMonthYear.ListByGivenMonthOrYear(userInput);
				break;
			case "4":
				Presentation.clearConsoleDisplay();
				minMaxSearch.findMinAndMaxtempForLocation(userInput);
				break;
			case "5":
				Presentation.clearConsoleDisplay();
				minMaxMenus.minSubmenu(userInput);
				break;
			case "6":
				Presentation.clearConsoleDisplay();
				minMaxMenus.maxSubmenu(userInput);
				break;
			case "7":
				Presentation.clearConsoleDisplay();
				listOrderByConditions.listStationsByFirstMeasurement(userInput);
				break;

			default:
				Presentation.clearConsoleDisplay();
				break;
			}
		} while (!confirmInput.equalsIgnoreCase("Q"));
		System.out.println("Menu closed");
		userInput.close();
	}
}
