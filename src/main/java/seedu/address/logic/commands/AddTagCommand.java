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
 * Adds a set of tags from all the people in the current GuestBook
 */
public class AddTagCommand extends Command {
    public static final String COMMAND_WORD = "addTag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds the specified tags from all "
            + "persons in the list.\n"
            + "Parameters: "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "VIP " + PREFIX_TAG + "Paid";
    static final String MESSAGE_ADDED_TAG_SUCCESS = "Successfully added all tags to %1$d persons";

    private static final String MESSAGE_NO_PERSON_IN_LIST = "No persons in the list!";
    private static final String MESSAGE_NO_PERSON_TO_ADD_TAG = "All existing persons in the list " +
                                                               "already have the specified tags!";
    private static Logger logger = Logger.getLogger("execute");
    private final Set<Tag> tagsToAdd;
    private int numberOfPeopleToAddTags = 0;

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

        ReadOnlyAddressBook currentAddressBookReadOnly = model.getAddressBook();

        // Uses edited AddressBook Model to make an editable AddressBook for addTag() to work
        AddressBook currentAddressBook = new AddressBook(currentAddressBookReadOnly);
        List<Person> currentList = model.getFilteredPersonList();

        if (currentList.isEmpty()) {
            throw new CommandException(MESSAGE_NO_PERSON_IN_LIST);
        } else {
            if (calculateNumberOfPeopleToAddTags(currentList) == 0) {
                throw new CommandException(MESSAGE_NO_PERSON_TO_ADD_TAG);
            }

            for (Tag tagToBeAdded: tagsToAdd) {
                currentAddressBook.addTag(tagToBeAdded);
            }
            logger.log(Level.INFO, "All tags added successfully");
            model.resetData(currentAddressBook);
            model.commitAddressBook();

            return new CommandResult(String.format(MESSAGE_ADDED_TAG_SUCCESS, numberOfPeopleToAddTags));
        }
    }

    /**
     * Calculates how many people in the list already have all of the tags specified
     * @param currentList the current list of guests
     */
    private int calculateNumberOfPeopleToAddTags(List<Person> currentList) {
        assert numberOfPeopleToAddTags == 0 : "Number of people to add tags to should be 0 initially!";

        Set<Tag> currentTags;

        for (Person personToBeEdited : currentList) {
            currentTags = personToBeEdited.getTags();
            for (Tag tagsToBeAdded: tagsToAdd) {
                try {
                    if (!currentTags.contains(tagsToBeAdded)) {
                        numberOfPeopleToAddTags++;
                        break;
                    }
                } catch (IllegalArgumentException ex) {
                    logger.log(Level.WARNING, "Incorrect format for tags", ex);
                }
            }
        }

        return numberOfPeopleToAddTags;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof AddTagCommand)) {
            return false;
        }
        // state check
        AddTagCommand e = (AddTagCommand) other;
        return tagsToAdd.equals(e.tagsToAdd);
    }
}
