package ai.c3.dti.parsecsv.processors;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class LocationMappings {

	private Map<String, String> lookupTable = new HashMap<String, String>(); 
	
	public LocationMappings() throws IOException, URISyntaxException, CsvValidationException {

		Reader reader = new FileReader("WorldBankMappings.csv");
		
		CSVReader csvReader = new CSVReader(reader);
		String[] line;
		while ((line = csvReader.readNext()) != null) {
			
			lookupTable.put(line[1], line[2]);
			
		}
		reader.close();
		csvReader.close();
	}

	public Map<String, String> getLookupTable() {
		return lookupTable;
	}

	public void setLookupTable(Map<String, String> lookupTable) {
		this.lookupTable = lookupTable;
	}

	public static void main(String[] args) {
		
		try {
			LocationMappings mappings = new LocationMappings();
			
			System.out.println(mappings.getLookupTable().keySet());
			System.out.println(mappings.getLookupTable().values());
			
		} catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
