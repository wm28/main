package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;

//@@author aaryamNUS
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code RemoveTagCommand}.
 */
public class RemoveTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();
    private Set<Tag> tagsToRemove = new HashSet<>(Arrays.asList(new Tag("NORMAL"), new Tag("VIP")));
    private Set<Tag> noCommonTags = new HashSet<>(Arrays.asList(new Tag("Unused"), new Tag("Invalid")));

    @Test
    public void execute_validSetOfTags_success() {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(tagsToRemove);
        String expectedMessage = String.format(RemoveTagCommand.MESSAGE_REMOVED_TAG_SUCCESS, 2);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        for (Tag eachTagsToDelete: tagsToRemove) {
            expectedModel.deleteTag(eachTagsToDelete);
        }
        expectedModel.commitAddressBook();

        assertCommandSuccess(removeTagCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noPersonWithTags_throwsCommandException() {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(noCommonTags);
        String expectedMessage = RemoveTagCommand.MESSAGE_NO_PERSON_WITH_TAG;

        assertCommandFailure(removeTagCommand, model, commandHistory, expectedMessage);
    }

    /**
     * 1. Deletes a set of tags from all persons in the filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the set of tags of the all previous peerons in the
     * unfiltered list is different from the set of tags at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the set of tags object.
     */
    @Test
    public void executeUndoRedo_validSetOfTags_success() throws Exception {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(tagsToRemove);
        String expectedMessage = String.format(RemoveTagCommand.MESSAGE_REMOVED_TAG_SUCCESS, 2);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        for (Tag eachTagsToDelete: tagsToRemove) {
            expectedModel.deleteTag(eachTagsToDelete);
        }
        expectedModel.commitAddressBook();

        // removeTag -> set of tags deleted
        removeTagCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered person list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same tags removed again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_noPersonsWithTags_failure() {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(noCommonTags);
        String expectedMessage = RemoveTagCommand.MESSAGE_NO_PERSON_WITH_TAG;

        // execution failed -> address book state not added into model
        assertCommandFailure(removeTagCommand, model, commandHistory, expectedMessage);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        RemoveTagCommand removeTagFirstCommand = new RemoveTagCommand(noCommonTags);
        RemoveTagCommand removeTagSecondCommand = new RemoveTagCommand(tagsToRemove);

        // same object -> returns true
        assertTrue(removeTagFirstCommand.equals(removeTagFirstCommand));

        // not same values -> returns false
        RemoveTagCommand removeTagFirstCommandCopy = new RemoveTagCommand(noCommonTags);
        assertFalse(removeTagFirstCommand.equals(removeTagFirstCommandCopy));

        // different types -> returns false
        assertFalse(removeTagFirstCommand.equals(new Tag("Veg")));

        // null -> returns false
        assertFalse(removeTagFirstCommand.equals(new Tag("Test")));

        // different objects -> returns false
        assertFalse(removeTagFirstCommand.equals(removeTagSecondCommand));
    }
}
