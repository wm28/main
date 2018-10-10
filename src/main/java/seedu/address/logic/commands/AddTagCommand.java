package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.tag.Tag;

//@@author aaryamNUS
/**
 * Adds a set of tags from all the people in the current GuestList
 */
public class AddTagCommand extends Command {
    public static final String COMMAND_WORD = "addTag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds the specified tags from all " +
            "persons in the list.\n"
            + "Parameters: "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "VIP " + PREFIX_TAG + "Paid";

    public static final String MESSAGE_REMOVED_TAG_SUCCESS = "Successfully added all tags to all persons";

    private final Set<Tag> tagsToAdd;

    /**
     * @param tagsToAdd of the person in the filtered person list to edit
     */
    public AddTagCommand(Set<Tag> tagsToAdd) {
        requireNonNull(tagsToAdd);
        this.tagsToAdd = tagsToAdd;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        /**
         * currentAddressBookReadOnly is instantiated from the Model Interface, to give
         * an unmodifiable AddressBook. However, currentAddressBook uses the AddressBook API
         * to make give an editable AddressBook for the addTag() function to properly execute
         */
        ReadOnlyAddressBook currentAddressBookReadOnly = model.getAddressBook();
        AddressBook currentAddressBook = new AddressBook(currentAddressBookReadOnly);

        /**
         * The following code snippet uses the addTag(Tag) method in the
         * modified AddressBook API to modify the currentAddressBook and add
         * the specified tags to each person
         */
        for (Tag tagToBeAdded: tagsToAdd) {
            currentAddressBook.addTag(tagToBeAdded);
        }

        /**
         * Resets the data of the application with the most updated AddressBook
         * in order to highlight the removal of tags once all steps are complete.
         * More importantly, the AddressBook is committed to allow undo/redo commands
         * to properly work
         */
        model.resetData(currentAddressBook);
        model.commitAddressBook();

        return new CommandResult(MESSAGE_REMOVED_TAG_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof MarkCommand)) {
            return false;
        }
        // state check
        AddTagCommand e = (AddTagCommand) other;
        return tagsToAdd.equals(e.tagsToAdd);
    }
}
