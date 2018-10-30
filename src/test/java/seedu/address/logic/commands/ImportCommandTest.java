package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_FILE_NOT_FOUND;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.converters.PersonConverter;
import seedu.address.logic.converters.exceptions.PersonDecodingException;
import seedu.address.logic.converters.exceptions.PersonEncodingException;
import seedu.address.logic.converters.fileformats.AdaptedPerson;
import seedu.address.logic.converters.fileformats.SupportedFile;
import seedu.address.logic.converters.fileformats.SupportedFileFormat;
import seedu.address.logic.converters.fileformats.csv.CsvAdaptedPerson;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.TypicalPersons;

public class ImportCommandTest {

    public static final String VALID_CSV_FILENAME = "valid.csv";
    public static final String INVALID_CSV_FILENAME = "valid.csv";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullSupportedFile_throwsAssertionError() {
        thrown.expect(AssertionError.class);
        new ImportCommand(null, new PersonConverterStub());
    }

    @Test
    public void constructor_nullPersonConverter_throwsAssertionError() {
        thrown.expect(AssertionError.class);
        new ImportCommand(new SupportedFileStub(), null);
    }

    @Test
    public void constructor_personConverterCsvFileMismatch_throwsAssertionError() {
        //To-be implemented
    }

    @Test
    public void constructor_personConverterCsvFileMatch_success() {
        ImportCommand importCommand = new ImportCommand(new CsvFileStub(), new CsvConverterStub());
        Assert.assertNotNull(importCommand);
    }

    @Test
    public void execute_invalidFileInCsvFile_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        ImportCommand importCommand = new ImportCommand(new CsvFileWithInvalidFile(), new CsvConverterStub());
        CommandResult commandResult = importCommand.execute(new ModelStubAcceptingPersonAdded(), commandHistory);
        assertEquals(String.format(MESSAGE_FILE_NOT_FOUND, INVALID_CSV_FILENAME), commandResult.feedbackToUser);
    }


    @Test
    public void execute_csvConverterFailure_throwsCommandException() throws Exception {
        ImportCommand importCommand = new ImportCommand(new CsvFileStubWithTypicalPersons(),
                new CsvConverterAlwaysFailingConversion());
        CommandResult commandResult = importCommand.execute(new ModelStubAcceptingPersonAdded(), commandHistory);
        assertEquals(String.format(ImportCommand.MESSAGE_IMPORT_CSV_RESULT, 0, TypicalPersons.NUM_PERSONS,
                VALID_CSV_FILENAME), commandResult.feedbackToUser);
    }


    @Test
    public void execute_successfulImportOfAllEntriesInCsvFile_success() throws Exception {
        ImportCommand importCommand = new ImportCommand(new CsvFileStubWithTypicalPersons(),
                new CsvConverterAlwaysSuccessfulConversion());

        CommandResult commandResult = importCommand.execute(new ModelStubAcceptingPersonAdded(), commandHistory);
        assertEquals(String.format(ImportCommand.MESSAGE_IMPORT_CSV_RESULT, TypicalPersons.NUM_PERSONS,
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
     * A CsvFile stub that have an invalid/non-existent file
     */
    private class CsvFileWithInvalidFile extends CsvFileStub {
        @Override
        public List<AdaptedPerson> readAdaptedPersons() throws IOException {
            throw new IOException(String.format(MESSAGE_FILE_NOT_FOUND, INVALID_CSV_FILENAME));
        }

        @Override
        public String getFileName() {
            return INVALID_CSV_FILENAME;
        }
    }

    /**
     * A CsvFile stub that replicates persons from TypicalPersons
     */
    private class CsvFileStubWithTypicalPersons extends CsvFileStub {
        @Override
        public List<AdaptedPerson> readAdaptedPersons() throws IOException {
            List<AdaptedPerson> persons = new ArrayList<>();
            persons.add(new CsvAdaptedPerson(TypicalPersons.CSV_ALICE));
            persons.add(new CsvAdaptedPerson(TypicalPersons.CSV_BENSON));
            persons.add(new CsvAdaptedPerson(TypicalPersons.CSV_CARL));
            persons.add(new CsvAdaptedPerson(TypicalPersons.CSV_DANIEL));
            persons.add(new CsvAdaptedPerson(TypicalPersons.CSV_ELLE));
            persons.add(new CsvAdaptedPerson(TypicalPersons.CSV_FIONA));
            persons.add(new CsvAdaptedPerson(TypicalPersons.CSV_GEORGE));
            return persons;
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
     * A CsvConverter stub that always fails to decode
     */
    private class CsvConverterAlwaysFailingConversion extends PersonConverterStub {
        @Override
        public Person decodePerson(AdaptedPerson person) throws PersonDecodingException {
            throw new PersonDecodingException("Person fails to decode");
        }

        @Override
        public SupportedFileFormat getSupportedFileFormat() {
            return SupportedFileFormat.CSV;
        }
    }

    /**
     * A CsvConverter stub that always successfully decodes. Persons are based on TypicalPersons list for
     * simplicity.
     */
    private class CsvConverterAlwaysSuccessfulConversion extends PersonConverterStub {
        private Queue<Person> personList = new LinkedList<>(TypicalPersons.getTypicalPersons());

        @Override
        public Person decodePerson(AdaptedPerson person) throws PersonDecodingException {
            if (!personList.isEmpty()) {
                Person chosenPerson = personList.peek();
                personList.remove();
                return chosenPerson;
            } else {
                throw new PersonDecodingException("No one left");
            }
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
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public void commitAddressBook() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
