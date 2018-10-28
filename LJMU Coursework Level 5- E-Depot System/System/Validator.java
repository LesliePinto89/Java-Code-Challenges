package system;

import static java.lang.System.out;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Validator {
	/**
	 * checks dates are not in the past 
	 * @param date
	 * @return date
	 */
	public static boolean isDateInPast(LocalDateTime date) {
		LocalDateTime now = LocalDateTime.now();
		return date.isBefore(now);

	}
	/**
	 * Needed to prevent erroneous dates like 30th Feb as this would be passed as 2nd March.
	 * @param date
	 * @param userDate
	 * @param formatter
	 * @return
	 */
	public static boolean isSensibleDate(LocalDateTime date, String userDate, DateTimeFormatter formatter) {
		String checkValidDate = date.format(formatter);
		return checkValidDate.equals(userDate);

	}
	/**
	 * Ensures that a work schedule's start date is at least 48hrs from the
	 * current date and time. Returning a user to the main menu options if this
	 * not the case.
	 * 
	 * @param startDate
	 * @param formatter 
	 * @return a valid work schedule start date
	 */
	public static boolean isValidStartDate(LocalDateTime startDate, DateTimeFormatter formatter) {
		LocalDateTime timeNow = LocalDateTime.now();
		Duration isValidStartDate = Duration.between(timeNow, startDate);
		Duration otherDuration = Duration.ofHours(48);
		if (isValidStartDate.compareTo(otherDuration) < 0) {
			out.format(
					"Work schedule start times must be at least 48 hours from now, the time now is %s",
					timeNow.format(formatter));
			return false;
		}
		return true;
	}


	/**
	 * Ensures that the duration of a work schedule is no longer than 72 hours, does not equal 0 hours
	 * and start date is before the end date.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return a valid work schedule length
	 */
	public static boolean isWorkScheduleDurationValid(LocalDateTime startDate, LocalDateTime endDate) {
		Duration scheduleLength = Duration.between(startDate, endDate);
		Duration maxScheduleLength = Duration.ofHours(72);
		if (scheduleLength.isNegative() || scheduleLength.isZero()) {
			out.format("Work schedule end date must be after start date");
			return false;
		}
		if (scheduleLength.compareTo(maxScheduleLength) > 0) {
			out.format("Work schedule length must be no greater than 72hrs %s",
					scheduleLength.toString());
			return false;
		}
		return true;
	}
	
	

	/**
	 * Converts user input to an integer.
	 * Validates user input for errors looping until an integer is given
	 * @param message
	 * @param console
	 * @return 0 if invalid, 1 or higher if correct
	 */
	public static int getIntegerFromUser(String message, Scanner console) {
		String input = null;
		int number = 0;
		while (number == 0) {
			try {
				out.println(message);
				input = console.next();
				number = Integer.parseInt(input);
			} catch (NumberFormatException nfe) {
			}
		}
		return number;
	}
	
	/**
	 * Converts user input to a boolean.
	 * Validates user input for errors looping until a boolean is given
	 * @param message
	 * @param console
	 * @return null if an invalid entry is given  
	 */
	public static boolean getBooleanFromUser(String message, Scanner console) {
		out.println(message);
		Boolean output = null;
		while(output == null) {
			String input = console.next();
			if (input.equalsIgnoreCase("T") || input.equalsIgnoreCase("F") ) {
				output = Boolean.parseBoolean(input);	
			} else {
				out.println("Please enter T or F");
			}
		}
		return output;
	}
}
