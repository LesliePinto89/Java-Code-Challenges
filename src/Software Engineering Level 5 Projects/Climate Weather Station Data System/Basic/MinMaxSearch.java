package patriots.basic.search;

import java.awt.AWTException;
/**
 * MinMaxSearch 
 * Provides all min max searches for sun, rain and temperature for either a given location or across all stations. 
 */
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Scanner;

import patriots.basic.datareader.ClimateDataReader;
import patriots.basic.model.ClimateStation;
import patriots.basic.model.Observation;
import patriots.basic.userinteraction.Presentation;

public class MinMaxSearch {

	private final ClimateDataReader dataReader;

	public MinMaxSearch(ClimateDataReader dataReader) {
		this.dataReader = dataReader;
	}

	/**
	 * findMinAndMaxForLocation - Requests a location name and matches it to
	 * known locations by calling the findLocationFromUser method. If a location
	 * is found the OutputMinAndMax method is called and the min and max
	 * temperatures for a location are displayed.
	 * 
	 * @param userInput
	 * @throws AWTException
	 */
	public void findMinAndMaxtempForLocation(Scanner userInput) throws AWTException {
		System.out
				.print("Feature set D: Find the minimum and maximum recorded temperatures for a given location along with when they were recorded. (Basic)\n"
						+ Presentation.bar + "\n");
		System.out.print("Please enter a name of a weather station: ");
		userInput.nextLine();
		String location = userInput.nextLine();
		ClimateStation found = findLocationFromUser(location);
		if (found == null) {
			Presentation.clearConsoleDisplay();
			System.out.println("Location not found!!\n");
		} else {
			outputMinAndMax(found, userInput);
		}
	}

	/**
	 * outputMinAndMax - Searches a given location's list of observations to
	 * obtain the min and max temperatures and date they occurred.
	 * 
	 * @param found
	 * @param userInput
	 * @throws AWTException
	 */
	private void outputMinAndMax(ClimateStation found, Scanner userInput) throws AWTException {
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		int minmonth = 0;
		int maxMonth = 0;
		int minYear = 0;
		int maxYear = 0;

		for (int i = 0; i < found.getObservations().size(); i++) {
			Observation ob = found.getObservations().get(i);
			if (ob.getTotalMinDegC() != null && ob.getTotalMinDegC() < min) {
				min = ob.getTotalMinDegC();
				minmonth = ob.getMonth();
				minYear = ob.getYear();
			}
			if (ob.getTotalMaxDegC() != null && ob.getTotalMaxDegC() > max) {
				max = ob.getTotalMaxDegC();
				maxMonth = ob.getMonth();
				maxYear = ob.getYear();
			}
		}
		Month minMonthName = Month.of(minmonth);
		System.out.print("\nSummary\n" + Presentation.bar + "\nMin: " + min + " degrees, " + "Min Date: "
				+ minMonthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + minYear);
		Month maxMonthName = Month.of(maxMonth);
		System.out.println(" - Max: " + max + " degrees, " + "Max Date: "
				+ maxMonthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + maxYear);
		Presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * findLocationFromUser - Takes the user input String provided by
	 * findMinAndMaxForLocation and exhaustively searches the list until a
	 * location is found, if the location is not matched the search returns
	 * null.
	 * 
	 * @param locationName
	 * @return
	 */
	private ClimateStation findLocationFromUser(String locationName) {
		for (int i = 0; i < dataReader.getClimateStations().size(); i++) {
			ClimateStation station = dataReader.getClimateStations().get(i);
			if (locationName.equalsIgnoreCase(station.getLocation())) {
				return station;
			}
		}
		return null;
	}

	/**
	 * findLocationWithMinimumTemperature - Searches the list of ClimateStations
	 * and each ClimateStation's internal list of Observations to locate the
	 * lowest temperature recorded across all of the station and displays this
	 * to the user along with the location and date recorded.
	 * 
	 * @param userInput
	 * @throws AWTException
	 */
	public void findLocationWithMinimumTemperature(Scanner userInput) throws AWTException {
		double min = Double.POSITIVE_INFINITY;
		String locationName = null;
		int month = 0;
		int year = 0;

		for (int i = 0; i < dataReader.getClimateStations().size(); i++) {
			ClimateStation station = dataReader.getClimateStations().get(i);
			for (int j = 0; j < station.getObservations().size(); j++) {
				Observation observations = station.getObservations().get(j);
				if (observations.getTotalMinDegC() != null && observations.getTotalMinDegC() < min) {
					min = observations.getTotalMinDegC();
					locationName = station.getLocation();
					month = observations.getMonth();
					year = observations.getYear();
				}
			}
		}
		System.out.println("Find location with minimum temperature\n" + Presentation.bar);
		Month monthName = Month.of(month);
		System.out.println("The location with the minimum temperature is: " + locationName + ", " + min + " Degrees in "
				+ monthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + year + "\n");
		Presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * findLocationWithMaximumTemperature - Searches the list of ClimateStations
	 * and each ClimateStation's internal list of Observations to locate the
	 * highest temperature recorded across all of the stations and displays this
	 * to the user along with the location and date recorded.
	 * 
	 * @param userInput
	 * @throws AWTException
	 */
	public void findLocationWithMaximumTemperature(Scanner userInput) throws AWTException {
		double max = Double.NEGATIVE_INFINITY;
		String locationName = null;
		int month = 0;
		int year = 0;

		for (int i = 0; i < dataReader.getClimateStations().size(); i++) {
			ClimateStation station = dataReader.getClimateStations().get(i);
			for (int j = 0; j < station.getObservations().size(); j++) {
				Observation observations = station.getObservations().get(j);
				if (observations.getTotalMaxDegC() != null && observations.getTotalMaxDegC() > max) {
					max = observations.getTotalMaxDegC();
					locationName = station.getLocation();
					month = observations.getMonth();
					year = observations.getYear();
				}
			}
		}
		System.out.println("Find location with maximum temperature\n" + Presentation.bar);
		Month monthName = Month.of(month);
		System.out.println("The location with the maximum temperature is: " + locationName + ", Degrees: " + max
				+ " in " + monthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + year + "\n");
		Presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * findLocationWithMaximumSunHours - Searches the list of ClimateStations
	 * and each ClimateStation's internal list of Observations to locate the
	 * highest number of sun hours recorded across all of the stations and
	 * displays this to the user along with the location and date recorded.
	 * 
	 * @param userInput
	 * @throws AWTException
	 */
	public void findLocationWithMaximumSunHours(Scanner userInput) throws AWTException {
		double max = Double.NEGATIVE_INFINITY;
		String locationName = null;
		int month = 0;
		int year = 0;
		for (int i = 0; i < dataReader.getClimateStations().size(); i++) {
			ClimateStation station = dataReader.getClimateStations().get(i);
			for (int j = 0; j < station.getObservations().size(); j++) {
				Observation observations = station.getObservations().get(j);
				if (observations.getSunshineInHours() != null && observations.getSunshineInHours() > max) {
					max = observations.getSunshineInHours();
					locationName = station.getLocation();
					month = observations.getMonth();
					year = observations.getYear();
				}
			}
		}
		System.out.println("Find location with maximum sun hours\n" + Presentation.bar);
		Month monthName = Month.of(month);
		System.out.println("The location with the maximum hours of sun is: " + locationName + " which had " + max
				+ " hours in " + monthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + year + "\n");
		Presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * findLocationWithMinimumSunHours - Searches the list of ClimateStations
	 * and each ClimateStation's internal list of Observations to locate the
	 * lowest number of sun hours recorded across all of the stations and
	 * displays this to the user along with the location and date recorded.
	 * 
	 * @param userInput
	 * @throws AWTException
	 */
	public void findLocationWithMinimumSunHours(Scanner userInput) throws AWTException {
		double min = Double.POSITIVE_INFINITY;
		String locationName = null;
		int month = 0;
		int year = 0;

		for (int i = 0; i < dataReader.getClimateStations().size(); i++) {
			ClimateStation station = dataReader.getClimateStations().get(i);
			for (int j = 0; j < station.getObservations().size(); j++) {
				Observation observations = station.getObservations().get(j);
				if (observations.getSunshineInHours() != null && observations.getSunshineInHours() < min) {
					min = observations.getSunshineInHours();
					locationName = station.getLocation();
					month = observations.getMonth();
					year = observations.getYear();
				}
			}
		}

		Month monthName = Month.of(month);
		System.out.println("Find location with minimum sun hours\n" + Presentation.bar);
		System.out.println("The location with the minimum hours of sun is: " + locationName + " which had " + min
				+ " hours in " + monthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + year + "\n");
		Presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * findLocationWithMaximumRainfall - Searches the list of ClimateStations
	 * and each ClimateStation's internal list of Observations to locate the
	 * highest amount of rainfall recorded across all of the stations and
	 * displays this to the user along with the location and date recorded.
	 * 
	 * @param userInput
	 * @throws AWTException
	 */
	public void findLocationWithMaximumRainfall(Scanner userInput) throws AWTException {
		double max = Double.NEGATIVE_INFINITY;
		String locationName = null;
		int month = 0;
		int year = 0;

		for (int i = 0; i < dataReader.getClimateStations().size(); i++) {
			ClimateStation station = dataReader.getClimateStations().get(i);
			for (int j = 0; j < station.getObservations().size(); j++) {
				Observation observations = station.getObservations().get(j);
				if (observations.getRainInMillimeters() != null && observations.getRainInMillimeters() > max) {
					max = observations.getRainInMillimeters();
					locationName = station.getLocation();
					month = observations.getMonth();
					year = observations.getYear();
				}
			}
		}
		System.out.println("Find maximum rainfall from all locations\n" + Presentation.bar);
		Month monthName = Month.of(month);
		System.out.println("The location with the maximum rainfall is: " + locationName + " which had " + max
				+ " millimeters in " + monthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + year + "\n");
		Presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * findLocationWithMinimumRainfall - Searches the list of ClimateStations
	 * and each ClimateStation's internal list of Observations to locate the
	 * lowest amount of rainfall recorded across all of the stations and
	 * displays this to the user along with the location and date recorded.
	 * 
	 * @param userInput
	 * @param userInput
	 * @throws AWTException
	 */
	public void findLocationWithMinimumRainfall(Scanner userInput) throws AWTException {
		double min = Double.POSITIVE_INFINITY;
		String locationName = null;
		int month = 0;
		int year = 0;

		for (int i = 0; i < dataReader.getClimateStations().size(); i++) {
			ClimateStation station = dataReader.getClimateStations().get(i);
			for (int j = 0; j < station.getObservations().size(); j++) {
				Observation observations = station.getObservations().get(j);
				if (observations.getRainInMillimeters() != null && observations.getRainInMillimeters() < min) {
					min = observations.getRainInMillimeters();
					locationName = station.getLocation();
					month = observations.getMonth();
					year = observations.getYear();
				}
			}
		}
		System.out.println("Find minimum rainfall from all locations\n" + Presentation.bar);
		Month monthName = Month.of(month);
		System.out.println("The location with the minimum rainfall is: " + locationName + " which had " + min
				+ " millimeters in " + monthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + year + "\n");
		Presentation.pauseAndClearScreen(userInput);
	}
}
