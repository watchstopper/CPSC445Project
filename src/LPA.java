public class LPA {
	private static String sequence1Alignment = "";
	private static String sequence2Alignment = "";

	private static final int A = 1;
	private static final int B = -3;
	private static final int G = 5;
	private static final int S = 2;
	private static final int MIN_VALUE = -50000;

	private static int getDistance(char c1, char c2) {
		if (c1 == c2) {
			return A;
		} else {
			return B;
		}
	}

	private static int maxScore(int[][] matrix, int d, int m) {
		int max = Integer.MIN_VALUE;

		for (int j = 0; j < m; j++) {
			for (int i = 0; i < d; i++) {
				if (matrix[i][j] > max) {
					max = matrix[i][j];
				}
			}
		}

		return max;
	}

	private static int maxI(int[][] matrix, int d, int m) {
		int max = Integer.MIN_VALUE;
		int maxI = 0;

		for (int j = 0; j < m; j++) {
			for (int i = 0; i < d; i++) {
				if (matrix[i][j] > max) {
					max = matrix[i][j];
					maxI = i;
				}
			}
		}

		return maxI;
	}

	private static int maxJ(int[][] matrix, int d, int m) {
		int max = Integer.MIN_VALUE;
		int maxJ = 0;

		for (int j = 0; j < m; j++) {
			for (int i = 0; i < d; i++) {
				if (matrix[i][j] > max) {
					max = matrix[i][j];
					maxJ = j;
				}
			}
		}

		return maxJ;
	}

	private static void reconstructAlignment(char[] sequence1, char[] sequence2,
			int[][] matrixM_1, int[][] matrixM_2, int[][] matrixM_3,
			int maxScoreMatrix, int maxI, int maxJ) {
		int i = maxI;
		int j = maxJ;
		int sequence1Index = maxI - 1;
		int sequence2Index = maxJ - 1;

		while (true) {
			if (maxScoreMatrix == 1 && i > 0 && j > 0) {
				sequence1Alignment += sequence1[sequence1Index];
				sequence2Alignment += sequence2[sequence2Index];
				i--;
				j--;
				sequence1Index--;
				sequence2Index--;
			} else if (maxScoreMatrix == 2 && i > 0 && j > 0) {
				sequence1Alignment += sequence1[sequence1Index];
				sequence2Alignment += "-";
				i--;
				sequence1Index--;
			} else if (maxScoreMatrix == 3 && i > 0 && j > 0) {
				sequence1Alignment += "-";
				sequence2Alignment += sequence2[sequence2Index];
				j--;
				sequence2Index--;
			} else {
				sequence1Alignment = new StringBuilder(sequence1Alignment).reverse().toString();
				sequence2Alignment = new StringBuilder(sequence2Alignment).reverse().toString();

				return;
			}

			if (matrixM_1[i][j] >= matrixM_2[i][j] && matrixM_1[i][j] >= matrixM_3[i][j]
					&& matrixM_1[i][j] >= 0) {
				maxScoreMatrix = 1;
			} else if (matrixM_2[i][j] >= matrixM_3[i][j] && matrixM_2[i][j] >= 0) {
				maxScoreMatrix = 2;
			} else if (matrixM_3[i][j] >= 0) {
				maxScoreMatrix = 3;
			} else {
				maxScoreMatrix = 0;
			}
		}
	}

	public static int getMaxAlignmentScore(String sequence1, String sequence2) {
		char[] sequence1arr = sequence1.toCharArray();
		char[] sequence2arr = sequence2.toCharArray();

		int d = sequence1.length() + 1;
		int m = sequence2.length() + 1;

		int[][] matrixM = new int[d][m];
		int[][] matrixM_1 = new int[d][m];
		int[][] matrixM_2 = new int[d][m];
		int[][] matrixM_3 = new int[d][m];

		// Initialization
		for (int j = 0; j < m; j++) {
			matrixM[0][j] = 0;
		}

		for (int i = 1; i < d; i++) {
			matrixM_1[i][0] = -(G + i * S);
		}

		for (int j = 0; j < m; j++) {
			matrixM_2[0][j] = MIN_VALUE;
		}

		for (int i = 1; i < d; i++) {
			matrixM_3[i][0] = MIN_VALUE;
		}

		// Computation
		for (int j = 1; j < m; j++) {
			for (int i = 1; i < d; i++) {
				matrixM_1[i][j] = matrixM[i - 1][j - 1]
						+ getDistance(sequence1arr[i - 1], sequence2arr[j - 1]);
				matrixM_2[i][j] = Math.max(matrixM_2[i - 1][j] - S,
						matrixM[i - 1][j] - (G + S));
				matrixM_3[i][j] = Math.max(matrixM_3[i][j - 1] - S,
						matrixM[i][j - 1] - (G + S));
				matrixM[i][j] = Math.max(Math.max(matrixM_1[i][j], matrixM_2[i][j]),
						Math.max(matrixM_3[i][j], 0));
			}
		}

		// Traceback
		// Determine which matrix to start the traceback from
		int maxScoreMatrix = 0;
		int maxI = 0;
		int maxJ = 0;

		if (maxScore(matrixM_1, d, m) >= maxScore(matrixM_2, d, m)
				&& maxScore(matrixM_1, d, m) >= maxScore(matrixM_3, d, m)
				&& maxScore(matrixM_1, d, m) >= 0) {
			maxScoreMatrix = 1;
			maxI = maxI(matrixM_1, d, m);
			maxJ = maxJ(matrixM_1, d, m);
		} else if (maxScore(matrixM_2, d, m) >= maxScore(matrixM_3, d, m)
				&& maxScore(matrixM_2, d, m) >= 0) {
			maxScoreMatrix = 2;
			maxI = maxI(matrixM_2, d, m);
			maxJ = maxJ(matrixM_2, d, m);
		} else if (maxScore(matrixM_3, d, m) >= 0) {
			maxScoreMatrix = 3;
			maxI = maxI(matrixM_3, d, m);
			maxJ = maxJ(matrixM_3, d, m);
		}

		// Start traceback
		reconstructAlignment(sequence1arr, sequence2arr, matrixM_1,
				matrixM_2, matrixM_3, maxScoreMatrix, maxI, maxJ);

		// Termination
		return maxScore(matrixM, d, m);
	}

	public static String getSequence1Alignment() {
		return sequence1Alignment;
	}

	public static String getSequence2Alignment() {
		return sequence2Alignment;
	}
}
