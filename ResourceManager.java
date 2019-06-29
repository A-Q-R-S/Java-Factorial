import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceManager {
	final private File 	bufferedFile;
	
	public ResourceManager () throws FileNotFoundException {
		bufferedFile = new File(System.getProperty("user.dir") + "/src/resources.txt");
		//bufferedFile = new File("D:/Java_Workspace/Factorial-v0.0/src/resources.txt");
	}
	
	public String getResource (final String resName) {
		String result = ""; 
		try {
			if (bufferedFile == null) {
				throw new FileNotFoundException("Buffered resources file not initialized!");
			}
			
			final Scanner fileIn = new Scanner (bufferedFile);
			boolean found = false;
			
			while (fileIn.hasNextLine() && !found)
			{
				fileIn.nextLine();
				if (fileIn.findInLine (resName) != null)
				{
					final String line = fileIn.nextLine();
					final String lineArr[] = line.split("%");
					result = lineArr[lineArr.length-1];
					found = true;
				}
			}
			
			fileIn.close();
			
		} catch (FileNotFoundException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		}
		
		return result;
	}
}
