package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.io.File;

import edu.brandeis.cs.cs131.pa1.filter.CurrentWorkingDirectory;
/**
 * Implements the cd command. Causes shell to change directory if a valid directory path is entered.
 * @author jessebecklevisohn
 *
 */
public class ChangeDirectoryFilter extends ErrorHandling{
	
	private String dest;
	
	/**
	 * Constructor  calls the constructor of the errorHandling class with parameters cmd and 2.
	 * Checks the the method is called with the correct parameters. 
	 * @param cmd: This is a string representing a shell command. 
	 */
	public ChangeDirectoryFilter(String cmd) {
		super(cmd, 2);
		requiresParameter();
		this.dest = words[1];
	}
	
	
	/**
	 * Since, this method does not read input from an input pipe, 
	 * the process method is overriden, and processLine is never called.
	 * @param line, the line received from the input queue.
	 */
	@Override
	protected String processLine(String line) {
		return null;
	}
	
	
	/**
	 * This function executes the cd command to switch the currentWorkingDirectory to the dest.
	 * The function gets the full file path for the directory and creates the file directory. 
	 * It then checks if the dest is not a directory and throws an error if it is.
	 * If the dest is the special command, ".." and the parent exists, currentWorking Directory is set to the parent.
	 * If the dest is not the special command ".", the currentWorking Directory is set to dest.
	 */
	@Override
	public void process() {
		//Gets the full path to the directory
		String directory = CurrentWorkingDirectory.get() + CurrentWorkingDirectory.getPathSeparator();
		
		//If dest is the special command "..", get the file's parent directory
		if (dest.equals("..")) {
			File f = new File(directory);
			String parent = f.getParent();
			if (parent != null) {
				CurrentWorkingDirectory.setTo(parent);
			}
		
		// If the dest is a directory name, set the current working directory to that directory.
		} else if (!dest.equals(".")) {
			File f = new File(directory + dest);
			directoryNotFound(f);
			CurrentWorkingDirectory.setTo(directory + dest);
		}
		
		//The last case is if dest is ".". In this case, the current working directory stay the same.
	}


	@Override
	/**
	 * Checks for input and output errors.
	 */
	protected Boolean checkErrors() {
		cannotHaveInput();
		cannotHaveOutput(next);
		return errorFlag;
	}

}
