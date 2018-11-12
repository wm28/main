package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.logic.commands.MarkCommand;

public class MarkCommandParserTest {
    private MarkCommandParser parser = new MarkCommandParser();

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "001", String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }

}
