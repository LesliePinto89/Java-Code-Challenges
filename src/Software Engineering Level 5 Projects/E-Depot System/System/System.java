package system;

import static java.lang.System.out;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import depot.Depot;
import depot.Driver;
import depot.Manager;
import depot.Tanker;
import depot.Truck;
import depot.Vehicle;
import depot.WorkSchedule;

public class System {

	/**
	 * Format for input and output of date times. ISO 8601 format.
	 */
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	/**
	 * Contains all the systems depots
	 */
	private Map<Integer, Depot> depots = new HashMap<>();

	private Scanner console = new Scanner(java.lang.System.in);

	private Driver loggedInDriver;
	private Depot currentDepot;

	/**
	 * Obtains and lists depot names so that a user can select a depot to log
	 * into. If a user does not select a depot or Q option the menu loops
	 */
	public void getDepot() {
		while (currentDepot == null) {
			out.format("Please select by ID from the list below \n");
			out.format("ID - Name \n");
			for (Depot depot : depots.values()) {
				out.format(depot.getDepotID() + "  - " + depot.getLocationName() + "\n");
			}
			out.format("Press Q to quit \n");
			out.format("\nChoice:");
			String choice = console.next();
			if (choice.equalsIgnoreCase("Q")) {
				java.lang.System.exit(0);
			}
			try {
				int depotNo = Integer.parseInt(choice);
				currentDepot = depots.get(depotNo);
				console.nextLine();
			} catch (NumberFormatException nfe) {
			}
		}
	}

	/**
	 * Removes a vehicle from Depot Used with move vehicle method
	 * 
	 * @param existingVehicle
	 */
	private void removeVehicleFromDepot(Vehicle existingVehicle) {
		currentDepot.removeVehicle(existingVehicle.getRegNo());
	}

	/**
	 * Moves a vehicle selected by registration number from one depot to
	 * another, checking that the vehicle exists and is available. 3 seconds
	 * simulates travel time between one depot and another after which
	 * confirmation is provided of a move.
	 */
	private void moveVehicle() {
		out.println("Please specify the registration number of the " + "vehicle you would like to move");
		console.nextLine();
		String regNo = console.nextLine();
		Vehicle selectedVehicle = currentDepot.getVehicle(regNo);
		if (selectedVehicle == null) {
			out.format("This registration number %s does not exist in Depot %s \n\n", regNo,
					currentDepot.getLocationName());
			return;
		}
		LocalDateTime moveDate = getValidDateFromUser("move", "transfer");
		boolean result = selectedVehicle.isAvailableForMove(moveDate);
		if (result) {
			final Depot depotDestination = getDepotFromUser();
			out.println("Please enter Y to confirm the vehicle move");
			String confirmation = console.next();
			if (confirmation.equalsIgnoreCase("Y")) {
				Runnable vehicleMover = new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						depotDestination.addVehicle(selectedVehicle);
					}
				};
				removeVehicleFromDepot(selectedVehicle);
				Thread t = new Thread(vehicleMover);
				t.start();
				out.format(selectedVehicle.getRegNo() + " has moved to " + depotDestination.getLocationName() + "\n");

			} else {
				try {
					menu();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} else {
			out.print(
					"Sorry this vehicle cannot be moved as it has work schedules" + "that clash with the move date \n");
		}

	}

	/**
	 * Requests destination for a vehicle to be moved to, ensuring that the
	 * depot is one on the list and that a vehicle cannot accidentally be sent
	 * to its own depot
	 * 
	 * @return destination
	 */
	private Depot getDepotFromUser() {
		Depot destination = null;
		while (destination == null) {
			out.format("Please select by ID from the list below the depot you wish to move the vehicle to \n");
			out.format("ID - Name \n");
			for (Depot depot : depots.values()) {
				out.format(depot.getDepotID() + "  - " + depot.getLocationName() + "\n");
			}

			String choice = console.next();

			try {
				int depotNo = Integer.parseInt(choice);
				destination = depots.get(depotNo);
				console.nextLine();
				String currentDepotName = this.currentDepot.getLocationName();
				for (Depot depot : depots.values()) {
					out.format(depot.getDepotID() + "  - " + depot.getLocationName() + "\n");
				}
				if (destination.getLocationName().equals(currentDepotName)) {
					out.format("You cannot move a vehicle to its own depot you are logged into %s ", currentDepotName);
					destination = null;
				}
			} catch (NumberFormatException nfe) {
			}
		}
		return destination;
	}

	/**
	 * Lists work schedules for Drivers. If a driver is also a manger they are
	 * able to view the work schedules for other drivers in the depot.
	 */
	private void viewWorkSchedule() {
		List<WorkSchedule> retrivedSchedules;
		Driver foundDriver = null;
		if (loggedInDriver instanceof Manager) {
			out.format("Please type in the driver's name: ");
			String desiredName = console.next();
			desiredName = Character.toUpperCase(desiredName.charAt(0)) + desiredName.substring(1);
			foundDriver = currentDepot.getDriver(desiredName);
			if (foundDriver == null) {
				out.format("Name not found\n");
				return;
			}
			retrivedSchedules = foundDriver.getSchedules();
			if (retrivedSchedules.isEmpty()) {
				out.println("No schedules have been made for " + desiredName + "\n");
				return;
			}
		} else {
			retrivedSchedules = loggedInDriver.getSchedules();
			if (retrivedSchedules.isEmpty()) {
				out.format("No schedules have been made for" + loggedInDriver.getUserName());
			}
		}
		for(WorkSchedule ws : retrivedSchedules) {
			out.print(ws.toString());
		}

	}

	/**
	 * Creates a work schedule by obtaining from the user relevant information,
	 * applying validation to each input with the exception of client name
	 * 
	 * @throws ParseException
	 */
	private void createWorkSchedule() throws ParseException {
		String client = getClient();
		String dateType = "work schedule";
		LocalDateTime startDate = getValidDateFromUser("start", dateType);
		if (!Validator.isValidStartDate(startDate, formatter)) {
			return;
		}
		LocalDateTime endDate = getValidDateFromUser("end", dateType);
		if (!Validator.isWorkScheduleDurationValid(startDate, endDate)) {
			return;
		}
		Vehicle selectedVehicle = selectVehicle(startDate, endDate);
		if (selectedVehicle == null) {
			return;
		}
		Driver selectedDriver = selectDriver(startDate, endDate);
		if (selectedDriver == null) {
			return;
		}

		WorkSchedule newWorkSchedule = new WorkSchedule.Builder()
				.withClient(client)
				.withDriver(selectedDriver)
				.withVehicle(selectedVehicle)
				.withStartDate(startDate)
				.withEndDate(endDate)
				.build();
		selectedVehicle.setSchedule(newWorkSchedule);
		selectedDriver.setSchedule(newWorkSchedule);
		currentDepot.addWorkSchedule(newWorkSchedule);
	}

	/**
	 * Obtains the name of a client, no validation is applied as no requirements
	 * were provided for this feature
	 * 
	 * @return
	 */
	private String getClient() {
		out.format("Please enter the name of the client \n");
		console.nextLine();
		String client = console.nextLine();
		return client;
	}

	/**
	 * Lists a depot's drivers available to undertake a work schedule. It is
	 * assumed that all drivers can drive all vehicles so no filtering other
	 * than checking their availability is applied.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List of Drivers
	 */
	private List<Driver> listAvailableDrivers(LocalDateTime startDate, LocalDateTime endDate) {
		List<Driver> availableDrivers = currentDepot.getAvailableDrivers(startDate, endDate);
		if (availableDrivers.isEmpty()) {
			out.format("Sorry there are no available drivers");
			return null;
		} else {
			out.format("Available Driver(s) \n");
			int number = 1;
			for (Driver driver : availableDrivers) {
				out.format(number + " " + driver.getUserName() + "\n");
				number++;
			}
		}
		return availableDrivers;
	}

	/**
	 * Requests user to select a driver from the list of available drivers
	 * Ensures that an invalid entry cannot be made
	 * 
	 * @param startDate
	 * @param endDate
	 * @return driver selected for work schedule
	 */
	private Driver selectDriver(LocalDateTime startDate, LocalDateTime endDate) {
		List<Driver> availableDrivers = listAvailableDrivers(startDate, endDate);
		if (availableDrivers == null) {
			return null;
		}
		Driver selectedDriver = null;

		while (selectedDriver == null) {
			int selectedDriverNo = Validator
					.getIntegerFromUser("Please enter a number to add a driver to the workschedule", console);
			selectedDriver = availableDrivers.get(selectedDriverNo - 1);
		}
		return selectedDriver;

	}

	/**
	 * Requests a user to select the type of vehicle they require and then
	 * select the vehicle from those available of that type.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private Vehicle selectVehicle(LocalDateTime startDate, LocalDateTime endDate) {
		out.format("Please enter the type of vehicle you want \n");
		String vehicleType = console.next();
		List<Vehicle> availableVehicles = filterVehicleByType(startDate, endDate, vehicleType);
		listAvailableVehicles(availableVehicles);
		return selectVehicle(availableVehicles);
	}

	/**
	 * Filters a depots vehicles by type and work schedule requirements. Ensures
	 * that only valid entries can be made.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param vehicleType
	 * @return suitable vehicles available for the work schedule
	 */
	private List<Vehicle> filterVehicleByType(LocalDateTime startDate, LocalDateTime endDate, String vehicleType) {
		List<Vehicle> availableVehicles;

		while (!vehicleType.equalsIgnoreCase("tanker") & !vehicleType.equalsIgnoreCase("truck")) {
			out.format("Please enter Truck or Tanker\n");
			vehicleType = console.next();
		}

		if (vehicleType.equalsIgnoreCase("tanker")) {
			int liquidCargoWeight = 0;
			while (liquidCargoWeight == 0) {
				liquidCargoWeight = Validator.getIntegerFromUser("Please enter the amount litres", console);

			}

			String workScheduleLiquidtype1 = "chemical";
			String workScheduleLiquidtype2 = "oil";
			String liquidType = null;
			while (!workScheduleLiquidtype1.equalsIgnoreCase(liquidType)
					&& !workScheduleLiquidtype2.equalsIgnoreCase(liquidType)) {
				out.format("Please enter the liquid type \n");
				liquidType = console.next();
			}
			availableVehicles = currentDepot.getAvailableTankers(startDate, endDate, liquidCargoWeight, liquidType);
		}

		else {
			int cargoWeight = 0;
			while (cargoWeight == 0) {
				cargoWeight = Validator.getIntegerFromUser("Please enter the weight of the load", console);
			}
			availableVehicles = currentDepot.getAvailableTrucks(startDate, endDate, cargoWeight);
		}
		return availableVehicles;
	}

	/**
	 * Lists filtered vehicles available for the work schedule
	 * 
	 * @param availableVehicles
	 */
	private void listAvailableVehicles(List<Vehicle> availableVehicles) {
		if (availableVehicles.isEmpty()) {
			out.format("Sorry no available vehicles");
		} else {
			out.format("Available Vehicle(s) \n");
			int count = 1;
			for (Vehicle vehicle : availableVehicles) {
				out.format(count + " " + vehicle.getRegNo() + "\n");
				count++;
			}
		}
	}

	/**
	 * Selects a vehicle to add to the work schedule
	 * 
	 * @param availableVehicles
	 * @return selected vehicle for the work schedule
	 */
	private Vehicle selectVehicle(List<Vehicle> availableVehicles) {
		if (availableVehicles == null || availableVehicles.isEmpty()) {
			return null;
		}
		Vehicle selectedVehicle = null;
		while (selectedVehicle == null) {
			int selectedVehicleNo = Validator
					.getIntegerFromUser("Please enter a number to add a vehicle to the workschedule", console);
			selectedVehicle = availableVehicles.get(selectedVehicleNo - 1);
		}
		return selectedVehicle;

	}

	/**
	 * Obtains and ensures an entered date is valid i.e. will not accept the
	 * following 2019/02/30 25:00
	 * 
	 * @param dateName
	 * @return a valid date
	 */
	private LocalDateTime getValidDateFromUser(String dateName, String dateType) {

		LocalDateTime date = null;
		boolean validDateEntered = false;
		boolean isInThePast = false;
		while (!validDateEntered || isInThePast) {
			out.format("Please enter the %s date for the %s in the following format 'yyyy-MM-dd HH:mm'\n", dateName,
					dateType);

			try {
				String dateString = console.nextLine();
				date = LocalDateTime.parse(dateString, formatter);

				validDateEntered = Validator.isSensibleDate(date, dateString, formatter);
				isInThePast = Validator.isDateInPast(date);
				if (isInThePast) {
					out.println("Dates entered must not be in the past");
				}
			} catch (DateTimeException dte) {
			}
		}
		return date;
	}
	
	/**
	 * Creates a new vehicle (either Truck or Tanker).
	 * Requests vehicle fields from user and delegates to createTanker or createTruck methods to create
	 * specific tanker or truck related fields 
	 */
	
	private void createNewVehicle() {
		out.println("Please enter the Vehicle make: e.g. Volvo");
		String vehicleMake = console.next();
		out.println("Please enter the model: e.g FM11");
		String vehicleModel = console.next();
		int vehicleWeight = Validator.getIntegerFromUser("Please enter the weight of the vehicle: e.g 12000", console);
		out.println("Please enter the registration number of the vehicle");
		String vehicleRegNo = console.next();
		String vehicleType = null;
		while (vehicleType == null
				|| (!vehicleType.equalsIgnoreCase("Tanker") && !vehicleType.equalsIgnoreCase("Truck"))) {
			out.println("Please enter the vehicle type: e.g. Tanker or Truck");
			vehicleType = console.next();
		}
		if (vehicleType.equalsIgnoreCase("Tanker")) {
			createNewTanker(vehicleMake, vehicleModel, vehicleWeight, vehicleRegNo);
		} else {
			createNewTruck(vehicleMake, vehicleModel, vehicleWeight, vehicleRegNo);
		}
	}
	
	/**
	 * Creates new Truck using parameters obtained by createNewVehicle
	 * @param vehicleMake
	 * @param vehicleModel
	 * @param vehicleWeight
	 * @param vehicleRegNo
	 */
	private void createNewTruck(String vehicleMake, String vehicleModel, int vehicleWeight, String vehicleRegNo) {
		int cargoCapacity = 0;
		while (cargoCapacity == 0) {
			cargoCapacity = Validator.getIntegerFromUser("Please enter the the cargo capacity of the Truck: e.g. 19000",
					console);
		}
		Truck newTruck = new Truck(cargoCapacity, vehicleMake, vehicleModel, vehicleWeight, vehicleRegNo);
		currentDepot.addVehicle(newTruck);
		out.format("Truck registration number: %s has been added to %s \n", vehicleRegNo,
				currentDepot.getLocationName());
	}
	
	/**
	 * Creates new Tanker using parameters obtained by createNewVehicle
	 * @param vehicleMake
	 * @param vehicleModel
	 * @param vehicleWeight
	 * @param vehicleRegNo
	 */
	private void createNewTanker(String vehicleMake, String vehicleModel, int vehicleWeight, String vehicleRegNo) {
		int liquidCapacity = 0;
		while (liquidCapacity == 0) {
			liquidCapacity = Validator
					.getIntegerFromUser("Please enter the the liquid capacity of the Tanker: e.g. 19000", console);

		}
		String tankerType = null;
		while (tankerType == null
				|| (!tankerType.equalsIgnoreCase("oil") && !tankerType.equalsIgnoreCase("chemical"))) {
			out.println("Please enter the type of tanker: e.g. Chemical");
			tankerType = console.next();
		}
		Tanker newTanker = new Tanker(liquidCapacity, tankerType, vehicleMake, vehicleModel, vehicleWeight,
				vehicleRegNo);
		currentDepot.addVehicle(newTanker);
		out.format("Tanker registration number: %s has been added to %s \n", vehicleRegNo,
				currentDepot.getLocationName());
	}

	/**
	 * Menu for eDepot System. If a User is a Manager options 2,3,4 and 5 will be
	 * displayed otherwise only option 1 to view a users work schedule.
	 * 
	 * @throws ParseException
	 */
	public void menu() throws ParseException {
		while (true) {
			out.format("Main menu\n");
			out.format("Type \"1\" to view work schedule\n");
			if (loggedInDriver instanceof Manager) {
				out.format("Type \"2\" to move a vehicle to another depot\n");
				out.format("Type \"3\" to create a work schedule\n");
				out.format("Type \"4\" to add a new vehicle to the depot\n");
				out.format("Type \"5\" to add a new driver to the depot\n");
			}
			out.format("Type \"L\" to logout\n");
			out.format("Type \"Q\" to quit\n");
			out.format("Command: ");
			String choice = console.next();
			switch (choice) {
			case "1":
				viewWorkSchedule();
				break;
			case "2":
				if (loggedInDriver instanceof Manager) {
					moveVehicle();
				}
				break;
			case "3":
				if (loggedInDriver instanceof Manager) {
					createWorkSchedule();
				}
				break;
			case "4":
				if (loggedInDriver instanceof Manager) {
					createNewVehicle();
				}
				break;
			case "5":
				if (loggedInDriver instanceof Manager)
					createDriver();
				out.format("\n");
				break;
			case "l":
			case "L":
				currentDepot = null;
				loggedInDriver = null;
				return;
			case "q":
			case "Q":
				console.close();
				java.lang.System.exit(0);
			default:
				break;
			}
		}
	}

	/**
	 * Creates a driver based on user input (either manager or just a driver).
	 */
	private void createDriver() {
		out.println("Please enter driver's username: ");
		String userName = console.next();
		out.println("Please enter driver's password: ");
		String password = console.next();
		boolean isManager = Validator.getBooleanFromUser("Please state whether the driver is manager T "
			 													+ "or driver F: e.g. T or F", console);
		
		Driver newDriver;
		if(isManager == true) {
			newDriver = new Manager(userName, password);
		}
		else {
			newDriver = new Driver(userName, password);
		}
		currentDepot.addDriver(newDriver);
		out.format("Driver with username: %s has been added to %s \n", userName,
				currentDepot.getLocationName());
	}

	/**
	 * Requests user's name and password and delegates to the currentDepot to
	 * login.
	 */
	public void logOn() {
		while (loggedInDriver == null) {
			out.format("Please enter your username: ");
			String inputName = console.nextLine();
			// Changes first letter to upper case to match txt file
			inputName = Character.toUpperCase(inputName.charAt(0)) + inputName.substring(1);
			out.format("Please enter your password: ");
			String inputPassword = console.nextLine();
			loggedInDriver = currentDepot.logOn(inputName, inputPassword);
			if (loggedInDriver == null) {
				out.format("\nCredentials not found, please try again\n");
			}
		}
		if (loggedInDriver.isRequiringNotification()) {
			out.println("Your workschedules have been updated");
		}
	}

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		System system = new System();
		system.depots = LoadData.loadDepots();
		LoadData.loadVehicles(system.depots);
		LoadData.loadDrivers(system.depots);

		Runnable stateChanger = new StateChanger(system);
		Thread t = new Thread(stateChanger);
		t.start();
		// When a user logs out they have to pick a depot before logging back
		// in.
		while (true) {
			system.getDepot();
			system.logOn();
			system.menu();
		}
	}

	protected Collection<Depot> getDepots() {
		return depots.values();
	}
}
