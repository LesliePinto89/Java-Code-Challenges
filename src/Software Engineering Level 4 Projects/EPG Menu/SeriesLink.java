import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SeriesLink {

	public static int i = 0;
	public static int innerArrayIndex = 0; // get titles from inner ArrayLists
	public static int col = 0;
	public static ArrayList<Programme> seriesLinkList = new ArrayList<Programme>();
	public static long diff; // calculates time span between title dates
	public static long diffHours;
	public static int foundCount = 0;
	public static int previousSeriesSlot = 0;
	public static int nextSeriesSlot = 1;

	public static void seriesLink() throws FileNotFoundException {
		String aMatch = XmlHandler.outerList.get(i).get(innerArrayIndex).getTitle();
		Search.startTime = Search.sdfmt2.format(XmlHandler.outerList.get(i).get(innerArrayIndex).getStart());
		EPGMenu.endPaddingSpacer = String.format(String.format("%%%ds", 94), "");
		for (int row = i; row < XmlHandler.outerList.size(); row++) {
			while (col < XmlHandler.outerList.get(row).size()) {

				if ((Search.bypass == true) && (XmlHandler.outerList.get(row).get(col).getTitle().equals(aMatch))
						|| (XmlHandler.outerList.get(row).get(col).getTitle().equals(aMatch))
								&& (XmlHandler.outerList.get(row).get(col).getStart().equals(Search.startTime))) {

					if ((foundCount == 0) && (XmlHandler.outerList.get(row).get(col).getDesc().contains("[S]"))) {
						seriesLinkList.add(XmlHandler.outerList.get(row).get(col));
						foundCount++;
						col++;
					} else if ((foundCount >= 1)
							&& (XmlHandler.outerList.get(row).get(col).getDesc().contains("[S]"))) {
						seriesLinkList.add(XmlHandler.outerList.get(row).get(col));
						diff = seriesLinkList.get(nextSeriesSlot).getStart().getTime()
								- seriesLinkList.get(previousSeriesSlot).getStart().getTime();
						diffHours = diff / (60 * 60 * 1000) % 24;
						EPGMenu.endPaddingSpacer = String.format(String.format("%%%ds", 94), "");
						System.out.println("|" + EPGMenu.endPaddingSpacer + "  |");
						EPGMenu.endPaddingSpacer = String.format(String.format("%%%ds", 20), "");
						System.out.println("|This episode is part of a series, the next episode is in " + diffHours
								+ " hours from now" + EPGMenu.endPaddingSpacer + "   |");
						foundCount++;
						nextSeriesSlot++;
						previousSeriesSlot++;
						col++;
						return;
					}
				}
				if ((row == XmlHandler.dir.listFiles().length - 1)
						&& (col == XmlHandler.outerList.get(row).size() - 1)) {
					System.out.println("|" + EPGMenu.endPaddingSpacer + "  |");
					EPGMenu.endPaddingSpacer = String.format(String.format("%%%ds", 59), "");
					System.out.println("|This episode is part of a series. " + EPGMenu.endPaddingSpacer + "   |");
					return;
				}
				col++;
			}
		}
		return;
	}

}
