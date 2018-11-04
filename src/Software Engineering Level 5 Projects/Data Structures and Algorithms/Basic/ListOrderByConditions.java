package patriots.basic.search;

import java.awt.AWTException;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import patriots.basic.datareader.ClimateDataReader;
import patriots.basic.model.ClimateStation;
import patriots.basic.model.Observation;
import patriots.basic.userinteraction.Presentation;

public class ListOrderByConditions {

	/**
	 * This class has search options based on location and order constraints
	 **/

	// Variables utilised in below methods
	private int searchOuterIndex = 0;
	private int lineCounter = 0;
	private boolean increaseLineCounter = true;
	private String chooseToFile = "";
	private final ClimateDataReader reader;

	/**
	 * Provide non static access for methods in this class to the data reader
	 * 
	 * @param reader
	 *            - Non static reference to the ClimateDataReader.class
	 **/
	public ListOrderByConditions(ClimateDataReader reader) {
		this.reader = reader;
	}

	/**
	 * searchAllValidLocations - Prints out location only details for each
	 * station stored in memory
	 * 
	 * @param {Scanner}
	 *            userInput
	 * @param getAllStations
	 *            - reference to ClimateStation.class
	 * @param searchIndex
	 * @throws AWTException
	 *             - When Abstract Window Toolkit exception has occurred.
	 **/
	public void searchAllValidLocations(Scanner userInput) throws AWTException {
		StringBuilder sb = new StringBuilder();
		sb.append("Feature set A - List all the recording locations for which you have data. (Basic)\n");
		sb.append(Presentation.bar);		 
		sb.append("\n");

		for (int searchIndex = 0; searchIndex < reader.getClimateStations().size(); searchIndex++) {
			ClimateStation getAllStations = reader.getClimateStations().get(searchIndex);
			sb.append(getAllStations);
			sb.append("\n");
		}
		sb.append(Presentation.bar);
		System.out.println(sb.toString());
		Presentation.pauseAndClearScreen(userInput);
	}

	/**
	 * searchForGivenLocations - Search for the station that matches user input.
	 * When found, print out each observation entry. If user entry is not found,
	 * return not found reply message.
	 * 
	 * @param userInput
	 * @param locationChoice
	 *            - String used to compare (match) user input from Scanner to
	 *            climate location data.
	 * @param searchByStation
	 *            - reference to ClimateStation.class
	 * @param measurements
	 *            - reference to Observation.class
	 * @param observationToString
	 * @throws AWTException
	 **/
	public void searchForGivenLocations(Scanner userInput, String locationChoice) throws AWTException {
		for (int i = 0; i < reader.getClimateStations().size(); i++) {
			ClimateStation searchByStation = reader.getClimateStations().get(i);
			if (locationChoice.equals(searchByStation.getLocation())) {
				System.out.println("\nAll observation details for: " + locationChoice + "\n" + Presentation.bar);
				for (int j = 0; j < searchByStation.getObservations().size(); j++) {
					Observation measurements = searchByStation.getObservations().get(j);
					String observationToString = measurements.toString();
					observationToString = observationToString.replace("null", "Missing");
					System.out.println(observationToString);

					if (lineCounter <= 10) {
						checkToAddToFile(userInput, searchByStation);
						if (chooseToFile.equals("yes")) {
							break;
						}
					} else {
						continue;
					}
				}
				break; // end search for matching station
			} else if (searchOuterIndex == reader.getClimateStations().size() - 1) {
				System.out.println("\nThe Location: " + locationChoice + " is not in the database\n");
				break;
			}
			searchOuterIndex++;
		}
		Presentation.pauseAndClearScreen(userInput);
		searchOuterIndex = 0;
		lineCounter = 0;
		chooseToFile = "";
	}

	/**
	 * checkToAddToFile - This is used with above searchForGivenLocations
	 * method. As each line is printed a counter is incremented. When a set
	 * limit is reached, the user has the option of printing output to a text
	 * file or to not do so.
	 * 
	 * @param userInput
	 * @param searchByStation
	 * @param increaseLineCounter
	 *            - boolean used to facilitate user's choices
	 * @param lineCounter
	 * @param chooseToFile
	 * @throws AWTException
	 **/
	public void checkToAddToFile(Scanner userInput, ClimateStation searchByStation) throws AWTException {

		if (increaseLineCounter == true) {
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
	}

	/**
	 * writeToTempFile - Connects from above checkToAddFile method. Add each
	 * observation record for the matching user input station into a temporary
	 * text file, and opens it upon writing completion. Once the user has
	 * confirmed they are finished with file, it is deleted.
	 * 
	 * @param searchByStation
	 * @param userInput
	 * @param dataToString
	 * @param userDone
	 *            - String used to confirm user is "done" reading the text file
	 * @param tempFile
	 * @param bw
	 * @param measurements
	 * @throws AWTException
	 **/
	private void writeToTempFile(ClimateStation searchByStation, Scanner userInput) throws AWTException {

		String dataToString = "";
		try {
			File tempFile = File.createTempFile("tempfile", ".txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
			bw.write("All observation details for: " + searchByStation.getLocation());
			bw.newLine();
			bw.write(Presentation.bar);
			bw.newLine();

			for (Observation measurements : searchByStation.getObservations()) {
				dataToString = measurements.toString();
				dataToString = dataToString.replaceAll("null", "Missing");
				bw.write(dataToString);
				bw.newLine();
				bw.newLine();
			}

			bw.close();
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().edit(tempFile);
			}
			System.out.print("\nHave you finished looking at the file? - Type \"yes\" or \"no\": ");
			String userDone = userInput.next();
			while (!userDone.equals("yes")) {
				System.out.print("\nNow are you finished looking at the file? - Type \"yes\" or \"no\": ");
				userDone = userInput.next();
				continue;
			}
			tempFile.delete();
			System.out.println("File deleted");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * listStationsByFirstMeasurement - Search through each station in memory
	 * and store its location, and first (earliest) observation date in another
	 * list of memory. Next, sort the new list in order of smallest (earliest)
	 * observation date.
	 * 
	 * @param userInput
	 * @param orderedList
	 * @param singleIndex
	 * @param outerIndex
	 * @param innerIndex
	 * @param firstRecordStation
	 * @param firstRecord
	 * @param storeFirstObservation
	 * @param lowerObservation
	 *            Stores the current index in the orderedList-1 and is the lower
	 *            compareTo value in insert sort
	 * @param higherObservation
	 *            Stores the current index in the orderedList and is the higher
	 *            compareTo value in insert sort
	 * @param counter
	 * @param month
	 *            - Used to change month integer values to name equivalents
	 * @param reader
	 * @throws AWTException
	 **/
	public void listStationsByFirstMeasurement(Scanner userInput) throws AWTException {
		List<LocationAndDate> orderedList = new LinkedList<LocationAndDate>();
		for (int singleIndex = 0; singleIndex < reader.getClimateStations().size(); singleIndex++) {
			ClimateStation firstRecordStation = reader.getClimateStations().get(singleIndex);
			if (firstRecordStation.getObservations().size() > 0) {
				Observation firstRecord = firstRecordStation.getObservations().get(0);
				LocationAndDate storeFirstObservation = new LocationAndDate();
				storeFirstObservation.setCLocation(firstRecordStation.getLocation());
				storeFirstObservation.setOMonth(firstRecord.getMonth());
				storeFirstObservation.setOYear(firstRecord.getYear());
				orderedList.add(storeFirstObservation);
			}
		}

		// Below are comparisons between two intentionally non-optimised array
		// sorting algorithms. We have used a selection sort in the basic
		// version as to show the worst
		// choice for array sorting compared to the advanced version.

		// Selection sort approach - best, average and worst time complexity is
		// O (n^2).
		for (int i = 0; i < orderedList.size() - 1; i++) {
			int index = i;
			for (int j = i + 1; j < orderedList.size(); j++)
				if (orderedList.get(j).getOYear() < orderedList.get(index).getOYear())
					index = j;
			LocationAndDate smallerNumber = orderedList.get(index);
			orderedList.set(index, orderedList.get(i));
			orderedList.set(i, smallerNumber);
		}

		/*
		 * Insertion sort approach - best time complexity is O (n) while average
		 * and worst is O (n^2)
		 * 
		 * for (int outerIndex = 1; outerIndex < orderedList.size();
		 * outerIndex++) { for (int innerIndex = outerIndex; innerIndex > 0;
		 * innerIndex--) { LocationAndDate lowerObservation =
		 * orderedList.get(innerIndex - 1); LocationAndDate higherObservation =
		 * orderedList.get(innerIndex); if
		 * (higherObservation.getOYear().compareTo(lowerObservation.getOYear())
		 * < 0) { orderedList.set(innerIndex, lowerObservation);
		 * orderedList.set(innerIndex - 1, higherObservation); } else { break; }
		 * } }
		 */

		int counter = 1;
		System.out.println(
				"Feature set G: List recording locations in chronological order of their first weather measurement (Basic) \n"
						+ Presentation.bar + "\n");

		for (int singleIndex = 0; singleIndex < orderedList.size(); singleIndex++) {
			Month month = Month.of(orderedList.get(singleIndex).getOMonth());
			System.out.print(counter + ") Station: " + orderedList.get(singleIndex).getCLocation() + " ");
			System.out.print(" | First Measurement - " + month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " ");
			System.out.println(orderedList.get(singleIndex).getOYear() + " ");
			counter++;
		}
		Presentation.pauseAndClearScreen(userInput);
	}
}
