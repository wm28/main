package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_FILE_NOT_FOUND;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.converters.exceptions.PersonDecodingException;
import seedu.address.logic.converters.fileformats.AdaptedPerson;
import seedu.address.logic.converters.fileformats.csv.CsvAdaptedPerson;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;
import seedu.address.testutil.stubs.CsvFileStub;
import seedu.address.testutil.stubs.CsvPersonConverterStub;
import seedu.address.testutil.stubs.ModelStubAcceptingPersonAdded;
import seedu.address.testutil.stubs.PersonConverterStub;
import seedu.address.testutil.stubs.SupportedFileStub;

/**
 * Contains unit tests for ImportCommand.
 */
public class ImportCommandTest {

    private static final String VALID_CSV_FILENAME = "valid.csv";
    private static final String INVALID_CSV_FILENAME = "invalid.csv";

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
    public void constructor_csvPersonConverterCsvFileMatch_success() {
        ImportCommand importCommand = new ImportCommand(new CsvFileStub(), new CsvPersonConverterStub());
        Assert.assertNotNull(importCommand);
    }

    @Test
    public void execute_invalidFileInCsvFile_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        ImportCommand importCommand = new ImportCommand(new CsvFileWithInvalidFile(), new CsvPersonConverterStub());
        CommandResult commandResult = importCommand.execute(new ModelStubAcceptingPersonAdded(), commandHistory);
        assertEquals(String.format(MESSAGE_FILE_NOT_FOUND, INVALID_CSV_FILENAME), commandResult.feedbackToUser);
    }


    @Test
    public void execute_csvPersonConverterFailure_correctFeedback() throws Exception {
        ImportCommand importCommand = new ImportCommand(new CsvFileStubWithTypicalPersons(),
                new CsvPersonConverterAlwaysFailingConversion());
        CommandResult commandResult = importCommand.execute(new ModelStubAcceptingPersonAdded(), commandHistory);
        assertEquals(String.format(ImportCommand.MESSAGE_IMPORT_CSV_RESULT, 0, TypicalPersons.NUM_PERSONS,
                VALID_CSV_FILENAME), commandResult.feedbackToUser);
    }


    @Test
    public void execute_successfulImportOfAllEntriesInCsvFile_success() throws Exception {
        ImportCommand importCommand = new ImportCommand(new CsvFileStubWithTypicalPersons(),
                new CsvPersonConverterAlwaysSuccessfulConversion());

        CommandResult commandResult = importCommand.execute(new ModelStubAcceptingPersonAdded(), commandHistory);
        assertEquals(String.format(ImportCommand.MESSAGE_IMPORT_CSV_RESULT, TypicalPersons.NUM_PERSONS,
                TypicalPersons.NUM_PERSONS, VALID_CSV_FILENAME), commandResult.feedbackToUser);
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
     * A CsvFile stub that replicates reading from a file containing persons from TypicalPersons
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

        @Override
        public String getFileName() {
            return VALID_CSV_FILENAME;
        }
    }


    /**
     * A CsvPersonConverter stub that always fails to decode
     */
    private class CsvPersonConverterAlwaysFailingConversion extends CsvPersonConverterStub {
        @Override
        public Person decodePerson(AdaptedPerson person) throws PersonDecodingException {
            throw new PersonDecodingException("Person fails to decode");
        }
    }

    /**
     * A CsvPersonConverter stub that always successfully decodes. Persons are based on TypicalPersons list for
     * simplicity.
     */
    private class CsvPersonConverterAlwaysSuccessfulConversion extends CsvPersonConverterStub {
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
    }
}
