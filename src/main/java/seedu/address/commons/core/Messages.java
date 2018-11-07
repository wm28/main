package seedu.address.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_UNSUPPORTED_FILE_EXTENSION = "Invalid file extension! \n%1$s";
    public static final String MESSAGE_INVALID_FILE_PATH = "Invalid file path! \n%1$s";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_FILE_NOT_FOUND = "%1$s does not exists!";
    public static final String MESSAGE_FILE_ALREADY_EXIST = "%1$s already exists!";

    //@@author aaryamNUS
    public static final String MESSAGE_USERNAME_NOT_PROVIDED = "Error: you have not provided your username "
                                                                + "for authentication!";
    public static final String MESSAGE_PASSWORD_NOT_PROVIDED = "Error: you have not provided your password "
                                                                + "for authentication!";
    public static final String MESSAGE_EMAIL_SUBJECT_NOT_PROVIDED = "Error: you have not included the subject "
                                                                    + "of your email!";
    public static final String MESSAGE_EMAIL_MESSAGE_NOT_PROVIDED = "Error: you have not included the message "
                                                                    + "of your email!";
    public static final String MESSAGE_NO_EMAIL_SENT_MESSAGE = "No emails sent to any guests!";
    public static final String MESSAGE_INVALID_EMAIL = "Error: The email address you have provided is invalid!";
    public static final String MESSAGE_NO_INTERNET_CONNECTION_OR_INVALID_CREDENTIALS = "Error: could not send "
                                                             + "email, either your "
                                                             + "internet connection is not strong or the "
                                                             + "credentials provided are invalid!";
    public static final String MESSAGE_NO_PERSONS_WITH_TAGS = "Error: no guests in the current list have the "
                                                             + "specified tags!";

}
