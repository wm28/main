package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
public class AddTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();
    private Set<Tag> tagsToAdd = new HashSet<>(Arrays.asList(new Tag("NORMAL"), new Tag("VIP")));
    private Set<Tag> allNewTags = new HashSet<>(Arrays.asList(new Tag("HUSBAND"), new Tag("TEST")));

    @Test
    public void execute_validSetOfTags_success() {
        AddTagCommand addTagCommand = new AddTagCommand(tagsToAdd);
        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADDED_TAG_SUCCESS, 7);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        for (Tag eachTagsToAdd: tagsToAdd) expectedModel.addTag(eachTagsToAdd);
        expectedModel.commitAddressBook();

        assertCommandSuccess(addTagCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * 1. Adds a set of tags to all persons in the filtered list.
     * 2. Undo the addition.
     * 3. The unfiltered list should be shown now. Verify that the set of tags of the all previous persons in the
     * unfiltered list is different from the set of tags at the filtered list.
     * 4. Redo the addition. This ensures {@code RedoCommand} adds the set of tags object.
     */
    @Test
    public void executeUndoRedo_validSetOfTags_success() throws Exception {
        AddTagCommand addTagCommand = new AddTagCommand(allNewTags);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        for (Tag eachTagsToAdd: allNewTags) expectedModel.addTag(eachTagsToAdd);
        expectedModel.commitAddressBook();

        // removeTag -> set of tags deleted
        addTagCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered person list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same tags added again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        AddTagCommand addTagFirstCommand = new AddTagCommand(tagsToAdd);
        AddTagCommand addTagSecondCommand = new AddTagCommand(allNewTags);

        // same object -> returns true
        assertTrue(addTagFirstCommand.equals(addTagFirstCommand));

        // not same values -> returns false
        AddTagCommand addTagFirstCommandCopy = new AddTagCommand(tagsToAdd);
        assertFalse(addTagFirstCommand.equals(addTagFirstCommandCopy));

        // different types -> returns false
        assertFalse(addTagFirstCommand.equals(new Tag("Veg")));

        // null -> returns false
        assertFalse(addTagFirstCommand.equals(new Tag("Test")));

        // different objects -> returns false
        assertFalse(addTagFirstCommand.equals(addTagSecondCommand));
    }
}
