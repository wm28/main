package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

//@@author aaryamNUS
/**
 * Removes a set of tags from all the people in the current GuestBook
 */
public class RemoveTagCommand extends Command {
    public static final String COMMAND_WORD = "removeTag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the specified tag "
            + "from all persons in the list.\n"
            + "Parameters: "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "VIP " + PREFIX_TAG + "Paid";

    private static final String MESSAGE_REMOVED_TAG_SUCCESS = "Successfully removed all tags from %1$d persons";
    private static final String MESSAGE_NO_PERSON_WITH_TAG = "No persons in the list have the specified tags";

    private static Logger logger = Logger.getLogger("calculateNumberOfPeopleToChange");
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
        requireNonNull(model);

        List<Person> currentList = model.getFilteredPersonList();
        ReadOnlyAddressBook currentAddressBookReadOnly = model.getAddressBook();

        // Uses edited AddressBook Model to make an editable AddressBook for removeTag() to work
        AddressBook currentAddressBook = new AddressBook(currentAddressBookReadOnly);

        // Calculates number of guests to change, and performs the removeTag() command afterwards
        calculateNumberOfPeopleToChange(currentList);
        removeTags(model, currentAddressBook);

        return new CommandResult(String.format(MESSAGE_REMOVED_TAG_SUCCESS, numberOfPeopleToChange));
    }

    /**
     * Calculates how many people in the list have at least one tag matching with the set of
     * tags to be removed.
     * @param currentList the current list of guests
     */
    private void calculateNumberOfPeopleToChange(List<Person> currentList) {
        assert numberOfPeopleToChange == 0 : "numberOfPeopleToChange should start at 0";

        Set<Tag> currentTags;

        for (Person personToBeEdited : currentList) {
            currentTags = personToBeEdited.getTags();
            for (Tag tagToBeRemoved: tagsToRemove) {
                try {
                    if (currentTags.contains(tagToBeRemoved)) {
                        numberOfPeopleToChange++;
                        break;
                    }
                } catch (IllegalArgumentException ex) {
                    logger.log(Level.WARNING, "Incorrect format for tags", ex);
                }
            }
        }
    }

    /**
     * Performs the remove tag function by removing a set of tags from all guests in the
     * guest list and then updated the model addressBook accordingly
     */
    private void removeTags(Model model, AddressBook currentAddressBook) throws CommandException {
        if (numberOfPeopleToChange == 0) {
            throw new CommandException(MESSAGE_NO_PERSON_WITH_TAG);
        } else {
            for (Tag tagToBeRemoved: tagsToRemove) {
                currentAddressBook.removeTag(tagToBeRemoved);
            }
            logger.log(Level.INFO, "All tags removed successfully");

            model.resetData(currentAddressBook);
            model.commitAddressBook();
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof RemoveTagCommand)) {
            return false;
        }
        // state check
        RemoveTagCommand e = (RemoveTagCommand) other;
        return tagsToRemove.equals(e.tagsToRemove);
    }
}
