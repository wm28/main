package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.util.CsvUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CsvConverter;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Exports currently filtered guest list to a CSV file
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports guests to a CSV file. "
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " guestlist.csv";
    public static final String MESSAGE_EXPORT_CSV_RESULT = "Successfully exported %1$d guests to %2$s";

    private Path csvFile;

    public ExportCommand(String fileName) {
        assert fileName != null : "FileName cannot be null";
        csvFile = Paths.get(fileName);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        try {
            List<String> result = new ArrayList<>();
            for (Person person : model.getFilteredPersonList()) {
                result.add(new CsvConverter().convertToCsv(person));
            }
            CsvUtil.saveDataLinesToFile(csvFile, result);
        } catch (IOException ioe) {
            throw new CommandException(ioe.getMessage());
        }

        return new CommandResult(String.format(MESSAGE_EXPORT_CSV_RESULT,
                model.getFilteredPersonList().size(), csvFile.toString()));
    }
}
