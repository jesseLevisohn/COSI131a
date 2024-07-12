/**
 * 
 */
package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.io.File;

import edu.brandeis.cs.cs131.pa1.filter.Filter;
import edu.brandeis.cs.cs131.pa1.filter.Message;

/**
 * @author jessebecklevisohn
 * This class provides the implementation of each of the errors that we need to handle.
 * The class is created to provide better code reuse of the error messages.
 * since every command utilizes the same error messages and conditions.
 */
public abstract class ErrorHandling extends SequentialFilter{
	
	protected int numWords = 0; 
	protected String cmd;
	protected String[] words;
	protected Boolean errorFlag = false;
	
	/**
	 * Constructor with no parameters
	 */
	public ErrorHandling() {
		super();
	}
	
	/**
	 * Constructor with one parameters, the command. This is used when the command 
	 * has no parameters but might throw an error message.
	 * @param cmd, the command.
	 */
	public ErrorHandling(String cmd) {
		super();
		this.cmd = cmd;
	}
	
	/**
	 * Constructor with two parameters, the command and the number of words. This is used when the command 
	 * has a parameter.
	 * @param cmd, the command
	 * @param words, the number of words the command should have
	 */
	public ErrorHandling(String cmd, int words) {
		super();
		this.numWords = words;
		this.cmd = cmd;
		this.words = this.cmd.split(" ");
		
	}
	
	/**
	 * Error message for when the command is missing a parameter.
	 */
	public void requiresParameter() {
		if (words.length != numWords) {
			System.out.print(Message.REQUIRES_PARAMETER.with_parameter(cmd));
			errorFlag = true;
		}
	}
	
	/**
	 * Error message for when the command has illegal input.
	 */
	public void cannotHaveInput() {
		if (input != null && !input.isEmpty()) {
			System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter(cmd));
			errorFlag = true;
		}
	}
	
	
	/**
	 * Error message for when the command has illegal input.
	 */
	public void cannotHaveOutput(Filter next) {
		if (next != null && !(next instanceof PrintFilter)) {
			System.out.print(Message.CANNOT_HAVE_OUTPUT.with_parameter(cmd));
			errorFlag = true;
		}
	}
	
	/**
	 * Error message for when the command needs input but none is given.
	 */
	public void requiresInput(Filter prev) {
		if (prev == null) {
			System.out.print(Message.REQUIRES_INPUT.with_parameter(cmd));
			errorFlag = true;
		}
	}
	
	/**
	 * Method that throws the directory not found exception.
	 * @param f
	 */
	public void directoryNotFound(File f) {
		if (!f.exists() || !f.isDirectory()) {
			System.out.print(Message.DIRECTORY_NOT_FOUND.with_parameter(cmd));
			errorFlag = true;
		}
	}
	
	/**
	 * This method will be run between when the command is read and when process is 
	 * called on each of the commands. It will check if the input and output 
	 * restrictions on the commands are met.
	 */
	protected abstract Boolean checkErrors();
	
}
