package patriots.basic.model;

import java.util.LinkedList;
import java.util.List;

public class ClimateStation {

	/**
	 * The data model that provides structure to climate station objects
	 **/

	// Variables utilise below methods
	private String location;
	private String coOrdinates;
	private String latitude;
	private String longitude;
	private String altitude;
	private List<Observation> stationObservations = new LinkedList<Observation>();

	// Use above linked list to allow adding observation data to above list.
	public boolean addObservation(Observation newObservation) {
		stationObservations.add(newObservation);
		return true;
	}

	// Use with search function to "get the station"
	public List<Observation> getObservations() {
		return stationObservations;
	}

	// Getters and Setters
	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setCoOrdinates(String coOrdinates) {
		this.coOrdinates = coOrdinates;
	}

	public String getCoOrdinates() {
		return coOrdinates;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getlatitude() {
		return latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setAboveAverageSeaLevel(String altitude) {
		this.altitude = altitude;
	}

	public String getAboveAverageSeaLevel() {
		return altitude;
	}

	public String toString() {

		return "Location: " + location + " | Co-Ordinates:" + coOrdinates + " | Latitude: " + latitude
				+ " | Longitude: " + longitude + " | Altitude: " + altitude + "|";

	}
}
