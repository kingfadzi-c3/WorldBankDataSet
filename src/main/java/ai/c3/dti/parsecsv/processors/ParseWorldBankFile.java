package ai.c3.dti.parsecsv.processors;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class ParseWorldBankFile {

	public String processFile(MultipartFile file) {

		String outFileName = null;

		List<String[]> allLines = new ArrayList<String[]>();

		System.out.print("starting...");

		try {

			String[] header = new String[6];

			header[0] = ("Country Name");
			header[1] = ("Country Code");
			header[2] = ("Indicator Name");
			header[3] = ("Indicator Code");
			header[4] = ("Year");
			header[5] = ("Value");

			allLines.add(header);

			Reader reader1 = new BufferedReader(new InputStreamReader(file.getInputStream()));

			CSVReader reader = new CSVReader(reader1);
			Iterator<String[]> iter = reader.iterator();

			while (iter.hasNext()) {

				String[] currentLine = iter.next();

				if (currentLine.length > 4) {

					if (!currentLine[0].equals("Country Name")) {
						String[] shortLine = createShortLine(currentLine);
						List<String[]> flattenedLines = createFlattenedLines(currentLine, shortLine);
						allLines.addAll(flattenedLines);
					}
				}

			}

			outFileName = file.getOriginalFilename();

			csvWriterAll(allLines, outFileName + "_FORMATTED.csv");

			reader.close();

			System.out.print("finished...");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

		return outFileName;

	}

	public String[] createShortLine(String[] line) {

		String[] shortLine = new String[6];

		for (int i = 0; i <= 4; i++) {
			shortLine[i] = line[i];
		}
		return shortLine;

	}

	public void printLine(String[] line) {

		System.out.println(line[0] + "," + line[1] + "," + line[2] + "," + line[3] + "," + line[4] + "," + line[5]);

	}

	public List<String[]> createFlattenedLines(String[] currentLine, String[] line) {

		List<String[]> strings = new ArrayList<String[]>();

		int startYear = 1960;

		for (int i = 4; i <= 62; i++) {

			String[] newString = line.clone();

			newString[4] = String.valueOf(startYear);
			newString[5] = currentLine[i];

			startYear = startYear + 1;

			strings.add(newString);
		}

		return strings;

	}

	public void csvWriterAll(List<String[]> stringArray, String path) throws Exception {
		CSVWriter writer = new CSVWriter(new FileWriter(path));
		writer.writeAll(stringArray);
		writer.close();
	}

}
