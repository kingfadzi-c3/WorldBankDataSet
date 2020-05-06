package ai.c3.dti.parsecsv.processors;

import java.util.TreeMap;

public class LineItem {

	private String countryName;
	private String countryCode;
	private String indicatorName;
	private String indicatorCode;
	private TreeMap<String, String> metrics;
	
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
	public String getIndicatorName() {
		return indicatorName;
	}
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}
	public String getIndicatorCode() {
		return indicatorCode;
	}
	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}
	public TreeMap<String, String> getMetrics() {
		return metrics;
	}
	public void setMetrics(TreeMap<String, String> metrics) {
		this.metrics = metrics;
	}
	@Override
	public String toString() {
		return "LineItem [countryName=" + countryName + ", countryCode=" + countryCode + ", indicatorName="
				+ indicatorName + ", indicatorCode=" + indicatorCode + ", metrics=" + metrics + "]";
	}
	
	
}
