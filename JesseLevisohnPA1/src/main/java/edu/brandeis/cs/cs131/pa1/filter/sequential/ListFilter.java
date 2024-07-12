package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.io.File;

import edu.brandeis.cs.cs131.pa1.filter.CurrentWorkingDirectory;
/**
 * Implements ls. Lists the files in the current working directory
 * @author jessebecklevisohn
 *
 */
public class ListFilter extends ErrorHandling{
	
	private String CWD;
	
	/**
	 * Constructor for ListFilter. Calls the constructor of the errorHandling class with the parameters cmd. 
	 * The method then sets the field CWD to the current working directory.
	 * @param cmd: This is a string representing a shell command. 
	 */
	public ListFilter(String cmd) {
		super(cmd);
		this.CWD = CurrentWorkingDirectory.get();
	}

	/**
	 * This method will never run because the process method is overriden.
	 */
	@Override
	protected String processLine(String line) {
		return null;
	}
	
	@Override
	/**
	 * Overrides the sequentialFilter class' process method. The method throws an error if it was given an input.
	 * It then lists the files in the names of the files in the directory and writes them to the output pipe.
	 */
	public void process() {
		File dir = new File(CWD);
		String[] fileList = dir.list();
		for (String f : fileList)	{
			output.write(f);
		}
	}

	@Override
	/**
	 * Checks for I/O errors
	 */
	protected Boolean checkErrors() {
		cannotHaveInput();
		return errorFlag;
	}
	
}
