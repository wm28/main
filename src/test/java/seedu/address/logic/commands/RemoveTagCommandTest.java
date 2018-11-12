package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.NO_COMMON_TAGS;
import static seedu.address.logic.commands.CommandTestUtil.TAGS_TO_REMOVE;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.TypicalPersons;
import seedu.address.testutil.stubs.ModelStub;

//@@author aaryamNUS
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code RemoveTagCommand} using model stubs.
 */
public class RemoveTagCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullSetOfTags_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new RemoveTagCommand(null);
    }

    @Test
    public void execute_validSetOfTags_success() {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(TAGS_TO_REMOVE);
        String expectedMessage = String.format(RemoveTagCommand.MESSAGE_REMOVED_TAG_SUCCESS, 2);

        ModelStubContainingTypicalPersons modelStub = new ModelStubContainingTypicalPersons();
        ModelManager expectedModel = new ModelManager(modelStub.getAddressBook(), new UserPrefs());
        for (Tag eachTagsToDelete: TAGS_TO_REMOVE) {
            expectedModel.deleteTag(eachTagsToDelete);
        }
        expectedModel.commitAddressBook();

        assertCommandSuccess(removeTagCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noPersonWithTags_throwsCommandException() {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(NO_COMMON_TAGS);
        String expectedMessage = RemoveTagCommand.MESSAGE_NO_PERSON_WITH_TAG;

        ModelStubContainingTypicalPersons modelStub = new ModelStubContainingTypicalPersons();
        ModelManager actualModel = new ModelManager(modelStub.getAddressBook(), new UserPrefs());

        assertCommandFailure(removeTagCommand, actualModel, commandHistory, expectedMessage);
    }

    @Test
    public void execute_emptyAddressBook_throwsCommandException() {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(NO_COMMON_TAGS);
        String expectedMessage = RemoveTagCommand.MESSAGE_NO_PERSON_WITH_TAG;

        ModelEmptyAddressBookStub modelStub = new ModelEmptyAddressBookStub();
        ModelManager actualModel = new ModelManager(modelStub.getAddressBook(), new UserPrefs());

        assertCommandFailure(removeTagCommand, actualModel, commandHistory, expectedMessage);
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
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(TAGS_TO_REMOVE);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        for (Tag eachTagsToDelete: TAGS_TO_REMOVE) {
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
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(NO_COMMON_TAGS);
        String expectedMessage = RemoveTagCommand.MESSAGE_NO_PERSON_WITH_TAG;

        // execution failed -> address book state not added into model
        assertCommandFailure(removeTagCommand, model, commandHistory, expectedMessage);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        RemoveTagCommand removeTagFirstCommand = new RemoveTagCommand(NO_COMMON_TAGS);
        RemoveTagCommand removeTagSecondCommand = new RemoveTagCommand(TAGS_TO_REMOVE);

        // same object -> returns true
        assertEquals(removeTagFirstCommand, removeTagFirstCommand);

        // not same values -> returns false
        RemoveTagCommand removeTagFirstCommandCopy = new RemoveTagCommand(NO_COMMON_TAGS);
        assertEquals(removeTagFirstCommand, removeTagFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(removeTagFirstCommand, new Tag("Veg"));

        // null -> returns false
        assertNotEquals(removeTagFirstCommand, new Tag("Test"));

        // different objects -> returns false
        assertNotEquals(removeTagFirstCommand, removeTagSecondCommand);
    }

    /**
     * A Model stub containing TypicalPersons that returns an AddressBook to be
     * used in various tests
     */
    private class ModelStubContainingTypicalPersons extends ModelStub {
        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableList(TypicalPersons.getTypicalPersons());
        }

        @Override
        public void deleteTag(Tag tag) {
            requireNonNull(tag);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return TypicalPersons.getTypicalAddressBook();
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            // called by {@code RemoveTagCommand#execute()}
        }

        @Override
        public void commitAddressBook() {
            // called by {@code RemoveTagCommand#execute()}
        }
    }

    /**
     * A Model stub that contains an empty AddressBook, and hence no guests
     */
    private class ModelEmptyAddressBookStub extends ModelStub {
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
