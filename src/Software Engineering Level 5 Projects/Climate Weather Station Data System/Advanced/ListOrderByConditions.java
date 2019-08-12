package patriots.advanced.search;

import java.awt.AWTException;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import patriots.advanced.datareader.ClimateDataReader;
import patriots.advanced.model.ClimateStation;
import patriots.advanced.model.Observation;
import patriots.advanced.model.ObservationDate;
import patriots.advanced.userinteraction.Presentation;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class ListOrderByConditions {

	/** This class completes feature set 1,2 and 7 **/

	// Variables for below methods
	private int lineCounter = 0;
	private String chooseToFile = "";
	private final ClimateDataReader reader;
	private Presentation presentation;

	/**
	 * Provide non static access for methods in this class to the data reader
	 **/

	public ListOrderByConditions(ClimateDataReader reader, Presentation presentation) {
		this.reader = reader;
		this.presentation = presentation;
	}

	/**
	 * searchAllValidLocations - Prints out location only details for each
	 * station stored in memory
	 * 
	 * @param userInput
	 * @param BAR
	 * @throws AWTException
	 *             - When Abstract Window Toolkit exception has occurred.
	 **/
	public void searchAllValidLocations(Scanner userInput) throws AWTException {
		StringBuilder sb = new StringBuilder();
		sb.append("Feature set A - List all the recording locations for which you have data. (Advanced)\n");
		sb.append(presentation.BAR);
		sb.append("\n");

		for (ClimateStation getAllStations : reader.getClimateStations().values()) {
			sb.append(getAllStations.getDetailInformation());
			sb.append("\n");
		}
		sb.append(presentation.BAR);
		System.out.println(sb.toString());
		presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * checkToAddToFile - This is used with above searchForGivenLocations
	 * method. As each line is printed a counter is incremented. When a set
	 * limit is reached, the user has the option of printing output to a text
	 * file or to not do so.
	 * 
	 * @param userInput
	 * @param lineCounter
	 * @param chooseToFile
	 * @throws AWTException
	 **/
	public void checkToAddToFile(Scanner userInput, ClimateStation searchByStation) throws AWTException {
		lineCounter++;
		if (lineCounter == 10) {
			System.out.print("\nDo you want to open these details into a txt file? - Type \"yes\" or \"no\": ");
			chooseToFile = userInput.next().toLowerCase();
			if (chooseToFile.equals("yes")) {
				writeToTempFile(searchByStation, userInput);
			} else if (chooseToFile.equals("no")) {
				lineCounter++;
			}
		}
	}

	/**
	 * This method gets all observation records for a user defined station
	 * 
	 * @param userInput
	 * @param locationChoice
	 * @param lineCounter
	 * @param chooseToFile
	 * @param AWTException
	 * 
	 * @throws AWTException
	 **/
	public void searchForGivenLocations(Scanner userInput, String locationChoice) throws AWTException {

		// Use get method in map structure which is faster than basic's linear
		// iteration
		ClimateStation searchByStation = reader.getClimateStations().get(locationChoice);
		if (searchByStation == null) {
			System.out.println("\nThe Location: " + locationChoice + " is not in the database.");
		} else {
			System.out.println("\nAll observation details for: " + locationChoice + "\n" + presentation.BAR);

			// Use one for each loop instead of basic's nester for each loop
			for (Observation measurements : searchByStation.getObservations().values()) {
				String convertToString = measurements.toString();
				convertToString = convertToString.replace("null", "Missing");
				System.out.println(convertToString);

				// This is just to give option of adding output to text file
				// when on fixed line.
				if (lineCounter <= 10) {
					checkToAddToFile(userInput, searchByStation);
					if (chooseToFile.equals("yes")) {
						break;
					}
				} else {
					continue;
				}
			}
		}
		presentation.pauseAndClearScreen(userInput);
		lineCounter = 0;
		chooseToFile = "";
	}

	/**
	 * This takes the elements from the below method's TreeMap and utilised
	 * various collections types to sort feature set 7's data
	 * 
	 * @param unsortMap
	 * @param preplist
	 * @param sortedMap
	 * @param entry
	 *            - returns a collection view of map
	 * @return sortedMap
	 **/
	private static Map<String, ObservationDate> sortByValue(Map<String, ObservationDate> unsortMap) {

		// First convert the Map to a List collection with a Map generic
		List<Map.Entry<String, ObservationDate>> preplist = new LinkedList<>(unsortMap.entrySet());

		// Next, sort the list with a Collections.sort(), and a Comparator to
		// find the lowest between each station's first year record.
		Collections.sort(preplist, (o1, o2) -> (o1.getValue().getYear()).compareTo(o2.getValue().getYear()));

		// Loop through sorted list, put it into a new insertion order Map -
		// LinkedHashMap. This is based on HashMap, but retains insertion order,
		// without the overhead occurred with TreeMaps.
		Map<String, ObservationDate> sortedMap = new LinkedHashMap<>();
		for (Map.Entry<String, ObservationDate> entry : preplist) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/**
	 * This method uses a Tree map to store the first record from each station,
	 * sort it using the above method and then iterate and print each
	 * measurement out.
	 * 
	 * @param userInput
	 * @param unSortedMap
	 * @param entry
	 *            - returns a collection view of map
	 * @param sortedMap
	 * @param month
	 *            - Number to String conversion
	 * @throws AWTException
	 **/
	public void listStationsByFirstMeasurement(Scanner userInput) throws AWTException {

		// Changed from tree map to hash map as its faster, plus the initial
		// insertion order is unordered anyway.
		Map<String, ObservationDate> unSortedMap = new HashMap<String, ObservationDate>();
		for (ClimateStation firstRecordStation : reader.getClimateStations().values()) {
			if (firstRecordStation.getObservations().size() > 0) {
				Observation firstRecord = firstRecordStation.getObservations().firstEntry().getValue();
				ObservationDate getMeasurement = new ObservationDate();
				getMeasurement.setMonth(firstRecord.getDate().getMonth());
				getMeasurement.setYear(firstRecord.getDate().getYear());
				unSortedMap.put(firstRecordStation.getLocation(), getMeasurement);
			}
		}
		Map<String, ObservationDate> sortedMap = sortByValue(unSortedMap);
		System.out.println(
				"Feature set G: List recording locations in chronological order of their first weather measurement (Advanced) \n"
						+ presentation.BAR + "\n");

		for (Map.Entry<String, ObservationDate> entry : sortedMap.entrySet()) {
			Month month = Month.of(entry.getValue().getMonth());
			System.out.println("Station: " + entry.getKey() + " - Date: "
					+ month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " / " + entry.getValue().getYear());
		}
		presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * writeToTempFile - Connects from above checkToAddFile method. Add each
	 * observation record for the matching user input station into a temporary
	 * text file, and opens it upon writing completion. Once the user has
	 * confirmed they are finished with file, it is deleted.
	 * 
	 * @param userInput
	 * @param dataToString
	 * @param userDone
	 * @param temp
	 * @param bw
	 **/
	private void writeToTempFile(ClimateStation searchByStation, Scanner userInput) {

		String dataToString = "";
		try {
			File temp = File.createTempFile("tempfile", ".txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
			bw.write("All observation details for: " + searchByStation.getLocation());
			bw.newLine();
			bw.write(presentation.BAR);
			bw.newLine();

			for (Observation measurements : searchByStation.getObservations().values()) {
				dataToString = measurements.toString();
				dataToString = dataToString.replaceAll("null", "Missing");
				bw.write(dataToString);
				bw.newLine();
				bw.newLine();
			}

			bw.close();
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().edit(temp);
			}
			System.out.print("\nHave you finished looking at the file? - Type \"yes\" or \"no\": ");
			String userDone = userInput.next();
			while (!userDone.equals("yes")) {
				System.out.print("\nNow are you finished looking at the file? - Type \"yes\" or \"no\": ");
				userDone = userInput.next();
			}
			temp.delete();
			System.out.println("File deleted");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
