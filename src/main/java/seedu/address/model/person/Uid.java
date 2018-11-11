package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

//@@author kronicler
/**
 * Represents a Person's UID in the guest list.
 * Guarantees: immutable; is valid as declared in {@link #isValidUid(String)}
 */
public class Uid {
    public static final String MESSAGE_UID_CONSTRAINTS =
            "UID should only contain alphanumeric characters, and should be between 5 to 20 characters long.\n"
            + "Auto-generate UID: u/00000 , e.g u/00000 -> UID: 415670 (randomly-generated)\n"
            + "User defined UID: u/ + NOT 00000, e.g u/00001 -> UID: 00001";

    /*
     * Ensures that only a string of alpha numeric characters are accepted and they are between 5 to 20 characters long
     */
    public static final String UID_VALIDATION_REGEX = "\\p{Alnum}{5,20}";

    public final String uidValue;

    /**
     * Constructs a {@code Uid}.
     *
     * @param uid is a string of numbers that the Person holds.
     */
    public Uid(String uid) {
        requireNonNull(uid);
        checkArgument(isValidUid(uid), MESSAGE_UID_CONSTRAINTS);
        uidValue = uid;
    }

    /**
     * Returns true if a given string is a valid Uid.
     */
    public static boolean isValidUid(String test) {
        return test.matches(UID_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return uidValue;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Uid // instance of handles nulls
                && uidValue.equals(((Uid) other).uidValue)); // state check
    }

    @Override
    public int hashCode() {
        return uidValue.hashCode();
    }

}
