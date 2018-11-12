package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.logic.commands.AddTagCommand;

//@@author aaryamNUS
/**
 * AddTagCommandParserTest checks for various user input situations, such as tags without
 * any prefix, tags with only the prefix, and if no input is presented
 */
public class AddTagCommandParserTest {
    private AddTagCommandParser parser = new AddTagCommandParser();

    @Test
    public void parse_noPrefixSpecifiedForAllTags_throwsParseException() {
        assertParseFailure(parser, "Veg Gold VIP", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyPrefixSpecified_success() {
        assertParseFailure(parser, "t", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "    ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonAlphanumeric_throwsParseException() {
        assertParseFailure(parser, "%$", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddTagCommand.MESSAGE_USAGE));
    }
}
