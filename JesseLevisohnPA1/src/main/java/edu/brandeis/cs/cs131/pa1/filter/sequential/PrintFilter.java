package edu.brandeis.cs.cs131.pa1.filter.sequential;
/**
 * Implements the print filter. Prints the output to STDOUT at the end of a command 
 * unless the output is redirected to a file.
 * @author jessebecklevisohn
 *
 */
public class PrintFilter extends SequentialFilter{
	
	
	/**
	 * Constructor for PrintFilter. Calls the constructor of the sequentialFilter class.
	 * @param cmd: This is a string representing a shell command. 
	 */
	public PrintFilter() {
		super();
	}

	@Override
	/**
	 * The process method of the sequential filter method is overriden so this method will never be called.
	 */
	protected String processLine(String line) {
		return null;
	}
	
	@Override
	/**
	 * Prints out everything in the input pipe.
	 */
	public void process() {
		while (!input.isEmpty()) {
			System.out.println(input.read());
		}
	}

}
