package com.nil.nse;

import java.util.Comparator;

public class ExhOpenIntrestComparator implements Comparator<NSEOpenIntersert> {

	@Override
	public int compare(NSEOpenIntersert s1, NSEOpenIntersert s2) {
		if (s1.getExhOpenIntrest() == s2.getExhOpenIntrest())
			return 0;
		else if (s1.getExhOpenIntrest() < s2.getExhOpenIntrest())
			return 1;
		else
			return -1;

	}

}
