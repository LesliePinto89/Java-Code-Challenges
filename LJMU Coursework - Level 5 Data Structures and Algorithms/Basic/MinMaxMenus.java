package patriots.basic.menu;

import java.awt.AWTException;
/**
 * MinMaxMenu.
 * Provides sub menu for min and max searches.  
 */
import java.util.Scanner;

import patriots.basic.search.MinMaxSearch;
import patriots.basic.userinteraction.Presentation;

public class MinMaxMenus {

	private final MinMaxSearch minMaxSearch;

	public MinMaxMenus(MinMaxSearch minMaxSearch) {
		this.minMaxSearch = minMaxSearch;
	}

	public void minSubmenu(Scanner userInput) throws AWTException {

		String confirmInput = null;
		do {
			System.out.println(
					"Feature set F: Find the location with the minimum: rainfall, sunhours and temperature. (Basic)\n"
							+ Presentation.bar);
			System.out.print("Type 1 to list min rainfall. \n");
			System.out.print("Type 2 to list min sunhours. \n");
			System.out.print("Type 3 to list min temperature \n");
			System.out.print("Type Q to quit.\n\nOption: ");
			confirmInput = userInput.next();

			switch (confirmInput) {
			case "1":
				Presentation.clearConsoleDisplay();
				minMaxSearch.findLocationWithMinimumRainfall(userInput);
				break;
			case "2":
				Presentation.clearConsoleDisplay();
				minMaxSearch.findLocationWithMinimumSunHours(userInput);
				break;
			case "3":
				Presentation.clearConsoleDisplay();
				minMaxSearch.findLocationWithMinimumTemperature(userInput);
				break;
			default:
				Presentation.clearConsoleDisplay();
			}
		} while (!confirmInput.equalsIgnoreCase("Q"));
	}

	public void maxSubmenu(Scanner userInput) throws AWTException {
		String confirmInput = null;
		do {
			System.out.println(
					"Feature set E: Find the location with the maximum: rainfall, sunhours and temperature. (Basic)\n"
							+ Presentation.bar);
			System.out.print("Type 1 to list max rainfall. \n");
			System.out.print("Type 2 to list max sunhours. \n");
			System.out.print("Type 3 to list max temperature \n");
			System.out.print("Type Q to quit.\n\nOption: ");
			confirmInput = userInput.next();

			switch (confirmInput) {
			case "1":
				Presentation.clearConsoleDisplay();
				minMaxSearch.findLocationWithMaximumRainfall(userInput);
				break;
			case "2":
				Presentation.clearConsoleDisplay();
				minMaxSearch.findLocationWithMaximumSunHours(userInput);
				break;
			case "3":
				Presentation.clearConsoleDisplay();
				minMaxSearch.findLocationWithMaximumTemperature(userInput);
				break;
			default:
				Presentation.clearConsoleDisplay();
			}
		} while (!confirmInput.equalsIgnoreCase("Q"));
	}
}
