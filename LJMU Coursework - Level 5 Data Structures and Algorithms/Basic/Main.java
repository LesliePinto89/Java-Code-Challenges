package patriots.basic;

import java.awt.AWTException;
import java.io.FileNotFoundException;

import patriots.basic.datareader.ClimateDataReader;
import patriots.basic.menu.ClimateMenu;

public class Main {

	/**
	 * This class loads all climate data into memory and then goes to the
	 * climate menu class.
	 * 
	 * @param dataReader
	 * @param menu
	 * @throws FileNotFoundException
	 *             Attempt to open the file by specified pathname has failed.
	 * @throws AWTException
	 *             When Abstract Window Toolkit exception has occurred.
	 **/
	public static void main(String[] args) throws FileNotFoundException, AWTException {
		ClimateDataReader dataReader = new ClimateDataReader();
		dataReader.readWeatherStationFiles();
		ClimateMenu menu = new ClimateMenu(dataReader);
		menu.welcomeMenu();
	}
}
