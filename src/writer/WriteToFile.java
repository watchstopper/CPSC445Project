package writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteToFile {

		public static void writeNewTxtFile(ArrayList<String> los, String fn) throws IOException{
			BufferedWriter writer = new BufferedWriter(new FileWriter(fn));
			try {
				for(int i=0;i<los.size();i++){
					writer.write(los.get(i));
					writer.newLine();
					writer.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				writer.close();
			}
			
		}
	
	
	
	
	
	
}
