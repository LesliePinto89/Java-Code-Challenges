import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Search {
	private static int i = 0;
	public static DateFormat sdfmt2 = new SimpleDateFormat("HH:mm");
	public static boolean bypass = false; // affects custom, all, empty search
	private static int counter = 0; // Track titles in search
	private static int matches = 0; // Display total number of tracked titles
	public static String startTime = ""; // user input start time in String
	private static String programmeStart = "";
	public static String endTime = "";
	public static String channelId;
	public static String descToString = "";
	public static String title = "";
	public static int innerArrayIndex = 0; // get titles from inner ArrayLists
	private static int spacer = 4; // create padding using for loops
	private static int channelIndex = 0; // Gets channel from inner ArrayLists
	private static boolean notInArray = true;
	public static String scheduleOrSeries = ("| Exit = return  | A = Add to schedule  | R = Remove from schedule  |");
	public static String searchListAllOnly = ("| Exit = return  ====================================================");

	/**
	 * This is the first stage of the search function. The functionality it
	 * handles is: Custom search, All title matches search, Add custom search to
	 * schedule, Search not found, and Return to Welcome Screen.
	 * 
	 * @throws AWTException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public static void searchWelcome(NodeList nList, ArrayList<Programme> innerList) throws AWTException,
			XPathExpressionException, ParserConfigurationException, ParseException, IOException, SAXException {
		TimeScheme.time();
		System.out.println(EPGMenu.midBar);
		System.out.println(EPGMenu.endPaddingSpacer = String.format(String.format("%%%ds", 44), "") + "Search");
		System.out.println(EPGMenu.midBar + "\n" + EPGMenu.fancyBar);

		System.out.println("What is the name of the programme?                                          Type " + "\""
				+ "Exit" + "\"" + " to return");
		title = EPGMenu.input.nextLine().toLowerCase();
		if (title.equals("exit")) {
			EPGMenu.wipeConsole();
			EPGMenu.welcomeScreen();
		}
		i = 0;
		innerArrayIndex = 0;
		channelIndex = 0;
		System.out.println("Do you want to search for all matches - yes / no");

		// Takes user to choice method.
		searchChoice();

		String moreChoices = EPGMenu.input.nextLine().toLowerCase();
		while (moreChoices != null) {
			switch (moreChoices) {
			case "a":
				Schedule.addToSchedule();
			case "exit":
				EPGMenu.wipeConsole();
				EPGMenu.welcomeScreen();
			default:
				System.out.println("Input choice is not recognized, please choice from the menu.");
				moreChoices = EPGMenu.input.nextLine().toLowerCase();
			}
		}
	}

	/**
	 * This is the second stage of the search function. Here the user can type
	 * "yes" and do a search for all titles in the programming source
	 * (XmlHandler.class , 2-D ArrayList), and return all matches. If the user
	 * types "no", the menu will look for a match to the keywords the user will
	 * type, and will return a single match.
	 * 
	 * @throws AWTException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public static void searchChoice() throws XPathExpressionException, AWTException, IOException,
			ParserConfigurationException, ParseException, SAXException {
		sdfmt2 = new SimpleDateFormat("HH:mm"); // outputs date in format
		String userInput = EPGMenu.input.nextLine();

		switch (userInput) {
		case "yes":
			bypass = true;
			searchBase();
			System.out.print(searchListAllOnly + " " + matches + " match / matches found    | \n");
			counter = 0;
			matches = 0;
			System.out.println(EPGMenu.gridBar + "\n" + "Type " + "\"" + "Exit" + "\"" + " to return");
			userInput = EPGMenu.input.nextLine().toLowerCase();
			while (!userInput.equals("exit")) {
				System.out.println("Please try again");
				userInput = EPGMenu.input.nextLine().toLowerCase();
			}
			EPGMenu.wipeConsole();
			EPGMenu.welcomeScreen();

		case "no":
			System.out.print("What time does it start? \n");
			bypass = false;
			startTime = EPGMenu.input.nextLine().toLowerCase();
			searchBase();
			return;

		default:
			System.out.print("Input not recongized, please try again");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			EPGMenu.wipeConsole();
			EPGMenu.welcomeScreen();
		}
	}

	/**
	 * This is the third of the search function. Both types of search functions
	 * use this code as a base to perform their design. Certain variables have
	 * been added so that both functions only use the code they need to work as
	 * desired.
	 * 
	 * @throws ParseException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	public static void searchBase() throws XPathExpressionException, AWTException, IOException,
			ParserConfigurationException, SAXException, ParseException {

		while (i < XmlHandler.dir.listFiles().length) {
			foundOneOrAll(); // Checks if custom or all search is a match
			if ((bypass == false) // Limits custom search to one result
					&& (title.equals(XmlHandler.outerList.get(i).get(innerArrayIndex).getTitle().toLowerCase())
							&& (startTime.equals(programmeStart = sdfmt2
									.format(XmlHandler.outerList.get(i).get(innerArrayIndex).getStart()))))) {
				System.out.print(scheduleOrSeries + " " + matches + " match / matches found    |\n");
				counter = 0;
				matches = 0;
				System.out.println(EPGMenu.gridBar + "\n" + "What do you want to do now?");
				return;
			}
			notFoundInSearch(); // If no match found in either search function.
			if ((i == XmlHandler.dir.listFiles().length - 1)
					&& (innerArrayIndex == XmlHandler.outerList.get(i).size() - 1)) {
				return;
			}
			innerArrayIndex++;
			// When at channel end, go up one channel and repeat until no more.
			if (innerArrayIndex == XmlHandler.outerList.get(i).size()) {
				i++;
				channelIndex++;
				innerArrayIndex = 0;
			}
		}
	}

	/**
	 * Allows the user to turn the menu on. Makes of the wipeConsole() method to
	 * make the illusion of the menu being turned off.
	 * 
	 * @param i
	 *            Variable used to go through channels in the directory.
	 * @param outerList
	 *            The channel section of the 2-D ArrayList titles source
	 * @param innerList
	 *            The programme section of the 2-D ArrayList titles source
	 * 
	 */
	public static void notFoundInSearch() throws XPathExpressionException, ParserConfigurationException, ParseException,
			IOException, SAXException, AWTException {
		if ((((i == XmlHandler.dir.listFiles().length - 1)
				&& (innerArrayIndex == XmlHandler.outerList.get(i).size() - 1) && (notInArray == true)
				&& ((!title.equals(XmlHandler.outerList.get(i).get(innerArrayIndex).getTitle().toLowerCase())
						&& (!startTime.equals(programmeStart = sdfmt2
								.format(XmlHandler.outerList.get(i).get(innerArrayIndex).getStart())))))))) {
			try {
				System.out.print("Entry not found ");
				Thread.sleep(2000);
				EPGMenu.wipeConsole();

			} catch (InterruptedException ex) {
			}
			EPGMenu.welcomeScreen();
		}
		return;
	}

	/**
	 * This checks if the keyword/s from either search are in the programmes'
	 * source.
	 * 
	 * @throws AWTException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public static void foundOneOrAll() throws AWTException, IOException, XPathExpressionException,
			ParserConfigurationException, SAXException, ParseException {
		if ((bypass == true)
				&& (title.equals(XmlHandler.outerList.get(i).get(innerArrayIndex).getTitle().toLowerCase()))
				|| ((title.equals(XmlHandler.outerList.get(i).get(innerArrayIndex).getTitle().toLowerCase())
						&& (startTime.equals(programmeStart = sdfmt2
								.format(XmlHandler.outerList.get(i).get(innerArrayIndex).getStart())))))) {
			if (counter == 0) {
				EPGMenu.wipeConsole();
				TimeScheme.time();
			}
			counter++;
			matches = counter;
			notInArray = false;
			System.out.print("|" + XmlHandler.outerList.get(i).get(innerArrayIndex).getTitle() + ":");
			startTime = sdfmt2.format(XmlHandler.outerList.get(i).get(innerArrayIndex).getStart());
			System.out.print(" " + startTime + " -");
			endTime = sdfmt2.format(XmlHandler.outerList.get(i).get(innerArrayIndex).getEnd());
			System.out.print(" " + endTime);
			EPGMenu.endPaddingSpacer = String.format(String.format("%%%ds", spacer), "");
			for (spacer = title.length(); spacer < 82; spacer++) {
				System.out.print(" ");
				if (spacer == 55) {
					System.out.print("Channel: ");
					channelId = XmlHandler.listChannel.get(channelIndex).getid().toUpperCase();
					System.out.print(channelId);
					for (spacer = channelId.length(); spacer <= 15; spacer++) {
						System.out.print(" ");
						if (spacer == 15) {
							System.out.println("|");
							System.out.println(EPGMenu.midBar);
							getDescription(); // gets description with padding
							return;
						}
					}
				}
			}
		}
		return;
	}

	/**
	 * This displays the tiles from both search types to a fized structure.
	 * 
	 * @throws FileNotFoundException
	 */
	public static void getDescription() throws FileNotFoundException {
		descToString = XmlHandler.outerList.get(i).get(innerArrayIndex).getDesc();
		spacer = 0;
		int fixedEndPoint = 95;
		int beginPoint = 0;
		while (spacer <= descToString.length()) {
			if (spacer == fixedEndPoint) {
				if (spacer < descToString.length()) {
					System.out.println("|" + descToString.substring(beginPoint, fixedEndPoint) + " |");
					beginPoint = fixedEndPoint;
					fixedEndPoint = beginPoint + 95;
					spacer++;
					continue;
				}
			} else if (spacer == descToString.length()) {
				System.out.print("|" + descToString.substring(beginPoint, descToString.length()));
				for (spacer = descToString.length(); spacer <= fixedEndPoint; spacer++) {
					System.out.print(" ");
					if (spacer == fixedEndPoint) {
						System.out.println("|");
						SeriesLink.seriesLink(); // Checks if part of a series.
						Schedule.titleInSchedule(); // Checks if in Schedule.
						System.out.println(EPGMenu.fancyBar);
						return;
					}
				}
			}
			spacer++;
		}
	}
}
