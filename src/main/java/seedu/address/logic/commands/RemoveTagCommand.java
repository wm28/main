package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

//@@author aaryamNUS
/**
 * Removes a particular tag from all guests in the current list
 */
public class RemoveTagCommand extends Command {

    public static final String COMMAND_WORD = "removetag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "Deletes the specified tag of all entries in the list. "
            + "Parameters: t/tag t/... (multiple tags can be specified) "
            + PREFIX_TAG + "[TAG]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_TAG + "Veg";

    public static final String MESSAGE_ARGUMENTS = "Tag: %2$s";

    private final String tag;


    /**
     * @tag to be removed from guestlist
     */
    public RemoveTagCommand(String tag) {
        requireAllNonNull(tag);

        this.tag = tag;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        throw new CommandException(String.format(MESSAGE_ARGUMENTS, tag));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if the same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if(!(other instanceof RemoveTagCommand)) {
            return false;
        }

        // state check
        RemoveTagCommand e = (RemoveTagCommand) other;
        return tag.equals(e.tag);
    }
}
