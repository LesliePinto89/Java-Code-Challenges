package patriots.advanced.menu;

import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.util.Scanner;

import patriots.advanced.datareader.ClimateDataReader;
import patriots.advanced.search.ListForMonthYear;
import patriots.advanced.search.ListOrderByConditions;
import patriots.advanced.search.MinMaxSearch;
import patriots.advanced.userinteraction.Presentation;

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
	 * @throws FileNotFoundException
	 * @throws AWTException
	 **/
	public ClimateMenu(ClimateDataReader dataReader) throws FileNotFoundException, AWTException {
		this.dataReader = dataReader;
	}

	/**
	 * Provides a user various search options for station data in memory
	 * 
	 * @param userInput
	 * @param BAR
	 * @param confirmInput
	 * @throws AWTException
	 **/
	public void welcomeMenu() throws AWTException {
		Presentation presentation = new Presentation();
		MinMaxSearch minMaxSearch = new MinMaxSearch(dataReader, presentation);
		MinMaxMenus minMaxMenus = new MinMaxMenus(minMaxSearch, presentation);
		ListForMonthYear listForMonthYear = new ListForMonthYear(dataReader, presentation);
		ListOrderByConditions listOrderByConditions = new ListOrderByConditions(dataReader, presentation);
		Scanner userInput = new Scanner(System.in);
		String confirmInput = null;
		do {

			System.out.println("Welcome to the Patriots Climate Station database. \n" + presentation.BAR);
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
				presentation.clearConsoleDisplay();
				listOrderByConditions.searchAllValidLocations(userInput);
				break;
			case "2":
				presentation.clearConsoleDisplay();
				System.out
						.print("Feature set B: List all weather observations for a given location, in chronological order. (Advanced)\n"
								+ presentation.BAR + "\nLocation name: ");
				userInput.nextLine(); // clears buffer
				String locationChoice = userInput.nextLine().toUpperCase();
				listOrderByConditions.searchForGivenLocations(userInput, locationChoice);
				break;
			case "3":
				presentation.clearConsoleDisplay();
				listForMonthYear.listObservationsForGivenYearMonth(userInput);
				break;
			case "4":
				presentation.clearConsoleDisplay();
				minMaxSearch.findMinAndMaxtempForLocation(userInput);
				break;
			case "5":
				presentation.clearConsoleDisplay();
				minMaxMenus.minSubmenu(userInput);
				break;
			case "6":
				presentation.clearConsoleDisplay();
				minMaxMenus.maxSubmenu(userInput);
				break;
			case "7":
				presentation.clearConsoleDisplay();
				listOrderByConditions.listStationsByFirstMeasurement(userInput);
				break;

			default:
				presentation.clearConsoleDisplay();
				break;
			}
		} while (!confirmInput.equalsIgnoreCase("Q"));
		System.out.println("Menu closed");
		userInput.close();
	}
}
