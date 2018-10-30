package seedu.address.model.event;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class EventNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EventName(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidEventName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new EventName(invalidEventName));
    }

    @Test
    public void isValidEventName() {
        // null name
        Assert.assertThrows(NullPointerException.class, () -> EventName.isValidEventName(null));

        // invalid name
        assertFalse(EventName.isValidEventName("Wedding#Cheryl")); // contains non-alphanumeric character, not '
        assertFalse(EventName.isValidEventName(" ")); // spaces only
        assertFalse(EventName.isValidEventName("^")); // only non-alphanumeric characters
        assertFalse(EventName.isValidEventName("")); //empty string

        // valid name
        assertTrue(EventName.isValidEventName("Cheryl's Wedding")); // alphabets and ' only
        assertTrue(EventName.isValidEventName("12345")); // numbers only
        assertTrue(EventName.isValidEventName("80th birthday")); // alphanumeric characters
        assertTrue(EventName.isValidEventName("Networking Conference")); // with capital letters
        assertTrue(EventName.isValidEventName("Indian Cultural Society's Indian Cultural Bazaar")); // long names
    }
}
