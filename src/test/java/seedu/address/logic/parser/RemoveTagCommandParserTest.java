package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.logic.commands.RemoveTagCommand;

//@@author aaryamNUS
/**
 * RemoveTagCommandParserTest checks for various user input situations, such as tags without
 * any prefix, tags with only the prefix, and if no input is presented
 */
public class RemoveTagCommandParserTest {

    private RemoveTagCommandParser parser = new RemoveTagCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "    ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                RemoveTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noPrefixSpecifiedForAllTags_throwsParseException() {
        assertParseFailure(parser, "Silver GuestSpeaker", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                RemoveTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyPrefixSpecified_throwsParseException() {
        assertParseFailure(parser, "t", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                RemoveTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonAlphanumeric_throwsParseException() {
        assertParseFailure(parser, "%$", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                RemoveTagCommand.MESSAGE_USAGE));
    }
}
