package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VENUE;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;

//@@author SandhyaGopakumar
/**
 * Adds an event to the application.
 */
public class AddEventCommand extends Command {
    public static final String COMMAND_WORD = "add_event";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an event to the application. "
            + "Parameters: "
            + PREFIX_NAME + "NAME " + " "
            + PREFIX_DATE + "DATE" + " "
            + PREFIX_VENUE + "VENUE" + " "
            + PREFIX_START_TIME + "START TIME" + " "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Wedding " + " "
            + PREFIX_DATE + "10/10/2018 " + " "
            + PREFIX_VENUE + "Mandarin Hotel, 5th floor, Room 1A" + " "
            + PREFIX_START_TIME + "10:00 AM" + " "
            + PREFIX_TAG + "ClassicTheme";
    public static final String MESSAGE_SUCCESS = "New event added: %1$s";
    public static final String MESSAGE_DUPLICATE_EVENT = "An event already exists in the application";
    private final Event toAdd;
    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddEventCommand(Event event) {
        requireNonNull(event);
        toAdd = event;
    }
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (model.hasEvent()) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }
        model.addEvent(toAdd);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddEventCommand // instanceof handles nulls
                && toAdd.equals(((AddEventCommand) other).toAdd));
    }
}
