package patriots.basic.datareader;

import java.awt.AWTException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import patriots.basic.model.ClimateStation;
import patriots.basic.model.Observation;

public class ClimateDataReader {

	/**
	 * Class organises all needed data from climate station files into memory.
	 **/

	// Variables for below methods
	private String currentTextRead;
	private List<ClimateStation> climateStations = new LinkedList<ClimateStation>();
	private File currentFile;

	/**
	 * Open each climate file from gitLab repository to be used in mentioned
	 * method call.
	 * 
	 * @param directory
	 * @param file
	 * @param currentFile
	 * 
	 * @throws FileNotFoundException
	 * @throws AWTException
	 **/
	public void readWeatherStationFiles() throws FileNotFoundException, AWTException {
		File directory = new File("Weather Station Files");
		for (File file : directory.listFiles()) {
			currentFile = file;
			addLocationDataToArrayList();
		}
	}

	/**
	 * Gets all climate station objects from memory when method is called
	 * 
	 * @param climateStations
	 * @return climateStations
	 **/
	public List<ClimateStation> getClimateStations() {
		return climateStations;
	}

	/**
	 * Handles unwanted data problems when organising data from station files
	 * 
	 * @param readFromFile
	 * @param newObservation
	 * @param currentTextRead
	 * @param noteStored
	 **/
	private void sortByAnamoly(Observation newObservation, Scanner readFromFile) {
		currentTextRead = readFromFile.next();

		String noteStored;
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
	 * @param newObservation
	 * @param location
	 * @param newObservation
	 * @param currentTextRead
	 * @param noteStored
	 * @throws FileNotFoundException
	 * @throws AWTException
	 **/
	private void addLocationDataToArrayList() throws FileNotFoundException, AWTException {
		Scanner readFromFile = new Scanner(currentFile);
		currentTextRead = readFromFile.next();
		do {

			ClimateStation location = new ClimateStation();
			currentTextRead = currentTextRead.toUpperCase();
			location.setLocation(currentTextRead);
			currentTextRead = readFromFile.next();
			if (currentTextRead.contains("Location")) {
				readFromFile.useDelimiter(",");
				location.setCoOrdinates(readFromFile.next());
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
				case "Lon":
					readFromFile.useDelimiter(",");
					location.setLongitude(readFromFile.next());
					readFromFile.reset();
					readFromFile.next();
					location.setAboveAverageSeaLevel(readFromFile.next());
					climateStations.add(location);
					currentTextRead = readFromFile.nextLine();
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
				location.getObservations().add(newObservation);

			}

		} while (readFromFile.hasNextLine());
		readFromFile.close();
	}
}
