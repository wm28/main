package seedu.address.model.event;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class EventVenueTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EventVenue(null));
    }

    @Test
    public void constructor_invalidVenue_throwsIllegalArgumentException() {
        String invalidEventVenue = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new EventVenue(invalidEventVenue));
    }

    @Test
    public void isValidEventVenue() {
        // null venue
        Assert.assertThrows(NullPointerException.class, () -> EventVenue.isValidEventVenue(null));

        // invalid venue
        // contains non-alphanumeric character, not ',', '#', '-':
        assertFalse(EventVenue.isValidEventVenue("The Carlton @ Times Square"));
        assertFalse(EventVenue.isValidEventVenue(" ")); // spaces only
        // only non-alphanumeric characters:
        assertFalse(EventVenue.isValidEventVenue("^"));
        // only non-alphanumeric character:
        assertFalse(EventVenue.isValidEventVenue("#"));
        assertFalse(EventVenue.isValidEventVenue("")); //empty string

        // valid venue
        // alphabets only
        assertTrue(EventVenue.isValidEventVenue("Novotel Tour de Eiffel"));
        // Capital letters, numbers and 1 allowed character only
        assertTrue(EventVenue.isValidEventVenue("SRC-RM1"));
        // alphanumeric characters and 2 allowed characters
        assertTrue(EventVenue.isValidEventVenue("E-Cube, Auditorium 1"));
        // Numbers with capital letters and 3 allowed characters
        assertTrue(EventVenue.isValidEventVenue("EA, #01-02"));
        assertTrue(EventVenue.isValidEventVenue("E-Cube #01-02, EA, "
                + "National University of Singapore")); // long venue names
    }
}
