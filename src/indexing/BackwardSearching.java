package indexing;

import java.util.ArrayList;

public class BackwardSearching {
	

	// fun: produce table
	// input: string (would be easier to input FIRST)
	// return: Array of ints
	public static ArrayList<CharOccurrence> prodOccTable(String s){
		
		ArrayList<CharOccurrence> countTable = new ArrayList<CharOccurrence>();
		for(int i=0;i<s.length()-1;i++){
			if(CharOccurrence.containsChar(countTable,s.charAt(i)) == false ){
				CharOccurrence co = new CharOccurrence(s.charAt(i),i);
				countTable.add(co);
			}
		}
		return countTable;
			
	}
	
	// fun: given a string(firstcol), an index and a character, count how many instances the character appears in the string before the given index
	// input: string (firstcol), int (index), char (character to be counted)
	// output: int (the count)
	public static int countRank(String firstcol, String bwt, int index, char c){
		int count = 0;
		for(int j=0;j<index;j++){
			if(bwt.charAt(j)==c){
				count++;
			}
		}
		return count;
	}
	
	
	
	// fun: return range of suffixes that are prefixes to query
	// input: string (query), ArrayList<CharOccurrence> (count table), BWT and First column
	// output: array of ints (index range start and end)
	public static ArrayList<Integer> saRangeOfQuery(String query, ArrayList<CharOccurrence> table, String bwt, String firstcol){
		
		int start = 1;
		int end = bwt.length();
		for(int i=query.length()-1;i>=0;i--){
			start = CharOccurrence.getCount(table, query.charAt(i)) + countRank(firstcol, bwt, start-1, query.charAt(i)) + 1;
			end = CharOccurrence.getCount(table, query.charAt(i)) + countRank(firstcol, bwt, end, query.charAt(i));
		}
		// last step ==> add indexes to sarange
		ArrayList<Integer> sarange = new ArrayList<Integer>();
		sarange.add(start);
		sarange.add(end);
		return sarange;
		
	}
	
	

}
