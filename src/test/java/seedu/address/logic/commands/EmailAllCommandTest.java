package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.AddressBook;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.stubs.ModelStub;

//@@author aaryamNUS
/**
 * Contains unit tests for {@code EmailAllCommandTest} using model stubs.
 * Note: as this command mainly relies on 3rd party APIs such as JavaMailAPI to function properly,
 * only a proportion of the methods of this command may be tested, without having to
 * create a mock-email server (a non-trivial task) to truly unit test the EmailAllCommand fully
 */
public class EmailAllCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_emptyAddressBook_throwsCommandException() {
        EmailAllCommand emailAllCommand = new EmailAllCommand();
        String expectedMessage = Messages.MESSAGE_EMPTY_LIST;

        ModelEmptyAddressBookStub modelStub = new ModelEmptyAddressBookStub();
        ModelManager actualModel = new ModelManager(modelStub.getAddressBook(), new UserPrefs());

        assertCommandFailure(emailAllCommand, actualModel, commandHistory, expectedMessage);
    }

    @Test
    public void equals() {
        EmailAllCommand emailAllFirstCommand = new EmailAllCommand();
        EmailAllCommand emailAllSecondCommand = new EmailAllCommand();

        // same object -> returns true
        assertEquals(emailAllFirstCommand, emailAllFirstCommand);

        // different types -> returns false
        assertNotEquals(emailAllFirstCommand, new Tag("garbage"));

        // null -> returns false
        assertNotEquals(emailAllFirstCommand, new Tag("extracharacters"));

        // same objects -> returns true
        assertEquals(emailAllFirstCommand, emailAllSecondCommand);
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
