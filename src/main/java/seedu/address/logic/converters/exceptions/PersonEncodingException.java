package seedu.address.logic.converters.exceptions;

//@@author wm28

/**
 * Represents an encoding error encountered by a PersonConverter.
 */
public class PersonEncodingException extends Exception {
    public PersonEncodingException(String message) {
        super(message);
    }

    public PersonEncodingException(String message, Throwable cause) {
        super(message, cause);
    }
}
//@@author

