package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.brandeis.cs.cs131.pa1.filter.CurrentWorkingDirectory;
import edu.brandeis.cs.cs131.pa1.filter.Message;

/**
 * Implements the cat filter. This command reads <file>’s contents into pipe or STDOUT.
 * @author jessebecklevisohn
 *
 */
public class CatFilter extends ErrorHandling{
	
	private FileReader reader;
	private BufferedReader br;
	
	
	/**
	 * Constructor for CatFilter. Calls the constructor of the errorHandling class with the parameters cmd and 2. 
	 * The method then checks whether the command has the correct parameter and gets the full path name for the file.
	 * Initializes a FileReader and BufferedReader to read the file. 
	 * @param cmd: This is a string representing a shell command. 
	 */
	public CatFilter(String cmd){
		super(cmd, 2);
		requiresParameter();
		String fname = CurrentWorkingDirectory.get() + CurrentWorkingDirectory.getPathSeparator() + words[1];
		try {
			this.reader = new FileReader(fname);
			this.br = new BufferedReader(reader);
		} catch (FileNotFoundException e) {
			System.out.print(Message.FILE_NOT_FOUND.with_parameter(cmd));
			errorFlag = true;
		}
	}
	
	/**
	 * This method is never accessed because the process method in the sequentialFilter class is overrriden.
	 */
	@Override
	protected String processLine(String line) {
		return null;
	}
	
	/**
	 * This method overrides the process method in the sequentialFilter class
	 * because there is no input to the cat filter. The method reads one line
	 * of the file at a time and writes the line to the output pipe.
	 */
	@Override
	public void process() {
		try {
			String l = br.readLine();
			while (l != null) {
	            output.write(l);
	            l = br.readLine();
	        }
			reader.close();	
		} catch (IOException e){
			System.out.print(Message.FILE_NOT_FOUND.with_parameter(cmd));
			errorFlag = true;
		}
	}

	@Override
	/**
	 * Checks for input and output errors.
	 */
	protected Boolean checkErrors() {
		cannotHaveInput();
		return errorFlag;
	}

}
