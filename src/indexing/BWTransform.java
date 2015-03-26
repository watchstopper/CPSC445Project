package indexing;


import java.util.Arrays;

public class BWTransform {
	String originalString;
	
	public BWTransform(String o){
		
		originalString = o;
	}
	
	
	// fun: generate all possible rotations of text
	// input: String (the original String)
	// return: array of strings
	public static String[] genRotations(String original){
		
		String[] s = new String[original.length()+1] ;
		s[0] = "$" + original;		
		for(int i=1; i<original.length()+1; i++){
			s[i] = s[i-1].substring(1) + s[i-1].charAt(0);
		}
		return s;
	}
	
	// fun: sort the array of rotations
	// input: array containing rotations
	// return: n/a
	public static void sortRotations(String[] rotations){
		Arrays.sort(rotations);
		for(int i=0; i<rotations.length; i++){
			System.out.println(rotations[i]);
		}
	}
	
	// fun: extract the BWT
	// input: array containing rotations
	// return: String (the BWT)
	public static String extractLastChar(String[] rotations){
		
		String BWT = "";
		for(int i=0; i<rotations.length; i++){
			BWT = BWT + rotations[i].charAt(rotations[i].length()-1);
		}
		System.out.println(BWT);
		return BWT;
	}
	
	// fun: based on the BWT, revert back to the original string
	// input: String (the BWT)
	// return: String (the original String)
	public static String revertOriginal(String bwt){
		String[] temp = new String[bwt.length()];
		
		for(int j=0; j<temp.length;j++){
			temp[j] = "" + bwt.charAt(j);
		}
		Arrays.sort(temp);
		
		for(int i=1; i<bwt.length(); i++){
			for(int j=0; j<temp.length;j++){
				temp[j] = bwt.charAt(j) + temp[j];
			}
			Arrays.sort(temp);
		}
		System.out.println();
		System.out.println("reverted array");
		for(int i=0; i<temp.length; i++){
			System.out.println(temp[i]);
		}
		
		return "";
	}
	
	// just some testing
	public static void main(String[] args){
		
		String test = "banana";
		String[] testRot = genRotations(test);
		sortRotations(testRot);
		String testBWT = extractLastChar(testRot);
		
		System.out.println(testBWT);
		
		revertOriginal(testBWT);
		
	}
}



