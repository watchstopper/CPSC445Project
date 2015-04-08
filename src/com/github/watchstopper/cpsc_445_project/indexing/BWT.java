package com.github.watchstopper.cpsc_445_project.indexing;
import java.util.ArrayList;
import java.util.Arrays;

public class BWT {
	// FUNCTION: Reverses a string.
	// INPUT: String
	// RETURN: String (reversed)
	public static String reverseString(String s) {
		return new StringBuilder(s).reverse().toString();
	}

	// FUNCTION: Generate all possible rotations of a string.
	// INPUT: String
	// RETURN: String[] (array of rotations)
	public static String[] generateRotations(String s) {
		String[] rotations = new String[s.length() + 1];
		rotations[0] = "$" + s;		

		for (int i = 1; i < s.length() + 1; i++) {
			rotations[i] = rotations[i - 1].substring(1)
					+ rotations[i - 1].charAt(0);
		}

		return rotations;
	}

	// FUNCTION: Sort the array of rotations.
	// INPUT: String[] (array of rotations)
	// RETURN: N/A
	public static void sortRotations(String[] rotations) {
		Arrays.sort(rotations);
	}

	// FUNCTION: Extract the BWT.
	// INPUT: String[] (array of rotations)
	// RETURN: String (BWT)
	public static String extractLastColumn(String[] rotations) {
		String bwt = "";

		for (int i = 0; i < rotations.length; i++) {
			bwt += rotations[i].charAt(rotations[i].length() - 1);
		}

		return bwt;
	}

	// FUNCTION: Extract the first column of the array of rotations.
	// INPUT: String[] (array of rotations)
	// RETURN: String (the first column of the array of rotations)
	public static String extractFirstColumn(String[] rotations) {
		String firstColumn = "";

		for (int i = 0; i < rotations.length; i++) {
			firstColumn += rotations[i].charAt(0);
		}

		return firstColumn;
	}

	// FUNCTION: Generate the BWT for the reverse of a given string.
	// INPUT: String
	// RETURN: String (BWT for reverse of the given string)
	public static String getReverseBWT(String s) {
		s = reverseString(s);
		String[] rotations = generateRotations(s);
		sortRotations(rotations);

		return extractLastColumn(rotations);
	}

	// FUNCTION: Get the CharOccurrence table for a given string.
	// INPUT: String
	// RETURN: ArrayList<CharOccurrence> (count table)
	public static ArrayList<CharOccurrence> getCharOccurrenceTable(String s) {
		s = reverseString(s);
		String[] rotations = generateRotations(s);
		sortRotations(rotations);
		String firstColumn = extractFirstColumn(rotations);

		ArrayList<CharOccurrence> countTable = new ArrayList<CharOccurrence>();
		countTable = BackwardSearch.produceCharOccurrenceTable(firstColumn);

		return countTable;
	}

	// FUNCTION: Print the sorted array of rotations for the reverse of a
	//           given string to the console output.
	// INPUT: String
	// RETURN: N/A
	public static void printReverseRotations(String s) {
		s = reverseString(s);
		String[] rotations = generateRotations(s);
		sortRotations(rotations);

		for (String r : rotations) {
			System.out.println(r);
		}
	}

	// FUNCTION: Print the sorted array of rotations for a given string
	//           to the console output.
	// INPUT: String
	// RETURN: N/A
	public static void printRotations(String s) {
		String[] rotations = generateRotations(s);
		sortRotations(rotations);

		for (String r : rotations) {
			System.out.println(r);
		}
	}

	// FUNCTION: Revert the BWT back to the original string.
	// INPUT: String (BWT)
	// RETURN: String (original)
	public static String revertBWT(String bwt) {
		String[] rotations = new String[bwt.length()];

		for (int i = 0; i < rotations.length; i++) {
			rotations[i] = bwt.substring(i, i + 1);
		}

		Arrays.sort(rotations);

		for (int i = 1; i < bwt.length(); i++) {
			for (int j = 0; j < rotations.length; j++) {
				rotations[j] = bwt.charAt(j) + rotations[j];
			}

			Arrays.sort(rotations);
		}

		return rotations[0].substring(1, rotations[0].length());
	}
}
