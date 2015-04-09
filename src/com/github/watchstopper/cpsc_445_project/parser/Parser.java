package com.github.watchstopper.cpsc_445_project.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
	// FUNCTION: Obtains the sequence stored in a text file.
	// INPUT: String (file path)
	// RETURN: String (sequence)
	public static ArrayList<String> parseSequenceFile(String filePath) {
		ArrayList<String> sequence = new ArrayList<String>();
		File file = new File(filePath);
		Scanner scanner;

		try {
			scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				sequence.add(scanner.nextLine());
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found. Program aborted.");
			System.exit(-1);
		}

		return sequence;
	}
	
	
	
}
