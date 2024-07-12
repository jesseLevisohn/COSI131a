package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.util.List;
import java.util.Scanner;

import edu.brandeis.cs.cs131.pa1.filter.Message;

/**
 * The main implementation of the REPL loop (read-eval-print loop). It reads
 * commands from the user, parses them, executes them and displays the result.
 */
public class SequentialREPL {
	
	/**
	 * The main method that will execute the REPL loop
	 * 
	 * @param args not used
	 */
	public static void main(String[] args){

		Scanner consoleReader = new Scanner(System.in);
		System.out.print(Message.WELCOME);

		while (true) {
			System.out.print(Message.NEWCOMMAND);

			// read user command, if its just whitespace, skip to next command
			String cmd = consoleReader.nextLine();
			if (cmd.trim().isEmpty()) {
				continue;
			}

			// exit the REPL if user specifies it
			if (cmd.trim().equals("exit")) {
				break;
			}
			try {
				// Creates a list of the commands in the line and links them to each other.
				List<SequentialFilter> cmdList = SequentialCommandBuilder.createFiltersFromCommand(cmd);
				SequentialCommandBuilder.linkFilters(cmdList);
				Boolean thrown = false;
				
				//Iterates over the list of filters and processes each one.
				for (SequentialFilter filter : cmdList) {
					
					// Checks whether the file exists that is the parameter of the cat filter.
					if (filter instanceof CatFilter) {
						thrown = ((CatFilter) filter).errorFlag;
					}
					
					// Checks whether there were any other errors.
					if (filter instanceof ErrorHandling) {
						thrown = ((ErrorHandling) filter).checkErrors();
					}	
					if (thrown) {
						break;
					}
					filter.process();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		System.out.print(Message.GOODBYE);
		consoleReader.close();

	}

}
