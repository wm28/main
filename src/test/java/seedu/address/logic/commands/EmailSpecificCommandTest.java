package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.DUPLICATE_TAGS;
import static seedu.address.logic.commands.CommandTestUtil.NO_COMMON_TAGS;
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
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.TypicalPersons;
import seedu.address.testutil.stubs.ModelStub;

//@@author aaryamNUS
/**
 * Contains unit tests for {@code EmailSpecificCommand} using model stubs.
 * Note: as this command mainly relies on 3rd party APIs such as JavaMailAPI to function properly,
 * only a proportion of the methods of this command may be tested, without having to
 * create a mock-email server (a non-trivial task) to truly unit test the EmailSpecificCommand fully
 */
public class EmailSpecificCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullSetOfTags_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new EmailSpecificCommand(null);
    }

    @Test
    public void execute_noGuestHaveSpecifiedTags_throwsCommandException() {
        EmailSpecificCommand emailSpecificCommand = new EmailSpecificCommand(NO_COMMON_TAGS);
        String expectedMessage = Messages.MESSAGE_NO_PERSONS_WITH_TAGS;

        ModelContainingSameTagsStub modelStub = new ModelContainingSameTagsStub();
        ModelManager actualModel = new ModelManager(modelStub.getAddressBook(), new UserPrefs());

        assertCommandFailure(emailSpecificCommand, actualModel, commandHistory, expectedMessage);
    }

    @Test
    public void execute_emptyAddressBook_throwsCommandException() {
        EmailSpecificCommand emailSpecificCommand = new EmailSpecificCommand(NO_COMMON_TAGS);
        String expectedMessage = Messages.MESSAGE_EMPTY_LIST;

        ModelEmptyAddressBookStub modelStub = new ModelEmptyAddressBookStub();
        ModelManager actualModel = new ModelManager(modelStub.getAddressBook(), new UserPrefs());

        assertCommandFailure(emailSpecificCommand, actualModel, commandHistory, expectedMessage);
    }

    @Test
    public void equals() {
        EmailSpecificCommand emailSpecificFirstCommand = new EmailSpecificCommand(NO_COMMON_TAGS);
        EmailSpecificCommand emailSpecificSecondCommand = new EmailSpecificCommand(DUPLICATE_TAGS);

        // same object -> returns true
        assertEquals(emailSpecificFirstCommand, emailSpecificFirstCommand);

        // same values -> returns true
        EmailSpecificCommand emailSpecificFirstCommandCopy = new EmailSpecificCommand(NO_COMMON_TAGS);
        assertEquals(emailSpecificFirstCommand, emailSpecificFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(emailSpecificFirstCommand, new Tag("Veg"));

        // null -> returns false
        assertNotEquals(emailSpecificFirstCommand, new Tag("Test"));

        // different objects -> returns false
        assertNotEquals(emailSpecificFirstCommand, emailSpecificSecondCommand);
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
