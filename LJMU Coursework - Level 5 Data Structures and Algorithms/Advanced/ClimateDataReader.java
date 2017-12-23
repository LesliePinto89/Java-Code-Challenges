package patriots.advanced.datareader;

import java.awt.AWTException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import patriots.advanced.model.ClimateStation;
import patriots.advanced.model.Observation;

public class ClimateDataReader {

	/**
	 * Class organises all needed data from climate station files into memory.
	 **/

	// Variables for below methods
	private File currentFile;
	private String currentTextRead;

	// Use a Hash map for quick key value pair retrieval
	private Map<String, ClimateStation> climateStations = new HashMap<>();

	/**
	 * Open each climate file from gitLab repository to be used in mentioned
	 * method call.
	 * 
	 * @param dir
	 * @param file
	 * @param files
	 *            - Array to store file directory of files
	 * @param currentFile
	 * 
	 * @throws FileNotFoundException
	 * @throws AWTException
	 **/
	public void readWeatherStationFiles() throws FileNotFoundException, AWTException {

		File dir = new File("Weather Station Files");
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				currentFile = file;
				addLocationDataToArrayList();
			}
		}
	}

	/**
	 * Gets all climate station objects from memory when method is called
	 * 
	 * @return climateStations
	 **/
	public Map<String, ClimateStation> getClimateStations() {
		return climateStations;
	}

	/**
	 * Handles unwanted data problems when organising data from station files
	 * 
	 * @param noteStored
	 * @param readFromFile
	 * @param currentTextRead
	 **/
	private void sortByAnamoly(Observation newObservation, Scanner readFromFile) {
		String noteStored;
		currentTextRead = readFromFile.next();

		switch (currentTextRead) {
		case "---":
			currentTextRead = null;
			noteStored = "Missing data detected";
			newObservation.addNotes(noteStored);
			break;

		case "#":
			currentTextRead = readFromFile.next();
			break;

		case "Provisional":
			noteStored = "Provisional";
			newObservation.addNotes(noteStored);
			if (!readFromFile.hasNextLine()) {
				break;
			} else {
				currentTextRead = readFromFile.next();
				break;
			}
		default:
			if (currentTextRead.endsWith("*")) {
				currentTextRead = currentTextRead.replaceAll("\\*", "");
				noteStored = "Estimate data detected";
				newObservation.addNotes(noteStored);
			} else if (currentTextRead.endsWith("#")) {
				currentTextRead = currentTextRead.replaceAll("\\#", "");
				noteStored = "Sun hours from automatic Kipp & Zonen sensor ";
				newObservation.addNotes(noteStored);
			}
		}

		// Handle closed stations
		if (currentTextRead == null) {
			return;
		} else if (currentTextRead.equals("Site")) {
			readFromFile.nextLine();
			noteStored = "Site Closed";
			newObservation.addNotes(noteStored);
		}
	}

	/**
	 * Adds in desired order the data from station files into memory.
	 * 
	 * @param readFromFile
	 * @param currentTextRead
	 * @throws FileNotFoundException
	 **/
	private void addLocationDataToArrayList() throws FileNotFoundException {
		Scanner readFromFile = new Scanner(currentFile);
		currentTextRead = readFromFile.next();
		do {

			ClimateStation location = new ClimateStation();
			currentTextRead = currentTextRead.toUpperCase();
			location.setLocation(currentTextRead);
			currentTextRead = readFromFile.next();

			if (currentTextRead.contains("Location")) {
				readFromFile.useDelimiter(",");
				location.setCoordinates(readFromFile.next());
				readFromFile.reset();
				readFromFile.next();
				currentTextRead = readFromFile.next();
			}

			while (!currentTextRead.contains("deg")) {
				switch (currentTextRead) {
				case "Lat":
					currentTextRead = readFromFile.next();
					location.setLatitude(currentTextRead);
					currentTextRead = readFromFile.next();
					break;
				case "Lon":
					readFromFile.useDelimiter(",");
					location.setLongitude(readFromFile.next());
					readFromFile.reset();
					readFromFile.next();
					location.setAboveAverageSeaLevel(readFromFile.next());
					climateStations.put(location.getLocation(), location);
					currentTextRead = readFromFile.nextLine();
					break;
				default:
					currentTextRead = readFromFile.nextLine();
					break;
				}
			}
			currentTextRead = readFromFile.next();

			while (readFromFile.hasNextLine()) {
				Observation newObservation = new Observation();
				newObservation.setYear(Integer.parseInt(currentTextRead));
				newObservation.setMonth(readFromFile.nextInt());

				sortByAnamoly(newObservation, readFromFile);
				if (currentTextRead == null) {
					newObservation.setTotalMaxDegC(null);
				} else {
					newObservation.setTotalMaxDegC(Double.parseDouble(currentTextRead));
				}

				sortByAnamoly(newObservation, readFromFile);
				if (currentTextRead == null) {
					newObservation.setTotalMinDegC(null);
				} else {
					newObservation.setTotalMinDegC(Double.parseDouble(currentTextRead));
				}

				sortByAnamoly(newObservation, readFromFile);
				if (currentTextRead == null) {
					newObservation.setDaysOfAirFrost(null);
				} else {
					newObservation.setDaysOfAirFrost(Integer.parseInt(currentTextRead));
				}

				sortByAnamoly(newObservation, readFromFile);
				if (currentTextRead == null) {
					newObservation.setRainInMillimeters(null);
				} else {
					newObservation.setRainInMillimeters(Double.parseDouble(currentTextRead));
				}

				sortByAnamoly(newObservation, readFromFile);
				if (currentTextRead == null) {
					newObservation.setSunshineInHours(null);
				} else {
					newObservation.setSunshineInHours(Double.parseDouble(currentTextRead));
				}

				sortByAnamoly(newObservation, readFromFile);
				location.addObservation(newObservation);

			}
		} while (readFromFile.hasNextLine());
		readFromFile.close();
	}
}
