package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Event's name.
 * Guarantees: immutable; is valid as declared in {@link #isValidEventName(String)}
 */
//@@author SandhyaGopakumar
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
