package patriots.advanced.search;

import java.awt.AWTException;
import java.util.Scanner;

import patriots.advanced.datareader.ClimateDataReader;
import patriots.advanced.model.ClimateStation;
import patriots.advanced.model.Observation;
import patriots.advanced.model.ObservationDate;
import patriots.advanced.userinteraction.Presentation;

public class ListForMonthYear {

	/**
	 * Class that displays all station observations that match user defined
	 * month and year combination
	 **/

	private final ClimateDataReader dataReader;
	private Presentation presentation;

	public ListForMonthYear(ClimateDataReader dataReader, Presentation presentation) {
		this.dataReader = dataReader;
		this.presentation = presentation;
	}

	/**
	 * This method completes feature set 3. It checks for and returns
	 * observations that match the combination of a user chosen year and month
	 * 
	 * @param currentMeasurementToString
	 * @param month
	 * @param year
	 * @param userInput
	 * @param output
	 **/
	public void listObservationsForGivenYearMonth(Scanner userInput) throws AWTException {

		String currentMeasurementToString = "";

		// As there is always a year and month entry (no nulls), these variables
		// are primitive. Its important as it enables making a composite key
		// from the two in the map where their observation values are stored.
		int year = 0;
		int month = 0;
		try {
			System.out.println(
					"Feature set C: List weather observations for a given month / year across all recorded locations. (Advanced)\n"
							+ presentation.BAR);
			
			System.out.print("Please enter a year (yyyy): ");
			year = userInput.nextInt();
			System.out.print("\nPlease enter a month (mm) : ");
			month = userInput.nextInt();
		} catch (Exception e) {
			System.out.println("\nIncorrect format");
			userInput.nextLine();
			presentation.pauseAndClearScreen(userInput);
			return;
		}

		// Variables added to constructor in ObservationDate.class. Forms
		// composite key, treated as a Wrapper key object
		ObservationDate desiredDate = new ObservationDate(year, month);

		// If no match to the above composite key is found in the
		// map, then report error and exit
		StringBuilder output = new StringBuilder();
		System.out.println("\nResults that match (Advanced) \n" + presentation.BAR);
		for (ClimateStation matchedStation : dataReader.getClimateStations().values()) {

			Observation observationForDate = matchedStation.getObservations().get(desiredDate);
			if (observationForDate == null) {
				System.out.println("Entry not found");
				break;
			} else {
				currentMeasurementToString = observationForDate.toString().replaceAll("null", "Missing");
				output.append(matchedStation.getLocation());
				output.append("\n");
				output.append(presentation.LINE_BAR);
				output.append("\n");
				output.append(currentMeasurementToString);
				output.append("\n\n");
			}
		}
		System.out.print(output.toString());
		presentation.pauseAndClearScreen(userInput);
	}
}
