package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

//@@author Sarah
public class AttendanceTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Attendance(null));
    }

    @Test
    public void constructor_invalidAttendance_throwsIllegalArgumentException() {
        String invalidAttendance = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Attendance(invalidAttendance));
    }

    @Test
    public void isValidAttendance() {
        // null address
        Assert.assertThrows(NullPointerException.class, () -> Attendance.isValidAttendance(null));

        // invalid Attendance
        assertFalse(Attendance.isValidAttendance("")); // empty string
        assertFalse(Attendance.isValidAttendance(" ")); // spaces only

        // valid Attendance
        assertTrue(Attendance.isValidAttendance("PRESENT"));
        assertTrue(Attendance.isValidAttendance("ABSENT"));
        assertTrue(Attendance.isValidAttendance("N.A."));
    }
}
