package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_NAME;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_START_TIME;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TAG;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBookWithEvent;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.event.Event;
import seedu.address.testutil.EditEventDetailsBuilder;
import seedu.address.testutil.EventBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditEventCommandTest {

    private Model modelWithUserInitialisedEvent = new ModelManager(getTypicalAddressBookWithEvent(), new UserPrefs());
    private Model modelWithoutEvent = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedEventInitialised_success() {
        Event editedEvent = new EventBuilder().build();
        EditEventCommand.EditEventDetails eventDetails = new EditEventDetailsBuilder(editedEvent).build();
        EditEventCommand editEventCommand = new EditEventCommand(eventDetails);

        String expectedMessage = String.format(EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, editedEvent);

        Model expectedModel = new ModelManager(new AddressBook(
                modelWithUserInitialisedEvent.getAddressBook()), new UserPrefs());
        expectedModel.updateEvent(modelWithUserInitialisedEvent.getEventDetails());
        expectedModel.commitAddressBook();

        assertCommandSuccess(editEventCommand, modelWithUserInitialisedEvent,
                commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedEventInitialised_success() {
        Event existingEvent = modelWithUserInitialisedEvent.getEventDetails();
        EventBuilder eventBuilder = new EventBuilder(existingEvent);
        Event editedEvent = eventBuilder.withEventName(VALID_EVENT_NAME)
                .withEventStartTime(VALID_EVENT_START_TIME).withEventTags(VALID_EVENT_TAG).build();

        EditEventCommand.EditEventDetails eventDetails = new EditEventDetailsBuilder()
                .withEventName(VALID_EVENT_NAME).withEventStartTime(VALID_EVENT_START_TIME)
                .withEventTags(VALID_EVENT_TAG).build();
        EditEventCommand editEventCommand = new EditEventCommand(eventDetails);

        String expectedMessage = String.format(EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, editedEvent);

        Model expectedModel = new ModelManager(new AddressBook(
                modelWithUserInitialisedEvent.getAddressBook()), new UserPrefs());
        expectedModel.updateEvent(editedEvent);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editEventCommand, modelWithUserInitialisedEvent,
                commandHistory, expectedMessage, expectedModel);
    }
}
