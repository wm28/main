package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.converters.PersonConverter;
import seedu.address.logic.converters.exceptions.PersonEncodingException;
import seedu.address.logic.converters.fileformats.AdaptedPerson;
import seedu.address.logic.converters.fileformats.SupportedFile;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

//@@author wm28
/**
 * Exports currently filtered guest list to a CSV file
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports guests to a CSV file. "
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " guestbook.csv";
    public static final String MESSAGE_EXPORT_CSV_RESULT = "Successfully exported %1$d/%2$d guests to %3$s";
    public static final String MESSAGE_NO_PERSONS = "There are no persons to export!";

    private static Logger logger = Logger.getLogger("execute");

    private final SupportedFile supportedFile;
    private final PersonConverter personConverter;
    private int totalPersons;
    private int successfulExports;

    public ExportCommand(SupportedFile supportedFile, PersonConverter personConverter) {
        assert supportedFile != null : "supportedFile cannot be null";
        assert personConverter != null : "personConverter cannot be null";
        assert personConverter.getSupportedFileFormat().equals(supportedFile.getSupportedFileFormat())
                : "supportedFile and personConverter does not support the same file format";
        this.personConverter = personConverter;
        this.supportedFile = supportedFile;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        ObservableList<Person> filteredList = model.getFilteredPersonList();
        if (filteredList.size() == 0) {
            logger.log(Level.INFO, "No persons to export from the guest list");
            throw new CommandException(MESSAGE_NO_PERSONS);
        }
        totalPersons = filteredList.size();
        successfulExports = totalPersons;

        try {
            List<AdaptedPerson> result = exportPersons(filteredList);
            supportedFile.writeAdaptedPersons(result);
        } catch (NoSuchFileException nsfe) {
            logger.log(Level.INFO, "File path provided is invalid");
            String errorMessage = String.format(Messages.MESSAGE_INVALID_FILE_PATH, nsfe.getMessage());
            throw new CommandException(errorMessage, nsfe);
        } catch (FileAlreadyExistsException faee){
            logger.log(Level.INFO, "CSV File provided already exist", supportedFile.getFileName());
            throw new CommandException(faee.getMessage(), faee);
        } catch (IOException ioe) {
            logger.log(Level.INFO, "Failed to read from SupportedFile");
            throw new CommandException(ioe.getMessage(), ioe);
        }

        return new CommandResult(String.format(MESSAGE_EXPORT_CSV_RESULT,
                successfulExports, totalPersons, supportedFile.getFileName()));

    }

    /**
     * Exports persons to csv-formatted strings
     */
    private List<AdaptedPerson> exportPersons(ObservableList<Person> personList) {
        List<AdaptedPerson> result = new ArrayList<>();
        for (Person person : personList) {
            try {
                result.add(personConverter.encodePerson(person));
            } catch (PersonEncodingException pee) {
                successfulExports--;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ExportCommand)) {
            return false;
        }

        ExportCommand otherEc = (ExportCommand) other;
        return supportedFile.getFileName().equals(otherEc.supportedFile.getFileName())
                && personConverter.getSupportedFileFormat().equals(otherEc.personConverter.getSupportedFileFormat());
    }
}
//@@author
