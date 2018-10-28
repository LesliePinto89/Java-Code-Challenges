package system;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import depot.Depot;
import depot.Driver;
import depot.Manager;
import depot.Tanker;
import depot.Truck;
import depot.Vehicle;

public class LoadData {

	/**
	 * Loads all depots from file system into the new depots HashMap. A depot is
	 * identified by depot ID number and depot name
	 * @return 
	 * 
	 * @throws FileNotFoundException
	 */
	public static Map<Integer, Depot> loadDepots() throws FileNotFoundException {
		Map<Integer, Depot> newDepots = new HashMap<>();
		File depotsFile = new File("Depots.txt");
		Scanner inFile = new Scanner(depotsFile);
		while (inFile.hasNext()) {
			int depotID = inFile.nextInt();
			String depotLocation = inFile.next();
			Depot newDepot = new Depot();
			newDepot.setDepotID(depotID);
			newDepot.setLocationName(depotLocation);
			newDepots.put(depotID, newDepot);
		}
		inFile.close();
		return newDepots;
	}


	/**
	 * Loads vehicles from file. Vehicles area associated with a depot by a
	 * depot's ID number
	 * @param depots 
	 * 
	 * @throws FileNotFoundException
	 */
	public static void loadVehicles(Map<Integer, Depot> depots) throws FileNotFoundException {
		File vehiclesFile = new File("Vehicles.txt");
		Scanner inFile = new Scanner(vehiclesFile);
		while (inFile.hasNext()) {
			int depotID = inFile.nextInt();
			String vehicleMake = inFile.next();
			String vehicleModel = inFile.next();
			int vehicleWeight = inFile.nextInt();
			String regNo = inFile.next();
			String isTanker = inFile.next();
			Vehicle newVehicle;
			newVehicle = createVehicle(inFile, vehicleMake, vehicleModel, vehicleWeight, regNo, isTanker);
			addVehicleToDepot(depotID, newVehicle, depots);
		}
		inFile.close();

	}

	/**
	 * Creates the different types of vehicles for a depot, either a truck or
	 * tanker differentiating tanker types
	 * 
	 * @param inFile
	 * @param vehicleMake
	 * @param vehicleModel
	 * @param vehicleWeight
	 * @param regNo
	 * @param isTanker
	 * @return new Vehicle (a truck or a tanker)
	 */
	private static Vehicle createVehicle(Scanner inFile, String vehicleMake, String vehicleModel, int vehicleWeight,
			String regNo, String isTanker) {
		Vehicle newVehicle;
		if (isTanker.equalsIgnoreCase("Tanker")) {
			int liquidCapacity = inFile.nextInt();
			String liquidType = inFile.next();
			newVehicle = new Tanker(liquidCapacity, liquidType, vehicleMake, vehicleModel, vehicleWeight, regNo);
		} else {
			int cargoCapacity = inFile.nextInt();
			newVehicle = new Truck(cargoCapacity, vehicleMake, vehicleModel, vehicleWeight, regNo);
		}
		return newVehicle;
	}

	/**
	 * Adds a vehicle to their Depot's Vehicles HashMap
	 * 
	 * @param depotID
	 * @param newVehicle
	 * @param depots 
	 */
	private static void addVehicleToDepot(int depotID, Vehicle newVehicle, Map<Integer, Depot> depots) {
		if (depots.containsKey(depotID)) {
			Depot addToDepot = depots.get(depotID);
			addToDepot.addVehicle(newVehicle);
		}
	}
	


	/**
	 * Loads all drivers from file into their depot. Drivers are associated with
	 * a depot by a depot ID number
	 * @param depots 
	 * 
	 * @throws FileNotFoundException
	 */
	public static void loadDrivers(Map<Integer, Depot> depots) throws FileNotFoundException {
		File driverFile = new File("Drivers.txt");
		Scanner inFile = new Scanner(driverFile);
		while (inFile.hasNext()) {
			String driverID = inFile.next();
			String userName = inFile.next();
			String password = inFile.next();
			String isManager = inFile.next();
			int depotID = inFile.nextInt();
			Driver newDriver = createDriver(userName, password, isManager);
			newDriver.setID(driverID);
			addDriverToDepot(depotID, newDriver, depots);
		}
		inFile.close();
	}

	/**
	 * Checks if a driver is a manager by checking T or F. If a driver is a
	 * manager they will be given the options to create a work schedule and move
	 * a vehicle in the menu options.
	 * 
	 * @param userName
	 * @param password
	 * @param isManager
	 * @return Driver or Manager
	 */
	private static Driver createDriver(String userName, String password, String isManager) {
		Driver newDriver;
		if (isManager.equals("T")) {
			newDriver = new Manager(userName, password);
		} else {
			newDriver = new Driver(userName, password);
		}
		return newDriver;
	}

	/**
	 * Adds a driver to their Depot's Drivers HashMap
	 * 
	 * @param depotID
	 * @param newDriver
	 * @param depots 
	 */
	private static void addDriverToDepot(int depotID, Driver newDriver, Map<Integer, Depot> depots) {
		if (depots.containsKey(depotID)) {
			Depot addToDepot = depots.get(depotID);
			addToDepot.addDriver(newDriver);
		}
	}
}
