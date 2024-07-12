package edu.brandeis.cs.cs131.pa1.filter.sequential;

import edu.brandeis.cs.cs131.pa1.filter.CurrentWorkingDirectory;

/**
 * Implements the pwd command. Returns current working directory.
 * @author jessebecklevisohn
 *
 */
public class WorkingDirectoryFilter extends ErrorHandling{

	/**
	 * Constructor of Working Directory calls the constructor of the errorHandling class with parameter cmd. 
	 * The method then checks whether the command has the correct input.
	 * @param cmd: This is a string representing a shell command. 
	 */
	public WorkingDirectoryFilter(String cmd) {
		super(cmd);
	}

	@Override
	/**
	 * This method cannot have input so this method will never be used.
	 */
	protected String processLine(String line) {
		return null;
	}
	
	@Override
	/**
	 * Overrides the process method of the sequentialFilter class.
	 * Writes the name of the current working directory to the output pipe.
	 */
	public void process() {
		output.write(CurrentWorkingDirectory.get());
	}

	@Override
	/**
	 * Checks for I/O errors
	 * @return, true if an error was thrown
	 */
	protected Boolean checkErrors() {
		cannotHaveInput();
		return errorFlag;
	}
}
