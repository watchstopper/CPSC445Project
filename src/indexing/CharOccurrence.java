package indexing;

import java.util.ArrayList;

public class CharOccurrence {

	char character;
	int countoflexsmaller; // number of total characters within string that are lexicographically smaller than character
	
	
	public CharOccurrence(char c, int s){
		character = c;
		countoflexsmaller = s;
	}
	
	// fun: check a char in the CharOccurrence table
	// input: char , ArrayList<CharOccurrence> (the table)
	// output: int
	// note: iterating over all because we know we are dealing with dna so should only iterate max 4 times
	public static int getCount(ArrayList<CharOccurrence> table, char c){
		int count = 0;
		for(int i=0; i<table.size();i++){
			if(table.get(i).character==c){
				count = table.get(i).countoflexsmaller;
			}
		}
		return count;
	}
		
	
	public static boolean containsChar(ArrayList<CharOccurrence> table, char c){
		boolean contains = false;
		int i=0;
		while(contains==false && i<table.size()){
			if(table.get(i).character == c){
				contains = true;
			}
			i++;	
		}
		
		return contains;
	}
	
	
	
}
