package seedu.address.model.event;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class EventStartTimeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EventStartTime(null));
    }

    @Test
    public void constructor_invalidStartTime_throwsIllegalArgumentException() {
        String invalidEventStartTime = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new EventStartTime(invalidEventStartTime));
    }

    @Test
    public void isValidEventStartTime() {
        // null start time
        Assert.assertThrows(NullPointerException.class, () -> EventStartTime.isValidEventStartTime(null));

        // invalid start time
        assertFalse(EventStartTime.isValidEventStartTime("10 PM"));
        // ^not in 12 hour, X:YY AM/PM format
        assertFalse(EventStartTime.isValidEventStartTime(" ")); // spaces only
        assertFalse(EventStartTime.isValidEventStartTime("23:00"));
        // ^not in 12 hour format
        assertFalse(EventStartTime.isValidEventStartTime("06:00 PM"));
        //  ^not in 12 hour, X:YY AM/PM format where X = [1 - 12]
        assertFalse(EventStartTime.isValidEventStartTime("6:2 PM"));
        // ^invalid YY in X:YY 12 hour format
        assertFalse(EventStartTime.isValidEventStartTime("6:20 A M"));
        // ^invalid AM/PM
        assertFalse(EventStartTime.isValidEventStartTime("")); //empty string

        // valid start time
        assertTrue(EventStartTime.isValidEventStartTime("6:25 AM"));
        assertTrue(EventStartTime.isValidEventStartTime("12:59 PM"));
    }
}
