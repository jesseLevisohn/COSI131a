package edu.brandeis.cs.cs131.pa1.filter.sequential;

/**
 * A class that implements the word count filter, wc.
 * @author jessebecklevisohn
 *
 */
public class WordCountFilter extends ErrorHandling{
	
	private int lines;
	private int words;
	private int chars;
	
	/**
	 * Constructor for WordCountFilter
	 */
	public WordCountFilter(String cmd) {
		super(cmd, 1);
	}
	
	
	@Override
	/**
	 * This method will never be used because the process method is overriden.
	 * @param line
	 * @return null
	 */
	protected String processLine(String line) {
		return null;
	}
	
	@Override
	/**
	 * This method overrides the process method in the sequentialFIlter class 
	 * so that all of the lines, words, and chars, can be counted in all the lines of the input.
	 */
	public void process() {
		while (!input.isEmpty()) {
			String line = input.read();
			this.lines ++;
			countWords(line);
			countChars(line);
		}
		output.write(this.lines + " " + this.words + " " + this.chars);
	}
	
	/**
	 * Counts the words in the file that is piped into the filter
	 */
	public void countWords(String line) {
		String[] wordsInLine = line.split(" ");
		this.words += wordsInLine.length;
	}
	
	/**
	 * Counts the characters in the file that is piped into the filter
	 */
	public void countChars(String line) {
		this.chars += line.length();
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
