package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.DeleteEventCommand.MESSAGE_NO_EVENT_DETAILS;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBookWithEvent;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteEventCommandTest {

    private Model modelWithUserInitialisedEvent = new ModelManager(getTypicalAddressBookWithEvent(), new UserPrefs());
    private Model modelWithoutEvent = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_deletePossible_success() {
        DeleteEventCommand deleteEventCommand = new DeleteEventCommand();

        String expectedMessage = String.format(DeleteEventCommand.MESSAGE_DELETE_EVENT_SUCCESS);
        ModelManager expectedModel = new ModelManager(modelWithUserInitialisedEvent.getAddressBook(), new UserPrefs());
        expectedModel.deleteEvent();
        expectedModel.commitAddressBook();

        assertCommandSuccess(deleteEventCommand, modelWithUserInitialisedEvent,
                commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteNotPossible_throwsCommandException() {
        DeleteEventCommand deleteEventCommand = new DeleteEventCommand();

        assertCommandFailure(deleteEventCommand, modelWithoutEvent, commandHistory, MESSAGE_NO_EVENT_DETAILS);
    }

    @Test
    public void executeUndoRedo_deletePossible_success() throws Exception {
        DeleteEventCommand deleteEventCommand = new DeleteEventCommand();
        Model expectedModel = new ModelManager(modelWithUserInitialisedEvent.getAddressBook(), new UserPrefs());
        expectedModel.deleteEvent();
        expectedModel.commitAddressBook();

        deleteEventCommand.execute(modelWithUserInitialisedEvent, commandHistory);

        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), modelWithUserInitialisedEvent,
                commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first person deleted again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), modelWithUserInitialisedEvent,
                commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_deleteNotPossible_failure() {
        DeleteEventCommand deleteEventCommand = new DeleteEventCommand();

        // execution failed -> address book state not added into model
        assertCommandFailure(deleteEventCommand, modelWithoutEvent, commandHistory, MESSAGE_NO_EVENT_DETAILS);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), modelWithoutEvent, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), modelWithoutEvent, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }
}
