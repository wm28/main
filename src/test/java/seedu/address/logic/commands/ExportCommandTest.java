package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_FILE_ALREADY_EXIST;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_FILE_PATH;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.converters.exceptions.PersonEncodingException;
import seedu.address.logic.converters.fileformats.AdaptedPerson;
import seedu.address.logic.converters.fileformats.csv.CsvAdaptedPerson;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;
import seedu.address.testutil.stubs.CsvFileStub;
import seedu.address.testutil.stubs.CsvPersonConverterStub;
import seedu.address.testutil.stubs.ModelStubContainingTypicalPersons;
import seedu.address.testutil.stubs.PersonConverterStub;
import seedu.address.testutil.stubs.SupportedFileStub;

public class ExportCommandTest {

    private static final String ALREADY_EXISTING_CSV_FILENAME = "existing.csv";
    private static final String VALID_CSV_FILENAME = "valid.csv";
    private static final String INVALID_CSV_FILE_PATH = "invalidPath/NoSuchPath/test.csv";

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
    public void constructor_csvPersonConverterCsvFileMatch_success() {
        ExportCommand exportCommand = new ExportCommand(new CsvFileStub(), new CsvPersonConverterStub());
        Assert.assertNotNull(exportCommand);
    }

    @Test
    public void execute_exportToAlreadyExistingFile_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        ExportCommand exportCommand = new ExportCommand(new CsvFileWithAlreadyExistingFile(),
                new CsvPersonConverterAlwaysSuccessfulConversion());
        CommandResult commandResult = exportCommand.execute(new ModelStubContainingTypicalPersons(), commandHistory);
        assertEquals(String.format(MESSAGE_FILE_ALREADY_EXIST, ALREADY_EXISTING_CSV_FILENAME),
                commandResult.feedbackToUser);
    }

    @Test
    public void execute_exportToInvalidFilePath_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        ExportCommand exportCommand = new ExportCommand(new CsvFileWithInvalidFilePath(),
                new CsvPersonConverterAlwaysSuccessfulConversion());
        CommandResult commandResult = exportCommand.execute(new ModelStubContainingTypicalPersons(), commandHistory);
        assertEquals(String.format(MESSAGE_INVALID_FILE_PATH, INVALID_CSV_FILE_PATH),
                commandResult.feedbackToUser);
    }

    @Test
    public void execute_csvPersonConverterFailure_correctFeedback() throws Exception {
        ExportCommand exportCommand = new ExportCommand(new CsvFileAlwaysSuccessfulWrite(),
                new CsvPersonConverterAlwaysFailingConversion());
        CommandResult commandResult = exportCommand.execute(new ModelStubContainingTypicalPersons(), commandHistory);
        assertEquals(String.format(ExportCommand.MESSAGE_EXPORT_CSV_RESULT, 0, TypicalPersons.NUM_PERSONS,
                VALID_CSV_FILENAME), commandResult.feedbackToUser);
    }

    @Test
    public void execute_successfulExportOfAllPersons_success() throws Exception {
        ExportCommand exportCommand = new ExportCommand(new CsvFileAlwaysSuccessfulWrite(),
                new CsvPersonConverterAlwaysSuccessfulConversion());

        CommandResult commandResult = exportCommand.execute(new ModelStubContainingTypicalPersons(), commandHistory);
        assertEquals(String.format(ExportCommand.MESSAGE_EXPORT_CSV_RESULT, TypicalPersons.NUM_PERSONS,
                TypicalPersons.NUM_PERSONS, VALID_CSV_FILENAME), commandResult.feedbackToUser);
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
     * A CsvFile stub that has an invalid file path
     */
    private class CsvFileWithInvalidFilePath extends CsvFileStub {
        @Override
        public void writeAdaptedPersons(List<AdaptedPerson> adaptedPersons) throws IOException {
            throw new NoSuchFileException(INVALID_CSV_FILE_PATH);
        }

        @Override
        public String getFileName() {
            return INVALID_CSV_FILE_PATH;
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

        @Override
        public String getFileName() {
            return VALID_CSV_FILENAME;
        }
    }


    /**
     * A CsvPersonConverter stub that always fails to encode
     */
    private class CsvPersonConverterAlwaysFailingConversion extends CsvPersonConverterStub {
        @Override
        public AdaptedPerson encodePerson(Person person) throws PersonEncodingException {
            throw new PersonEncodingException("Person fails to encode");
        }
    }

    /**
     * A CsvPersonConverter stub that always successfully encodes.
     */
    private class CsvPersonConverterAlwaysSuccessfulConversion extends CsvPersonConverterStub {
        @Override
        public AdaptedPerson encodePerson(Person person) throws PersonEncodingException {
            return new CsvAdaptedPerson("Encoded person");
        }
    }
}
