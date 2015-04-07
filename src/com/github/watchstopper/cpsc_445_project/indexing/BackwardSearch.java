package com.github.watchstopper.cpsc_445_project.indexing;
import java.util.ArrayList;

public class BackwardSearch {
	// FUNCTION: Produce CharOccurrence table.
	// INPUT: String (would be easier to input FIRST)
	// RETURN: Array of ints
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
	// INPUT: String (BWT), int (index), char (character to be counted)
	// RETURN: int (the count)
	public static int getRank(String bwt, int index, char c) {
		int rank = 0;

		for (int i = 0; i < index; i++) {
			if (bwt.charAt(i) == c) {
				rank++;
			}
		}

		return rank;
	}

	// FUNCTION: Return range of suffices, in the specified range of the
	//           text, for which the query is a prefix.
	// INPUT: String (query), ArrayList<CharOccurrence> (count table),
	//        String (BWT), int (start index), end (end index)
	// RETURN: Array of ints (index range start and end)
	public static ArrayList<Integer> getSARange(String query,
			ArrayList<CharOccurrence> table, String bwt, int start, int end) {
		for (int i = query.length() - 1; i >= 0; i--) {
			start = CharOccurrence.getCount(table, query.charAt(i))
					+ getRank(bwt, start - 1, query.charAt(i)) + 1;
			end = CharOccurrence.getCount(table, query.charAt(i))
					+ getRank(bwt, end, query.charAt(i));
		}

		ArrayList<Integer> saRange = new ArrayList<Integer>();
		saRange.add(start - 1);
		saRange.add(end - 1);

		return saRange;
	}

	// FUNCTION: Check if a given query exists in the specified range
	//           of the text.
	// INPUT: String (query), ArrayList<CharOccurrence> (count table),
	//        String (BWT), int (start index), end (end index)
	// RETURN: true if query exists in the text, false otherwise
	public static boolean containsQuery(String query,
			ArrayList<CharOccurrence> table, String bwt, int start, int end) {
		ArrayList<Integer> saRange = new ArrayList<Integer>();
		saRange = getSARange(query, table, bwt, start, end);

		if (saRange.get(0) <= saRange.get(1)) {
			return true;
		} else {
			return false;
		}
	}
}
