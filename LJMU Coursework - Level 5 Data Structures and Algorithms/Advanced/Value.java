package patriots.advanced.search;

/**
 * Class holds a single minimum / maximum value for a location e.g. the value
 * for maximum rainfall.
 *
 */
public class Value {

	private String location;
	private double value;
	private int month;
	private int year;

	// Getters and Setters
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
