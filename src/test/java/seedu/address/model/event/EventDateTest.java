package seedu.address.model.event;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class EventDateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EventDate(null));
    }

    @Test
    public void constructor_invalidDate_throwsIllegalArgumentException() {
        String invalidEventDate = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new EventDate(invalidEventDate));
    }

    @Test
    public void isValidEventDate() {
        // null date
        Assert.assertThrows(NullPointerException.class, () -> EventDate.isValidEventDate(null));

        // invalid date
        assertFalse(EventDate.isValidEventDate("12th Nov 2018")); // date in an invalid format
        assertFalse(EventDate.isValidEventDate(" ")); // spaces only
        assertFalse(EventDate.isValidEventDate("29/02/2019")); // date does not exist
        assertFalse(EventDate.isValidEventDate("1/6/2018")); // date is before current date
        assertFalse(EventDate.isValidEventDate("")); //empty string

        // valid date
        assertTrue(EventDate.isValidEventDate("01/12/2018")); // valid date following dd/mm/yyyy format
        assertTrue(EventDate.isValidEventDate("1/12/2019")); // valid date
    }
}
