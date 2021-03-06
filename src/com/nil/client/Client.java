package com.nil.client;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nil.download.HttpDownloadUtility;
import com.nil.download.ReaplacementOfChar;
import com.nil.download.UnzipUtility;
import com.nil.nse.ExhOpenIntrestComparator;
import com.nil.nse.NSEOpenIntersert;

public class Client {

	public static void main(String[] args) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
		LocalDateTime now = LocalDateTime.now();
		// System.out.println(dtf.format(now));

		// for downloading
		// "https://www.nseindia.com/archives/nsccl/mwpl/nseoi_16012019.zip";
		String fileURL = "https://www.nseindia.com/archives/nsccl/mwpl/nseoi_" + dtf.format(now) + ".zip";
		String saveDir = "F:/NES/download";

		// for unzip
		String zipFilePath = "F:/NES/download/" + "nseoi_" + dtf.format(now) + ".zip";
		String destDirectory = "F:/NES" + "/" + dtf.format(now);

		// for replacement
		// F:\NES\16012019\nseoi_16012019.xml
		String file = "F:/NES" + "/" + dtf.format(now) + "/nseoi_" + dtf.format(now) + ".xml";
		String search = "&";
		String replacement = "-";
		ReaplacementOfChar.replace(file, search, replacement);

		try {
			HttpDownloadUtility.downloadFile(fileURL, saveDir);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		UnzipUtility unzipper = new UnzipUtility();
		try {
			Thread.sleep(100);
			unzipper.unzip(zipFilePath, destDirectory);
		} catch (Exception ex) {
			// some errors occurred ex.printStackTrace(); }

			String filePath = "F:/NES/";// <<11012019>>/nseoi_<<11012019>>.xml";
			String folderNameList[] = getAllNSEFolders(filePath);

			Map<String, List<NSEOpenIntersert>> dateWiseOpenInterMap = new HashMap<String, List<NSEOpenIntersert>>();
			for (String folderName : folderNameList) {
				String nesOpenInterFile = filePath + File.separator + folderName + File.separator + "nseoi_"
						+ folderName + ".xml";
				if (new File(nesOpenInterFile).exists()) { // if file exist
															// check
					dateWiseOpenInterMap.put(folderName, getNSEOpenIntersert(nesOpenInterFile));
				} else {
					continue;
				}
			}
			System.out.println(dateWiseOpenInterMap.size());

			int index = 0;
			List<NSEOpenIntersert> ll1 = new ArrayList<NSEOpenIntersert>();
			List<NSEOpenIntersert> ll2 = new ArrayList<NSEOpenIntersert>();
			List<NSEOpenIntersert> commonList = new ArrayList<NSEOpenIntersert>();
			for (Map.Entry<String, List<NSEOpenIntersert>> entry : dateWiseOpenInterMap.entrySet()) {
				if (index == 0) {
					ll1 = entry.getValue();
				} else if (index == 2) {
					ll2 = entry.getValue();
					commonList = getUniqueElement(ll1, ll2);
				} else {
					commonList = getUniqueElement(commonList, entry.getValue());
				}
				index++;
			}

			Map<String, List<NSEOpenIntersert>> finalDataMap = new HashMap<String, List<NSEOpenIntersert>>();
			for (Map.Entry<String, List<NSEOpenIntersert>> entry : dateWiseOpenInterMap.entrySet()) {
				List<NSEOpenIntersert> temp = entry.getValue();
				List<NSEOpenIntersert> finalList = getUniqueElement(temp, commonList);
				// System.out.println("entry::"+entry.getKey());
				for (NSEOpenIntersert l : finalList) {
					// System.out.println(l.getScripName()+"--"+
					// l.getExhOpenIntrest());
					if (finalDataMap.get(l.getScripName()) != null) {
						List<NSEOpenIntersert> listTemp = finalDataMap.get(l.getScripName());
						listTemp.add(l);
						finalDataMap.put(l.getScripName(), listTemp);
					} else {
						List<NSEOpenIntersert> tempList = new ArrayList<NSEOpenIntersert>();
						tempList.add(l);
						finalDataMap.put(l.getScripName(), tempList);
					}
				}
			}
			for (Map.Entry<String, List<NSEOpenIntersert>> entry : finalDataMap.entrySet()) {
				StringBuilder str = new StringBuilder();
				for (NSEOpenIntersert l : entry.getValue()) {
					str.append(l.getDate()).append("$").append(l.getExhOpenIntrest()).append("--");
				}
				System.out.println(entry.getKey() + "::" + str + "");
			}

		}
	}

	public static List<NSEOpenIntersert> getUniqueElement(List<NSEOpenIntersert> list1, List<NSEOpenIntersert> list2) {
		List<NSEOpenIntersert> newList = new ArrayList<NSEOpenIntersert>();
		for (NSEOpenIntersert l1 : list1) {
			for (NSEOpenIntersert l2 : list2) {
				if (l1.equals(l2)) {
					newList.add(l1);
				}
			}
		}
		return newList;
	}

	public static String[] getAllNSEFolders(String filePath) {
		File file = new File(filePath);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		return directories;
	}

	public static List<NSEOpenIntersert> getNSEOpenIntersert(String path) {
		File xmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		List<NSEOpenIntersert> nseList = null;
		List<NSEOpenIntersert> nseList1 = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("MWPL");

			nseList = new ArrayList<NSEOpenIntersert>();
			nseList1 = new ArrayList<NSEOpenIntersert>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				nseList.add(getNSEOpenIntersert(nodeList.item(i)));
			}
			Collections.sort(nseList, new ExhOpenIntrestComparator());

			int c = 0;
			for (NSEOpenIntersert nse : nseList) {
				nseList1.add(nse);
				c++;
				if (c == 10) {
					break;
				}
				// System.out.println(nse.toString());
			}
		} catch (SAXException | ParserConfigurationException | IOException e1) {
			e1.printStackTrace();
		}
		return nseList1;
	}

	private static NSEOpenIntersert getNSEOpenIntersert(Node node) {
		// XMLReaderDOM domReader = new XMLReaderDOM();
		NSEOpenIntersert nse = new NSEOpenIntersert();
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			nse.setDate(getTagValue("Date", element));
			nse.setIsin(getTagValue("ISIN", element));
			nse.setScripName(getTagValue("Scrip_Name", element));
			nse.setNseSymbol(getTagValue("NSE_Symbol", element));
			nse.setMwlLimit(Double.parseDouble(getTagValue("MWPL_Limit", element)));
			nse.setOpenInterest(Double.parseDouble(getTagValue("Open_Interest", element)));
			nse.setExhOpenIntrest(nse.getOpenInterest() / nse.getMwlLimit() * 100);
		}
		return nse;
	}

	private static String getTagValue(String tag, Element element) {
		Node node;
		try {
			NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
			node = (Node) nodeList.item(0);
			if (node != null) {
				return node.getNodeValue();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}