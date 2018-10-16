package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import seedu.address.commons.util.CsvUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.converters.PersonConverter;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

//@@author wm28
/**
 * Imports multiple guests into the guest list of the current event via a CSV file
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports guests into current event through a CSV file. "
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " guestlist.csv";

    public static final String MESSAGE_IMPORT_CSV_RESULT = "Successfully imported %1$d of %2$d guests from %3$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private Path csvFile;
    private PersonConverter personConverter;
    private List<String> guestData;
    private int totalGuests;
    private int successfulImports;

    public ImportCommand(String fileName, PersonConverter personConverter) {
        assert !fileName.isEmpty();
        assert personConverter != null;
        csvFile = Paths.get(fileName);
        this.personConverter = personConverter;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        try {
            guestData = CsvUtil.getDataLinesFromFile(csvFile);
            totalGuests = guestData.size();
            successfulImports = totalGuests;
            importPersons(guestData, model);
        } catch (IOException e) {
            throw new CommandException(e.getMessage());
        }

        model.commitAddressBook();
        return new CommandResult(
                String.format(MESSAGE_IMPORT_CSV_RESULT, successfulImports, totalGuests, csvFile.getFileName()));
    }

    /**
     * Imports persons to the guest list
     */
    private void importPersons(List<String> guestData, Model model) {
        for (String guest : guestData) {
            try {
                Person toAdd = personConverter.decodePerson(guest);
                addPerson(toAdd, model);
            } catch (ParseException pe) {
                successfulImports--;
            } catch (CommandException ce) {
                successfulImports--;
            }
        }

    }

    /**
     * Adds a person to the guest list
     */
    private void addPerson(Person toAdd, Model model) throws CommandException {
        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
        model.addPerson(toAdd);
    }
}
//@@author
