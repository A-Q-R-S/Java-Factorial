import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceManager {
	private File 	bufferedFile = null;
	
	public ResourceManager () throws FileNotFoundException {
		bufferedFile = new File("D:/Java_Workspace/Factorial-v0.0/src/resources.txt");
	}
	
	public String getResource (String resource) {
		String result = ""; 
		try {
			Scanner fileIn = new Scanner (bufferedFile);
			boolean found = false;
			
			while (fileIn.hasNextLine() && !found) {
				fileIn.nextLine();
				if (fileIn.findInLine (resource) != null) {
					found = true;
					String line = fileIn.nextLine();
					String lineArr[] = line.split("%");
					result = lineArr[lineArr.length-1];
				}
			}
			
			fileIn.close();
			
		} catch (FileNotFoundException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		}
		
		return result;
	}
}
