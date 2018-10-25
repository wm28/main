package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

//@@author kronicler
/**
 * Represents a Person's ID in the guest list.
 * Guarantees: immutable; is valid as declared in {@link #isValidId(String)}
 */
public class Id {
    public static final String MESSAGE_ID_CONSTRAINTS =
            "Id should only contain numeric characters, it can be left blank";

    /*
     * Ensures that only a string of numeric characters are accepted
     */
    public static final String ID_VALIDATION_REGEX = "[\\p{Digit}]*";

    public final String idValue;

    /**
     * Constructs a {@code ID}.
     *
     * @param id is a string of numbers that the Person holds.
     */
    public Id(String id) {
        requireNonNull(id);
        checkArgument(isValidId(id), MESSAGE_ID_CONSTRAINTS);
        idValue = id;
    }

    /**
     * Returns true if a given string is a valid ID.
     */
    public static boolean isValidId(String test) {
        return test.matches(ID_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return idValue;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Id // instanceof handles nulls
                && idValue.equals(((Id) other).idValue)); // state check
    }

    @Override
    public int hashCode() {
        return idValue.hashCode();
    }

}
