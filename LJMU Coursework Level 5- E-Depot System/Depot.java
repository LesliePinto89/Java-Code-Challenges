package depot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Depot {

	private int depotID;
	private String locationName;

	private Map<String, Driver> drivers = new ConcurrentHashMap<>();
	private Map<String, Vehicle> vehicles = new ConcurrentHashMap<>();
	private List<WorkSchedule> workSchedules = Collections.synchronizedList(new ArrayList<>());
	

	public Depot() {
	}

	public Depot(int depotID, String locationName) {
		this.depotID = depotID;
		this.locationName = locationName;
	}

	public int getDepotID() {
		return depotID;
	}

	public void setDepotID(int depotID) {
		this.depotID = depotID;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	@Override
	public String toString() {
		return "Depot [depotName=" + depotID + ", locationName=" + locationName + "]";
	}

	public Driver logOn(String userName, String password) {
		Driver driver = drivers.get(userName);

		//If driver name is found but password is incorrect
		if (driver != null && driver.checkPassword(password) == false) {
			driver = null;
		} 
		return driver;
	}

	public void addDriver(Driver driver) {
		drivers.put(driver.getUserName(), driver);
	}

	public Driver getDriver(String userName) {
		return drivers.get(userName);
	}

	public Collection<Driver> getDrivers() {
		return drivers.values();
	}
	
	public Collection<Vehicle> getVehicles() {
		return vehicles.values();
	}
	
	

	public Vehicle getVehicle(String regNo) {
		return vehicles.get(regNo);
	}
	
	/**
	 * Gets a list of available Tankers. Delegates to the getAvailableVehicles method to check against work schedules
	 * and then filters this list based on liquid type and required liquid capacity.
	 * and is available 
	 * @param startDate
	 * @param endDate
	 * @param liquidCargoWeight
	 * @param liquidType
	 * @return
	 */
	public List<Vehicle> getAvailableTankers(LocalDateTime startDate, LocalDateTime endDate, int liquidCargoWeight,
			String liquidType) {
		List<Vehicle> allTankers = getAvailableVehicles("Tanker", startDate, endDate);
		List<Vehicle> filteredTankers = new ArrayList<>();
		for (Vehicle tanker: allTankers) {
			if (liquidType.equalsIgnoreCase(((Tanker)tanker).getLiquidType()) 
					&& ((Tanker)tanker).getLiquidCapacity() >= liquidCargoWeight) {
				filteredTankers.add(tanker);
			}
		}
		return filteredTankers;
	}
	
	/**
	 * Gets a list of available trucks. Delegates to the getAvailableVehicles method to check against work schedules
	 * and then filters this list based on the required capacity.
	 * @param start
	 * @param end
	 * @param requiredCapacity
	 * @return
	 */
	public List<Vehicle> getAvailableTrucks(LocalDateTime start, LocalDateTime end, int requiredCapacity) {
		List<Vehicle> allTrucks =  getAvailableVehicles("Truck", start, end);
		List<Vehicle> filteredTrucks = new ArrayList<>();
		for (Vehicle truck : allTrucks) {
			if (((Truck)truck).getCargoCapacity() >= requiredCapacity) {
				filteredTrucks.add(truck);
			}
		}
		return filteredTrucks;
	}
	
	/**
	 * Gets a list of available vehicles of the type specified in the vehicleType field that don't have clashing work schedules.
	 * @param vehicleType
	 * @param start
	 * @param end
	 * @param requiredCapacity
	 * @return a list of available vehicles 
	 */
	private List<Vehicle> getAvailableVehicles(String vehicleType, LocalDateTime start, LocalDateTime end) {
		List<Vehicle> availableVehicles = new  ArrayList<>();
		for (Vehicle vehicle : vehicles.values()) {
			if (vehicle.isAvailable(start, end)) {
				if(vehicleType.equalsIgnoreCase("Tanker")) {
					if (vehicle instanceof Tanker) {
						availableVehicles.add(vehicle);
					}
				} else {
					if (vehicle instanceof Truck) {
						availableVehicles.add(vehicle);
					}
				}
			}
		}
		return availableVehicles;
	}
	
	/**
	 * Obtains a list of available drivers for a work schedule	 
	 * @param start
	 * @param end
	 * @return a list of available drivers
	 */
	public List<Driver> getAvailableDrivers(LocalDateTime start, LocalDateTime end) {
		List<Driver> availableDrivers = new ArrayList<>();
		for (Driver driver : drivers.values()) {
			if (driver.isAvailable(start, end)) {
				availableDrivers.add(driver);
			}
		}
		return availableDrivers ;
	}

	/**
	 * Adds a vehicle to a depot used for transfer of vehicles
	 * @param newVehicle
	 */
	public void addVehicle(Vehicle newVehicle) {
		vehicles.put(newVehicle.getRegNo(), newVehicle);
	}
	
	/**
	 * Removes vehicle from depot used with transfer of a vehicle from one depot to another
	 */
	public void removeVehicle(String regNo) {
		vehicles.remove(regNo);
	}

	public List<WorkSchedule> getWorkSchedules() {
		return workSchedules;
	}

	public void addWorkSchedule(WorkSchedule workSchedule) {
		this.workSchedules.add(workSchedule);
	}
}
