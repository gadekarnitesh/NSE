package com.nil.download;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class ReaplacementOfChar {
	public static void replace(String file, String search, String replacement) {
		try {
			FileReader fr = new FileReader(file);
			String s;
			String totalStr = "";
			try (BufferedReader br = new BufferedReader(fr)) {
				while ((s = br.readLine()) != null) {
					totalStr += s;
				}
				totalStr = totalStr.replaceAll(search, replacement);
				FileWriter fw = new FileWriter(file);
				fw.write(totalStr);
				fw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
