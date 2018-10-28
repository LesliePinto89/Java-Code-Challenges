package patriots.advanced.search;

import java.awt.AWTException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Scanner;

import patriots.advanced.datareader.ClimateDataReader;
import patriots.advanced.model.ClimateStation;
import patriots.advanced.userinteraction.Presentation;

/**
 * MinMaxSearch Locates minimum and maximum values for rainfall, sun hours and
 * temperature for each ClimateStations stored within the system.
 *
 */
public class MinMaxSearch {

	private final ClimateDataReader dataReader;
	private Aggregate allLocations;
	private Presentation presentation;

	public MinMaxSearch(ClimateDataReader dataReader, Presentation presentation) {
		this.dataReader = dataReader;
		this.presentation = presentation;
	}

	/**
	 * Obtains input String from user calls findLocation method and displays
	 * output using outputMinAndMax method
	 * 
	 * @param userInput
	 * @param location
	 * @throws AWTException
	 */
	public void findMinAndMaxtempForLocation(Scanner userInput) throws AWTException {
		System.out
		.print("Feature set D: Find the minimum and maximum recorded temperatures for a given location along with when they were recorded. (Advanced)\n"
				+ presentation.BAR + "\n");
		
		System.out.print("Please enter a name of a weather station: ");
		userInput.nextLine(); // clears buffer
		String location = userInput.nextLine();
		ClimateStation found = findLocationFromUser(location);
		if (found == null) {
			presentation.clearConsoleDisplay();
			System.out.println("Location not found!!\n");
		} else {
			outputMinAndMax(found, userInput);
		}
	}

	/**
	 * Displays minimum and maximum temperatures for a given location using a
	 * Climate Stations aggregate.
	 * 
	 * @param userInput
	 * @param min
	 * @param max
	 * @param minmonth
	 * @param maxMonth
	 * @param minYear
	 * @param maxYear
	 * @param minMonthName
	 * @throws AWTException
	 */
	private void outputMinAndMax(ClimateStation station, Scanner userInput) throws AWTException {
		Aggregate aggregate = station.getAggregate();
		double min = aggregate.getMinTemp().getValue();
		double max = aggregate.getMaxTemp().getValue();
		int minmonth = aggregate.getMinTemp().getMonth();
		int maxMonth = aggregate.getMaxTemp().getMonth();
		int minYear = aggregate.getMinTemp().getYear();
		int maxYear = aggregate.getMaxTemp().getYear();

		Month minMonthName = Month.of(minmonth);
		System.out.print("\nSummary\n" + presentation.BAR + "\nMin: " + min + " degrees, " + "Min Date: "
				+ minMonthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + minYear);
		Month maxMonthName = Month.of(maxMonth);
		System.out.println(" - Max: " + max + " degrees, " + "Max Date: "
				+ maxMonthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + maxYear);
		presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * Matches user input String against location names for weather stations.
	 * 
	 * @param locationName
	 * @return
	 */
	private ClimateStation findLocationFromUser(String locationName) {
		return dataReader.getClimateStations().get(locationName.toUpperCase());
	}

	/**
	 * Compares ClimateStation's minimum aggregate values to find record of the
	 * lowest temperature, its date (month and year) and location and displays
	 * this information.
	 * 
	 * @param userInput
	 * @param min
	 * @param month
	 * @param year
	 * @param locationName
	 * @param monthName
	 * @throws AWTException
	 */
	public void findLocationWithMinimumTemperature(Scanner userInput) throws AWTException {
		if (allLocations == null) {
			populateAggregates();
		}
		Value minTemp = allLocations.getMinTemp();
		double min = minTemp.getValue();
		String locationName = minTemp.getLocation();
		int month = minTemp.getMonth();
		int year = minTemp.getYear();

		System.out.println("Find location with minimum temperature\n" + presentation.BAR);
		Month monthName = Month.of(month);
		System.out.println("The location with the minimum temperature is: " + locationName + ", " + min + " Degrees in "
				+ monthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + year + "\n");
		presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * Compares ClimateStation's maximum aggregate values to find record of the
	 * highest temperature, its date (month and year) and location and displays
	 * this information.
	 * 
	 * @param userInput
	 * @param max
	 * @param month
	 * @param year
	 * @param locationName
	 * @param monthName
	 * @throws AWTException
	 */
	public void findLocationWithMaximumTemperature(Scanner userInput) throws AWTException {
		if (allLocations == null) {
			populateAggregates();
		}
		Value maxTemp = allLocations.getMaxTemp();
		double max = maxTemp.getValue();
		String locationName = maxTemp.getLocation();
		int month = maxTemp.getMonth();
		int year = maxTemp.getYear();

		System.out.println("Find location with maximum temperature\n" + presentation.BAR);
		Month monthName = Month.of(month);
		System.out.println("The location with the maximum temperature is: " + locationName + ", Degrees: " + max
				+ " in " + monthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + year + "\n");
		presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * Compares ClimateStation's maximum aggregate values to find record of the
	 * most hours of sun it's date (month and year) and location and displays
	 * this information.
	 * 
	 * @param userInput
	 * @param max
	 * @param locationName
	 * @param month
	 * @param year
	 * @param monthName
	 * @throws AWTException
	 */
	public void findLocationWithMaximumSunHours(Scanner userInput) throws AWTException {
		if (allLocations == null) {
			populateAggregates();
		}

		Value maxSunHours = allLocations.getMaxSunHours();
		double max = maxSunHours.getValue();
		String locationName = maxSunHours.getLocation();
		int month = maxSunHours.getMonth();
		int year = maxSunHours.getYear();

		System.out.println("Find location with maximum sun hours\n" + presentation.BAR);
		Month monthName = Month.of(month);
		System.out.println("The location with the maximum hours of sun is: " + locationName + " which had " + max
				+ " hours in " + monthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + year + "\n");
		presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * Compares ClimateStation's maximum aggregate values to find record of the
	 * least hours of sun it's date (month and year) and location and displays
	 * this information.
	 * 
	 * @param userInput
	 * @param locationName
	 * @param min
	 * @param month
	 * @param year
	 * @param monthName
	 * @throws AWTException
	 */
	public void findLocationWithMinimumSunHours(Scanner userInput) throws AWTException {
		if (allLocations == null) {
			populateAggregates();
		}
		Value minSunHours = allLocations.getMinSunHours();
		double min = minSunHours.getValue();
		String locationName = minSunHours.getLocation();
		int month = minSunHours.getMonth();
		int year = minSunHours.getYear();

		Month monthName = Month.of(month);
		System.out.println("Find location with minimum sun hours\n" + presentation.BAR);
		System.out.println("The location with the minimum hours of sun is: " + locationName + " which had " + min
				+ " hours in " + monthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + year + "\n");
		presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * Compares ClimateStation's maximum aggregate values to find record of the
	 * most rainfall it's date (month and year) and location and displays this
	 * information.
	 * 
	 * @param userInput
	 * @param locationName
	 * @param max
	 * @param month
	 * @param year
	 * @param monthName
	 * @throws AWTException
	 */
	public void findLocationWithMaximumRainfall(Scanner userInput) throws AWTException {
		if (allLocations == null) {
			populateAggregates();
		}
		Value maxRainfall = allLocations.getMaxRainfall();
		double max = maxRainfall.getValue();
		String locationName = maxRainfall.getLocation();
		int month = maxRainfall.getMonth();
		int year = maxRainfall.getYear();

		System.out.println("Find maximum rainfall from all locations\n" + presentation.BAR);
		Month monthName = Month.of(month);
		System.out.println("The location with the maximum rainfall is: " + locationName + " which had " + max
				+ " millimeters in " + monthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + year + "\n");
		presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * Compares ClimateStation's maximum aggregate values to find record of the
	 * least rainfall it's date (month and year) and location and displays this
	 * information.
	 * 
	 * @param userInput
	 * @param locationName
	 * @param min
	 * @param month
	 * @param year
	 * @param monthName
	 * @throws AWTException
	 */
	public void findLocationWithMinimumRainfall(Scanner userInput) throws AWTException {
		if (allLocations == null) {
			populateAggregates();
		}
		Value minRainfall = allLocations.getMinRainfall();
		double min = minRainfall.getValue();
		String locationName = minRainfall.getLocation();
		int month = minRainfall.getMonth();
		int year = minRainfall.getYear();

		System.out.println("Find minimum rainfall from all locations\n" + presentation.BAR);
		Month monthName = Month.of(month);
		System.out.println("The location with the minimum rainfall is: " + locationName + " which had " + min
				+ " millimeters in " + monthName.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + year + "\n");
		presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * Creates aggregate list of values for all stations. allLocations is only
	 * populated when required. Methods that use it call this method if it is
	 * null.
	 */
	private void populateAggregates() {
		if (allLocations != null) {
			return;
		}
		allLocations = new Aggregate();
		for (ClimateStation station : dataReader.getClimateStations().values()) {
			Aggregate locationAggregate = station.getAggregate();
			if (locationAggregate.getMinTemp().getValue() < allLocations.getMinTemp().getValue()) {
				allLocations.setMinTemp(locationAggregate.getMinTemp());
			}
			if (locationAggregate.getMaxTemp().getValue() > allLocations.getMaxTemp().getValue()) {
				allLocations.setMaxTemp(locationAggregate.getMaxTemp());
			}
			if (locationAggregate.getMinRainfall().getValue() < allLocations.getMinRainfall().getValue()) {
				allLocations.setMinRainfall(locationAggregate.getMinRainfall());
			}
			if (locationAggregate.getMaxRainfall().getValue() > allLocations.getMaxRainfall().getValue()) {
				allLocations.setMaxRainfall(locationAggregate.getMaxRainfall());
			}

			if (locationAggregate.getMinSunHours().getValue() < allLocations.getMinSunHours().getValue()) {
				allLocations.setMinSunHours(locationAggregate.getMinSunHours());
			}

			if (locationAggregate.getMaxSunHours().getValue() > allLocations.getMaxSunHours().getValue()) {
				allLocations.setMaxSunHours(locationAggregate.getMaxSunHours());
			}
		}
	}
}
