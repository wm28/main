package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_FILE_EXTENSION;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_FILE_PATH;

import seedu.address.commons.util.FileUtil;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.converters.CsvConverter;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author wm28
/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser {

    public static final String DEFAULT_FILENAME_FORMAT = "exportedGuestList.csv";

    /**
     * Parses the given argument {@code String} in the context of the ImportCommand
     * and returns an ImportCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ExportCommand parse(String arg) throws ParseException {
        String trimmedArg = arg.trim();
        if (trimmedArg.isEmpty()) {
            return new ExportCommand(DEFAULT_FILENAME_FORMAT, new CsvConverter());
        } else if (!FileUtil.isValidPath(trimmedArg)) {
            throw new ParseException(String.format(MESSAGE_INVALID_FILE_PATH, ImportCommand.MESSAGE_USAGE));
        } else if (!FileUtil.isValidFileExtension(trimmedArg, "csv")) {
            throw new ParseException(String.format(MESSAGE_INVALID_FILE_EXTENSION, ImportCommand.MESSAGE_USAGE));
        }
        return new ExportCommand(trimmedArg, new CsvConverter());
    }
}
//@@author
