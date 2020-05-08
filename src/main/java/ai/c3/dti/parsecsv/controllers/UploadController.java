package ai.c3.dti.parsecsv.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ai.c3.dti.parsecsv.processors.InvalidFileException;
import ai.c3.dti.parsecsv.processors.LineItem;
import ai.c3.dti.parsecsv.processors.NewLineItem;
import ai.c3.dti.parsecsv.processors.ParseWBFile;

@Controller
public class UploadController {

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/csv")
	public void downloadCSV(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("fileName") String fileName) {

		String dataDirectory = request.getServletContext().getContextPath();
		
		System.out.println("dataDirectory: "+dataDirectory);
		
		Path file = Paths.get(dataDirectory, fileName);
		if (Files.exists(file)) {
			response.setContentType("text/csv");
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			try {
				Files.copy(file, response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		} else {
			throw new RuntimeException("file not found: "+file.getFileName());
		}
	}
	
	
	@PostMapping("/upload-csv-file")
	public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) throws Exception {

		// validate file
		if (file.isEmpty()) {
			model.addAttribute("message", "Please select a CSV file to upload.");
			model.addAttribute("status", false);
		} else {

			ParseWBFile parse = new ParseWBFile();

			try {

				Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

				ArrayList<LineItem> lines = parse.createLineItems(reader);

				System.out.println("completed parse.createLineItems(reader);");

				List<NewLineItem> transformed = parse.transfomGrid(lines);

				System.out.println("completed parse.transfomGrid(lines);");

				List<String[]> stringArray = parse.transformToStringArray(transformed);

				System.out.println("completed parse.transformToStringArray(transformed);");

				parse.csvWriterAll(stringArray, file.getOriginalFilename().concat("_PROCESSED.csv"));

				System.out.println("completed parse.csvWriterAll(stringArray, \"out.csv\");");

			} catch (InvalidFileException e) {
				throw new RuntimeException("Sorry that is an invalid file",e);
			} 

			model.addAttribute("status", "Successed");
			model.addAttribute("fileName", file.getOriginalFilename().concat("_PROCESSED.csv"));

		}

		return "file-upload-status";
	}
}
