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
            "Attendance should only contain alphanumeric characters, spaces and '.', "
                    + "and it should not be blank.\n"
            + " The following words are accepted (ignoring case): \"ABSENT\", \"PRESENT\", \"N.A.\"\n"
            + " Any words besides these will not be accepted and a blank field will be seen.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String ATTENDANCE_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum}.-]*";

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
        if ((test.equalsIgnoreCase("ABSENT")
                || test.equalsIgnoreCase("PRESENT")
                || test.equalsIgnoreCase("N.A."))
                && test.matches(ATTENDANCE_VALIDATION_REGEX)) {
            return true;
        }

        return false;
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
