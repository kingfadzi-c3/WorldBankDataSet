package ai.c3.dti.parsecsv.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import ai.c3.dti.parsecsv.processors.LineItem;
import ai.c3.dti.parsecsv.processors.NewLineItem;
import ai.c3.dti.parsecsv.processors.ParseWBFile;
import ai.c3.dti.parsecsv.processors.ParseWorldBankFile;

@Controller
public class UploadController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload-csv-file")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

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
        	
        	
            model.addAttribute("status", true);
            model.addAttribute("download", file.getOriginalFilename().concat("_PROCESSED.csv"));

        }

        return "file-upload-status";
    }
}
