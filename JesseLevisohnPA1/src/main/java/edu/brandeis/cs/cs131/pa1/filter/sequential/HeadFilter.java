package edu.brandeis.cs.cs131.pa1.filter.sequential;
/**
 * This class implements the head filter.
 * Returns up to the first 10 lines from piped input.
 * @author jessebecklevisohn
 *
 */
public class HeadFilter extends ErrorHandling{
	
	private int count = 0;
	
	/**
	 * Constructor  calls the constructor of the errorHandling class with parameters cmd. 
	 * @param cmd: This is a string representing a shell command. 
	 */
	public HeadFilter(String cmd) {
		super(cmd);
	}

	/**
	 * The method implements the processLine method inherited from sequentialFilter.
	 * The method returns the line if the there have been less than 10 lines read from the input.
	 * @param line, a string.
	 * @return line if count is less than 10, else null.
	 */
	@Override
	protected String processLine(String line) {
		if (count < 10) {
			this.count ++;
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
