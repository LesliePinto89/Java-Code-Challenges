package patriots.basic.search;

public class LocationAndDate {

	/**
	 * Data model that is used to sort stations data by first record
	 **/

	// Variables for below methods
	private String location;
	private Integer month;
	private Integer year;

	// Getters and Setters
	public void setCLocation(String location) {
		this.location = location;
	}

	public String getCLocation() {
		return location;
	}

	public void setOMonth(Integer month) {
		this.month = month;
	}

	public Integer getOMonth() {
		return month;
	}

	public void setOYear(Integer year) {
		this.year = year;
	}

	public Integer getOYear() {
		return year;
	}

	public String toString() {
		return location + " " + month + " " + year;
	}
}
