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
        assertTrue(Uid.isValidUid("01214DC"));
        assertTrue(Uid.isValidUid("16817SDF"));
        assertTrue(Uid.isValidUid("01110SDDFG"));
        assertTrue(Uid.isValidUid("99911SDFE"));
        assertTrue(Uid.isValidUid("A00DF1"));
        assertTrue(Uid.isValidUid("ASACB"));
        assertTrue(Uid.isValidUid("ASBCDNVMDJKFDDEKFJDD"));
        assertTrue(Uid.isValidUid("00000000000000000001"));
        assertTrue(Uid.isValidUid("o0o0o0o0o0o0o0o0o0o0"));

        //Invalid
        assertFalse(Uid.isValidUid("dfdafdsafdafdas~!ds"));
        assertFalse(Uid.isValidUid("01oiewo201iweorfje>"));
        assertFalse(Uid.isValidUid("101010101010101010101"));
        assertFalse(Uid.isValidUid("DFFKEW:FLKF:DLFJDFfdjLFKJDfjdlFKJFLKDFEHLRKWJWFOIF#()#$"));
        assertFalse(Uid.isValidUid("FJDlfdKFJDLFKDJFLSDFKJFLkfj"));
        assertFalse(Uid.isValidUid("1"));
        assertFalse(Uid.isValidUid(""));
        assertFalse(Uid.isValidUid(" "));
        assertFalse(Uid.isValidUid("10"));
        assertFalse(Uid.isValidUid("110"));
        assertFalse(Uid.isValidUid("1011"));
        assertFalse(Uid.isValidUid("a"));
        assertFalse(Uid.isValidUid("vb"));
        assertFalse(Uid.isValidUid("fds"));
        assertFalse(Uid.isValidUid("dfsg"));
        assertFalse(Uid.isValidUid("$00001"));
        assertFalse(Uid.isValidUid("?./m,FD"));
        assertFalse(Uid.isValidUid("!!!!!!!!!"));
        assertFalse(Uid.isValidUid("!~"));
    }
}
