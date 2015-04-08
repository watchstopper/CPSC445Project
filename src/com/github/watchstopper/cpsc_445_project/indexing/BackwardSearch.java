package com.github.watchstopper.cpsc_445_project.indexing;
import java.util.ArrayList;

public class BackwardSearch {
	// FUNCTION: Produce CharOccurrence table.
	// INPUT: String
	// RETURN: ArrayList<CharOccurrence> (count table)
	public static ArrayList<CharOccurrence> produceCharOccurrenceTable(String s) {
		ArrayList<CharOccurrence> countTable = new ArrayList<CharOccurrence>();

		for (int i = 0; i < s.length(); i++) {
			if (CharOccurrence.containsChar(countTable, s.charAt(i)) == false) {
				CharOccurrence co = new CharOccurrence(s.charAt(i), i);
				countTable.add(co);
			}
		}

		return countTable;	
	}

	// FUNCTION: Given a string (BWT), an index and a character,
	//           count the number of instances the character appears in the
	//           string before the given index.
	// INPUT: String (BWT), int (index), char
	// RETURN: int (rank)
	public static int getRank(String bwt, int index, char c) {
		int rank = 0;

		for (int i = 0; i < index; i++) {
			if (bwt.charAt(i) == c) {
				rank++;
			}
		}

		return rank;
	}

	// FUNCTION: Return range of suffices in the specified range of the
	//           text for which the query is a prefix.
	// INPUT: String (query), ArrayList<CharOccurrence> (count table),
	//        String (BWT), int (start index), int (end index)
	// RETURN: int[] (index range: int[0] = start, int[1] = end)
	public static int[] getSARange(String query,
			ArrayList<CharOccurrence> table, String bwt, int start, int end) {
		start++;
		end++;

		for (int i = query.length() - 1; i >= 0; i--) {
			start = CharOccurrence.getCount(table, query.charAt(i))
					+ getRank(bwt, start - 1, query.charAt(i)) + 1;
			end = CharOccurrence.getCount(table, query.charAt(i))
					+ getRank(bwt, end, query.charAt(i));
		}

		int[] saRange = new int[2];
		saRange[0] = start - 1;
		saRange[1] = end - 1;

		return saRange;
	}

	// FUNCTION: Check if a given SA range is valid.
	// INPUT: int[] (SA range)
	// RETURN: true if start <= end, false otherwise
	public static boolean isValid(int[] saRange) {
		if (saRange[0] <= saRange[1]) {
			return true;
		} else {
			return false;
		}
	}
}
