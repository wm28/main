package seedu.address.model.event;


import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class EventName {

    public static final String MESSAGE_EVENTNAME_CONSTRAINTS =
            "Event names should only contain alphanumeric characters and spaces, and it should not be blank";

    public static final String EVENTNAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullEventName;

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
     * Returns true if a given string is a valid name.
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