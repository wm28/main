package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_UNUSED;
import static seedu.address.logic.commands.CommandTestUtil.allNewTags;
import static seedu.address.logic.commands.CommandTestUtil.allSameTags;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.duplicateTags;
import static seedu.address.logic.commands.CommandTestUtil.tagsToAdd;
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
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.TypicalPersons;
import seedu.address.testutil.stubs.ModelStub;

//@@author aaryamNUS
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code AddTagCommand} using model stubs.
 */
public class AddTagCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullSetOfTags_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddTagCommand(null);
    }

    @Test
    public void execute_validSetOfTags_success() {
        AddTagCommand addTagCommand = new AddTagCommand(tagsToAdd);
        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADDED_TAG_SUCCESS, 7);

        ModelStubContainingTypicalPersons modelStub = new ModelStubContainingTypicalPersons();
        ModelManager expectedModel = new ModelManager(modelStub.getAddressBook(), new UserPrefs());
        for (Tag eachTagsToAdd: tagsToAdd) {
            expectedModel.addTag(eachTagsToAdd);
        }
        expectedModel.commitAddressBook();

        assertCommandSuccess(addTagCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateSetOfValidTags_success() {
        AddTagCommand addTagCommand = new AddTagCommand(duplicateTags);
        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADDED_TAG_SUCCESS, 7);

        ModelStubContainingTypicalPersons modelStub = new ModelStubContainingTypicalPersons();
        ModelManager expectedModel = new ModelManager(modelStub.getAddressBook(), new UserPrefs());
        expectedModel.addTag(new Tag(VALID_TAG_UNUSED));
        expectedModel.commitAddressBook();

        assertCommandSuccess(addTagCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noNewTagAdded_throwsCommandException() {
        AddTagCommand addTagCommand = new AddTagCommand(allSameTags);
        String expectedMessage = AddTagCommand.MESSAGE_NO_PERSON_TO_ADD_TAG;

        ModelContainingSameTagsStub modelStub = new ModelContainingSameTagsStub();
        ModelManager actualModel = new ModelManager(modelStub.getAddressBook(), new UserPrefs());

        assertCommandFailure(addTagCommand, actualModel, commandHistory, expectedMessage);
    }

    @Test
    public void execute_emptyAddressBook_throwsCommandException() {
        AddTagCommand addTagCommand = new AddTagCommand(tagsToAdd);
        String expectedMessage = AddTagCommand.MESSAGE_NO_PERSON_IN_LIST;

        ModelEmptyAddressBookStub modelStub = new ModelEmptyAddressBookStub();
        ModelManager actualModel = new ModelManager(modelStub.getAddressBook(), new UserPrefs());

        assertCommandFailure(addTagCommand, actualModel, commandHistory, expectedMessage);
    }

    /**
     * 1. Adds a set of tags to all persons in the filtered list.
     * 2. Undo the addition.
     * 3. The unfiltered list should be shown now. Verify that the set of tags of the all previous persons in the
     *    unfiltered list is different from the set of tags at the filtered list.
     * 4. Redo the addition. This ensures {@code RedoCommand} adds the set of tags object.
     */
    @Test
    public void executeUndoRedo_validSetOfTags_success() throws Exception {
        AddTagCommand addTagCommand = new AddTagCommand(allNewTags);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        for (Tag eachTagsToAdd: allNewTags) {
            expectedModel.addTag(eachTagsToAdd);
        }
        expectedModel.commitAddressBook();

        // addTag -> set of tags added
        addTagCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered guest list to show all guests
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
        assertEquals(addTagFirstCommand, addTagFirstCommand);

        // same values -> returns true
        AddTagCommand addTagFirstCommandCopy = new AddTagCommand(tagsToAdd);
        assertEquals(addTagFirstCommand, addTagFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(addTagFirstCommand, new Tag("Veg"));

        // null -> returns false
        assertNotEquals(addTagFirstCommand, new Tag("Test"));

        // different objects -> returns false
        assertNotEquals(addTagFirstCommand, addTagSecondCommand);
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
        public void addTag(Tag tag) {
            requireNonNull(tag);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return TypicalPersons.getTypicalAddressBook();
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            // called by {@code AddTagCommand#execute()}
        }

        @Override
        public void commitAddressBook() {
            // called by {@code AddTagCommand#execute()}
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

    /**
     * A Model stub that contains guests with the same tags in its AddressBook
     */
    private class ModelContainingSameTagsStub extends ModelStub {
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBookBuilder().withPerson(TypicalPersons.AMY)
                    .withPerson(TypicalPersons.BOB).withPerson(TypicalPersons.DANNY).build();
        }
    }
}
