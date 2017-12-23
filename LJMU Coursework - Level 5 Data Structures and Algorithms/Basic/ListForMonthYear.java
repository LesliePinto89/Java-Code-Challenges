package patriots.basic.search;

import java.awt.AWTException;

/**
* This class has search option based on entered month / year from the user
*/
import java.util.Scanner;

import patriots.basic.datareader.ClimateDataReader;
import patriots.basic.model.ClimateStation;
import patriots.basic.model.Observation;
import patriots.basic.userinteraction.Presentation;

public class ListForMonthYear {

	private final ClimateDataReader dataReader;

	/**
	 * Provide non-static access for the methods in ClimateDataReader
	 * 
	 * @param dataReader
	 *            This is by accessing the weather station files and get the
	 *            data from the climate station class
	 */
	public ListForMonthYear(ClimateDataReader dataReader) {
		this.dataReader = dataReader;
	}

	/**
	 * ListByGivenMonthOrYear - Prints out details of observation for specific
	 * month / year
	 * 
	 * @param userInput
	 *            - By using system.in based scanner for user input
	 * @param getclimateStation
	 *            - By using reference from climateStaion class
	 * @param currentMeasurementToString
	 * @throws AWTException
	 *             - Works when Abstract Window Toolkit exception has occurred.
	 */
	public void ListByGivenMonthOrYear(Scanner userInput) throws AWTException {
		System.out.println(
				"Feature set C: List weather observations for a given month / year across all recorded locations. (Basic)\n"
						+ Presentation.bar);

		String currentMeasurementToString = "";
		System.out.print("Please enter a year (yyyy):");
		Integer year = userInput.nextInt();
		System.out.print("\nPlease enter a month (mm):");
		Integer month = userInput.nextInt();

		for (int i = 0; i < dataReader.getClimateStations().size(); i++) {
			ClimateStation listMonthYear = dataReader.getClimateStations().get(i);
			boolean printLocation = true;
			for (int j = 0; j < listMonthYear.getObservations().size(); j++) {
				Observation obtainMonthYear = listMonthYear.getObservations().get(j);
				if (month.equals(obtainMonthYear.getMonth()) && year.equals(obtainMonthYear.getYear())) {
					if (printLocation == true) {
						System.out.println("\n" + listMonthYear.getLocation() + " - Results that match year-month: "
								+ year + "/" + month + "\n" + Presentation.bar);
						printLocation = false;
					}
					currentMeasurementToString = obtainMonthYear.toString();
					currentMeasurementToString = currentMeasurementToString.replaceAll("null", "Missing");
					System.out.println(currentMeasurementToString);
				}
			}
		}
		Presentation.pauseAndClearScreen(userInput);
	}
}
