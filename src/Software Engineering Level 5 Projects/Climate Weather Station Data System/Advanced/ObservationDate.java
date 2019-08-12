package patriots.advanced.model;

public class ObservationDate implements Comparable<ObservationDate> {

	/**
	 * This class provides observation date details to complete feature set 3
	 * and 7.
	 **/

	// Integer types prevented making a Wrapper key object. So change them
	// to primitive.
	private int year;
	private int month;

	public ObservationDate() {
	}

	public ObservationDate(int year, int month) {
		this.year = year;
		this.month = month;
	}

	// It is important to override the original equals() and hashcode() methods
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ObservationDate))
			return false;
		ObservationDate key = (ObservationDate) o;
		return year == key.year && month == key.month;
	}

	@Override
	public int hashCode() {
		int result = year;
		result = 31 * result + month;
		return result;
	}

	// Getters and Setters - Made year and month Integers to be used with
	// collections.sort in ListOrderByConditions comparator
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	@Override
	public int compareTo(ObservationDate o) {
		if (this.year == o.year) {
			return this.month - o.month;
		} else
			return this.year - o.year;
	}
}
