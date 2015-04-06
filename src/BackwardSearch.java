import java.util.ArrayList;

public class BackwardSearch {
	// FUNCTION: Produce CharOccurrence table.
	// INPUT: String (would be easier to input FIRST)
	// RETURN: Array of ints
	public static ArrayList<CharOccurrence> produceCharOccurrenceTable(String s) {
		ArrayList<CharOccurrence> countTable = new ArrayList<CharOccurrence>();

		for (int i = 0; i < s.length() - 1; i++) {
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

	// FUNCTION: Return range of suffices for which the query is a prefix.
	// INPUT: String (query), ArrayList<CharOccurrence> (count table),
	//        String (BWT)
	// RETURN: Array of ints (index range start and end)
	public static ArrayList<Integer> getSARange(String query,
			ArrayList<CharOccurrence> table, String bwt) {
		int start = 1;
		int end = bwt.length();

		for (int i = query.length() - 1; i >= 0; i--) {
			start = CharOccurrence.getCount(table, query.charAt(i))
					+ getRank(bwt, start - 1, query.charAt(i)) + 1;
			end = CharOccurrence.getCount(table, query.charAt(i))
					+ getRank(bwt, end, query.charAt(i));
		}

		// last step ==> add indexes to saRange
		ArrayList<Integer> saRange = new ArrayList<Integer>();
		saRange.add(start - 1);
		saRange.add(end - 1);

		return saRange;
	}
}
