package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.EmailSpecificCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

//@@author aaryamNUS
/**
 * Parses input arguments and creates a new EmailSpecificCommand object
 */
public class EmailSpecificCommandParser implements Parser<EmailSpecificCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EmailSpecificCommand
     * and returns an EmailSpecificCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EmailSpecificCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        if (!EmailSpecificCommandParser.arePrefixesPresent(argMultimap, PREFIX_TAG)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailSpecificCommand.MESSAGE_USAGE));
        }

        Set<Tag> tagsOfGuests = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        return new EmailSpecificCommand(tagsOfGuests);
    }

    /**
     * Returns true if the tag prefix does not return empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
