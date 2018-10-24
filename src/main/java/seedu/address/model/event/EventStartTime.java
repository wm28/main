package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Event's start time.
 * Guarantees: immutable; is valid as declared in {@link #isValidEventStartTime(String)}
 */
public class EventStartTime {

    //Identity fields
    public static final String MESSAGE_EVENTSTARTTIME_CONSTRAINTS =
            "Event's start time should only contain alphanumeric characters "
                    + "and spaces in the 12 hour format and should not be blank.";

    public static final String EVENTSTARTTIME_VALIDATION_REGEX = "(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)";

    private String fullEventStartTime;

    /**
     * Constructs a {@code eventStartTime}.
     *
     * @param eventStartTime A valid event start time.
     */
    public EventStartTime(String eventStartTime) {
        requireNonNull(eventStartTime);
        checkArgument(isValidEventStartTime(eventStartTime), MESSAGE_EVENTSTARTTIME_CONSTRAINTS);
        fullEventStartTime = eventStartTime;
    }

    /**
     * Accessor method for eventStartTime
     */
    public String getEventStartTime() {
        return this.fullEventStartTime;
    }
    /**
     * Setter method for eventStartTime
     */
    public void setEventStartTime(String eventStartTime) {
        this.fullEventStartTime = eventStartTime;
    }
    /**
     * Returns true if a given string is a valid event start time.
     */
    public static boolean isValidEventStartTime(String test) {
        return test.matches(EVENTSTARTTIME_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullEventStartTime;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EventStartTime // instanceof handles nulls
                && fullEventStartTime.equals(((EventStartTime) other).fullEventStartTime)); // state check
    }

    @Override
    public int hashCode() {
        return fullEventStartTime.hashCode();
    }

}
