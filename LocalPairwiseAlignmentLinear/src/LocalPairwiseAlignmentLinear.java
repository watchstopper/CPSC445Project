import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LocalPairwiseAlignmentLinear {
	private static List<String> sequenceNames;
	private static List<Integer> sequenceScores;
	private static List<String> sequence1Alignments;
	private static List<String> sequence2Alignments;
	private static List<Integer> sequence1StartingPositions;
	private static List<Integer> sequence2StartingPositions;
	private static List<Integer> alignmentLengths;

	private static final String SEQUENCE_1 = "ZN768_HUMAN";
	private static final String SEQUENCE_PATH = "ZN768_HUMAN.txt";
	private static final String FASTA_PATH = "sequences.fasta";

	private static void printMatchInfo(int score, String name, int i) {
		System.out.println("Index=" + i + " Name=" + name + " Score=" + score);
		System.out.println("START POS in " + name + ": " + sequence2StartingPositions.get(i)
				+ " START POS in " + SEQUENCE_1 + ": " + sequence1StartingPositions.get(i)
				+ " LENGTH: " + alignmentLengths.get(i));
		System.out.println(sequence2Alignments.get(i));
		System.out.println(sequence1Alignments.get(i));
	}

	private static void findThreeBestMatches(List<String> sequenceNames,
			List<Integer> sequenceScores) {
		int max1 = Integer.MIN_VALUE;
		int max2 = Integer.MIN_VALUE;
		int max3 = Integer.MIN_VALUE;
		String max1Name = "";
		String max2Name = "";
		String max3Name = "";
		int max1Index = -1;
		int max2Index = -1;
		int max3Index = -1;
		int score = 0;

		for (int i = 0; i < sequenceScores.size(); i++) {
			score = sequenceScores.get(i);

			if (score > max1) {
				max1 = score;
				max1Name = sequenceNames.get(i);
				max1Index = i;
			}
		}

		for (int i = 0; i < sequenceScores.size(); i++) {
			score = sequenceScores.get(i);

			if (score > max2 && !sequenceNames.get(i).equals(max1Name)) {
				max2 = score;
				max2Name = sequenceNames.get(i);
				max2Index = i;
			}
		}

		for (int i = 0; i < sequenceScores.size(); i++) {
			score = sequenceScores.get(i);

			if (score > max3 && !sequenceNames.get(i).equals(max1Name)
					&& !sequenceNames.get(i).equals(max2Name)) {
				max3 = score;
				max3Name = sequenceNames.get(i);
				max3Index = i;
			}
		}

		printMatchInfo(max1, max1Name, max1Index);
		System.out.println();
		printMatchInfo(max2, max2Name, max2Index);
		System.out.println();
		printMatchInfo(max3, max3Name, max3Index);
		System.out.println();
	}

	private static int max(int[][] matrixF, int n, int m) {
		int max = Integer.MIN_VALUE;

		for (int j = 0; j < m; j++) {
			for (int i = 0; i < n; i++) {
				if (matrixF[i][j] > max) {
					max = matrixF[i][j];
				}
			}
		}

		return max;
	}

	private static int maxJ(int[][] matrixF, int n, int m) {
		int max = Integer.MIN_VALUE;
		int maxJ = -1;

		for (int j = 0; j < m; j++) {
			for (int i = 0; i < n; i++) {
				if (matrixF[i][j] > max) {
					max = matrixF[i][j];
					maxJ = j;
				}
			}
		}

		return maxJ;
	}

	private static int maxI(int[][] matrixF, int n, int m) {
		int max = Integer.MIN_VALUE;
		int maxI = -1;

		for (int j = 0; j < m; j++) {
			for (int i = 0; i < n; i++) {
				if (matrixF[i][j] > max) {
					max = matrixF[i][j];
					maxI = i;
				}
			}
		}

		return maxI;
	}

	private static void reconstructAlignment(char[] seq1, char[] seq2, char[][] tracebackMatrix,
			int n, int m) {
		String seq1Alignment = "";
		String seq2Alignment = "";
		int length = 0;
		int i = n;
		int j = m;
		int seq1Index = n - 1;
		int seq2Index = m - 1;

		while (true) {
			if (tracebackMatrix[i][j] == 'D') {
				seq1Alignment += seq1[seq1Index];
				seq2Alignment += seq2[seq2Index];
				i--;
				j--;
				seq1Index--;
				seq2Index--;
			} else if (tracebackMatrix[i][j] == 'L') {
				seq1Alignment += seq1[seq1Index];
				seq2Alignment += "-";
				i--;
				seq1Index--;
			} else if (tracebackMatrix[i][j] == 'U') {
				seq1Alignment += "-";
				seq2Alignment += seq2[seq2Index];
				j--;
				seq2Index--;
			} else {
				sequence1StartingPositions.add(i + 1);
				sequence2StartingPositions.add(j + 1);
				alignmentLengths.add(length);
				sequence1Alignments.add(new StringBuilder(seq1Alignment).reverse().toString().substring(0,
						Math.min(60, seq1Alignment.length())));
				sequence2Alignments.add(new StringBuilder(seq2Alignment).reverse().toString().substring(0,
						Math.min(60, seq2Alignment.length())));

				return;
			}

			length++;
		}
	}

	private static char tracebackMax(int n1, int n2, int n3, int n4) {
		if (n1 >= n2 && n1 >= n3 && n1 >= n4) {
			return 'D';
		} else if (n2 >= n3 && n2 >= n4) {
			return 'L';
		} else if (n3 >= n4) {
			return 'U';
		} else {
			return 'N';
		}
	}

	private static int max(int n1, int n2, int n3, int n4) {
		return Math.max(Math.max(n1, n2), Math.max(n3, n4));
	}

	private static int getMaxAlignmentScore(String seq1, String seq2) {
		int d = 4;

		char[] seq1arr = seq1.toCharArray();
		char[] seq2arr = seq2.toCharArray();

		int n = seq1.length() + 1;
		int m = seq2.length() + 1;

		int[][] matrixF = new int[n][m];

		// Initialization
		matrixF[0][0] = 0;

		for (int i = 1; i < n; i++) {
			matrixF[i][0] = 0;
		}

		for (int j = 1; j < m; j++) {
			matrixF[0][j] = 0;
		}

		char[][] tracebackMatrix = new char[n][m];

		// Computation
		for (int j = 1; j < m; j++) {
			for (int i = 1; i < n; i++) {
				matrixF[i][j] = max(matrixF[i - 1][j - 1]
						+ Blosum.getDistance(seq1arr[i - 1], seq2arr[j - 1]),
						matrixF[i - 1][j] - d, matrixF[i][j - 1] - d, 0);

				// Traceback
				// Store pointer to cell from which maximal value was obtained
				tracebackMatrix[i][j] = tracebackMax(matrixF[i - 1][j - 1]
						+ Blosum.getDistance(seq1arr[i - 1], seq2arr[j - 1]),
						matrixF[i - 1][j] - d, matrixF[i][j - 1] - d, 0);
			}
		}

		// Traceback
		// Use pointers to reconstruct the alignment
		reconstructAlignment(seq1arr, seq2arr, tracebackMatrix, maxI(matrixF, n, m),
				maxJ(matrixF, n, m));

		// Termination
		return max(matrixF, n, m);
	}

	private static void parseFastaFile(String sequencePath, String fastaPath) {
		String seq1 = "";
		String seq2 = "";
		File sequence = new File(sequencePath);

		try {
			Scanner scanner = new Scanner(sequence);

			while (scanner.hasNextLine()) {
				seq1 += scanner.nextLine();
			}

			scanner.close();
		} catch (IOException e) {
			System.out.println("Error parsing sequence file");
		}

		File fasta = new File(fastaPath);
		boolean newSequence = false;
		String name = "";

		try {
			Scanner scanner = new Scanner(fasta);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				if (line.startsWith(">")) {
					if (!seq2.isEmpty()) {
						sequenceNames.add(name);
						sequenceScores.add(getMaxAlignmentScore(seq1, seq2));
					}

					newSequence = true;
					name = line.substring(line.lastIndexOf("|") + 1, line.indexOf("_") + 6);

					if (name.equals("ZN768_HUMAN")) {
						scanner.close();

						return;
					}
				} else {
					if (newSequence) {
						seq2 = "";
						newSequence = false;
					}

					seq2 += line;
				}
			}

			scanner.close();
		} catch (IOException e) {
			System.out.println("Error parsing FASTA file");
		}
	}

	public static void main(String[] args) {
		sequenceNames = new ArrayList<String>();
		sequenceScores = new ArrayList<Integer>();
		sequence1Alignments = new ArrayList<String>();
		sequence2Alignments = new ArrayList<String>();
		sequence1StartingPositions = new ArrayList<Integer>();
		sequence2StartingPositions = new ArrayList<Integer>();
		alignmentLengths = new ArrayList<Integer>();

		parseFastaFile(SEQUENCE_PATH, FASTA_PATH);
		findThreeBestMatches(sequenceNames, sequenceScores);

		System.out.println("End of program");
	}
}
