package seedu.address.logic.commands;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Payment;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Uid;
import seedu.address.model.tag.Tag;

//@@author kronicler
/**
 * Edits the details of an existing person in the address book.
 */
public class MarkCommand extends GeneralMarkCommand {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks a person as present "
            + "using their unique UID. "
            + "This will also change the attendance associated with the person to Present.\n"
            + "Parameters: "
            + "[UID] "
            + "Example: " + COMMAND_WORD
            + " 708944";

    private final Uid uid;
    private Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param uid of the person in the filtered person list to edit
     */
    public MarkCommand(Uid uid) {
        super(uid);
        this.uid = uid;
        this.editPersonDescriptor = new EditPersonDescriptor();
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        return super.performAttendanceTaking(model, true);
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
        MarkCommand e = (MarkCommand) other;
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
        public EditPersonDescriptor() {
            setName(null);
            setPhone(null);
            setEmail(null);
            setPayment(null);
            setAttendance(new Attendance("PRESENT"));
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
