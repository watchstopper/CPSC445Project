package com.github.watchstopper.cpsc_445_project.dp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import com.github.watchstopper.cpsc_445_project.indexing.BWT;
import com.github.watchstopper.cpsc_445_project.indexing.BackwardSearch;
import com.github.watchstopper.cpsc_445_project.indexing.CharOccurrence;
import com.github.watchstopper.cpsc_445_project.parser.Parser;

public class LPA {
	private static String text;
	private static char[] pattern;
	private static String bwt;
	private static ArrayList<CharOccurrence> countTable;
	private static ArrayList<String> matches;
	

	private static Stack<int[]> saRanges;
	private static int n;
	private static int m;
	private static int depth;
	private static int seqCounter;

	private static int[][] matrixN;
	private static int[][] matrixN_1;
	private static int[][] matrixN_2;
	private static int[][] matrixN_3;

	private static String sequence1Alignment = "";
	private static String sequence2Alignment = "";
	private static int maxScore = 0;

	private static final String TEXT_PATH = "sequences\\text.txt";
	private static final String PATTERN_PATH = "sequences\\pattern.txt";
	private static final int A = 1;
	private static final int B = -3;
	private static final int G = 5;
	private static final int S = 2;
	private static final int MIN_VALUE = -50000;

	// FUNCTION: Initialize the DP tables.
	// INPUT: N/A
	// RETURN: N/A
	private static void initializeMatrices() {
		matrixN = new int[n][m];
		matrixN_1 = new int[n][m];
		matrixN_2 = new int[n][m];
		matrixN_3 = new int[n][m];

		for (int j = 0; j < m; j++) {
			matrixN[0][j] = 0;
			matrixN_2[0][j] = MIN_VALUE;
		}

		for (int i = 1; i < n; i++) {
			matrixN_1[i][0] = -(G + i * S);
			matrixN_3[i][0] = MIN_VALUE;
		}
	}

	// FUNCTION: Determine if we have a matched or mismatched pair.
	// INPUT: char, char
	// RETURN: a if matched pair, b otherwise
	private static int getDistance(char c1, char c2) {
		if (c1 == c2) {
			return A;
		} else {
			return B;
		}
	}

	// FUNCTION: Fill the current row of the DP tables.
	// INPUT: char (the 'z' in zX^-1)
	// RETURN: N/A
	private static void fillRow(char c) {
		for (int j = 1; j < m; j++) {
			// Simplified recurrences
			matrixN_1[depth][j] = Math.max(MIN_VALUE,
					matrixN[depth - 1][j - 1] + getDistance(c, pattern[j - 1]));

			matrixN_2[depth][j] = Math.max(MIN_VALUE,
					Math.max(matrixN[depth - 1][j] - (G + S), matrixN_2[depth - 1][j] - S));

			matrixN_3[depth][j] = Math.max(MIN_VALUE,
					Math.max(matrixN[depth][j - 1] - (G + S), matrixN_3[depth][j - 1] - S));

			matrixN[depth][j] = Math.max(matrixN_1[depth][j],
					Math.max(matrixN_2[depth][j], matrixN_3[depth][j]));
		}
	}

	// FUNCTION: Determine if it is pointless to continue down a node.
	// INPUT: N/A
	// RETURN: true if the current row has no values greater than zero,
	//         false otherwise
	private static boolean meaninglessRow() {
		for (int j = 1; j < m; j++) {
			if (matrixN[depth][j] > 0) {
				return false;
			}
		}

		return true;
	}

	// FUNCTION: Update the maximum score if applicable.
	// INPUT: N/A
	// RETURN: true if the maximum score was updated,
	//         false otherwise
	private static boolean updateMaxScore() {
		boolean maxScoreUpdated = false;

		for (int j = 1; j < m; j++) {
			if (matrixN[depth][j] > maxScore) {
				maxScore = matrixN[depth][j];
				maxScoreUpdated = true;
			}
		}

		return maxScoreUpdated;
	}

	// FUNCTION: Determine the maximum score in the current row of
	//           the specified matrix.
	// INPUT: int[][] (matrix)
	// RETURN: int (maximum score)
	private static int maxScore(int[][] matrix) {
		int max = Integer.MIN_VALUE;

		for (int j = 0; j < m; j++) {
			if (matrix[depth][j] > max) {
				max = matrix[depth][j];
			}
		}

		return max;
	}

	// FUNCTION: Determine the index in the pattern (sequence 2)
	//           which holds the maximum score in the current row
	//           of the specified matrix.
	// INPUT: int[][] (matrix)
	// RETURN: int (pattern index)
	private static int maxJ(int[][] matrix) {
		int max = Integer.MIN_VALUE;
		int maxJ = 0;

		for (int j = 1; j < m; j++) {
			if (matrix[depth][j] > max) {
				max = matrix[depth][j];
				maxJ = j;
			}
		}

		return maxJ;
	}

	// FUNCTION: Carry out traceback to construct the alignment.
	// INPUT: char[] (the char array of X)
	// RETURN: N/A
	private static void traceback(char[] x) {
		sequence1Alignment = "";
		sequence2Alignment = "";
		int maxScoreMatrix = 0;
		int maxJ = 0;

		if (maxScore(matrixN_1) >= maxScore(matrixN_2)
				&& maxScore(matrixN_1) >= maxScore(matrixN_3)
				&& maxScore(matrixN_1) > 0) {
			maxScoreMatrix = 1;
			maxJ = maxJ(matrixN_1);
		} else if (maxScore(matrixN_2) >= maxScore(matrixN_3)
				&& maxScore(matrixN_2) >= 0) {
			maxScoreMatrix = 2;
			maxJ = maxJ(matrixN_2);
		} else if (maxScore(matrixN_3) >= 0) {
			maxScoreMatrix = 3;
			maxJ = maxJ(matrixN_3);
		}

		int i = depth;
		int j = maxJ;
		int xIndex = depth - 1;
		int patternIndex = maxJ - 1;

		while (true) {
			if (maxScoreMatrix == 1 && i > 0 && j > 0) {
				sequence1Alignment += x[xIndex];
				sequence2Alignment += pattern[patternIndex];
				i--;
				j--;
				xIndex--;
				patternIndex--;
			} else if (maxScoreMatrix == 2 && i > 0 && j > 0) {
				sequence1Alignment += x[xIndex];
				sequence2Alignment += "-";
				i--;
				xIndex--;
			} else if (maxScoreMatrix == 3 && i > 0 && j > 0) {
				sequence1Alignment += "-";
				sequence2Alignment += pattern[patternIndex];
				j--;
				patternIndex--;
			} else {
				break;
			}

			if (matrixN_1[i][j] >= matrixN_2[i][j] && matrixN_1[i][j] >= matrixN_3[i][j]
					&& matrixN_1[i][j] >= 0) {
				maxScoreMatrix = 1;
			} else if (matrixN_2[i][j] >= matrixN_3[i][j] && matrixN_2[i][j] >= 0) {
				maxScoreMatrix = 2;
			} else if (matrixN_3[i][j] >= 0) {
				maxScoreMatrix = 3;
			} else {
				maxScoreMatrix = 0;
			}
		}

		sequence1Alignment = BWT.reverseString(sequence1Alignment);
		sequence2Alignment = BWT.reverseString(sequence2Alignment);
	}

	// FUNCTION: Change the 'z' in zX^-1 to the next letter in the
	//           lexicographical sequence.
	// INPUT: String (old zX^-1)
	// RETURN: String (new zX^-1)
	private static String tryNextLetter(String x) {
		if (x.startsWith("A")) {
			x = "C" + x.substring(1, x.length());
		} else if (x.startsWith("C")) {
			x = "G" + x.substring(1, x.length());
		} else {
			x = "T" + x.substring(1, x.length());
		}

		return x;
	}

	// FUNCTION: Check whether we have used the last letter in the
	//           lexicographical sequence as 'z' in zX^-1.
	// INPUT: String (zX^-1)
	// RETURN: true if z = "T", false otherwise
	private static boolean exhaustedAlphabet(String x) {
		if (x.startsWith("T")) {
			return true;
		} else {
			return false;
		}
	}

	// FUNCTION: Simulate a pre-order traversal of the suffix trie of
	//           the text.
	// INPUT: N/A
	// RETURN: N/A
	private static void traverseSuffixTrie() {
		// Set start node
		String x = "A";
		depth = 1;
		boolean depthDecreased = false;

		while (depth > 0) {
			boolean tryNextLetter = true;

			if (!depthDecreased) {
				int[] saRange = saRanges.peek();
				int i = saRange[0];
				int j = saRange[1];
				saRange = BackwardSearch.getSARange(x.substring(0, 1),
						countTable, bwt, i, j);

				if (BackwardSearch.isValid(saRange)) {
					fillRow(x.charAt(0));

					if (!meaninglessRow()) {
						boolean maxScoreUpdated = updateMaxScore();

						if (maxScoreUpdated) {
							// Do traceback only if the maximum score changes
							traceback(BWT.reverseString(x).toCharArray());
						}

						// Move down the tree
						x = "A" + x;
						depth++;
						saRanges.push(saRange);
						tryNextLetter = false;
					}
				}
			}

			if (tryNextLetter) {
				if (!exhaustedAlphabet(x)) {
					// Try moving to a different child
					x = tryNextLetter(x);
					depthDecreased = false;
				} else {
					// Move back up the tree
					x = x.substring(1, x.length());
					depth--;
					depthDecreased = true;
					saRanges.pop();
				}
			}
		}
	}

	// FUNCTION: Use BWT-SW to find the best local alignment between
	//           the given text and pattern.
	// INPUT: N/A
	// RETURN: N/A
	private static void findBestLocalAlignment() {
		saRanges = new Stack<int[]>();
		int[] saRange = {0, bwt.length() - 1};
		saRanges.push(saRange);
		n = bwt.length() + 1;
		m = pattern.length + 1;

		initializeMatrices();
		traverseSuffixTrie();
	}

	public static void main(String[] args) {
		
		pattern = Parser.parseSequenceFile(PATTERN_PATH).toCharArray();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(TEXT_PATH));
			text = null;
			matches = new ArrayList<String>();
			seqCounter = 0;
			while((text=br.readLine()) != null){
				String temp = text.substring(0,1);
				if(!temp.equals(">")){
					bwt = BWT.getReverseBWT(text);
					countTable = new ArrayList<CharOccurrence>();
					countTable = BWT.getCharOccurrenceTable(text);
					findBestLocalAlignment();
					if(maxScore > pattern.length-3){
						matches.add(text);
					}
					seqCounter++;
					System.out.println("done" + seqCounter + "sequences");
					if(20%seqCounter==0){
						System.out.println(maxScore);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("error, file not found");
			System.exit(-1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!matches.isEmpty()){
			for(int i=0;i<matches.size()-1;i++){
				System.out.println(matches.get(i));
			}
			System.out.println(seqCounter + "sequences found");
		}else{
			System.out.println("no matches were found");
		}
		System.out.println();
		System.out.println("End of program");
	}
}
