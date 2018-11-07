package seedu.address.logic.commands;

import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Phone;
import seedu.address.testutil.TypicalPersons;

//@@author kronicler
public class MarkCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();
    public final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    public final Model modelNoPersons = new ModelManager();
    public final Model modelLimited = new ModelManager();
    public final Phone alicePhoneNumber = TypicalPersons.ALICE_PHONE_NUMBER;
    public final Phone bensonPhoneNumber = TypicalPersons.BENSON_PHONE_NUMBER;
    public final Phone invalidPhoneNumber = TypicalPersons.INVALID_PHONE_NUMBER;

    @Before
    public void setUp() {
        modelLimited.addPerson(TypicalPersons.ALICE);
    }

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new MarkCommand(null);
    }

    @Test
    public void execute_noPersonInGuestList_throwCommandException() throws CommandException {
        MarkCommand markCommand = new MarkCommand(alicePhoneNumber);
        thrown.expect(CommandException.class);
        markCommand.execute(modelNoPersons, commandHistory);
    }

    @Test
    public void execute_filteredGuestListWithoutThePhoneNumber_throwCommandException() throws CommandException {
        MarkCommand markCommand = new MarkCommand(bensonPhoneNumber);
        thrown.expect(CommandException.class);
        markCommand.execute(modelLimited, commandHistory);
    }

    @Test
    public void retrieveIndex_filteredGuestListWithThePhoneNumber_success() {
    }

    @Test
    public void retrieveIndex_unfilteredGuestListWithThePhoneNumber_success() {
    }

    @Test
    public void execute_phoneNumberExistsInGuestList_success() throws CommandException {
        MarkCommand markCommand = new MarkCommand(bensonPhoneNumber);
        markCommand.execute(model, commandHistory);
    }

    @Test
    public void execute_phoneNumberDoesNotExistInGuestList_throwCommandException() throws CommandException {
        thrown.expect(CommandException.class);
        MarkCommand markCommand = new MarkCommand(invalidPhoneNumber);
        markCommand.execute(model, commandHistory);
    }

}
