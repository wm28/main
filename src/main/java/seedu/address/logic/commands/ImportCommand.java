package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowImportReportEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.converters.PersonConverter;
import seedu.address.logic.converters.exceptions.PersonDecodingException;
import seedu.address.logic.converters.fileformats.AdaptedPerson;
import seedu.address.logic.converters.fileformats.SupportedFile;
import seedu.address.model.Model;
import seedu.address.model.error.ImportError;
import seedu.address.model.person.Person;
import seedu.address.model.person.Uid;

//@@author wm28
/**
 * Imports multiple guests into the guest list of the current event via a CSV file
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports guests into current event through a CSV file. "
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " guestbook.csv";

    public static final String MESSAGE_IMPORT_CSV_RESULT = "Successfully imported %1$d of %2$d guests from %3$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";
    public static final String MESSAGE_DUPLICATE_UID =
            "This UID is already used in the guest list. Please use another UID.";
    public static final Uid DEFAULT_TO_GENERATE_UID = new Uid("00000");


    private static Logger logger = Logger.getLogger("execute");

    private final SupportedFile supportedFile;
    private final PersonConverter personConverter;
    private List<ImportError> errors;
    private int successfulImports;
    private int totalImports;


    public ImportCommand(SupportedFile supportedFile, PersonConverter personConverter) {
        assert supportedFile != null : "SupportedFile cannot be null";
        assert personConverter != null : "personConverter cannot be null";
        assert personConverter.getSupportedFileFormat().equals(supportedFile.getSupportedFileFormat())
                : "supportedFile and personConverter does not support the same file format";
        this.supportedFile = supportedFile;
        this.personConverter = personConverter;
        errors = new ArrayList<>();
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        try {
            List<AdaptedPerson> persons = supportedFile.readAdaptedPersons();
            successfulImports = persons.size();
            totalImports = successfulImports;
            importPersons(persons, model);
        } catch (IOException ioe) {
            logger.log(Level.INFO, "Failed to read from CSV file: " + supportedFile.getFileName());
            throw new CommandException(ioe.getMessage(), ioe);
        }

        if (!errors.isEmpty()) {
            logger.log(Level.INFO, "Error exists in CSV file, triggering ImportReportWindow");
            EventsCenter.getInstance().post(new ShowImportReportEvent(errors));
        }
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_IMPORT_CSV_RESULT,
                successfulImports, totalImports, supportedFile.getFileName()));
    }

    /**
     * Imports persons to the guest list
     */
    private void importPersons(List<AdaptedPerson> persons, Model model) {
        for (AdaptedPerson person : persons) {
            try {
                Person toAdd = personConverter.decodePerson(person);
                addPerson(toAdd, model);
            } catch (PersonDecodingException pe) {
                errors.add(new ImportError(person.getFormattedString(), pe.getMessage()));
                successfulImports--;
            } catch (CommandException ce) {
                errors.add(new ImportError(person.getFormattedString(), ce.getMessage()));
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
        //@@author
        //@@author kronicler
        if (model.hasUid(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_UID);
        }

        if (toAdd.getUid().equals(DEFAULT_TO_GENERATE_UID)) {
            boolean unique = false;
            while (!unique) {
                Person temp = new Person(toAdd.getName(), toAdd.getPhone(), toAdd.getEmail(), toAdd.getPayment(),
                        toAdd.getAttendance(), generateUid(), toAdd.getTags());
                if (model.hasUid(temp) == false) {
                    unique = true;
                    toAdd = temp;
                }
            }
        }
        //@@author
        //@@wm28
        model.addPerson(toAdd);
    }

    //@@author
    //@@author kronicler
    /**
     * Generate a random UID
     */
    public Uid generateUid() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return new Uid(String.format("%06d", number));
    }
    //@@author
    //@@author wm28

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ImportCommand)) {
            return false;
        }

        ImportCommand otherIc = (ImportCommand) other;
        return supportedFile.getFileName().equals(otherIc.supportedFile.getFileName())
                && personConverter.getSupportedFileFormat().equals(otherIc.personConverter.getSupportedFileFormat());
    }
}
//@@author
