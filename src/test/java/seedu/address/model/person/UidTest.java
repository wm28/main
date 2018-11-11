package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class UidTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Uid(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidUid = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Uid(invalidUid));
    }

    @Test
    public void isEquals_success() {
        Uid uid = new Uid("401345");
        Uid otherUid = new Uid("401345");
        assertTrue(uid.equals(otherUid));
    }

    @Test
    public void isEquals_invalidUid_throwException() {
        Uid uid = new Uid("401345");
        Uid otherUid = new Uid("401335");
        assertFalse(uid.equals(otherUid));
    }

    @Test
    public void isValidUid() {
        // null Uid
        Assert.assertThrows(NullPointerException.class, () -> Uid.isValidUid(null));

        // Valid Uid
        assertTrue(Uid.isValidUid("10101"));
        assertTrue(Uid.isValidUid("01214"));
        assertTrue(Uid.isValidUid("16817"));
        assertTrue(Uid.isValidUid("01110"));
        assertTrue(Uid.isValidUid("99911"));
        assertTrue(Uid.isValidUid("A00001"));

        //Invalid
        assertFalse(Uid.isValidUid("1"));
        assertFalse(Uid.isValidUid(""));
        assertFalse(Uid.isValidUid(" "));
        assertFalse(Uid.isValidUid("10"));
        assertFalse(Uid.isValidUid("110"));
        assertFalse(Uid.isValidUid("1011"));
        assertFalse(Uid.isValidUid("dfs"));
        assertFalse(Uid.isValidUid("$00001"));
        assertFalse(Uid.isValidUid("?./m,"));
        assertFalse(Uid.isValidUid("asdf"));
        assertFalse(Uid.isValidUid("!~"));
    }
}
