package patriots.advanced.menu;

import java.awt.AWTException;
import java.util.Scanner;

import patriots.advanced.search.MinMaxSearch;
import patriots.advanced.userinteraction.Presentation;

public class MinMaxMenus {

	private final MinMaxSearch minMaxSearch;
	private Presentation presentation;

	public MinMaxMenus(MinMaxSearch minMaxSearch, Presentation presentation) {
		this.minMaxSearch = minMaxSearch;
		this.presentation = presentation;
	}

	public void minSubmenu(Scanner userInput) throws AWTException {
		String confirmInput = null;
		do {
			System.out
			.println("Feature set F: Find the location with the minimum: rainfall, sunhours and temperature. (Advanced)\n"
					+ presentation.BAR);
			
			System.out.print("Type 1 to list min rainfall. \n");
			System.out.print("Type 2 to list min sunhours. \n");
			System.out.print("Type 3 to list min temperature \n");
			System.out.print("Type Q to quit.\n\nOption: ");
			confirmInput = userInput.next();

			switch (confirmInput) {
			case "1":
				presentation.clearConsoleDisplay();
				minMaxSearch.findLocationWithMinimumRainfall(userInput);
				break;
			case "2":
				presentation.clearConsoleDisplay();
				minMaxSearch.findLocationWithMinimumSunHours(userInput);
				break;
			case "3":
				presentation.clearConsoleDisplay();
				minMaxSearch.findLocationWithMinimumTemperature(userInput);
				break;
			default:
				presentation.clearConsoleDisplay();
			}
		} while (!confirmInput.equalsIgnoreCase("Q"));
	}

	public void maxSubmenu(Scanner userInput) throws AWTException {
		String confirmInput = null;
		do {
			System.out
			.println("Feature set E: Find the location with the maximum: rainfall, sunhours and temperature. (Advanced)\n"
					+ presentation.BAR);
			
			System.out.print("Type 1 to list max rainfall. \n");
			System.out.print("Type 2 to list max sunhours. \n");
			System.out.print("Type 3 to list max temperature \n");
			System.out.print("Type Q to quit.\n\nOption: ");
			confirmInput = userInput.next();

			switch (confirmInput) {
			case "1":
				presentation.clearConsoleDisplay();
				minMaxSearch.findLocationWithMaximumRainfall(userInput);
				break;
			case "2":
				presentation.clearConsoleDisplay();
				minMaxSearch.findLocationWithMaximumSunHours(userInput);
				break;
			case "3":
				presentation.clearConsoleDisplay();
				minMaxSearch.findLocationWithMaximumTemperature(userInput);
				break;
			default:
				presentation.clearConsoleDisplay();
			}
		} while (!confirmInput.equalsIgnoreCase("Q"));
	}
}
