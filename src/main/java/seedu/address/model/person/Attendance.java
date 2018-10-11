package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

//@@ author Sarah
/**
 * Represents a Person's attendance in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAttendance(String)}
 */
public class Attendance {

    public static final String MESSAGE_ATTENDANCE_CONSTRAINTS =
            "Attendance should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String ATTENDANCE_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String attendanceValue;

    /**
     * Constructs a {@code Attendance}.
     *
     * @param attendance A valid attendance.
     */
    public Attendance(String attendance) {
        requireNonNull(attendance);
        checkArgument(isValidAttendance(attendance), MESSAGE_ATTENDANCE_CONSTRAINTS);
        attendanceValue = attendance;
    }

    /**
     * Returns true if a given string is a valid attendance.
     */
    public static boolean isValidAttendance(String test) {
        return test.matches(ATTENDANCE_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return attendanceValue;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Attendance // instanceof handles nulls
                && attendanceValue.equals(((Attendance) other).attendanceValue)); // state check
    }

    @Override
    public int hashCode() {
        return attendanceValue.hashCode();
    }

}
