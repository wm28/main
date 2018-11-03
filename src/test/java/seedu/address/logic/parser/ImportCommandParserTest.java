package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNSUPPORTED_FILE_EXTENSION;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_FILE_PATH;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.converters.CsvConverter;
import seedu.address.logic.converters.fileformats.csv.CsvFile;

public class ImportCommandParserTest {

    private ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "    ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPath_throwsParseException() {
        assertParseFailure(parser, "path@.csv",
                String.format(MESSAGE_INVALID_FILE_PATH, ImportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidFileExtension_throwsParseException() {
        assertParseFailure(parser, "testing.txt",
                String.format(MESSAGE_UNSUPPORTED_FILE_EXTENSION, ImportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_csvFileFormat_success() {
        String filename = "testing.csv";
        ImportCommand importCommand = new ImportCommand(new CsvFile(filename), new CsvConverter());
        assertParseSuccess(parser, filename, importCommand);
    }
}
