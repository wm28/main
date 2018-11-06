package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

//@@author aaryamNUS
/**
 * Sends an email to all of the guests in the specified list.
 */
public class EmailAllCommand extends Email {

    public static final String COMMAND_WORD = "emailAll";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sends an email to all guests in the "
            + "current filtered list\n"
            + "Parameters: none\n"
            + "Please ensure you don't enter any characters after the command word!\n"
            + "Example: " + COMMAND_WORD;

    private static final String MESSAGE_MAIL_ALL_PERSON_SUCCESS = "Successfully sent an email to %1$d emails, "
            + "could not send an email to %2$d emails will addresses: %3$s!";

    private static Logger logger = Logger.getLogger("execute");
    private static String username;
    private static String password;
    private static String emailSubject;
    private static String emailMessage;

    public EmailAllCommand() {}

    /**
     * Sends an email to all the persons in the current filtered list
     * @param model is instantiated to get the latest filtered person list
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Array of strings to store all the necessary information
        String[] information;

        // Retrieve the information through a method in the abstract super class Email
        // and then set the information via the 4 variables username, password, emailSubject,
        // and emailMessage
        information = retrieveInformation();
        setInformation(information);

        // Check for duplicate emails to ensure each guest only receives one email, even if
        // multiple guests have registered under the same email
        HashSet<String> personsToSendEmail = new HashSet<>();

        // Contains all the email addresses that aren't of a valid format
        HashSet<String> invalidEmailAddresses = new HashSet<>();

        // These variables are used in the command success message MESSAGE_MAIL_ALL_PERSON_SUCCESS
        StringBuilder invalidEmails = new StringBuilder();
        int successfulEmails = 0;
        int failedEmails = 0;

        // Check for invalid or duplicate emails, and create the list of valid email addresses to send to
        // NOTE: This part of the code was not abstracted out as it would involve the creation of a custom
        // class object to return different type of objects, thereby increasing dependency and coupling
        // within the code
        for (Person personToMail : lastShownList) {
            assert personToMail != null;

            if (!isValidEmail(personToMail.getEmail().toString())) {
                if (invalidEmailAddresses.contains(personToMail.getEmail().toString())) {
                    logger.log(Level.INFO, "Guest email address has already been marked as invalid!");
                } else {
                    invalidEmailAddresses.add(personToMail.getEmail().toString());
                    failedEmails++;
                    String invalidEmail = " || " + personToMail.getEmail().toString() + " || ";
                    invalidEmails.append(invalidEmail);
                }
            } else if (isValidEmail(personToMail.getEmail().toString())) {
                if (personsToSendEmail.contains(personToMail.getEmail().toString())) {
                    logger.log(Level.INFO, "Guest email address has already been sent an email!");
                } else {
                    personsToSendEmail.add(personToMail.getEmail().toString());
                    successfulEmails++;
                }
            }
        }

        // Creates a new session with the user Gmail account as the host
        Properties props = createPropertiesConfiguration();

        // Authenticate the user credentials
        EmailPasswordAuthenticator authenticate = new EmailPasswordAuthenticator();

        // Create a new session using the authenticated credentials and the properties of
        // the Gmail host
        Session session = Session.getDefaultInstance(props, authenticate);

        // Strings to represent the recipients of the email (i.e. all the guests in the list)
        String recipients = recipientCreator(personsToSendEmail);

        createAndSendEmail(username, emailSubject, emailMessage,
                recipients, session);

        logger.log(Level.INFO, "All emails sent successfully!");
        return new CommandResult(String.format(MESSAGE_MAIL_ALL_PERSON_SUCCESS, successfulEmails,
                failedEmails, invalidEmails));
    }

    /**
     * Sets the information retrieved from an EmailWindow
     */
    private static void setInformation(String[] information) {
        username = information[0];
        password = information[1];
        emailSubject = information[2];
        emailMessage = information[3];
    }

    /**
     * Authenticates the user account based on the credentials provided
     */
    private static class EmailPasswordAuthenticator extends Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    @Override
    public Properties createPropertiesConfiguration() {
        return super.createPropertiesConfiguration();
    }

    @Override
    public String[] retrieveInformation() throws CommandException {
        return super.retrieveInformation();
    }

    @Override
    public String recipientCreator(HashSet<String> personsToSendEmail) {
        return super.recipientCreator(personsToSendEmail);
    }

    @Override
    public void createAndSendEmail(String username, String emailSubject, String emailMessage,
                                   String recipient, Session session) throws CommandException {
        super.createAndSendEmail(username, emailSubject, emailMessage, recipient, session);
    }

    @Override
    public boolean isValidEmail(String guestAddress) {
        return super.isValidEmail(guestAddress);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EmailAllCommand)) {
            return false;
        }

        // state check
        EmailAllCommand e = (EmailAllCommand) other;
        return e.equals(other);
    }
}
