package patriots.basic.userinteraction;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Scanner;

public class Presentation {
	/**
	 * This class provides presentation elements for displaying information to a
	 * user.
	 */

	public static final String bar = "o========oo========oo=========oo========oo========oo========oo========oo=========oo========oo=========oo========oo========oo========o";

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
	public static void clearConsoleDisplay() throws AWTException {
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
	public static void pauseAndClearScreen(Scanner userInput) throws AWTException {
		System.out.print("\nType Q to quit: ");
		String choice = userInput.next().toLowerCase();

		while (!choice.equals("q")) {
			System.out.print("Please type the correct option: ");
			choice = userInput.next().toLowerCase();
		}
		Presentation.clearConsoleDisplay();
	}
}
