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
import seedu.address.model.person.Uid;
import seedu.address.testutil.TypicalPersons;

//@@author kronicler
public class UnmarkCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model modelNoPersons = new ModelManager();
    private final Model modelLimited = new ModelManager();
    private final Uid aliceUid = TypicalPersons.ALICE_UID;
    private final Uid bensonUid = TypicalPersons.BENSON_UID;
    private final Uid invalidUid = TypicalPersons.INVALID_UID;

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
        UnmarkCommand unmarkCommand = new UnmarkCommand(bensonUid);
        unmarkCommand.execute(model, commandHistory);
    }

    @Test
    public void execute_phoneNumberDoesNotExistInGuestList_throwCommandException() throws CommandException {
        thrown.expect(CommandException.class);
        UnmarkCommand unmarkCommand = new UnmarkCommand(invalidUid);
        unmarkCommand.execute(model, commandHistory);
    }

}
