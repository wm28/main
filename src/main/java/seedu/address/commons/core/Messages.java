package seedu.address.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_INVALID_FILE_EXTENSION = "Invalid file extension! \n%1$s";
    public static final String MESSAGE_INVALID_FILE_PATH = "Invalid file path! \n%1$s";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_FILE_NOT_FOUND = "%1$s does not exists!";
    public static final String MESSAGE_FILE_ALREADY_EXIST = "%1$s already exists!";
    public static final String MESSAGE_USERNAME_NOT_PROVIDED = "Error: you have not provided your username "
                                                                + "for authentication!";
    public static final String MESSAGE_PASSWORD_NOT_PROVIDED = "Error: you have not provided your password "
                                                                + "for authentication!";
    public static final String MESSAGE_EMAIL_SUBJECT_NOT_PROVIDED = "Error: you have not included the subject "
                                                                    + "of your email!";
    public static final String MESSAGE_EMAIL_MESSAGE_NOT_PROVIDED = "Error: you have not included the message "
                                                                    + "of your email!";
    public static final String MESSAGE_NO_EMAIL_SENT_MESSAGE = "No emails sent to any guests!";
    public static final String MESSAGE_NO_SUCH_ELEMENT_MESSAGE = "Error: Please specify your credentials, email message,"
                                                             + "and email subject in Credentials.txt and Message.txt";
    public static final String MESSAGE_NO_INTERNET_CONNECTION = "Error: could not send email, please ensure you have strong "
                                                             + "internet connectivity";
    public static final String MESSAGE_PERSONS_FILTERED_OVERVIEW = "Filtered by: %1$s\n";
    public static final String MESSAGE_INCORRECT_TAG_FORMAT = "Tags names should be alphanumeric";
}
