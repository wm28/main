package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

//@@author SandhyaGopakumar
/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteEventCommand extends Command {

    public static final String COMMAND_WORD = "delete_event";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the event in the addressbook currently.\n"
            + "Parameters: none\n"
            + "Please ensure you don't enter any characters after the command word!\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_DELETE_EVENT_SUCCESS = "Deleted event details.";
    public static final String MESSAGE_NO_EVENT_DETAILS = "Event details have not been put in yet.";


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (!model.hasEvent()) {
            throw new CommandException(MESSAGE_NO_EVENT_DETAILS);
        }

        model.deleteEvent();
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_DELETE_EVENT_SUCCESS));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteEventCommand); // state check
    }
}
