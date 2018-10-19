# SandhyaGopakumar
###### \main\java\seedu\address\logic\commands\AddEventCommand.java
``` java
/**
 * Adds an event to the application.
 */
public class AddEventCommand extends Command {
    public static final String COMMAND_WORD = "add_event";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an event to the application. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Wedding "
            + PREFIX_TAG + "18 Sep 2018 10AM ";
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
```
###### \main\java\seedu\address\logic\commands\DeleteEventCommand.java
``` java
/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteEventCommand extends Command {

    public static final String COMMAND_WORD = "delete_event";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the event in the addressbook currently.\n"
            + "Parameters: none\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_DELETE_EVENT_SUCCESS = "Deleted event details.";
    public static final String MESSAGE_NO_EVENT_DETAILS = "Event details have not been put in yet.";


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (!model.hasEvent()) {
            throw new CommandException(MESSAGE_NO_EVENT_DETAILS);
        }

        model.deleteEvent();
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_DELETE_EVENT_SUCCESS));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteEventCommand); // state check
    }
}
```
###### \main\java\seedu\address\logic\parser\AddEventCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddEventCommand object
 */
public class AddEventCommandParser implements Parser<AddEventCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddEventCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TAG);
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
        }
        EventName eventName = ParserUtil.parseEventName(argMultimap.getValue(PREFIX_NAME).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        Event event = new Event(eventName, tagList);
        return new AddEventCommand(event);
    }
    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \main\java\seedu\address\logic\parser\AddressBookParser.java
``` java
        case AddEventCommand.COMMAND_WORD:
            return new AddEventCommandParser().parse(arguments);
```
###### \main\java\seedu\address\logic\parser\AddressBookParser.java
``` java
        case DeleteEventCommand.COMMAND_WORD:
            return new DeleteEventCommand();
```
###### \main\java\seedu\address\model\AddressBook.java
``` java
    private final Event eventDetails;
```
###### \main\java\seedu\address\model\AddressBook.java
``` java
    public void setEvent(Event event) {
        this.eventDetails.setEvent(event);
    }
```
###### \main\java\seedu\address\model\AddressBook.java
``` java
    //event-level operations
    public void addEvent(Event e) {
        eventDetails.addEvent(e);
    }

    /** Deletes the event details stored in the addressbook. */
    public void deleteEvent() {
        eventDetails.deleteEvent();
    }

    public boolean hasEvent() {
        return eventDetails.isUserInitialised();
    }
```
###### \main\java\seedu\address\model\AddressBook.java
``` java
    @Override
    public Event getEventDetails() {
        return eventDetails;
    }
```
###### \main\java\seedu\address\model\event\Event.java
``` java
/**
 * Represents an event.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Event {

    //Identity fields
    private EventName eventName;
    private Set<Tag> eventTags = new HashSet<>();
    private boolean isNotInitialisedByUser;
    /**
     * Every field must be present and not null.
     */
    public Event(EventName eventName, Set<Tag> eventTags) {
        requireAllNonNull(eventName, eventTags);
        this.eventName = eventName;
        this.eventTags.addAll(eventTags);
        this.isNotInitialisedByUser = false;
    }
    public Event() {
        EventName eventName = new EventName("event not created yet");
        this.eventName = eventName;
        this.isNotInitialisedByUser = true;
    }

    public EventName getEventName() {
        return eventName;
    }

    public void setEvent(Event event) {
        if (!this.equals(event)) {
            this.eventName.setEventName(event.eventName.getEventName());
            this.eventTags = event.eventTags;
        }
    }

    /** Adds user-given details of the event. */
    public void addEvent(Event event) {
        if (!this.equals(event)) {
            this.eventName.setEventName(event.eventName.getEventName());
            this.eventTags = event.eventTags;
        }
        this.isNotInitialisedByUser = false;
    }
    /** Deletes user-given details of the event. */
    public void deleteEvent() {
        Event event = new Event();
        this.eventName.setEventName(event.eventName.getEventName());
        this.eventTags = event.eventTags;
        this.isNotInitialisedByUser = true;
    }

    public boolean isUserInitialised() {
        return !isNotInitialisedByUser;
    }
    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getEventTags() {
        return Collections.unmodifiableSet(eventTags);
    }

    /**
     * Returns true if both events of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two events.
     */
    public boolean isSameEvent(seedu.address.model.event.Event otherEvent) {
        if (otherEvent == this) {
            return true;
        }

        return otherEvent != null
                && otherEvent.getEventName().equals(getEventName());
    }

    /**
     * Returns true if both events have the same identity and data fields.
     * This defines a stronger notion of equality between two events.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof seedu.address.model.event.Event)) {
            return false;
        }

        seedu.address.model.event.Event otherEvent = (seedu.address.model.event.Event) other;
        return otherEvent.getEventName().equals(getEventName())
                && otherEvent.getEventTags().equals(getEventTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(eventName, eventTags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getEventName())
                .append(" Tags: ");
        getEventTags().forEach(builder::append);
        return builder.toString();
    }

}
```
###### \main\java\seedu\address\model\event\EventName.java
``` java
public class EventName {

    //Identity fields
    public static final String MESSAGE_EVENTNAME_CONSTRAINTS =
            "Event names should only contain alphanumeric characters and spaces, and it should not be blank";

    public static final String EVENTNAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    private String fullEventName;

    /**
     * Constructs a {@code eventName}.
     *
     * @param eventName A valid event name.
     */
    public EventName(String eventName) {
        requireNonNull(eventName);
        checkArgument(isValidEventName(eventName), MESSAGE_EVENTNAME_CONSTRAINTS);
        fullEventName = eventName;
    }

    /**
     * Accessor method for eventName
     */
    public String getEventName() {
        return this.fullEventName;
    }
    /**
     * Setter method for eventName
     */
    public void setEventName(String eventName) {
        this.fullEventName = eventName;
    }
    /**
     * Returns true if a given string is a valid event name.
     */
    public static boolean isValidEventName(String test) {
        return test.matches(EVENTNAME_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullEventName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EventName // instanceof handles nulls
                && fullEventName.equals(((EventName) other).fullEventName)); // state check
    }

    @Override
    public int hashCode() {
        return fullEventName.hashCode();
    }

}
```
###### \main\java\seedu\address\model\event\exceptions\DuplicateEventException.java
``` java
/**
 * Signals that the operation will result in duplicate Persons (Persons are considered duplicates if they have the same
 * identity).
 */
public class DuplicateEventException extends RuntimeException {
    public DuplicateEventException() {
        super("Operation would result in duplicate events");
    }
}
```
###### \main\java\seedu\address\model\event\exceptions\EventNotFoundException.java
``` java
/**
 * Signals that the operation is unable to find the specified event.
 */

public class EventNotFoundException extends RuntimeException {}
```
###### \main\java\seedu\address\model\Model.java
``` java
    /**
     * Returns true if an event with the same identity as {@code event} exists in the application.
     */
    boolean hasEvent();

    /**
     * Deletes the given event.
     * An event must have been initialised by the user in the application.
     */
    void deleteEvent();

    /**
     * Adds the given event.
     * {@code event} must not already exist in the application.
     */
    void addEvent(Event event);

    /**
     * Replaces the given event {@code target} with {@code editedEvent}.
     * {@code target} must exist in the application.
     * The person identity of {@code editedEvent} must not be the same as another existing event in the application.
     */
    //void updateEvent(Event target, Event editedEvent);
```
###### \main\java\seedu\address\model\Model.java
``` java
    /** Returns the details of the event currently residing in the addressbook. */
    Event getEventDetails();
```
###### \main\java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void addEvent(Event event) {
        versionedAddressBook.addEvent(event);
        indicateAddressBookChanged();
    }

    @Override
    public void deleteEvent() {
        versionedAddressBook.deleteEvent();
        indicateAddressBookChanged();
    }

    @Override
    public boolean hasEvent() {
        return versionedAddressBook.hasEvent();
    }

    /** Returns the details of the event currently residing in the addressbook. */
    @Override
    public Event getEventDetails() {
        return versionedAddressBook.getEventDetails();
    }
```
###### \main\java\seedu\address\model\ReadOnlyAddressBook.java
``` java
    Event getEventDetails();
```
###### \test\java\seedu\address\logic\commands\AddEventCommandTest.java
``` java
public class AddEventCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullEvent_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddEventCommand(null);
    }

    @Test
    public void execute_eventAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingEventAdded modelStub = new ModelStubAcceptingEventAdded();
        Event validEvent = new EventBuilder().build();

        CommandResult commandResult = new AddEventCommand(validEvent).execute(modelStub, commandHistory);

        assertEquals(String.format(AddEventCommand.MESSAGE_SUCCESS, validEvent), commandResult.feedbackToUser);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateEvent_throwsCommandException() throws Exception {
        Event validEvent = new EventBuilder().build();
        AddEventCommand addEventCommand = new AddEventCommand(validEvent);
        ModelStub modelStub = new ModelStubWithEvent(validEvent);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddEventCommand.MESSAGE_DUPLICATE_EVENT);
        addEventCommand.execute(modelStub, commandHistory);
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addEvent(Event event) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasEvent() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteEvent() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updatePerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTag(Tag tag) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTag(Tag tag) {
            throw new AssertionError("This method should not be called");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Event getEventDetails() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitAddressBook() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithEvent extends ModelStub {
        private final Event event;

        ModelStubWithEvent(Event event) {
            requireNonNull(event);
            this.event = event;
        }

        @Override
        public boolean hasEvent() {
            requireNonNull(event);
            return this.event.isUserInitialised();
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingEventAdded extends ModelStub {
        final Event event = new Event();

        @Override
        public boolean hasEvent() {
            return false;
        }

        @Override
        public void addEvent(Event newEvent) {
            requireNonNull(newEvent);
            event.addEvent(newEvent);
        }

        @Override
        public void commitAddressBook() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
```
