package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemoveTagCommand;

//@@author aaryamNUS
public class RemoveTagCommandParserTest {
    private RemoveTagCommandParser parser = new RemoveTagCommandParser();
    private final String nonEmptyTag = "Veg";

    @Test
    public void parse_formatSpecified_Success() {
        //have tag
        String userinput = PREFIX_TAG + nonEmptyTag;
        RemoveTagCommand expectedCommand = new RemoveTagCommand(nonEmptyTag);
        assertParseSuccess(parser, userinput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedmessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveTagCommand.MESSAGE_USAGE);

        //no parameters
        assertParseFailure(parser, RemoveTagCommand.COMMAND_WORD, expectedmessage);
    }
}
