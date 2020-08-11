import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class ReadFile implements Callable<Data> {
	File fRead;
	int totalWords=0;
	public  ReadFile(File fRead) { 
		this.fRead=fRead;
	}
	public Data call() {
		Scanner scanner=null;
		try {
			scanner = new Scanner(fRead);
			while(scanner.hasNextLine()) {
				String fileLine=scanner.nextLine();	
				String[] allWords= fileLine.split(" ");	
				totalWords+=allWords.length;	
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		return new Data(fRead.getName(),totalWords);  
	}
	 
}
