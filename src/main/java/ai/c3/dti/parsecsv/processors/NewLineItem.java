package ai.c3.dti.parsecsv.processors;

import java.util.TreeMap;

public class NewLineItem {

	private String countryName;
	private String countryCode;
	private String year;
	private TreeMap<String, String> metrics = new TreeMap<String, String>();

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public TreeMap<String, String> getMetrics() {
		return metrics;
	}

	public void setMetrics(TreeMap<String, String> metrics) {
		this.metrics = metrics;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "NewLineItem [countryName=" + countryName + ", countryCode=" + countryCode + ", year=" + year
				+ ", metrics=" + metrics + "]";
	}

}
