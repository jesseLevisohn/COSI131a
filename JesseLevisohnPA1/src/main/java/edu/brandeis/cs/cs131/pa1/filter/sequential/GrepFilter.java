package edu.brandeis.cs.cs131.pa1.filter.sequential;
/**
 * Implements the grep filter.
 * Returns all lines from piped input that contain <query>.
 * @author jessebecklevisohn
 *
 */
public class GrepFilter extends ErrorHandling {
	
	private String query;
	
	/**
	 * Constructor calls the constructor of the errorHandling class with parameters cmd, and 2. 
	 * The method then checks whether the command has the correct input and parameters.
	 * @param cmd: This is a string representing a shell command. 
	 */
	public GrepFilter(String cmd) {
		super(cmd, 2);
		requiresParameter();
		this.query = words[1];
	}
	
	
	/**
	 * The method implements the processLine method inherited from sequentialFilter.
	 * The method returns the line if the line contains the query and null otherwise.
	 * @param line, a string to be checked if it contains query.
	 * @return line if it contains query, else, null.
	 */
	@Override
	protected String processLine(String line) {
		if (line.contains(query)) {
			return line;
		}
		return null;
	}


	@Override
	/**
	 * Checks for I/O errors.
	 */
	protected Boolean checkErrors() {
		requiresInput(prev);
		return errorFlag;
	}
}
