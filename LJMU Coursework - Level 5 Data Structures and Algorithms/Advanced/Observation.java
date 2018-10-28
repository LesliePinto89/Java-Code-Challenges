package patriots.advanced.model;

import java.util.ArrayList;
import java.util.List;

public class Observation {

	/**
	 * This class is the data model that provides structure to observation
	 * objects.
	 **/
	private ObservationDate date = new ObservationDate();
	private Double totalMinDegC;
	private Double totalMaxDegC;
	private Double sunshineHours;
	private Double rainInMillimeters;
	private Integer frostyDays;

	private List<String> observationNotes = new ArrayList<String>();

	// Use above list to allow adding observation data to above list.
	public boolean addNotes(String newNote) {
		observationNotes.add(newNote);
		return true;
	}

	// Use with search function to "get the station"
	public List<String> getNote() {
		return observationNotes;
	}

	public ObservationDate getDate() {
		return date;
	}

	// Getters and Setters
	public void setTotalMinDegC(Double totalMinDegC) {
		this.totalMinDegC = totalMinDegC;
	}

	public Double getTotalMinDegC() {
		return totalMinDegC;
	}

	// Made to primitive type for retrieval from ObservationDate.class
	public void setYear(int year) {
		this.date.setYear(year);
	}

	// Made to primitive type for retrieval from ObservationDate.class
	public void setMonth(int month) {
		this.date.setMonth(month);
	}

	public void setTotalMaxDegC(Double totalMaxDegC) {
		this.totalMaxDegC = totalMaxDegC;
	}

	public Double getTotalMaxDegC() {
		return totalMaxDegC;
	}

	public void setDaysOfAirFrost(Integer frostyDays) {
		this.frostyDays = frostyDays;
	}

	public int getDaysOfAirFrost() {
		return frostyDays;
	}

	public void setRainInMillimeters(Double rainInMillimeters) {
		this.rainInMillimeters = rainInMillimeters;
	}

	public Double getRainInMillimeters() {
		return rainInMillimeters;
	}

	public void setSunshineInHours(Double sunshineHours) {
		this.sunshineHours = sunshineHours;
	}

	public Double getSunshineInHours() {
		return sunshineHours;
	}

	public String toString() {
		return "Year: " + date.getYear() + "| Month: " + date.getMonth() + "| Total Max Degree Celcius: " + totalMaxDegC
				+ "| Total Min Degree Celcius: " + totalMinDegC + "| Days Of Air Frost: " + frostyDays + "| Rain (mm): "
				+ rainInMillimeters + "| Sunshine Hours: " + sunshineHours + "| Notes: " + observationNotes;
	}
}
