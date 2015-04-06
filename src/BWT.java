import java.util.ArrayList;
import java.util.Arrays;

public class BWT {
	// FUNCTION: Generate all possible rotations of a string.
	// INPUT: String (the original string)
	// RETURN: Array of strings
	private static String[] generateRotations(String s) {
		String[] rotations = new String[s.length() + 1];
		rotations[0] = "$" + s;		

		for (int i = 1; i < s.length() + 1; i++) {
			rotations[i] = rotations[i - 1].substring(1) + rotations[i - 1].charAt(0);
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

	// FUNCTION: Revert the BWT back to the original string.
	// INPUT: String (BWT)
	// RETURN: String (the original string)
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

	public static void main(String[] args) {
		// Test code
		String s = "mississippi";
		String[] rotations = generateRotations(s);
		sortRotations(rotations);
		String bwt = extractLastColumn(rotations);
		String firstColumn = extractFirstColumn(rotations);
		revertBWT(bwt);

		ArrayList<CharOccurrence> countTable = new ArrayList<CharOccurrence>();
		countTable = BackwardSearch.produceCharOccurrenceTable(firstColumn);
		ArrayList<Integer> saRange = new ArrayList<Integer>();
		String query = "iss";
		saRange = BackwardSearch.getSARange(query, countTable, bwt);

		for (String r : rotations) {
			System.out.println(r);
		}

		System.out.println();

		for (int i = 0; i < saRange.size(); i++) {
			System.out.println(saRange.get(i));
		}
	}
}
