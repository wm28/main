package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_OUT_OF_BOUNDS;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.AddressBook;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.TypicalPersons;
import seedu.address.testutil.stubs.ModelStub;

//@@author aaryamNUS
/**
 * Contains unit tests for {@code MailCommand} using model stubs.
 * Note: as this command mainly relies on 3rd party APIs such as JavaMailAPI to function properly,
 * only a proportion of the methods of this command may be tested, without having to
 * create a mock-email server (a non-trivial task) to truly unit test the MailCommand fully
 */
public class MailCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullSetOfTags_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new MailCommand(null);
    }

    @Test
    public void execute_outOfBoundsIndex_throwsCommandException() throws Exception{
        MailCommand mailCommand = new MailCommand(INDEX_OUT_OF_BOUNDS);
        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

        ModelThreePersonsStub modelStub = new ModelThreePersonsStub();
        ModelManager actualModel = new ModelManager(modelStub.getAddressBook(), new UserPrefs());

        assertCommandFailure(mailCommand, actualModel, commandHistory, expectedMessage);
    }

    @Test
    public void execute_emptyAddressBook_throwsCommandException() throws Exception{
        MailCommand mailCommand = new MailCommand(INDEX_FIRST_PERSON);
        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

        ModelEmptyAddressBookStub modelStub = new ModelEmptyAddressBookStub();
        ModelManager actualModel = new ModelManager(modelStub.getAddressBook(), new UserPrefs());

        assertCommandFailure(mailCommand, actualModel, commandHistory, expectedMessage);
    }

    @Test
    public void equals(){
        MailCommand mailFirstCommand = new MailCommand(INDEX_FIRST_PERSON);
        MailCommand mailSecondCommand = new MailCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertEquals(mailFirstCommand, mailFirstCommand);

        // same values -> returns true
        MailCommand mailFirstCommandCopy = new MailCommand(INDEX_FIRST_PERSON);
        assertEquals(mailFirstCommand, mailFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(mailFirstCommand, INDEX_FIRST_PERSON);

        // different objects -> returns false
        assertNotEquals(mailFirstCommand, mailSecondCommand);
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
     * A Model stub that contains only three guests
     */
    private class ModelThreePersonsStub extends ModelStub {
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBookBuilder().withPerson(TypicalPersons.AMY)
                    .withPerson(TypicalPersons.BOB).withPerson(TypicalPersons.DANNY).build();
        }
    }
}
