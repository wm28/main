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
public class UnmarkCommandTest {

    private CommandHistory commandHistory = new CommandHistory();
    public final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    public final Model modelNoPersons = new ModelManager();
    public final Model modelLimited = new ModelManager();
    public final Phone alicePhoneNumber = TypicalPersons.ALICE_PHONE_NUMBER;
    public final Phone bensonPhoneNumber = TypicalPersons.BENSON_PHONE_NUMBER;
    public final Phone invalidPhoneNumber = TypicalPersons.INVALID_PHONE_NUMBER;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
    public void retrieveIndex_noPersonInGuestList_throwCommandException() {

    }

    @Test
    public void retrieveIndex_filteredGuestListWithoutThePhoneNumber_throwCommandException() {
    }

    @Test
    public void retrieveIndex_filteredGuestListWithThePhoneNumber_success() {
    }

    @Test
    public void retrieveIndex_unfilteredGuestListWithThePhoneNumber_success() {
    }

    @Test
    public void execute_phoneNumberExistsInGuestList_success() throws CommandException {
        UnmarkCommand unmarkCommand = new UnmarkCommand(bensonPhoneNumber);
        unmarkCommand.execute(model, commandHistory);
    }

    @Test
    public void execute_phoneNumberDoesNotExistInGuestList_throwCommandException() throws CommandException {
        thrown.expect(CommandException.class);
        UnmarkCommand unmarkCommand = new UnmarkCommand(invalidPhoneNumber);
        unmarkCommand.execute(model, commandHistory);
    }

}
