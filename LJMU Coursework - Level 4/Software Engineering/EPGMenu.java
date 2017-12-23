import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.text.ParseException;
import org.xml.sax.SAXException;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Scanner;

public class EPGMenu {

	/**
	 * Introduction: This is an EPG menu java application that had to be developed to complete 
   * the LJMU module - 4121COMP: Software Engineering Workshop. During the development cycle, the group
	 * members were myself and two others, but they had written no source code at all.
	 */

	/**
	 * Variables used in this class and accessed by others - Avoids redundancy.
	 */
	public static String fancyBar = "o======oo======oo=======oo=======oo========oo========oo=========oo=======oo=======oo=====oo======o";
	public static String gridBar = "|================================================================================================|";
	public static String legend = ("|i: [F = 2hrs | B = 2hrs | U = 4 channels | D = 4 channels  | Exit = back | Schedule | Search]:  |");
	public static String midBar = "|------------------------------------------------------------------------------------------------|";
	public static String endPaddingSpacer = "";
	public static boolean searchOrScroll = false;
	public static final Scanner input = new Scanner(System.in);
	public static int outerChannelCounter = 0;
	public static int innerChannelCounter = 0;
	public static int channelIdCounter = 0;
	public static int spacePadder = 0;
	public static int startViewPoint = 6;
	public static int endViewPoint = 9;

	private static int maxLimit = 4; // menu counter of 4 titles per up/down
	private static String channelId = "";
	private static int channelIndex = 0;
	private static int r = 0;
	private static int indexStart = 0;
	private static int indexEnd = 3;
	private static int turner = 0;

	// private static String userDirection = "";
	/**
	 * This method makes use of the Robot.awt class to clear the console
	 * display. It use keyboard codes to access Eclipe's console reset command
	 * to make the illusion of a new page.
	 * 
	 * @throws AWTException
	 */
	public static void wipeConsole() throws AWTException {
		Robot oscar = new Robot();
		oscar.keyPress(KeyEvent.VK_SHIFT);
		oscar.keyPress(KeyEvent.VK_F10);
		oscar.keyRelease(KeyEvent.VK_SHIFT);
		oscar.keyRelease(KeyEvent.VK_F10);
		oscar.keyPress(KeyEvent.VK_R);
		oscar.keyRelease(KeyEvent.VK_R);
		oscar.delay(500); // fixes the delay between method completion
	}

	/**
	 * Display programmes based on local time.Programmes will be in a grid and
	 * displayed up to 3 hour interval.
	 * 
	 * @throws AWTException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public static void tvListingsBase() throws XPathExpressionException, ParserConfigurationException, ParseException,
			IOException, SAXException, AWTException {
		TimeScheme.time();
		TimeScheme.duration();
		while (Search.innerArrayIndex < maxLimit) {
			channelId = XmlHandler.listChannel.get(channelIndex).getid().toUpperCase();
			if (channelIndex == maxLimit) {
				break;
			}
			System.out.print("|" + channelId);
			channelIndex++;
			r = 0;
			for (r = channelId.length(); r < 20; r++) {
				System.out.print(" ");
				if (r == 12) {
					System.out.print("|");
					if (channelIndex == 4) {
						System.out.print(XmlHandler.outerList.get(Search.innerArrayIndex++)
								.subList(indexStart, indexEnd).toString().replace("[", "").replace("]", ""));
						break;
					}
					System.out.println(XmlHandler.outerList.get(Search.innerArrayIndex++).subList(indexStart, indexEnd)
							.toString().replace("[", "").replace("]", ""));
					break;
				}
			}
		}
		System.out.println("\n" + gridBar);
		System.out.println(legend);
		System.out.print(gridBar + "\n");
		System.out.print(" Command:");
		navigationScreen();
	}

	/**
	 * This is where the user can decided what direction they want to scroll in
	 * the menu.
	 * 
	 * @throws AWTException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public static void navigationScreen() throws XPathExpressionException, ParserConfigurationException, ParseException,
			IOException, SAXException, AWTException {
		Scanner direction = new Scanner(System.in);
		String userDirection = direction.nextLine().toLowerCase();
		switch (userDirection) {

		case "b":
			if (indexStart == 0) {
				leftToRight();
				break;
			}
			TimeScheme.slotsChanger = TimeScheme.slotsChanger - 2;
			TimeScheme.slotsEnd = TimeScheme.slotsBegin;
			TimeScheme.slotsBegin = TimeScheme.slotsEnd - 2;
			indexEnd = indexStart;
			indexStart = indexEnd - 3;
			leftToRight();

		case "f":
			TimeScheme.slotsChanger = TimeScheme.slotsChanger + 2;
			TimeScheme.slotsBegin = TimeScheme.slotsEnd;
			TimeScheme.slotsEnd = TimeScheme.slotsBegin + 2;
			indexStart = indexEnd;
			indexEnd = indexStart + 3;
			leftToRight();

		case "u":
			if ((indexStart < indexEnd) && (maxLimit == 4)) {
				maxLimit = XmlHandler.dir.listFiles().length;
				Search.innerArrayIndex = XmlHandler.dir.listFiles().length - 4;
				channelIndex = XmlHandler.dir.listFiles().length - 4;
				upToDown();
			}

			turner--;
			Search.innerArrayIndex = Search.innerArrayIndex - 8;
			channelIndex = channelIndex - 8;
			maxLimit = maxLimit - 4;
			upToDown();

		case "d":
			if (maxLimit == XmlHandler.dir.listFiles().length) {
				maxLimit = 4;
				Search.innerArrayIndex = 0;
				channelIndex = 0;
				upToDown();
			}
			turner++;
			maxLimit = maxLimit + 4;
			upToDown();

		case "exit":
			wipeConsole();
			Search.innerArrayIndex = 0;
			channelIndex = 0;
			welcomeScreen();

		default:
			try {
				System.out.print("Command not recognized, please try again");
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			wipeConsole();
			Search.innerArrayIndex = 0;
			channelIndex = 0;
			tvListingsBase();
		}
		direction.close();
	}

	/**
	 * This is where the user can go left or right. Both function use the code
	 * below as a base, but use different variables to affect the outcome.
	 * 
	 * @throws AWTException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public static void leftToRight() throws AWTException, XPathExpressionException, ParserConfigurationException,
			ParseException, IOException, SAXException {
		wipeConsole();
		TimeScheme.time();
		TimeScheme.duration();
		// When scroll up past start page, take to last page
		if ((turner >= 1) && (indexStart < indexEnd)) {
			Search.innerArrayIndex = Search.innerArrayIndex - 4;
			channelIndex = channelIndex - 4;
		}
		// When scroll down past end page, take to firstPart page
		else if ((indexStart < indexEnd) && (turner < 1)) {
			Search.innerArrayIndex = Search.innerArrayIndex - 4;
			channelIndex = channelIndex - 4;
		}
		while (Search.innerArrayIndex < maxLimit) {
			channelId = XmlHandler.listChannel.get(channelIndex).getid().toUpperCase();
			channelIndex++;
			if (channelIndex == Search.innerArrayIndex) {
				break;
			}
			System.out.print("|" + channelId);
			r = 0;
			for (r = channelId.length(); r < 20; r++) {
				System.out.print(" ");
				if (r == 12) {
					System.out.print("|");
					if (channelIndex == maxLimit) {
						System.out.print(XmlHandler.outerList.get(Search.innerArrayIndex++)
								.subList(indexStart, indexEnd).toString().replace("[", "").replace("]", ""));
						break;
					}
					System.out.println(XmlHandler.outerList.get(Search.innerArrayIndex++).subList(indexStart, indexEnd)
							.toString().replace("[", "").replace("]", ""));
					break;
				}
			}
		}
		System.out.println("\n" + gridBar);
		System.out.println(legend);
		System.out.print(gridBar + "\n");
		System.out.print("Command:");
		navigationScreen();
	}

	/**
	 * This is where the user can go up or down. Both function use the code
	 * below as a base, but use different variables to affect the outcome.
	 * 
	 * @throws AWTException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public static void upToDown() throws AWTException, XPathExpressionException, ParserConfigurationException,
			ParseException, IOException, SAXException {
		wipeConsole();
		TimeScheme.time();
		TimeScheme.duration();
		while (maxLimit < maxLimit + 4) {
			if (channelIndex == maxLimit) {
				break;
			}
			channelId = XmlHandler.listChannel.get(channelIndex).getid().toUpperCase();
			System.out.print("|" + channelId);
			channelIndex++;
			r = 0;
			for (r = channelId.length(); r < 20; r++) {
				System.out.print(" ");
				if (r == 12) {
					System.out.print("|");
					if (channelIndex == maxLimit) {
						System.out.print(XmlHandler.outerList.get(Search.innerArrayIndex++)
								.subList(indexStart, indexEnd).toString().replace("[", "").replace("]", ""));
						break;
					}
					System.out.println(XmlHandler.outerList.get(Search.innerArrayIndex++).subList(indexStart, indexEnd)
							.toString().replace("[", "").replace("]", ""));
					break;
				}
			}
		}
		System.out.println("\n" + gridBar);
		System.out.println(legend);
		System.out.print(gridBar + "\n");
		System.out.print("Command:");
		navigationScreen();
	}

	/**
	 * The first page that the user will see with the available options to use.
	 * 
	 * @throws AWTException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public static void welcomeScreen() throws XPathExpressionException, ParserConfigurationException, ParseException,
			IOException, SAXException, AWTException {
		TimeScheme.time();
		String homeSpacer = String.format(String.format("%%%ds", 31), "");
		System.out.print(
				"|" + homeSpacer + "============== Home ==============" + homeSpacer + "|\n" + EPGMenu.gridBar + "\n");
		homeSpacer = String.format(String.format("%%%ds", 33), "");
		String baseSpacer = String.format(String.format("%%%ds", 33), "");
		System.out.print(
				"|" + baseSpacer + " > View Channels - (View)" + homeSpacer + "     |\n" + EPGMenu.midBar + "\n");
		homeSpacer = String.format(String.format("%%%ds", 27), "");
		System.out.print(
				"|" + baseSpacer + " > Search Programmes - (Search)" + homeSpacer + "     |\n" + EPGMenu.midBar + "\n");
		homeSpacer = String.format(String.format("%%%ds", 29), "");
		System.out.print(
				"|" + baseSpacer + " > View Schedule - (Schedule)" + homeSpacer + "     |\n" + EPGMenu.midBar + "\n");
		homeSpacer = String.format(String.format("%%%ds", 32), "");
		System.out.print(
				"|" + baseSpacer + " > Exit Menu - (Power Off)" + homeSpacer + "     |\n" + EPGMenu.gridBar + "\n");
		System.out.print(" Command: ");
		pickFromMenu();
	}

	/**
	 * This menu provides the user with the applcation's key functions.The first
	 * option displays programme listings, starting from local time to 3 hours
	 * later. The second option gives the user the option of a custom search and
	 * an all matches search.The third option retrieves the current programmes
	 * in the schedules list file and displays it to the user. The fourth option
	 * allows the user to turn the EPG menu off.
	 * 
	 * @throws AWTException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public static void pickFromMenu() throws XPathExpressionException, ParserConfigurationException, ParseException,
			IOException, SAXException, AWTException {
		Scanner readUserInput = new Scanner(System.in);
		String choice = readUserInput.nextLine().toLowerCase();
		switch (choice) {
		case "view":
			wipeConsole();
			EPGMenu.searchOrScroll = true;
			tvListingsBase();
			break;

		case "search":
			wipeConsole();
			EPGMenu.searchOrScroll = false;
			Search.searchWelcome(XmlHandler.nList, XmlHandler.innerList);
			break;

		case "schedule":
			wipeConsole();
			Schedule.readSchedule();
			break;

		case "power off":
			try {
				System.out.print(" Please wait  - Type " + "\"" + "Power On" + "\"" + " to turn menu on.");
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			powerOnAndOff();

		default:
			System.out.print(" Input not recognized, please try again.");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			wipeConsole();
			welcomeScreen();
		}
		readUserInput.close();
	}

	/**
	 * Allows the user to turn the menu on. Makes of the wipeConsole() method to
	 * make the illusion of the menu being turned off.
	 * 
	 * @throws AWTException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public static void powerOnAndOff() throws AWTException, XPathExpressionException, ParserConfigurationException,
			ParseException, IOException, SAXException {
		wipeConsole();
		String turnOn = input.nextLine().toLowerCase();
		switch (turnOn) {
		case "power on":
			try {
				System.out.print("Powering on, please wait");
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			wipeConsole();
			welcomeScreen();
		default:
			powerOnAndOff();
		}
	}

	public static void main(String[] args) throws IOException, SAXException, AWTException, ParserConfigurationException,
			ParseException, XPathExpressionException {
		XmlHandler.readXml();
		welcomeScreen();
	}
}
