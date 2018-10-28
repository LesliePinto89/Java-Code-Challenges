package patriots.advanced.search;

public class Aggregate {

	/**
	 * Aggregation of values for observation readings
	 */

	// Variables for below methods
	private Value minTemp;
	private Value maxTemp;
	private Value minRainFall;
	private Value maxRainFall;
	private Value minSunHours;
	private Value maxSunHours;

	/**
	 * Constructor where location is not yet known.
	 */
	public Aggregate() {
		this(null);
	}

	/**
	 * Constructor for a location sets default values for object infinity;
	 * 
	 * @param location
	 */
	public Aggregate(String location) {
		Value minTemp = new Value();
		minTemp.setLocation(location);
		minTemp.setValue(Double.POSITIVE_INFINITY);
		this.minTemp = minTemp;
		Value maxTemp = new Value();
		maxTemp.setLocation(location);
		maxTemp.setValue(Double.NEGATIVE_INFINITY);
		this.maxTemp = maxTemp;
		Value minRainFall = new Value();
		minRainFall.setLocation(location);
		minRainFall.setValue(Double.POSITIVE_INFINITY);
		this.minRainFall = minRainFall;
		Value maxRainFall = new Value();
		maxRainFall.setLocation(location);
		maxRainFall.setValue(Double.NEGATIVE_INFINITY);
		this.maxRainFall = maxRainFall;
		Value minSunHours = new Value();
		minSunHours.setLocation(location);
		minSunHours.setValue(Double.POSITIVE_INFINITY);
		this.minSunHours = minSunHours;
		Value maxSunHours = new Value();
		maxSunHours.setLocation(location);
		maxSunHours.setValue(Double.NEGATIVE_INFINITY);
		this.maxSunHours = maxSunHours;
	}

	/**
	 * Getters and setters
	 * 
	 */

	public Value getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(Value minTemp) {
		this.minTemp = minTemp;
	}

	public Value getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(Value maxTemp) {
		this.maxTemp = maxTemp;
	}

	public Value getMinRainfall() {
		return minRainFall;
	}

	public void setMinRainfall(Value minRainfall) {
		this.minRainFall = minRainfall;
	}

	public Value getMaxRainfall() {
		return maxRainFall;
	}

	public void setMaxRainfall(Value maxRainfall) {
		this.maxRainFall = maxRainfall;
	}

	public Value getMinSunHours() {
		return minSunHours;
	}

	public void setMinSunHours(Value minSunhours) {
		this.minSunHours = minSunhours;
	}

	public Value getMaxSunHours() {
		return maxSunHours;
	}

	public void setMaxSunHours(Value maxSunhours) {
		this.maxSunHours = maxSunhours;
	}
}
