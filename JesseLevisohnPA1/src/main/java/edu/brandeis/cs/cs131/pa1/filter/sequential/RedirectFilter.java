package edu.brandeis.cs.cs131.pa1.filter.sequential;


import java.io.FileWriter;
import java.io.IOException;

import edu.brandeis.cs.cs131.pa1.filter.CurrentWorkingDirectory;
import edu.brandeis.cs.cs131.pa1.filter.Message;

/**
 * Implements the redirect filter. > redirects the output of <cmd1> to be stored in <file>.
 * @author jessebecklevisohn
 *
 */
public class RedirectFilter extends ErrorHandling{
	
	private String fname;
	/**
	 * Constructor for RedirectFilter. Calls the constructor of the errorHandling class with the parameters cmd and 2. 
	 * The method then checks whether the command has the correct parameter.
	 * Saves the full file name for the output as the field fname.
	 * @param cmd: This is a string representing a shell command. 
	 */
	public RedirectFilter(String cmd) {
		super(cmd, 2);
		requiresParameter();
		this.fname = CurrentWorkingDirectory.get() + CurrentWorkingDirectory.getPathSeparator() + words[1];
	}

	@Override
	/**
	 * This method is never used because the process method of the sequentialFilter class is overriden here.
	 */
	protected String processLine(String line) {
		return null;
	}
	
	@Override
	/**
	 * This method overrides the process method from sequentialFilter.
	 * It loops over all the lines in the input pipe and writes each one to the file specified by the parameter.
	 */
	public void process() { 
		try {
			FileWriter writer = new FileWriter(this.fname);
			while (!input.isEmpty()) {
				String line = input.read();
				writer.write(line + "\n");
			}
			writer.close();
		} catch (IOException e) {
			System.out.print(Message.FILE_NOT_FOUND.with_parameter(cmd));
			errorFlag = true;
		}
	
	}

	@Override
	/**
	 * Checks for I/O errors.
	 * @return, true if an error was thrown.
	 */
	protected Boolean checkErrors() {
		requiresInput(prev);
		cannotHaveOutput(next);
		return errorFlag;
	}
	
}
