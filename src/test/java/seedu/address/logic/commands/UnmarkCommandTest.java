package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.GeneralMarkCommand.MESSAGE_UNMARK_PERSON_SUCCESS;
import static seedu.address.testutil.TypicalPersons.BENSON;
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
    private final Uid unknownUid = new Uid("00011");

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
        UnmarkCommand unmarkCommand = new UnmarkCommand(aliceUid);
        thrown.expect(CommandException.class);
        unmarkCommand.execute(modelNoPersons, commandHistory);
    }

    @Test
    public void retrieveIndex_filteredGuestListWithoutUid_throwCommandException() throws CommandException {
        UnmarkCommand unmarkCommand = new UnmarkCommand(bensonUid);
        thrown.expect(CommandException.class);
        unmarkCommand.execute(modelLimited, commandHistory);
    }

    @Test
    public void execute_uidExistsInGuestList_success() throws CommandException {
        UnmarkCommand unmarkCommand = new UnmarkCommand(bensonUid);
        CommandResult result = unmarkCommand.execute(model, commandHistory);
        CommandResult resultExpected = new CommandResult(String.format(MESSAGE_UNMARK_PERSON_SUCCESS,BENSON));
        assertTrue(result.feedbackToUser.equals(resultExpected.feedbackToUser));
    }

    @Test
    public void execute_uidDoesNotExistInGuestList_throwCommandException() throws CommandException {
        thrown.expect(CommandException.class);
        UnmarkCommand unmarkCommand = new UnmarkCommand(unknownUid);
        unmarkCommand.execute(model, commandHistory);
    }

}
