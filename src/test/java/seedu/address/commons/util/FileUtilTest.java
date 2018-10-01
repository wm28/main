package seedu.address.commons.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class FileUtilTest {

    @Test
    public void isValidPath() {
        // valid path
        assertTrue(FileUtil.isValidPath("valid/file/path"));

        // invalid path
        assertFalse(FileUtil.isValidPath("a\0"));

        // null path -> throws NullPointerException
        Assert.assertThrows(NullPointerException.class, () -> FileUtil.isValidPath(null));
    }

    @Test
    public void isCorrectFileExtension() {
        // correct file extension
        assertTrue(FileUtil.isValidFileExtension("valid/file/path.csv", "csv"));

        // incorrect file extension
        assertFalse(FileUtil.isValidFileExtension("valid/file/path.csv", "vsc"));

        // null path -> throws NullPointerException
        Assert.assertThrows(NullPointerException.class,
                () -> FileUtil.isValidFileExtension(null, "csv"));

        // null extension -> throws AssertionException
        Assert.assertThrows(NullPointerException.class,
                () -> FileUtil.isValidFileExtension("valid/file/path.csv", null));
    }
}
