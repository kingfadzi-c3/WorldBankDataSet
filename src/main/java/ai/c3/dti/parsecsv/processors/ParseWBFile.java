package ai.c3.dti.parsecsv.processors;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class ParseWBFile {

	private Set<String> countries = new LinkedHashSet<String>();
	private Set<String> indicatorCodes = new LinkedHashSet<String>();
	
	public ArrayList<LineItem> createLineItems(Reader reader) throws IOException, CsvValidationException {
		
		ArrayList<LineItem> lines = new ArrayList<LineItem>();
		
		CSVReader csvReader = new CSVReader(reader);
		
	     String[] currentLine;

	     int lineNumber = 0;
	     
	     while ((currentLine = csvReader.readNext()) != null) {
			 
	    	 lineNumber =  lineNumber + 1;
	    	 
			 if(lineNumber > 5) {

				 LineItem lineItem = new LineItem();
				 lineItem.setCountryName(currentLine[0]);
				 lineItem.setCountryCode(currentLine[1]);
				 lineItem.setIndicatorName(currentLine[2]);
				 lineItem.setIndicatorCode(currentLine[3]);
				 
				 TreeMap<String, String> metrics = createMetrics(currentLine);
				 
				 lineItem.setMetrics(metrics);
				 
				 lines.add(lineItem);
				 
				 countries.add(currentLine[0]);
				 indicatorCodes.add(currentLine[3]);
				 
			 }
			 
		 }
		 
	     csvReader.close();
	     
	     return lines;
	     
	}
	
	public TreeMap<String, String> createMetrics(String[] currentLine) {
		
		int startYear = 1960;
		
		TreeMap<String, String> metrics = new TreeMap<String, String>();
		
		for(int i=4;i<=62;i++) {
			
			metrics.put(String.valueOf(startYear), currentLine[i]);
					
			startYear = startYear + 1;		
			
		}

		return metrics;
		
	}
	
	public List<NewLineItem> transfomGrid(ArrayList<LineItem> lineItems) {
		
		List<NewLineItem> rows = new ArrayList<NewLineItem>();
		
		for(int i=1960; i<=2019; i++) {
			
			for(String country: countries) {
				
				for(LineItem lineItem: lineItems) {
					
					if(country.equals(lineItem.getCountryName())) {
						
						NewLineItem newLineItem = new NewLineItem();
						TreeMap<String, String> metrics = new TreeMap<String, String>();
						
						newLineItem.setCountryCode(lineItem.getCountryCode());
						newLineItem.setCountryName(lineItem.getCountryName());
						newLineItem.setYear(String.valueOf(i));
						
						for(String indicatorCode: indicatorCodes) {
							
							if(indicatorCode.equals(lineItem.getIndicatorCode())) {
								String value = lineItem.getMetrics().get(String.valueOf(i));
								metrics.put(indicatorCode, value);
							}
						}
						newLineItem.setMetrics(metrics);
						rows.add(newLineItem);
						
					}
					
				}
				
			}
			
		}
		
		System.out.println(rows.size());
		
		return rows;
		
	}
	
	public String[] createHeader(List<NewLineItem> rows) {
	
		int arraySize = indicatorCodes.size() + 3;
		
		String[] header = new String[arraySize];
		
		header[0] = "Country Name";
		header[1] = "Country Code";
		header[2] = "Year";
		
		int i = 3;
		for(String indicatorCode: indicatorCodes) {
			header[i] = indicatorCode; i++;
		}
		
		return header;
		
	}
	
	public List<String[]> transformToStringArray(List<NewLineItem> rows) {
		
		System.out.println("start transformToStringArray rows: "+rows.size());
		
		List<String[]> result = new ArrayList<String[]>();
		
		String[] header = createHeader(rows);
		
		result.add(header);
		
		int arraySize = indicatorCodes.size() + 3;
		
		ArrayList<String> line = new ArrayList<String>();
		
		for(NewLineItem row: rows) {
			
			if(!line.contains(row.getCountryName())) line.add(row.getCountryName());
			if(!line.contains(row.getCountryCode())) line.add(row.getCountryCode());
			if(!line.contains(row.getYear())) line.add(row.getYear());
			
			for(String indicatorCode: indicatorCodes) {
				String value = row.getMetrics().get(indicatorCode);
				if(value!=null) {
					
					line.add(value);
					
					System.out.println("added..."+value);
					
					if(line.size() == arraySize) {
						result.add(line.toArray(new String[arraySize]));
						line = new ArrayList<String>();
					}
				}
			}
			
		}
			
		return result;
	}
	
	public void csvWriterAll(Collection<String[]> stringArray, String path) throws Exception {
	     CSVWriter writer = new CSVWriter(new FileWriter(path));
	     writer.writeAll(stringArray);
	     writer.close();
	}
	
	public static void main(String[] args) {
		
		ParseWBFile parse = new ParseWBFile();
		
		try {
			
		   //Reader reader = new FileReader("GlobalHealthDataset_Fadzi.csv");
		   Reader reader = new FileReader("GlobalFinanceDataset_Fadzi.csv");
		   //Reader reader = new FileReader("bla.csv");
			
		   ArrayList<LineItem> lines = parse.createLineItems(reader);
		   
		   System.out.println("completed parse.createLineItems(reader);");
		   
		   List<NewLineItem> transformed = parse.transfomGrid(lines);
		   
		   System.out.println("completed parse.transfomGrid(lines);");
		   
		   List<String[]> stringArray = parse.transformToStringArray(transformed);
		   
		   System.out.println("completed parse.transformToStringArray(transformed);");
		   
		   parse.csvWriterAll(stringArray, "out.csv");
		   
		   System.out.println("completed parse.csvWriterAll(stringArray, \"out.csv\");");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}