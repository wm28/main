package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Payment;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Uid;
import seedu.address.model.tag.Tag;

//@@author kronicler

/**
 * Edits the details of an existing person in the address book.
 */
public abstract class GeneralMarkCommand extends Command {
    public static final String MESSAGE_MARK_PERSON_SUCCESS = "Marked Person as PRESENT: %1$s";
    public static final String MESSAGE_UNMARK_PERSON_SUCCESS = "Marked Person as ABSENT: %1$s";
    public static final String MESSAGE_UID_NOT_FOUND = "UID not found in the address book";
    public static final String MESSAGE_UID_DUPLICATE = "WARNING: There is more than one person with the same UID.";

    private final Uid uid;
    private Index index;
    private EditPersonDescriptor editPersonDescriptor;

    /**
     * @param uid of the person in the filtered person list to edit
     */
    public GeneralMarkCommand(Uid uid) {
        requireNonNull(uid);
        this.uid = uid;
    }

    /**
     * Scans through the list and compares the phone numbers to the one that is being searched
     * Assigns the index of the found person to the index of the command.
     * @param lastShownList {@code CommandHistory} which the command should operate on.
     * @throws CommandException if there are no matching persons in the list
     */
    public void retrieveIndex(List<Person> lastShownList) throws CommandException {
        int iterator = 0;
        int found = 0;
        int location = 0;
        for (Person p : lastShownList) {
            Uid temp = p.getUid();
            if (uid.equals(temp)) {
                found++;
                location = iterator;
            }
            iterator++;
        }
        if (found > 1) {
            throw new CommandException(MESSAGE_UID_DUPLICATE);
        }
        if (found == 0) {
            throw new CommandException(MESSAGE_UID_NOT_FOUND);
        }

        index = Index.fromZeroBased(location);
    }

    /**
     * Performs the task of attendance taking in the guest list.
     * This method handles both marking the person as Present or Absent.
     * @param model which the command should operate on to find the person's information
     * @throws CommandException when there is no such person with the identifier in the list
     */
    public CommandResult performAttendanceTaking(Model model, boolean isMark)
            throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        retrieveIndex(lastShownList);

        Person personToEdit = lastShownList.get(index.getZeroBased());

        if (isMark) {
            editPersonDescriptor = new EditPersonDescriptor("PRESENT");
        } else {
            editPersonDescriptor = new EditPersonDescriptor("ABSENT");
        }

        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);
        model.updatePerson(personToEdit, editedPerson);
        model.commitAddressBook();
        if (isMark) {
            return new CommandResult(String.format(MESSAGE_MARK_PERSON_SUCCESS, editedPerson));
        }
        return new CommandResult(String.format(MESSAGE_UNMARK_PERSON_SUCCESS, editedPerson));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    public Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = personToEdit.getName();
        Phone updatedPhone = personToEdit.getPhone();
        Email updatedEmail = personToEdit.getEmail();
        //@@author
        //@@author Sarah
        Payment updatedPayment = personToEdit.getPayment();
        //@@author
        //@@author kronicler
        Attendance updatedAttendance = editPersonDescriptor.getAttendance().orElse(personToEdit.getAttendance());
        Uid updatedUid = editPersonDescriptor.getUid().orElse(personToEdit.getUid());
        Set<Tag> updatedTags = personToEdit.getTags();

        return new Person(updatedName, updatedPhone, updatedEmail, updatedPayment,
                updatedAttendance, updatedUid, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instance of handles nulls
        if (!(other instanceof GeneralMarkCommand)) {
            return false;
        }

        // state check
        GeneralMarkCommand e = (GeneralMarkCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Payment payment;
        private Attendance attendance;
        private Uid uid;
        private Set<Tag> tags;

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(String updateAttendance) {
            setName(null);
            setPhone(null);
            setEmail(null);
            setPayment(null);
            setAttendance(new Attendance(updateAttendance));
            setUid(null);
            setTags(null);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }
        //@@author
        //@@author Sarah
        public void setPayment(Payment payment) {
            this.payment = payment;
        }

        public Optional<Payment> getPayment() {
            return Optional.ofNullable(payment);
        }
        //@@author
        //@@author kronicler
        public void setAttendance(Attendance attendance) {
            this.attendance = attendance;
        }

        public Optional<Attendance> getAttendance() {
            return Optional.ofNullable(attendance);
        }

        public void setUid(Uid uid) {
            this.uid = uid;
        }

        public Optional<Uid> getUid() {
            return Optional.ofNullable(uid);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getPayment().equals(e.getPayment())
                    && getAttendance().equals(e.getAttendance())
                    && getUid().equals(e.getUid())
                    && getTags().equals(e.getTags());
        }
    }
}
