package com.github.watchstopper.cpsc_445_project.indexing;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.github.watchstopper.cpsc_445_project.dp.LPA;

public class BWT {
	// FUNCTION: Obtains the sequence stored in a text file.
	// INPUT: String (file name)
	// RETURN: String (sequence)
	private static String parseSequenceFile(String filePath) {
		String sequence = "";
		File file = new File(filePath);
		Scanner scanner;

		try {
			scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				sequence += scanner.nextLine();
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found. Program aborted.");
			System.exit(-1);
		}

		return sequence;
	}

	// FUNCTION: Reverses a string.
	// INPUT: String
	// RETURN: String (reversed)
	private static String reverseString(String s) {
		return new StringBuilder(s).reverse().toString();
	}

	// FUNCTION: Generate all possible rotations of a text.
	// INPUT: String (the original text)
	// RETURN: Array of strings
	private static String[] generateRotations(String text) {
		String[] rotations = new String[text.length() + 1];
		rotations[0] = "$" + text;		

		for (int i = 1; i < text.length() + 1; i++) {
			rotations[i] = rotations[i - 1].substring(1)
					+ rotations[i - 1].charAt(0);
		}

		return rotations;
	}

	// FUNCTION: Sort the array of rotations.
	// INPUT: Array containing rotations
	// RETURN: N/A
	private static void sortRotations(String[] rotations) {
		Arrays.sort(rotations);
	}

	// FUNCTION: Extract the BWT.
	// INPUT: Array containing rotations
	// RETURN: String (BWT)
	private static String extractLastColumn(String[] rotations) {
		String bwt = "";

		for (int i = 0; i < rotations.length; i++) {
			bwt += rotations[i].charAt(rotations[i].length() - 1);
		}

		return bwt;
	}

	// FUNCTION: Extract the first column of the array of rotations.
	// INPUT: Array containing rotations
	// RETURN: String (the first column of the array of rotations)
	private static String extractFirstColumn(String[] rotations) {
		String firstColumn = "";

		for (int i = 0; i < rotations.length; i++) {
			firstColumn += rotations[i].charAt(0);
		}

		return firstColumn;
	}

	// FUNCTION: Revert the BWT back to the original text.
	// INPUT: String (BWT)
	// RETURN: String (the original text)
	private static String revertBWT(String bwt) {
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

	// FUNCTION: Simulate a pre-order traversal of the suffix trie for a
	//           given text in order to find the best local alignment.
	//           Output the alignment and alignment score to the console.
	// INPUT: String (BWT for the reversal of the original text),
	//        String (reversed pattern), ArrayList<CharOccurrence> (count table)
	// RETURN: N/A
	private static void findLocalAlignment(String bwt, String pattern,
			ArrayList<CharOccurrence> countTable) {
		String sequence1Alignment = "";
		String sequence2Alignment = "";
		int maxScore = 0;
		int depth = 1;
		boolean depthDecreased = false;
		String node = "A";

		while (depth > 0) {
			// Check if zX^-1 exists in T^-1 (i.e., check if the substring
			// X represented by the current node has an edge labeled with
			// z in the suffix trie of T)
			// NOTE: 'z' gets added in the previous iteration.
			boolean containsSubstring = BackwardSearch.containsQuery(node,
					countTable, bwt, 1, bwt.length());
			int score = 0;

			if (containsSubstring && !depthDecreased) {
				// Only run the alignment algorithm if zX^-1 exists in T^-1
				// and if we are not moving up the tree
				LPA.runLPA(node, pattern);
				score = LPA.getAlignmentScore();
			}

			if (score > 0) {
				if (score > maxScore) {
					// Keep track of the best alignment found so far
					maxScore = score;
					sequence1Alignment = LPA.getSequence1Alignment();
					sequence2Alignment = LPA.getSequence2Alignment();
				}

				if (!depthDecreased) {
					// Continue moving down the suffix trie
					node = "A" + node;
					depth++;
				} else {
					// Switch to the next letter in the alphabet before
					// continuing to move down the suffix trie
					if (node.startsWith("A")) {
						node = "C" + node.substring(1, node.length());
						depthDecreased = false;
					} else if (node.startsWith("C")) {
						node = "G" + node.substring(1, node.length());
						depthDecreased = false;
					} else if (node.startsWith("G")) {
						node = "T" + node.substring(1, node.length());
						depthDecreased = false;
					} else {
						// Move further up the suffix trie, since we have
						// exhausted the alphabet at this level
						node = node.substring(1, node.length());
						depth--;
					}
				}
			} else {
				// Prune the whole subtree rooted at the current node
				if (node.startsWith("A")) {
					node = "C" + node.substring(1, node.length());
				} else if (node.startsWith("C")) {
					node = "G" + node.substring(1, node.length());
				} else if (node.startsWith("G")) {
					node = "T" + node.substring(1, node.length());
				} else {
					node = node.substring(1, node.length());
					depth--;
					depthDecreased = true;
				}
			}
		}

		System.out.println(sequence1Alignment);
		System.out.println(sequence2Alignment);
		System.out.println("Score=" + maxScore);
	}

	public static void main(String[] args) {
		// Test code
		// Parse and reverse the text
		String text = reverseString(parseSequenceFile("sequences\\text.txt"));
		String[] rotations = generateRotations(text);
		sortRotations(rotations);
		String bwt = extractLastColumn(rotations);
		String firstColumn = extractFirstColumn(rotations);
		revertBWT(bwt);

		ArrayList<CharOccurrence> countTable = new ArrayList<CharOccurrence>();
		countTable = BackwardSearch.produceCharOccurrenceTable(firstColumn);

		// Parse and reverse the pattern
		String pattern = reverseString(parseSequenceFile("sequences\\pattern.txt"));
		findLocalAlignment(bwt, pattern, countTable);

		System.out.println();

		System.out.println("End of program");
	}
}
