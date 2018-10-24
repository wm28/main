package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.Arrays;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ContainsKeywordsPredicate;

//@@author Sarah
/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns an FilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        String[] keywords = trimmedArgs.split("\\s+");

        ArrayList<String> checking = new ArrayList<>(Arrays.asList(keywords));

        for (int i = 0; i < checking.size(); i++) {

            if ((checking.get(i).charAt(0) == 'n' || checking.get(i).charAt(0) == 'e'
                    || (checking.get(i).charAt(0) == 'p' && checking.get(i).charAt(1) == '/')
                    || (checking.get(i).charAt(0) == 'p' && checking.get(i).charAt(1) == 'a')
                    || checking.get(i).charAt(0) == 'a' || checking.get(i).charAt(0) == 't')
                    && (checking.get(i).charAt(1) == '/' || checking.get(i).charAt(2) == '/')) {

                return new FilterCommand(new ContainsKeywordsPredicate(Arrays.asList(keywords)));
            } else {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
            }
        }
        return new FilterCommand(new ContainsKeywordsPredicate(Arrays.asList(keywords)));
    }
}
