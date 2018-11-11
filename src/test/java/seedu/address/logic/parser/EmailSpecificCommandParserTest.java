package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAGS_TO_ADD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.EmailSpecificCommand;
import seedu.address.model.tag.Tag;

//@@author aaryamNUS
/**
 * EmailSpecificCommandParserTest checks for various user input situations, such as tags without
 * any prefix, tags with only the prefix, and if no input is presented
 */
public class EmailSpecificCommandParserTest {

    private EmailSpecificCommandParser parser = new EmailSpecificCommandParser();

    @Test
    public void parse_validTags_success() {
        assertParseSuccess(parser," " + PREFIX_TAG + "friend " + PREFIX_TAG + "added"
                , new EmailSpecificCommand(TAGS_TO_ADD));

        // With whitespace
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + " " + PREFIX_TAG + "friend " + PREFIX_TAG + "added"
                , new EmailSpecificCommand(TAGS_TO_ADD));
    }

    @Test
    public void parse_noPrefixSpecifiedForAllTags_throwsParseException() {
        assertParseFailure(parser, "Veg Gold VIP", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                EmailSpecificCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyPrefixSpecified_throwsParseException() {
        assertParseFailure(parser, "t", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                EmailSpecificCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "    ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                EmailSpecificCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonAlphanumeric_throwsParseException() {
        assertParseFailure(parser, "%$", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                EmailSpecificCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTagNames_throwsParseException() {
        // Tag prefix repeated
        assertParseFailure(parser, " t/t/", Tag.MESSAGE_TAG_CONSTRAINTS);

        // Non-alphanumeric characters used
        assertParseFailure(parser, " t/!{!", Tag.MESSAGE_TAG_CONSTRAINTS);
    }
}
