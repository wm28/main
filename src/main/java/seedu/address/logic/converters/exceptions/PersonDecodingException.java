package seedu.address.logic.converters.exceptions;

//@@author wm28

/**
 * Represents a decoding error encountered by a PersonConverter.
 */
public class PersonDecodingException extends Exception {
    public PersonDecodingException(String message) {
        super(message);
    }

    public PersonDecodingException(String message, Throwable cause) {
        super(message, cause);
    }
}
//@@author

