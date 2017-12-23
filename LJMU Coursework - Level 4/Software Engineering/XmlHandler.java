import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlHandler {
	public static NodeList nList = null;
	public static NodeList listTest = null;
	public static ArrayList<ArrayList<Programme>> outerList = new ArrayList<ArrayList<Programme>>();
	public static ArrayList<Programme> innerList = new ArrayList<Programme>();
	public static ArrayList<Programme> listChannel = new ArrayList<Programme>();
	public static File dir = new File("M://4121COMP - Software engineering workshop//xmlFolder");
	public static String getTitleId = "";
	public static int adder = 0; // index of NodeList elements to 2D ArrayList
	public static DateFormat sdfmt1 = new SimpleDateFormat("HHmm");

	public static ArrayList<ArrayList<Programme>> readXml() throws ParserConfigurationException, SAXException,
			IOException, ParseException, AWTException, XPathExpressionException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		dBuilder = dbFactory.newDocumentBuilder();
		for (File workfile : dir.listFiles()) {
			ArrayList<Programme> innerList = new ArrayList<Programme>();
			Programme emp2 = new Programme();
			if (workfile.isFile()) {
				Document doc = dBuilder.parse(workfile);
				listTest = doc.getElementsByTagName("channel");
				Element element = (Element) doc.adoptNode(listTest.item(0));
				getTitleId = element.getAttribute("id");
				emp2.setid(getTitleId);
				listChannel.add(emp2);
				// channel idSender = new channel();
				// if(EPGMenu.searchOrScroll ==true){
				// idSender.setid(listChannel);}
				Document doc2 = dBuilder.parse(workfile);
				nList = doc2.getElementsByTagName("programme");
				while (adder < nList.getLength()) {
					innerList.add(getProgrammes(nList.item(adder++)));
					if (adder == nList.getLength()) {
						outerList.add(innerList);
						// if(EPGMenu.searchOrScroll ==true){
						// channel channelSender = new channel();
						// channelSender.setProgramme(outerList);
						// EPGMenu.tvListingsBase();
						// }
						adder = 0;
						break;
					}
				}
			}
		}
		return outerList;
	}

	public static Programme getProgrammes(Node node) throws ParseException {
		Programme emp = new Programme();
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			emp.setStart(sdfmt1.parse(getTagValue("start", element)));
			emp.setDesc(getTagValue("desc", element));
			emp.setTitle(getTagValue("title", element));
			emp.setEnd(sdfmt1.parse(getTagValue("end", element)));
		}
		return emp;
	}

	public static String getTagValue(String tag, Element element) {
		NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodeList.item(0);
		return node.getNodeValue();
	}
}
