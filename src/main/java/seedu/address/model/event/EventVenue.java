package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Event's venue.
 * Guarantees: immutable; is valid as declared in {@link #isValidEventVenue(String)}
 */
public class EventVenue {

    //Identity fields
    public static final String MESSAGE_EVENTVENUE_CONSTRAINTS =
            "Event's venue should only contain alphanumeric characters, spaces, ',' or '#' and should not be blank.";

    public static final String EVENTVENUE_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ,#-]*";

    private String fullEventVenue;

    /**
     * Constructs a {@code eventVenue}.
     *
     * @param eventVenue A valid event venue.
     */
    public EventVenue(String eventVenue) {
        requireNonNull(eventVenue);
        checkArgument(isValidEventVenue(eventVenue), MESSAGE_EVENTVENUE_CONSTRAINTS);
        fullEventVenue = eventVenue;
    }

    /**
     * Accessor method for eventVenue
     */
    public String getEventVenue() {
        return this.fullEventVenue;
    }
    /**
     * Setter method for eventVenue
     */
    public void setEventVenue(String eventVenue) {
        this.fullEventVenue = eventVenue;
    }
    /**
     * Returns true if a given string is a valid event venue.
     */
    public static boolean isValidEventVenue(String test) {
        return test.matches(EVENTVENUE_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullEventVenue;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EventVenue // instanceof handles nulls
                && fullEventVenue.equals(((EventVenue) other).fullEventVenue)); // state check
    }

    @Override
    public int hashCode() {
        return fullEventVenue.hashCode();
    }

}
