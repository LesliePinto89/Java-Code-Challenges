package depot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Driver implements Schedulable{
	protected String userName;
	protected String password;
	protected int assignedDepot;
	protected String id;
	protected List <WorkSchedule> workSchedules = new ArrayList<>();
	private boolean isRequiringNotification;

	public Driver(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public boolean checkPassword(String userPassword) {
		return password.equals(userPassword);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDepotName(int assignedDepot) {
		this.assignedDepot = assignedDepot;
	}

	public int getDepotName() {
		return assignedDepot;

	}
	
	/**
	 * Checks a driver's work schedules to determine their availability ensuring that a work schedules
	 * start and end dates do not overlap any existing work schedules a vehicle may have. 
	 * @param start
	 * @param end
	 * @return whether a driver is available 
	 */
	
	public boolean isAvailable(LocalDateTime start, LocalDateTime end){
		for (WorkSchedule schedule: workSchedules) {
			if (!schedule.getStartDate().isAfter(end)
					&& !schedule.getEndDate().isBefore(start)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if the driver requires notification.
	 * Automatically sets requiring notification to false to avoid
	 * duplicate notifications. 
	 * @return 
	 */
	public boolean isRequiringNotification() {
		boolean isRequiringNotification = this.isRequiringNotification;
		this.isRequiringNotification = false;
		return isRequiringNotification;
	}
	
	/**
	 * Adds to a work schedule to the driver's list
	 * On doing so sets requirement for notification 
	 */
	public void setSchedule(WorkSchedule newWorkSchedule){
		workSchedules.add(newWorkSchedule);
		isRequiringNotification = true;
	}
	
	public List<WorkSchedule> getSchedules(){
		return workSchedules;
	}
	
}
