package com.github.watchstopper.cpsc_445_project.indexing;
import java.util.ArrayList;

public class CharOccurrence {
	private char c;
	private int lexSmallerCount;     // Number of total characters in string that
	                                 // are lexicographically smaller than character

	// FUNCTION: Create a CharOccurrence object.
	// INPUT: char, int (count of lexicographically smaller characters)
	// RETURN: CharOccurrence
	public CharOccurrence(char c, int lexSmallerCount) {
		this.c = c;
		this.lexSmallerCount = lexSmallerCount;
	}

	// FUNCTION: Get character.
	// INPUT: N/A
	// RETURN: char
	public char getChar() {
		return c;
	}

	// FUNCTION: Get count of lexicographically smaller characters.
	// INPUT: N/A
	// RETURN: int (count of lexicographically smaller characters)
	public int getLexSmallerCount() {
		return lexSmallerCount;
	}

	// FUNCTION: Check if a character is in the CharOccurrence table.
	// INPUT: ArrayList<CharOccurrence> (count table), char
	// RETURN: true if character is in the CharOccurrence table, false otherwise
	public static boolean containsChar(ArrayList<CharOccurrence> table, char c) {
		int i = 0;

		while (i < table.size()) {
			if (table.get(i).getChar() == c) {
				return true;
			}

			i++;	
		}

		return false;
	}

	// FUNCTION: Get count of lexicographically smaller characters
	//           for a character in the CharOccurrence table.
	// INPUT: ArrayList<CharOccurrence> (count table), char
	// RETURN: int (count of lexicographically smaller characters)
	// NOTE: We iterate over the entire CharOccurrence table, since
	//       we know the maximum number of iterations is 4 because we
	//       are working with DNA.
	public static int getCount(ArrayList<CharOccurrence> table, char c) {
		int count = -1;

		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).getChar() == c) {
				count = table.get(i).getLexSmallerCount();
			}
		}

		return count;
	}
}
