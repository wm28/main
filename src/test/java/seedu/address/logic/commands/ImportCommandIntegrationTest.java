package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.converters.CsvPersonConverter;
import seedu.address.logic.converters.fileformats.csv.CsvFile;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;


/**
 * Contains integration tests (interaction with the Model) for {@code ImportCommand}.
 */
public class ImportCommandIntegrationTest {

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager();
    }

    //This integration test is based on guests in TypicalPersons class
    @Test
    public void execute_importPersonsFromCsvFile_success() {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        for (Person person : TypicalPersons.getTypicalPersons()) {
            expectedModel.addPerson(person);
        }
        expectedModel.commitAddressBook();

        CsvFile csvFile = new CsvFile(TypicalPersons.TYPICAL_PERSONS_CSV);
        CsvPersonConverter csvPersonConverter = new CsvPersonConverter();
        ImportCommand importCommand = new ImportCommand(csvFile, csvPersonConverter);
        Path path = Paths.get(TypicalPersons.TYPICAL_PERSONS_CSV);
        String expectedMessage = String.format(ImportCommand.MESSAGE_IMPORT_CSV_RESULT,
                TypicalPersons.NUM_PERSONS, TypicalPersons.NUM_PERSONS, path.getFileName());

        assertCommandSuccess(importCommand, model, commandHistory, expectedMessage, expectedModel);
    }
}
