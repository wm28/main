package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a Event's date.
 * Guarantees: immutable; is valid as declared in {@link #isValidEventDate(String)}
 */
public class EventDate {

    //Identity fields
    public static final String MESSAGE_EVENTDATE_CONSTRAINTS =
            "Event's date should be valid, contain only numbers and "
                    + "forward slash and should follow the dd/mm/yyyy format.";

    private String fullEventDate;

    /**
     * Constructs a {@code eventDate}.
     *
     * @param eventDate A valid event date.
     */
    public EventDate(String eventDate) {
        requireNonNull(eventDate);
        checkArgument(isValidEventDate(eventDate), MESSAGE_EVENTDATE_CONSTRAINTS);
        fullEventDate = eventDate;
    }

    /**
     * Accessor method for eventDate
     */
    public String getEventDate() {
        return this.fullEventDate;
    }
    /**
     * Setter method for eventDate
     */
    public void setEventDate(String eventDate) {
        this.fullEventDate = eventDate;
    }

    public Date getFullEventDate() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        dateFormatter.setLenient(false);
        Date eventDate = null;
        try {
            eventDate = dateFormatter.parse(this.fullEventDate);
        } catch (Exception e) {
            eventDate = null;
        }
        return eventDate;
    }
    /**
     * Returns true if a given string is a valid event date.
     */
    public static boolean isValidEventDate(String test) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        dateFormatter.setLenient(false);
        try {
            dateFormatter.parse(test);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public String toString() {
        return fullEventDate;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EventDate // instanceof handles nulls
                && fullEventDate.equals(((EventDate) other).fullEventDate)); // state check
    }

    @Override
    public int hashCode() {
        return fullEventDate.hashCode();
    }

}
