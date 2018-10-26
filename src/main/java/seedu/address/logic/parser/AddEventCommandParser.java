package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VENUE;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;
import seedu.address.model.event.EventStartTime;
import seedu.address.model.event.EventVenue;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddEventCommand object
 */
public class AddEventCommandParser implements Parser<AddEventCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddEventCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DATE, PREFIX_VENUE, PREFIX_START_TIME, PREFIX_TAG);
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_DATE, PREFIX_VENUE, PREFIX_START_TIME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
        }
        EventName eventName = ParserUtil.parseEventName(argMultimap.getValue(PREFIX_NAME).get());
        EventDate eventDate = ParserUtil.parseEventDate(argMultimap.getValue(PREFIX_DATE).get());
        EventVenue eventVenue = ParserUtil.parseEventVenue(argMultimap.getValue(PREFIX_VENUE).get());
        EventStartTime eventStartTime = ParserUtil.parseEventStartTime(argMultimap.getValue(PREFIX_START_TIME).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        Event event = new Event(eventName, eventDate, eventVenue, eventStartTime, tagList);
        return new AddEventCommand(event);
    }
    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
