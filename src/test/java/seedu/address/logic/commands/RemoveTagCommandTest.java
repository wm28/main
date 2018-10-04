package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.RemoveTagCommand.MESSAGE_ARGUMENTS;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

//@@author aaryamNUS
/**
 * Contains integration tests (interaction with the model) and unit tests for the RemoveTagCommand.
 */
public class RemoveTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());


    @Test
    public void execute() {
        final String tag = "Some tag";

        assertCommandFailure(new RemoveTagCommand(tag), model, new CommandHistory(),
                String.format(MESSAGE_ARGUMENTS, tag));
    }

    @Test
    public void equals() {
        final RemoveTagCommand standardCommand = new RemoveTagCommand(VALID_TAG_HUSBAND);

        // same values --> returns true
        RemoveTagCommand commandWithSameValues = new RemoveTagCommand(VALID_TAG_HUSBAND);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object --> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null --> returns false
        assertFalse(standardCommand.equals(null));

        // different types --> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different tag --> returns false
        assertFalse(standardCommand.equals(new RemoveTagCommand(VALID_TAG_FRIEND)));
    }
}
