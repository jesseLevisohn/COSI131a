package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.util.ArrayList;

public class TailFilter extends ErrorHandling{
	
	private int maxLines = 10;
	private ArrayList<String> inputLines;
	
	/**
	 * Constructor calls the constructor of the errorHandling class with parameter cmd. 
	 * The method also initializes the field inputLines.
	 * @param cmd: This is a string representing a shell command. 
	 */
	public TailFilter(String cmd) {
		super(cmd);
		this.inputLines = new ArrayList<String>();
	}

	@Override
	/**
	 * This method is never used since the process method is overriden.
	 */
	protected String processLine(String line) {
		return null;
	}
	
	@Override
	/**
	 * Overrides the process method. This method reads the entire input into a stack line by line.
	 * If there are less than ten lines, all the lines are sent to the output pipe.
	 * Otherwise, the last ten lines are sent to the output pipe.
	 */
	public void process() {
		// Adds all the lines in the input to an arrayList
		while (!input.isEmpty()) {
			String line = input.read();
			inputLines.add(line);
		}
		int size = this.inputLines.size();
		
		// If the size is 10 or smaller, all the lines are sent to the output pipe.
		if (size <= maxLines) {
			for (String line : this.inputLines) {
				output.write(line);
			}
		
		// If there are more than 10 lines, the last 10 lines are sent to the output pipe.
		} else {
			for (int i = 0; i < maxLines; i ++) {
				output.write(this.inputLines.get(size - maxLines + i));
			}
		}
	}

	@Override
	/**
	 * Checks for I/O errors.
	 * @return true if an error was thrown
	 */
	protected Boolean checkErrors() {
		requiresInput(prev);
		return errorFlag;
	}

}
