import java.awt.AWTException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class Schedule {
	public static File scheduleList = new File("M://4121COMP - Software engineering workshop//scheduleListing.txt");
	private static int checker = 0;

	// checks if a custom search matches an entry in the schedule file
	public static void titleInSchedule() throws FileNotFoundException {
		Scanner findMatch = new Scanner(scheduleList);
		String checkString = "Channel: " + Search.channelId + " | " + "Programme: " + Search.title.toUpperCase() + " | "
				+ Search.startTime + " - " + Search.endTime;
		while (findMatch.hasNextLine()) {
			String lineFromFile = findMatch.nextLine();
			if (lineFromFile.equals(checkString)) {
				EPGMenu.endPaddingSpacer = String.format(String.format("%%%ds", 94), "");
				System.out.println("|" + EPGMenu.endPaddingSpacer + "  |");
				EPGMenu.endPaddingSpacer = String.format(String.format("%%%ds", 68), "");
				System.out.print("|This show has been scheduled" + EPGMenu.endPaddingSpacer + "|\n");
				break;
			}
		}
		findMatch.close();
		return;
	}

	public static void checkScheduleLimit() throws XPathExpressionException, ParserConfigurationException,
			ParseException, IOException, SAXException, AWTException {
		Scanner scannerTwo = new Scanner(scheduleList);
		while (scannerTwo.hasNextLine()) {
			if (!scannerTwo.hasNext()) {
				break;
			}
			String nextToken = scannerTwo.next();
			if (checker == 3) { // this affects the schedule limit
				scannerTwo.close();
				try {
					System.out.println(
							"The limit of three recordings has been met. Please remove recordings" + " to free space");
					Thread.sleep(2000);
					System.out.print("Returning to menu.");
					Thread.sleep(1000);
					System.out.print(".");
					Thread.sleep(1000);
					System.out.print(".");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				EPGMenu.wipeConsole();
				EPGMenu.welcomeScreen();
			} else if (nextToken.equalsIgnoreCase("Programme:")) {
				checker++;
			}
		}
	}

	public static void addToSchedule() throws IOException, XPathExpressionException, ParserConfigurationException,
			ParseException, SAXException, AWTException {
		// Check if limit reached before adding to schedule file
		checkScheduleLimit();
		checker = 0;
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(scheduleList, true));
		bWriter.write("Channel: " + Search.channelId + " | " + "Programme: " + Search.title.toUpperCase() + " | "
				+ Search.startTime + " - " + Search.endTime);
		bWriter.newLine();
		bWriter.write(EPGMenu.midBar.replace('|', '-') + System.getProperty("line.separator"));
		if (Search.descToString.length() < 96) {
			bWriter.write(
					Search.descToString + System.getProperty("line.separator") + System.getProperty("line.separator"));
			bWriter.write(EPGMenu.gridBar.replace('|', '=') + System.getProperty("line.separator"));
		} else {
			String descToString1 = Search.descToString.substring(0, 96);
			String descToString2 = Search.descToString.substring(96, Search.descToString.length());
			bWriter.write(descToString1 + System.getProperty("line.separator"));
			bWriter.write(descToString2 + System.getProperty("line.separator"));
			bWriter.write(EPGMenu.gridBar.replace('|', '=') + System.getProperty("line.separator"));
		}
		bWriter.close();
		try {
			System.out.print("The title has been added to schedule");
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}
		EPGMenu.wipeConsole();
		EPGMenu.welcomeScreen();
	}

	public static void readSchedule() throws XPathExpressionException, ParserConfigurationException, ParseException,
			IOException, SAXException, AWTException {
		TimeScheme.time();
		System.out.println(EPGMenu.midBar);
		EPGMenu.endPaddingSpacer = String.format(String.format("%%%ds", 44), "");
		System.out.println(EPGMenu.endPaddingSpacer + "Schedule");
		System.out.println(EPGMenu.midBar);
		System.out.println(EPGMenu.fancyBar);
		Scanner input2 = new Scanner(scheduleList);
		while (input2.hasNextLine()) {
			String line = input2.nextLine();
			System.out.println(line);
		}
		input2.close();
		System.out.print("Type " + "\"" + "Exit" + "\"" + " to return to main menu: ");
		String userExit = EPGMenu.input.nextLine().toLowerCase();
		switch (userExit) {
		case "exit":
			EPGMenu.wipeConsole();
			EPGMenu.welcomeScreen();
		default:
			System.out.print("Input not recongnized, please try again");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			EPGMenu.wipeConsole();
			readSchedule();
		}
	}
}
