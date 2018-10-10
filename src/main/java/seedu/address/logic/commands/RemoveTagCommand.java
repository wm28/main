package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Set;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

//@@author aaryamNUS
/**
 * Removes a set of tags from all the people in the current GuestList
 */
public class RemoveTagCommand extends Command {
    public static final String COMMAND_WORD = "removeTag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the specified tag "
            + "from all persons in the list.\n"
            + "Parameters: "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "VIP " + PREFIX_TAG + "Paid";

    public static final String MESSAGE_REMOVED_TAG_SUCCESS = "Successfully removed all tags from %1$d persons";
    public static final String MESSAGE_NO_PERSON_WITH_TAG = "No persons in the list have the specified tags";

    private int numberOfPeopleToChange = 0;
    private final Set<Tag> tagsToRemove;

    /**
     * @param tagsToRemove of the person in the filtered person list to edit
     */
    public RemoveTagCommand(Set<Tag> tagsToRemove) {
        requireNonNull(tagsToRemove);
        this.tagsToRemove = tagsToRemove;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        boolean needToChange;
        //This set will contain the current tags of each Person in the AddressBook
        Set<Tag> currentTags;

        requireNonNull(model);

        //This list represents the current filtered list in the Application
        List<Person> currentList = model.getFilteredPersonList();

        /**
         * currentAddressBookReadOnly is instantiated from the Model Interface, to give
         * an unmodifiable AddressBook. However, currentAddressBook uses the AddressBook API
         * to make give an editable AddressBook for the removeTag() function to properly execute
         */
        ReadOnlyAddressBook currentAddressBookReadOnly = model.getAddressBook();
        AddressBook currentAddressBook = new AddressBook(currentAddressBookReadOnly);

        /**
         * Nested loop below determines which of the guests in the current list will have
         * one or more of their tags removed, and which will not have any change at all.
         */
        for (Person personToBeEdited : currentList) {
            currentTags = personToBeEdited.getTags();
            needToChange = false;

            for (Tag tagToBeRemoved: tagsToRemove) {
                if (currentTags.contains(tagToBeRemoved)) {
                    needToChange = true;
                    break;
                }
            }

            if (needToChange) {
                numberOfPeopleToChange++;
            }
        }

        /**
         * The following code snippet uses the removeTag(Tag) method in the
         * modified AddressBook API to modify the currentAddressBook and remove
         * the specified tags from each person
         */
        for (Tag tagToBeRemoved: tagsToRemove) {
            currentAddressBook.removeTag(tagToBeRemoved);
        }

        /**
         * Resets the data of the application with the most updated AddressBook
         * in order to highlight the removal of tags once all steps are complete.
         * More importantly, the AddressBook is committed to allow undo/redo commands
         * to properly work
         */
        model.resetData(currentAddressBook);
        model.commitAddressBook();

        /**
         * If the number of guests whose tags have changed is zero, a
         * command exception should be specified to notify the user to
         * key in another tag that they would like to remove
         */
        if (numberOfPeopleToChange == 0) {
            throw new CommandException(MESSAGE_NO_PERSON_WITH_TAG);
        } else {
            return new CommandResult(String.format(MESSAGE_REMOVED_TAG_SUCCESS, numberOfPeopleToChange));
        }
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
        RemoveTagCommand e = (RemoveTagCommand) other;
        return tagsToRemove.equals(e.tagsToRemove);
    }
}
