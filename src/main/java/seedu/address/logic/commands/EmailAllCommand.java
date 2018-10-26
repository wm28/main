package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
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
 * Sends an email to the specified person in the guest list.
 */
public class EmailAllCommand extends Email {

    public static final String COMMAND_WORD = "emailAll";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sends an email to all guests in the "
            + "current filtered list\n"
            + "Example: " + COMMAND_WORD;

    private static final String MESSAGE_MAIL_ALL_PERSON_SUCCESS = "Successfully sent an email to %1$d persons, "
            + "could not send an email to %2$d guests will addresses: %3$s!";

    private static Logger logger = Logger.getLogger("execute");
    private static EmailAllCommand emailCommandSimpleton = null;
    private static String username;
    private static String password;

    private EmailAllCommand() {}

    /**
     * Applying the Simpleton design pattern to EmailAllCommand
     */
    public static EmailAllCommand getInstance() {
        if (emailCommandSimpleton == null) {
            emailCommandSimpleton = new EmailAllCommand();
        }
         return emailCommandSimpleton;
    }

    /**
     * Sends an email to all the persons in the current filtered list
     * @param model is instantiated to get the latest filtered person list
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        String emailSubject;
        String emailMessage;
        StringBuilder invalidEmails = new StringBuilder();
        int successfulEmails = 0;
        int failedEmails = 0;

        for (Person personToMail : lastShownList) {
            assert personToMail != null;

            if (!isValidEmail(personToMail.getEmail().toString())) {
                failedEmails++;
                String invalidEmail = " || " + personToMail.getEmail().toString() + " || ";
                invalidEmails.append(invalidEmail);
                continue;
            }

            // Retrieve all email fields and user credentials and validate that they are not null
            try {
                // Array of strings to store all the necessary information
                String[] information;

                // Retrieve the information through a method in the super class Email
                information = retrieveInformation();
                emailSubject = information[2];
                emailMessage = information[3];
                username = information[0];
                password = information[1];

                // Verify the information exists through the method in the super class Email
                checkFields(username, password, emailSubject, emailMessage);
            } catch (FileNotFoundException | NoSuchElementException | ArrayIndexOutOfBoundsException e) {
                failedEmails = lastShownList.size();
                successfulEmails = 0;

                for (Person person : lastShownList) {
                    String personEmail = person.getEmail().toString() + " ";
                    invalidEmails.append(personEmail);
                }

                break;
            }

            // Creates a new session with the user gmail account as the host
            Properties props = createPropertiesConfiguration();

            // Authenticate the user credentials
            EmailPasswordAuthenticator authenticate = new EmailPasswordAuthenticator();

            // Create a new session using the authenticated credentials and the properties of
            // the Gmail host
            Session session = Session.getDefaultInstance(props, authenticate);

            createAndSendEmail(username, emailSubject, emailMessage,
                    personToMail.getEmail().toString(), session);

            successfulEmails++;
        }

        logger.log(Level.INFO, "All emails sent successfully!");
        return new CommandResult(String.format(MESSAGE_MAIL_ALL_PERSON_SUCCESS, successfulEmails,
                failedEmails, invalidEmails));
    }

    @Override
    public Properties createPropertiesConfiguration() {
        return super.createPropertiesConfiguration();
    }

    @Override
    public String[] retrieveInformation() throws FileNotFoundException {
        return super.retrieveInformation();
    }

    @Override
    public void checkFields(String username, String password, String emailSubject,
                            String emailMessage) throws CommandException {
        super.checkFields(username, password, emailSubject, emailMessage);
        logger.log(Level.INFO, "All fields from Credentials.txt and Message.txt"
                + "received successfully");
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
