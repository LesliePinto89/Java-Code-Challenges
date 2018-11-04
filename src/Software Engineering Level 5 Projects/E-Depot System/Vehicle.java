package depot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Vehicle implements Schedulable{
	protected String make;
	protected String model;
	protected int weight;
	protected String regNo;
	protected List <WorkSchedule> workSchedules = new ArrayList<>();
	
	
	protected Vehicle(String make, String model, int weight, String regNo){
		this.make = make;
		this.model = model;
		this.weight = weight;
		this.regNo = regNo;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	/**
	 * Checks a vehicle's work schedules to determine their availability ensuring that a work schedules
	 * start and end dates do not overlap any existing work schedules a vehicle may have. 
	 * @param start
	 * @param end
	 * @return whether a vehicle is available 
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
	 * Checks whether a vehicle has any pending or active work schedules, if a vehicle has either it returns false
	 * @param moveDate
	 * @return whether a vehicle is available to be moved
	 */
	
	public boolean isAvailableForMove(LocalDateTime moveDate) {
		for (WorkSchedule schedule: workSchedules) {
			if ((schedule.getState() == Status.PENDING || schedule.getState() == Status.ACTIVE) 
				&& (schedule.getEndDate().isAfter(moveDate) || schedule.getEndDate().isEqual(moveDate))) {
					return false;
			}
		}
		return true;
	}
	
	public void setSchedule(WorkSchedule newWorkSchedule){
		workSchedules.add(newWorkSchedule);
	}
	
	public String toString() {
		return  this.getMake() + " " 
				+ this.getModel() + " " + this.getWeight() + " " 
				+ this.getRegNo() ;
	}
}
