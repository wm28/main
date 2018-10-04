package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;


import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RemoveTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author aaryamNUS

/**
 * Parses input arguments and creates a new {@code RemoveTagCommand} object
 */
public class RemoveTagCommandParser implements Parser<RemoveTagCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code RemoveTagCommand}
     * and returns a {@code RemoveTagCommand} object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public RemoveTagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        String tag = argumentMultimap.getValue(PREFIX_TAG).orElse("");

        return new RemoveTagCommand(tag);
    }
}
