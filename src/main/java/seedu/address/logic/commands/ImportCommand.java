package seedu.address.logic.commands;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Export current event guest list to a CSV file. "
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " guestlist.csv";

    public static final String MESSAGE_IMPORT_CSV_SUCCESS = "Successfully imported guests from %1$s";

    private Path csvFile;

    public ImportCommand(String fileName){
        csvFile = Paths.get(fileName);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException{
        requireNonNull(model);
        //Currently does nothing
        return new CommandResult(String.format(MESSAGE_IMPORT_CSV_SUCCESS, csvFile.toString()));
    }
}
