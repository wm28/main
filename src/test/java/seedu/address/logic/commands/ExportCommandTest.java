package seedu.address.logic.commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.converters.PersonConverter;
import seedu.address.logic.converters.exceptions.PersonDecodingException;
import seedu.address.logic.converters.exceptions.PersonEncodingException;
import seedu.address.logic.converters.fileformats.AdaptedPerson;
import seedu.address.logic.converters.fileformats.SupportedFile;
import seedu.address.logic.converters.fileformats.SupportedFileFormat;
import seedu.address.logic.converters.fileformats.csv.CsvAdaptedPerson;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.TypicalPersons;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_FILE_ALREADY_EXIST;

public class ExportCommandTest {

    private String ALREADY_EXISTING_CSV_FILENAME = "existing.csv";
    public static final String VALID_CSV_FILENAME = "valid.csv";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullSupportedFile_throwsAssertionError() {
        thrown.expect(AssertionError.class);
        new ExportCommand(null, new PersonConverterStub());
    }

    @Test
    public void constructor_nullPersonConverter_throwsAssertionError() {
        thrown.expect(AssertionError.class);
        new ExportCommand(new SupportedFileStub(), null);
    }

    @Test
    public void constructor_personConverterCsvFileMatch_success() {
        ExportCommand exportCommand = new ExportCommand(new CsvFileStub(), new CsvConverterStub());
        Assert.assertNotNull(exportCommand);
    }

    @Test
    public void execute_exportToAlreadyExistingFile_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        ExportCommand exportCommand = new ExportCommand(new CsvFileWithAlreadyExistingFile(),
                new CsvConverterAlwaysSuccessfulConversion());
        CommandResult commandResult = exportCommand.execute(new ModelStubContainingTypicalPersons(), commandHistory);
        assertEquals(String.format(MESSAGE_FILE_ALREADY_EXIST, ALREADY_EXISTING_CSV_FILENAME),
                commandResult.feedbackToUser);
    }

    @Test
    public void execute_csvConverterFailure_correctFeedback() throws Exception {
        ExportCommand exportCommand = new ExportCommand(new CsvFileAlwaysSuccessfulWrite(),
                new CsvConverterAlwaysFailingConversion());
        CommandResult commandResult = exportCommand.execute(new ModelStubContainingTypicalPersons(), commandHistory);
        assertEquals(String.format(ExportCommand.MESSAGE_EXPORT_CSV_RESULT, 0, TypicalPersons.NUM_PERSONS,
                VALID_CSV_FILENAME), commandResult.feedbackToUser);
    }

    @Test
    public void execute_successfulExportOfAllPersons_success() throws Exception {
        ExportCommand exportCommand = new ExportCommand(new CsvFileAlwaysSuccessfulWrite(),
                new CsvConverterAlwaysSuccessfulConversion());

        CommandResult commandResult = exportCommand.execute(new ModelStubContainingTypicalPersons(), commandHistory);
        assertEquals(String.format(ExportCommand.MESSAGE_EXPORT_CSV_RESULT, TypicalPersons.NUM_PERSONS,
                TypicalPersons.NUM_PERSONS, VALID_CSV_FILENAME), commandResult.feedbackToUser);
    }

    /**
     * A default SupportedFile stub that have all of the methods failing.
     */
    private class SupportedFileStub implements SupportedFile {
        @Override
        public List<AdaptedPerson> readAdaptedPersons() throws IOException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void writeAdaptedPersons(List<AdaptedPerson> adaptedPersons) throws IOException {
            throw new AssertionError("This method should not be called.");

        }

        @Override
        public SupportedFileFormat getSupportedFileFormat() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String getFileName() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A CsvFile stub, an implementation of SupportedFile in the CSV format
     */
    private class CsvFileStub extends SupportedFileStub {
        @Override
        public SupportedFileFormat getSupportedFileFormat() {
            return SupportedFileFormat.CSV;
        }

        @Override
        public String getFileName() {
            return VALID_CSV_FILENAME;
        }

    }

    /**
     * A CsvFile stub that have an already existing file
     */
    private class CsvFileWithAlreadyExistingFile extends CsvFileStub {
        @Override
        public void writeAdaptedPersons(List<AdaptedPerson> adaptedPersons) throws IOException {
            throw new FileAlreadyExistsException(String.format(MESSAGE_FILE_ALREADY_EXIST,
                    ALREADY_EXISTING_CSV_FILENAME));
        }

        @Override
        public String getFileName() {
            return ALREADY_EXISTING_CSV_FILENAME;
        }
    }

    /**
     * A CsvFile stub that always successfully writes
     */
    private class CsvFileAlwaysSuccessfulWrite extends CsvFileStub {
        @Override
        public void writeAdaptedPersons(List<AdaptedPerson> adaptedPersons) throws IOException {
            // called by {@code ExportCommand#execute()}
        }
    }


    /**
     * A default PersonConverter stub that have all of the methods failing.
     */
    private class PersonConverterStub implements PersonConverter {
        @Override
        public AdaptedPerson encodePerson(Person person) throws PersonEncodingException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Person decodePerson(AdaptedPerson person) throws PersonDecodingException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public SupportedFileFormat getSupportedFileFormat() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A CsvConverter stub, an implementation of PersonConverter in the CSV format
     */
    private class CsvConverterStub extends PersonConverterStub {
        @Override
        public SupportedFileFormat getSupportedFileFormat() {
            return SupportedFileFormat.CSV;
        }

    }

    /**
     * A CsvConverter stub that always fails to encode
     */
    private class CsvConverterAlwaysFailingConversion extends PersonConverterStub {
        @Override
        public AdaptedPerson encodePerson(Person person) throws PersonEncodingException {
            throw new PersonEncodingException("Person fails to encode");
        }

        @Override
        public SupportedFileFormat getSupportedFileFormat() {
            return SupportedFileFormat.CSV;
        }
    }

    /**
     * A CsvConverter stub that always successfully encodes.
     */
    private class CsvConverterAlwaysSuccessfulConversion extends PersonConverterStub {
        @Override
        public AdaptedPerson encodePerson(Person person) throws PersonEncodingException {
            return new CsvAdaptedPerson("Encoded person");
        }

        @Override
        public SupportedFileFormat getSupportedFileFormat() {
            return SupportedFileFormat.CSV;
        }
    }


    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addEvent(Event event) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasEvent() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteEvent() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updatePerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateEvent(Event editedEvent) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTag(Tag tag) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTag(Tag tag) {
            throw new AssertionError("This method should not be called");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Event getEventDetails() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitAddressBook() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains TypicalPersons
     */
    private class ModelStubContainingTypicalPersons extends ModelStub {

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableList(TypicalPersons.getTypicalPersons());
        }
    }
}
