package patriots.advanced.model;

import java.util.TreeMap;

import patriots.advanced.search.Aggregate;
import patriots.advanced.search.Value;

public class ClimateStation {

	/**
	 * This class is the data model that provides structure to climate station
	 * objects.
	 **/

	private String location;
	private String coordinates;
	private String latitude;
	private String longitude;
	private String altitude;
	private Aggregate aggregates;

	// Create a TreeMap as part of the station object.
	private TreeMap<ObservationDate, Observation> stationObservations = new TreeMap<>();

	// Use above list to allow adding observation data to above list.
	public boolean addObservation(Observation newObservation) {
		stationObservations.put(newObservation.getDate(), newObservation);
		return true;
	}

	// Use with search function to "get the station"
	public TreeMap<ObservationDate, Observation> getObservations() {
		return stationObservations;
	}

	// Getters and Setters
	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public String getCoordinates() {
		return coordinates;
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

	public Aggregate getAggregate() {
		if (aggregates == null) {
			populateAggregates();
		}
		return aggregates;
	}

	/**
	 * Populates aggregates data - obtains values for each min and max pair
	 * creating a summary of aggregate values for a ClimateStation. aggregates
	 * is only populated when required. Methods that use it call this method if
	 * it is null.
	 * 
	 */
	private void populateAggregates() {
		if (aggregates != null) {
			return;
		}
		aggregates = new Aggregate(location);

		for (Observation observation : this.stationObservations.values()) {
			Value minTemp = aggregates.getMinTemp();
			if (observation.getTotalMinDegC() != null && observation.getTotalMinDegC() < minTemp.getValue()) {
				minTemp.setValue(observation.getTotalMinDegC());
				minTemp.setYear(observation.getDate().getYear());
				minTemp.setMonth(observation.getDate().getMonth());
			}

			Value maxTemp = aggregates.getMaxTemp();
			if (observation.getTotalMaxDegC() != null && observation.getTotalMaxDegC() > maxTemp.getValue()) {
				maxTemp.setValue(observation.getTotalMaxDegC());
				maxTemp.setYear(observation.getDate().getYear());
				maxTemp.setMonth(observation.getDate().getMonth());
			}

			Value minRainfall = aggregates.getMinRainfall();
			if (observation.getRainInMillimeters() != null
					&& observation.getRainInMillimeters() < minRainfall.getValue()) {
				minRainfall.setValue(observation.getRainInMillimeters());
				minRainfall.setYear(observation.getDate().getYear());
				minRainfall.setMonth(observation.getDate().getMonth());
			}

			Value maxRainfall = aggregates.getMaxRainfall();
			if (observation.getRainInMillimeters() != null
					&& observation.getRainInMillimeters() > maxRainfall.getValue()) {
				maxRainfall.setValue(observation.getRainInMillimeters());
				maxRainfall.setYear(observation.getDate().getYear());
				maxRainfall.setMonth(observation.getDate().getMonth());
			}

			Value minSunhours = aggregates.getMinSunHours();
			if (observation.getSunshineInHours() != null && observation.getSunshineInHours() < minSunhours.getValue()) {
				minSunhours.setValue(observation.getSunshineInHours());
				minSunhours.setYear(observation.getDate().getYear());
				minSunhours.setMonth(observation.getDate().getMonth());
			}

			Value maxSunhours = aggregates.getMaxSunHours();
			if (observation.getSunshineInHours() != null && observation.getSunshineInHours() > maxSunhours.getValue()) {
				maxSunhours.setValue(observation.getSunshineInHours());
				maxSunhours.setYear(observation.getDate().getYear());
				maxSunhours.setMonth(observation.getDate().getMonth());
			}
		}
	}

	public String getDetailInformation() {
		return "Location: " + location + " | Co-Ordinates:" + coordinates + " | Latitude: " + latitude
				+ " | Longitude: " + longitude + " | Altitude: " + altitude + "|";
	}
}
