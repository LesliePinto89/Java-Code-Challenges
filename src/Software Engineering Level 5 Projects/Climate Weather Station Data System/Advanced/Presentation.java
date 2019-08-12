package patriots.advanced.userinteraction;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Scanner;

public class Presentation {

	/**
	 * This class provides presentation aesthetic elements for displaying
	 * information to a user.
	 */

	public final String BAR = "o========oo========oo=========oo========oo========oo========oo========oo=========oo========oo=========oo========oo========oo========o";
	public final String LINE_BAR = "------------------------------------------------------------------------------------------------------------------------------------";

	/**
	 * Clears the current console display - makes illusion of new display
	 * 
	 * @param oscar
	 *            - instance of AWT.robot class that provides auto keyboard
	 *            presses to run the eclipse "clear" console command.
	 * 
	 * @throws AWTException
	 *             - When Abstract Window Toolkit exception has occurred.
	 **/
	public void clearConsoleDisplay() throws AWTException {
		Robot oscar = new Robot();
		oscar.keyPress(KeyEvent.VK_SHIFT);
		oscar.keyPress(KeyEvent.VK_F10);
		oscar.keyRelease(KeyEvent.VK_SHIFT);
		oscar.keyRelease(KeyEvent.VK_F10);
		oscar.keyPress(KeyEvent.VK_R);
		oscar.keyRelease(KeyEvent.VK_R);
		oscar.delay(500); // fixes the delay between method processing
	}

	/**
	 * Prompts user to quit current method production and clears current console
	 * display.
	 * 
	 * @param userInput
	 * @param choice
	 * 
	 * @throws AWTException
	 **/
	public void pauseAndClearScreen(Scanner userInput) throws AWTException {
		System.out.print("\nType Q to quit: ");
		String choice = userInput.next().toLowerCase();

		while (!choice.equals("q")) {
			System.out.print("Please type the correct option: ");
			choice = userInput.next().toLowerCase();
		}
		clearConsoleDisplay();
	}
}
