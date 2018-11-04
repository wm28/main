package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.MailCommand;

/**
 * The following tests cover all paths for the parse command MailCommandParser, as well as
 * corner cases where the first argument is valid but the second argument is gibberish, for example
 * in the command: 'email 1 garbage'
 */
public class MailCommandParserTest {

    private MailCommandParser parser = new MailCommandParser();

    @Test
    public void parse_validArgs_returnsMailCommand() {
        assertParseSuccess(parser, "1", new MailCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validArgsWithSpaces_returnsMailCommand() {
        assertParseSuccess(parser, "1   ", new MailCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_nullArgs_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, MailCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_negativeIndex_throwsParseException() {
        assertParseFailure(parser, "-1", String.format(MESSAGE_INVALID_COMMAND_FORMAT, MailCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_outOfBoundsIndex_throwsParseException() {
        assertParseFailure(parser, "999", Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void parse_invalidAdditionalArguments_throwsParseException() {
        assertParseFailure(parser, "1 garbage", String.format(MESSAGE_INVALID_COMMAND_FORMAT, MailCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "abc", String.format(MESSAGE_INVALID_COMMAND_FORMAT, MailCommand.MESSAGE_USAGE));
    }
}
