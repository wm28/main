package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

//@@author aaryamNUS
/**
 * Sends an email to the specified person in the guest list.
 */
public class MailCommand extends Email {

    public static final String COMMAND_WORD = "email";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sends an email to the specified person "
            + "provided by INDEX.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "Example: " + COMMAND_WORD + " 1 ";

    private static final String MESSAGE_MAIL_PERSON_SUCCESS = "Successfully sent email!";

    private static Logger logger = Logger.getLogger("execute");
    private static String username;
    private static String password;
    private Index index;

    /**
     * @param index of the person in the filtered person list to edit
     */
    public MailCommand(Index index) {
        requireNonNull(index);

        this.index = index;
    }

    /**
     * Sends an email to the person at the specified INDEX
     * @param model
     * Note: the following code was adapted from the SendEmail.java class code provided by @Rish on stackoverflow
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        String emailSubject;
        String emailMessage;

        assert index != null;
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToMail = lastShownList.get(index.getZeroBased());
        assert personToMail != null;
        if (!isValidEmail(personToMail.getEmail().toString())) {
            throw new CommandException("Error: The email of the recipient is invalid!");
        }

        // Array of strings to store all the necessary information
        String[] information;
        // Retrieve the information through a method in the super class Email
        information = retrieveInformation();

        try {
            username = information[0];
            password = information[1];
            emailSubject = information[2];
            emailMessage = information[3];

            // Creates a new session with the user gmail account as the host
            Properties props = createPropertiesConfiguration();

            // Authenticate the user credentials
            EmailPasswordAuthenticator authenticate = new EmailPasswordAuthenticator();

            // Create a new session using the authenticated credentials and the properties of
            // the Gmail host
            Session session = Session.getDefaultInstance(props, authenticate);

            createAndSendEmailWithTicket(username, emailSubject, emailMessage,
                    personToMail.getEmail().toString(), session, personToMail.getEmail().toString());
        } catch (NullPointerException ne) {
            logger.log(Level.SEVERE, "Error: retrieving information was unsuccessful!");
        }

        logger.log(Level.INFO, "Email sent successfully");
        return new CommandResult(MESSAGE_MAIL_PERSON_SUCCESS);
    }

    @Override
    public Properties createPropertiesConfiguration() {
        return super.createPropertiesConfiguration();
    }

    @Override
    public String[] retrieveInformation() throws CommandException {
        return super.retrieveInformation();
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
    public void createAndSendEmailWithTicket(String username, String emailSubject, String emailMessage,
                                   String recipient, Session session, String guestUniqueId) throws CommandException {
        super.createAndSendEmailWithTicket(username, emailSubject, emailMessage, recipient, session, guestUniqueId);
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
        if (!(other instanceof MailCommand)) {
            return false;
        }

        // state check
        MailCommand e = (MailCommand) other;
        return index.equals(e.index);
    }
}
