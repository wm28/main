package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_FILE_PATH;
import static seedu.address.commons.core.Messages.MESSAGE_UNSUPPORTED_FILE_EXTENSION;

import java.util.Optional;

import seedu.address.commons.util.FileUtil;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.converters.CsvPersonConverter;
import seedu.address.logic.converters.fileformats.SupportedFileFormat;
import seedu.address.logic.converters.fileformats.csv.CsvFile;
import seedu.address.logic.parser.exceptions.ParseException;


//@@author wm28
/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses the given argument {@code String} in the context of the ExportCommand
     * and returns an ExportCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ExportCommand parse(String arg) throws ParseException {
        String trimmedArg = arg.trim();
        Optional<SupportedFileFormat> fileFormat;

        if (trimmedArg.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        } else if (!FileUtil.isValidPath(trimmedArg)) {
            throw new ParseException(String.format(MESSAGE_INVALID_FILE_PATH, ExportCommand.MESSAGE_USAGE));
        }

        fileFormat = SupportedFileFormat.findSupportedFileFormat(trimmedArg);

        if (fileFormat.isPresent()) {
            switch (fileFormat.get()) {
            case CSV:
                return new ExportCommand(new CsvFile(trimmedArg), new CsvPersonConverter());
            default:
                throw new ParseException(String.format(MESSAGE_UNSUPPORTED_FILE_EXTENSION,
                        ExportCommand.MESSAGE_USAGE));
            }
        } else {
            throw new ParseException(String.format(MESSAGE_UNSUPPORTED_FILE_EXTENSION, ExportCommand.MESSAGE_USAGE));
        }
    }
}
//@@author
