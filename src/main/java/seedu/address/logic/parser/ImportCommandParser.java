package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_FILE_EXTENSION;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_FILE_PATH;

import seedu.address.commons.util.FileUtil;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.converters.CsvConverter;
import seedu.address.logic.converters.fileformats.csv.CsvFile;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author wm28
/**
 * Parses input arguments and creates a new ImportCommand object
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    /**
     * Parses the given argument {@code String} in the context of the ImportCommand
     * and returns an ImportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ImportCommand parse(String arg) throws ParseException {
        String trimmedArg = arg.trim();

        if (trimmedArg.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        } else if (!FileUtil.isValidPath(trimmedArg)) {
            throw new ParseException(String.format(MESSAGE_INVALID_FILE_PATH, ImportCommand.MESSAGE_USAGE));
        } else if (!FileUtil.isValidFileExtension(trimmedArg, "csv")) {
            throw new ParseException(String.format(MESSAGE_INVALID_FILE_EXTENSION, ImportCommand.MESSAGE_USAGE));
        }

        return new ImportCommand(new CsvFile(trimmedArg), new CsvConverter());
    }
}
//@@author
