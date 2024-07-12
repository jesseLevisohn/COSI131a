package edu.brandeis.cs.cs131.pa1.filter.sequential;
/**
 * Implements the Uniq command. Removes any line from the piped input that is the same as the previous line.
 * @author jessebecklevisohn
 *
 */
public class UniqFilter extends ErrorHandling{
	
	private String previous;
	
	/**
	 * Constructor calls the constructor of the errorHandling class with parameter cmd. 
	 * @param cmd: This is a string representing a shell command. 
	 */
	public UniqFilter(String cmd) {
		super(cmd);
	}

	@Override
	/**
	 * Returns the line unless the line is the same as the previous line. 
	 * If the line is the same as the previous line, null is returned.
	 * @param line, a line of input
	 * @return, line is the line is unique, null otherwise.
	 */
	protected String processLine(String line) {
		if(this.previous == null || !this.previous.equals(line)) {
			this.previous = line;
			return line;
		}
		return null;
	}

	@Override
	/**
	 * Checks for I/O errors.
	 * @return, true if an error was thrown
	 */
	protected Boolean checkErrors() {
		requiresInput(prev);
		return errorFlag;
	}
}
