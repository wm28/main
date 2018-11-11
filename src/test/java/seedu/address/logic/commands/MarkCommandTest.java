package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.logic.commands.GeneralMarkCommand.MESSAGE_MARK_PERSON_SUCCESS;
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
public class MarkCommandTest {

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
        MarkCommand markCommand = new MarkCommand(aliceUid);
        thrown.expect(CommandException.class);
        markCommand.execute(modelNoPersons, commandHistory);
    }

    @Test
    public void execute_filteredGuestListWithoutTheUid_throwCommandException() throws CommandException {
        MarkCommand markCommand = new MarkCommand(bensonUid);
        thrown.expect(CommandException.class);
        markCommand.execute(modelLimited, commandHistory);
    }

    @Test
    public void execute_uidExistsInGuestList_success() throws CommandException {
        MarkCommand markCommand = new MarkCommand(bensonUid);
        CommandResult result = markCommand.execute(model, commandHistory);
        CommandResult resultExpected = new CommandResult(String.format(MESSAGE_MARK_PERSON_SUCCESS,BENSON));
        assertFalse(result.equals(resultExpected));
    }

    @Test
    public void execute_uidDoesNotExistInGuestList_throwCommandException() throws CommandException {
        thrown.expect(CommandException.class);
        MarkCommand markCommand = new MarkCommand(unknownUid);
        markCommand.execute(model, commandHistory);
    }

}
