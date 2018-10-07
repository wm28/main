package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Set;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Removes a particular tag to all of the people in the current GuestList
 */
public class RemoveTagCommand extends Command {
    public static final String COMMAND_WORD = "removeTag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the specified tag from all persons in the list.\n"
            + "Parameters: "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "VIP " + PREFIX_TAG + "Paid";

    public static final String MESSAGE_REMOVED_TAG_SUCCESS = "Successfully removed tag %1$s from %2$s persons";
    public static final String MESSAGE_NO_PERSON_WITH_TAG = "No persons in the list have the tag %1$s";

    private int numberOfPeopleToChange = 0;
    private final Tag tag;

    /**
     * @param tag of the person in the filtered person list to edit
     */
    public RemoveTagCommand(Tag tag) {
        requireNonNull(tag);
        this.tag = tag;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        boolean needToChange;
        Set<Tag> currentTags;

        requireNonNull(model);
        List<Person> currentList = model.getFilteredPersonList();

        for (Person personToBeEdited : currentList) {
            currentTags = personToBeEdited.getTags();

            if (currentTags.contains(tag)) {
                currentTags.remove(tag);
                needToChange = true;
            }
            else {
                needToChange = false;
            }

            if (needToChange) {
                numberOfPeopleToChange++;
                Person editedPerson = createEditedPerson(personToBeEdited, currentTags);
                model.updatePerson(personToBeEdited, editedPerson);
                model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
                model.commitAddressBook();
            }
            else {}
        }

        /*
         * If the number of guests whose tags have changed is zero, a
         * command exception should be specified to notify the user to
         * key in another tag that they would like to remove
         */
        if (numberOfPeopleToChange == 0) {
            throw new CommandException(String.format(MESSAGE_NO_PERSON_WITH_TAG, tag));
        }

        return new CommandResult(String.format(MESSAGE_REMOVED_TAG_SUCCESS, tag, numberOfPeopleToChange));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, Set<Tag> currentTags) {
        assert personToEdit != null;
        Name updatedName = personToEdit.getName();
        Phone updatedPhone = personToEdit.getPhone();
        Email updatedEmail = personToEdit.getEmail();
        Attendance updatedAttendance = personToEdit.getAttendance();
        Set<Tag> updatedTags = currentTags;

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAttendance, updatedTags);
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
        return tag.equals(e.tag);
    }
}