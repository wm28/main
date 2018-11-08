# SandhyaGopakumar
###### \java\seedu\address\logic\commands\AddEventCommand.java
``` java
/**
 * Adds an event to the application.
 */
public class AddEventCommand extends Command {
    public static final String COMMAND_WORD = "add_event";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an event to the application. "
            + "Parameters: "
            + PREFIX_NAME + "NAME " + " "
            + PREFIX_DATE + "DATE" + " "
            + PREFIX_VENUE + "VENUE" + " "
            + PREFIX_START_TIME + "START TIME" + " "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Wedding " + " "
            + PREFIX_DATE + "10/10/2019 " + " "
            + PREFIX_VENUE + "Mandarin Hotel, 5th floor, Room 1A" + " "
            + PREFIX_START_TIME + "10:00 AM" + " "
            + PREFIX_TAG + "ClassicTheme";
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
###### \java\seedu\address\logic\commands\DeleteEventCommand.java
``` java
/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteEventCommand extends Command {

    public static final String COMMAND_WORD = "delete_event";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the event in the addressbook currently.\n"
            + "Parameters: none\n"
            + "Please ensure you don't enter any characters after the command word!\n"
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
###### \java\seedu\address\logic\parser\AddressBookParser.java
``` java
        case AddEventCommand.COMMAND_WORD:
            return new AddEventCommandParser().parse(arguments);
```
###### \java\seedu\address\logic\parser\AddressBookParser.java
``` java
        case DeleteEventCommand.COMMAND_WORD:
            return new DeleteEventCommandParser().parse(arguments);
```
###### \java\seedu\address\logic\parser\CliSyntax.java
``` java
    public static final Prefix PREFIX_DATE = new Prefix("d/");
    public static final Prefix PREFIX_VENUE = new Prefix("v/");
    public static final Prefix PREFIX_START_TIME = new Prefix("st/");
```
###### \java\seedu\address\model\AddressBook.java
``` java
    private final Event eventDetails;
```
###### \java\seedu\address\model\AddressBook.java
``` java

    /**
     * Replaces the current details of the event with {@code event}.
     */
    public void setEvent(Event event) {
        this.eventDetails.setEvent(event);
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java
    //event-level operations
    /** Adds the details given by the user to event. */
    public void addEvent(Event e) {
        eventDetails.addEvent(e);
    }

    /** Deletes the event details stored in the addressbook. */
    public void deleteEvent() {
        eventDetails.deleteEvent();
    }

    /** Returns true if the event details given by the user is being stored in the addressbook. */
    public boolean hasEvent() {
        return eventDetails.isUserInitialised();
    }
```
###### \java\seedu\address\model\event\EventName.java
``` java
public class EventName {

    //Identity fields
    public static final String MESSAGE_EVENTNAME_CONSTRAINTS =
            "Event names should only contain alphanumeric characters and spaces, and it should not be blank";

    public static final String EVENTNAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ']*";

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
###### \java\seedu\address\model\event\exceptions\DuplicateEventException.java
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
###### \java\seedu\address\model\event\exceptions\EventNotFoundException.java
``` java
/**
 * Signals that the operation is unable to find the specified event.
 */

public class EventNotFoundException extends RuntimeException {}
```
###### \java\seedu\address\model\Model.java
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
     * {@code event} with details given by the user must not already exist in the application.
     */
    void addEvent(Event event);

    /**
     * Replaces the existing event with {@code editedEvent}.
     * The existing event must have been initialised by the user.
     * The event details of {@code editedEvent} must not be the same as the existing event.
     */
    void updateEvent(Event editedEvent);
```
###### \java\seedu\address\model\Model.java
``` java
    /** Returns the details of the event currently residing in the addressbook. */
    Event getEventDetails();
```
###### \java\seedu\address\model\ModelManager.java
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

    @Override
    public void updateEvent(Event editedEvent) {
        versionedAddressBook.setEvent(editedEvent);
        indicateAddressBookChanged();
    }

    /** Returns the details of the event currently residing in the addressbook. */
    @Override
    public Event getEventDetails() {
        return versionedAddressBook.getEventDetails();
    }
```
###### \java\seedu\address\model\ReadOnlyAddressBook.java
``` java
    Event getEventDetails();
```
