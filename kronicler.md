# kronicler
###### \java\seedu\address\logic\commands\GeneralMarkCommand.java
``` java

/**
 * Edits the details of an existing person in the address book.
 */
public abstract class GeneralMarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks a person as present "
            + "using their unique phone number. "
            + "This will also change the a/ tag associated with the person to Present.\n"
            + "Parameters: "
            + "[PHONE] "
            + "Example: " + COMMAND_WORD
            + " 91234567";

    public static final String MESSAGE_MARK_PERSON_SUCCESS = "Marked Person as PRESENT: %1$s";
    public static final String MESSAGE_UNMARK_PERSON_SUCCESS = "Marked Person as ABSENT: %1$s";
    public static final String MESSAGE_NOT_EDITED = "Phone number not found in the address book";

    private final Phone phone;
    private Index index;
    private EditPersonDescriptor editPersonDescriptor;

    /**
     * @param phone of the person in the filtered person list to edit
     */
    public GeneralMarkCommand(Phone phone) {
        requireNonNull(phone);
        this.phone = phone;
    }

    /**
     * Scans through the list and compares the phone numbers to the one that is being searched
     * Assigns the index of the found person to the index of the command.
     * @param lastShownList {@code CommandHistory} which the command should operate on.
     * @throws CommandException if there are no matching persons in the list
     */
    public void retrieveIndex(List<Person> lastShownList) throws CommandException {
        int x = 0;
        boolean isNotFound = true;
        for (Person p : lastShownList) {
            Phone temp = p.getPhone();
            if (phone.equals(temp)) {
                isNotFound = false;
                break;
            }
            x++;
        }
        if (isNotFound) {
            throw new CommandException(MESSAGE_NOT_EDITED);
        }

        index = Index.fromZeroBased(x);
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
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
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
```
###### \java\seedu\address\logic\commands\GeneralMarkCommand.java
``` java
        Attendance updatedAttendance = editPersonDescriptor.getAttendance().orElse(personToEdit.getAttendance());
        Set<Tag> updatedTags = personToEdit.getTags();

        return new Person(updatedName, updatedPhone, updatedEmail, updatedPayment,
                updatedAttendance, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
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
```
###### \java\seedu\address\logic\commands\GeneralMarkCommand.java
``` java
        public void setAttendance(Attendance attendance) {
            this.attendance = attendance;
        }

        public Optional<Attendance> getAttendance() {
            return Optional.ofNullable(attendance);
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
                    && getTags().equals(e.getTags());
        }
    }
}
```
###### \java\seedu\address\logic\commands\MarkCommand.java
``` java
/**
 * Edits the details of an existing person in the address book.
 */
public class MarkCommand extends GeneralMarkCommand {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks a person as present "
            + "using their unique phone number. "
            + "This will also change the a/ tag associated with the person to Present.\n"
            + "Parameters: "
            + "[PHONE] "
            + "Example: " + COMMAND_WORD
            + " 91234567";

    public static final String MESSAGE_MARK_PERSON_SUCCESS = "Marked Person as PRESENT: %1$s";
    public static final String MESSAGE_NOT_EDITED = "Phone number not found in the address book";

    private final Phone phone;
    private Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param phone of the person in the filtered person list to edit
     */
    public MarkCommand(Phone phone) {
        super(phone);
        this.phone = phone;
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
```
###### \java\seedu\address\logic\commands\MarkCommand.java
``` java
        public void setAttendance(Attendance attendance) {
            this.attendance = attendance;
        }

        public Optional<Attendance> getAttendance() {
            return Optional.ofNullable(attendance);
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
                    && getTags().equals(e.getTags());
        }
    }
}
```
###### \java\seedu\address\logic\commands\UnmarkCommand.java
``` java
/**
 * Edits the details of an existing person in the address book.
 */
public class UnmarkCommand extends GeneralMarkCommand {

    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks a person as absent "
            + "using their unique phone number. "
            + "This will also change the attendance associated with the person to Absent.\n"
            + "Parameters: "
            + "[PHONE] "
            + "Example: " + COMMAND_WORD
            + " 91234567 ";

    public static final String MESSAGE_MARK_PERSON_SUCCESS = "Marked person as ABSENT: %1$s";
    public static final String MESSAGE_NOT_EDITED = "Phone number not found in the address book";

    private final Phone phone;
    private Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param phone of the person in the filtered person list to edit
     */
    public UnmarkCommand(Phone phone) {
        super(phone);
        this.phone = phone;
        this.editPersonDescriptor = new EditPersonDescriptor();
    }

    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        return super.performAttendanceTaking(model, false);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnmarkCommand)) {
            return false;
        }

        // state check
        UnmarkCommand e = (UnmarkCommand) other;
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
            setAttendance(new Attendance("ABSENT"));
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

        public void setPayment(Payment payment) {
            this.payment = payment;
        }

        public Optional<Payment> getPayment() {
            return Optional.ofNullable(payment);
        }

        public void setAttendance(Attendance attendance) {
            this.attendance = attendance;
        }

        public Optional<Attendance> getAttendance() {
            return Optional.ofNullable(attendance);
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
                    && getTags().equals(e.getTags());
        }
    }
}
```
###### \java\seedu\address\logic\parser\MarkCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EditCommand object
 */
```
###### \java\seedu\address\logic\parser\MarkCommandParser.java
``` java
public class MarkCommandParser implements Parser<MarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        Phone phone;

        try {
            phone = ParserUtil.parsePhone(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE), pe);
        }

        return new MarkCommand(phone);
    }
}
```
###### \java\seedu\address\logic\parser\UnmarkCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EditCommand object
 */
public class UnmarkCommandParser implements Parser<UnmarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnmarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        Phone phone;

        try {
            phone = ParserUtil.parsePhone(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE), pe);
        }

        return new UnmarkCommand(phone);
    }
}
```
###### \java\seedu\address\model\person\Uid.java
``` java
/**
 * Represents a Person's UID in the guest list.
 * Guarantees: immutable; is valid as declared in {@link #isValidUid(String)}
 */
public class Uid {
    public static final String MESSAGE_UID_CONSTRAINTS =
            "Id should only contain numeric characters, it can be left blank";

    /*
     * Ensures that only a string of numeric characters are accepted
     */
    public static final String UID_VALIDATION_REGEX = "[\\p{Digit}]*";

    public final String uidValue;

    /**
     * Constructs a {@code Uid}.
     *
     * @param uid is a string of numbers that the Person holds.
     */
    public Uid(String uid) {
        requireNonNull(uid);
        checkArgument(isValidUid(uid), MESSAGE_UID_CONSTRAINTS);
        uidValue = uid;
    }

    /**
     * Returns true if a given string is a valid Uid.
     */
    public static boolean isValidUid(String test) {
        return test.matches(UID_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return uidValue;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Uid // instance of handles nulls
                && uidValue.equals(((Uid) other).uidValue)); // state check
    }

    @Override
    public int hashCode() {
        return uidValue.hashCode();
    }

}
```
