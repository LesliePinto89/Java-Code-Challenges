package depot;

import java.time.LocalDateTime;

public class WorkSchedule {
	private String client;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private Status state;
	private Driver driver;
	private Vehicle vehicle;
	
	private WorkSchedule() {
		state = Status.PENDING; //Default state
	}

	public String getClient() {
		return client;
	}


	public LocalDateTime getStartDate() {
		return startDate;
	}

	
	public LocalDateTime getEndDate() {
		return endDate;
	}
	
	public Status getState() {
		return state;
	}
	
	public Driver getDriver() {
		return driver;
	}

	

	public Vehicle getVehicle() {
		return vehicle;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("Client: ");
		sb.append(client);
		sb.append("\n");
		sb.append("Start Date: ");
		sb.append(startDate);
		sb.append("\n");
		sb.append("End Date: ");
		sb.append(endDate);
		sb.append("\n");
		sb.append("Status: ");
		sb.append(state.name());
		sb.append("\n");
		sb.append(vehicle.toString());
		sb.append("\n");
		sb.append(driver.getUserName());
		sb.append("\n\n");
		return sb.toString();
	}

	
	/**
	 * Prints update to status as status changes
	 * @param status
	 */
	public void setState(Status status) {
		state = status;
		System.out.format("\ndebug - workschedule status changed to %s\n", status.name());
	}
	
	/**
	 * Uses the builder pattern to ensure only a valid work schedule is created. 
	 * As a good description and explanation please see https://dzone.com/articles/design-patterns-the-builder-pattern
	 *
	 */
	public static class Builder {
		private String client;
		private LocalDateTime startDate;		
		private LocalDateTime endDate;
		private Driver driver;
		private Vehicle vehicle;
		
		public WorkSchedule build() {
			WorkSchedule workSchedule = new WorkSchedule();
			workSchedule.client = client;
			workSchedule.driver = driver;
			workSchedule.startDate = startDate;
			workSchedule.endDate = endDate;
			workSchedule.vehicle = vehicle;
			return workSchedule;
		}

		public Builder withClient(String client) {
			this.client = client;
			return this;
		}
		public Builder withStartDate(LocalDateTime startDate) {
			this.startDate = startDate;
			return this;
		}
		public Builder withEndDate(LocalDateTime endDate) {
			this.endDate = endDate;
			return this;
		}
		public Builder withDriver(Driver driver) {
			this.driver = driver;
			return this;
		}
		public Builder withVehicle(Vehicle vehicle) {
			this.vehicle = vehicle;
			return this;
		}

	}
}
