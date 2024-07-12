package edu.brandeis.cs.cs131.pa1.filter.sequential;

import edu.brandeis.cs.cs131.pa1.filter.Message;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * This class manages the parsing and execution of a command. It splits the raw
 * input into separated subcommands, creates subcommand filters, and links them
 * into a list.
 */
public class SequentialCommandBuilder {
	
	
	/**
	 * This method split the input by the pipe "|".
	 * It then iterates over the array of commands and construct a filter from each of them.
	 * Each of these filters is added to a LinkedList of filters.
	 * @param command, a line that the user enters into the Unix-ish shell.
	 * @return, a list of the sequential filters that were included in the input string.
	 */
	public static List<SequentialFilter> createFiltersFromCommand(String command) {
		List<SequentialFilter> filters = new LinkedList<SequentialFilter>();
		// split the commands on pipes
		String[] commands = command.split("\\|");
		
		// This flags whether the a printFilter will be needed at the end of the list of subcommands.
		Boolean printFlag = true;
		
		for (int i = 0; i < commands.length; i++) {
			String cmd = commands[i].trim();
			
			// If the command contains a redirect, the command is split into two sub-commands around the redirect.
			if (cmd.contains(">")){
				int index = cmd.indexOf('>');
				
				// If the redirect is the first command in the line, a requires input message is generated.
				if (index < 2) {
					System.out.print(Message.REQUIRES_INPUT.with_parameter(command));
					return null;
				} 
				
				//The first sub-command is added to the list
				String subcmd1 = cmd.substring(0, index);
				filters.add(constructFilterFromSubCommand(subcmd1));
				
				//The second sub-command is generated
				cmd = cmd.substring(index);
				
				// If the redirect was the last command, the flag for whether to append a print filter switches to false.
				if (i == commands.length - 1) {
					printFlag = false;
				}
			}
			filters.add(constructFilterFromSubCommand(cmd));
		}
		
		// Add a printFilter if the command did not end in a redirect
		if (printFlag) {
			filters.add(new PrintFilter());
		}
		
		return filters;
	}

	/**
	 * This method constructs a filter from a subcommand.
	 * It checks what the leading word of the subcommand is and creates the correct kind of filter.
	 * Otherwise, the COMMAND_NOT_FOUND message is generated.
	 * @param subCommand
	 * @return
	 */
	private static SequentialFilter constructFilterFromSubCommand(String subCommand) {
		String[] words = subCommand.split(" ");
		String cmd = words[0];
		
		if (cmd.equals("cat")) {
			return new CatFilter(subCommand);
		} else if (cmd.equals("cd")) {
			return new ChangeDirectoryFilter(subCommand);
		} else if (cmd.equals("grep")) {
			return new GrepFilter(subCommand);
		} else if (cmd.equals("head")) {
			return new HeadFilter(subCommand);
		} else if (cmd.equals("ls")) {
			return new ListFilter(subCommand);
		} else if (cmd.equals("tail")) {
			return new TailFilter(subCommand);
		} else if (cmd.equals("uniq")) {
			return new UniqFilter(subCommand);
		} else if (cmd.equals("wc")) {
			return new WordCountFilter(subCommand);
		} else if (cmd.equals("pwd")) {
			return new WorkingDirectoryFilter(subCommand);
		} else if (cmd.equals(">")) {
			return new RedirectFilter(subCommand);
		} else {
			System.out.print(Message.COMMAND_NOT_FOUND.with_parameter(subCommand));
			return null;
		}
		
	}
	
	/**
	 * This method links the filters in a list of filters together so that the output of one filter
	 * is piped to the input of the next sequential filter.
	 * @param filters
	 */
	public static void linkFilters(List<SequentialFilter> filters) {
		Iterator<SequentialFilter> i = filters.iterator();
		SequentialFilter curr = (SequentialFilter) i.next();
		while (i.hasNext()) {
			SequentialFilter next = (SequentialFilter) i.next();
			curr.setNextFilter(next);
			next.setPrevFilter(curr);
			curr = next;
		}
	}
}
