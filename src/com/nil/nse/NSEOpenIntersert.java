package com.nil.nse;

public class NSEOpenIntersert {
	private String date;
	private String isin;
	private String scripName;
	private String nseSymbol;
	private double mwlLimit;
	private double openInterest;
	private double exhOpenIntrest;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getScripName() {
		return scripName;
	}

	public void setScripName(String scripName) {
		this.scripName = scripName;
	}

	public String getNseSymbol() {
		return nseSymbol;
	}

	public void setNseSymbol(String nseSymbol) {
		this.nseSymbol = nseSymbol;
	}

	public double getMwlLimit() {
		return mwlLimit;
	}

	public void setMwlLimit(double mwlLimit) {
		this.mwlLimit = mwlLimit;
	}

	public double getOpenInterest() {
		return openInterest;
	}

	public void setOpenInterest(double openInterest) {
		this.openInterest = openInterest;
	}

	public double getExhOpenIntrest() {
		return exhOpenIntrest;
	}

	public void setExhOpenIntrest(double exhOpenIntrest) {
		this.exhOpenIntrest = exhOpenIntrest;
	}

	@Override
	public String toString() {
		return "NSEOpenIntersert [date=" + date + ", isin=" + isin + ", scripName=" + scripName + ", nseSymbol="
				+ nseSymbol + ", mwlLimit=" + mwlLimit + ", openInterest=" + openInterest + ", exhOpenIntrest="
				+ exhOpenIntrest + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		NSEOpenIntersert ob = (NSEOpenIntersert) obj;

		if (ob.isin.compareTo(this.isin)==0) {
			return true;
		}
		return false;
	}

}