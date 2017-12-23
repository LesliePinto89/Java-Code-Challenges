import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.YEAR;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeScheme {

	public static int slotsIndex = 0;
	public static int slotsBegin = 0; // increase displayed time durations
	public static int slotsEnd = 2; // time slots end times
	public static int slotsChanger = 8;

	// Display local time/date/year etc
	public static void time() {
		GregorianCalendar today = new GregorianCalendar();
		String[] weekdays = new DateFormatSymbols().getWeekdays();
		System.out.println("Three Muskateers EPG                                                       "
				+ weekdays[today.get(DAY_OF_WEEK)] + " " + today.get(DATE) + " " + today.get(YEAR) + " "
				+ today.get(Calendar.HOUR_OF_DAY) + ":" + today.get(Calendar.MINUTE) + "  ");
		System.out.println(EPGMenu.fancyBar);
	}

	// Time slots duration slotsChangers
	public static void duration() {
		Calendar cal = Calendar.getInstance();
		DateFormat slots = new SimpleDateFormat("HH:mm");
		System.out.print("|Channel      | ");
		if (!(slotsChanger == 8)) {
			cal.set(Calendar.HOUR_OF_DAY, slotsChanger);
			cal.set(Calendar.MINUTE, 00);
		} else {
			slotsChanger = 8;
			cal.set(Calendar.HOUR_OF_DAY, 6);
			cal.set(Calendar.MINUTE, 00);
		}
		// This loop controls the time slot structure.
		for (slotsIndex = slotsBegin; slotsIndex < slotsEnd;) {
			System.out.print("            ");
			System.out.print(slots.format(cal.getTime()));
			slotsIndex++;
			System.out.print(" - ");
			cal.add(Calendar.MINUTE, 120);
			System.out.print(slots.format(cal.getTime()));
			System.out.print("             |");
		}
		System.out.println("\n" + EPGMenu.midBar);
	}
}
