package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

//@@author aaryamNUS
/**
 * Sends an email to any guests that have at least one of the specified tags
 */
public class EmailSpecificCommand extends Email {

    public static final String COMMAND_WORD = "emailSpecific";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sends an email to the guests that have "
            + "at least one of the provided Tags.\n"
            + "Parameters: "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "VIP " + PREFIX_TAG + "Paid";

    private static final String MESSAGE_MAIL_PERSON_SUCCESS = "Successfully sent an email to %1$d emails, "
            + "did not send an email to %2$d guests!";

    private static Logger logger = Logger.getLogger("execute");
    private static String username;
    private static String password;
    private static String emailSubject;
    private static String emailMessage;
    private final Set<Tag> tagsToSend;

    /**
     * @param tagsToSend of the guests with the specified tags
     */
    public EmailSpecificCommand(Set<Tag> tagsToSend) {
        requireNonNull(tagsToSend);
        this.tagsToSend = tagsToSend;
    }

    /**
     * Sends an email to all guests that have at least one of the specified tags
     * @param model must be non-null
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Check for duplicate emails to ensure each guest only receives one email, even if
        // multiple guests have registered under the same email
        HashSet<String> personsToSendEmail = createMailingList(lastShownList, tagsToSend);

        // Strings to represent the recipients of the email (i.e. all the guests in the list)
        String recipients = recipientCreator(personsToSendEmail);

        try {
            // Array of strings to store all the necessary information
            String[] information;
            // Retrieve the information through a method in the super class Email
            information = retrieveInformation();
            setInformation(information);

            // Creates a new session with the user Gmail account as the host
            Properties props = createPropertiesConfiguration();

            // Authenticate the user credentials
            EmailPasswordAuthenticator authenticate = new EmailPasswordAuthenticator();

            // Create a new session using the authenticated credentials and the properties of
            // the Gmail host
            Session session = Session.getDefaultInstance(props, authenticate);

            createAndSendEmail(username, emailSubject, emailMessage,
                    recipients, session);
        } catch (NullPointerException ne) {
            logger.log(Level.SEVERE, "Error: retrieving information was unsuccessful!");
        }

        logger.log(Level.INFO, "Emails sent successfully to all guests with the specified tags!");
        return new CommandResult(String.format(MESSAGE_MAIL_PERSON_SUCCESS, personsToSendEmail.size(),
                lastShownList.size() - personsToSendEmail.size()));
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
     * Create a mailing list with all the email addresses of guests, this method
     * also filters out duplicate or invalid email addresses
     * @param lastShownList the last known guest list
     * @param tagsToSend list of tags to match with guests
     * @return the email list
     */
    private HashSet<String> createMailingList(List<Person> lastShownList, Set<Tag> tagsToSend) throws CommandException {
        HashSet<String> personsToSendEmail = new HashSet<>();

        // First check which of the guests have at least one of the specified tags,
        // and filter out any duplicate guests
        for (Person individualGuests : lastShownList) {
            for (Tag singleTags : tagsToSend) {
                if (individualGuests.getTags().contains(singleTags)) {
                    if (personsToSendEmail.contains(individualGuests.getEmail().toString())) {
                        logger.log(Level.INFO, "Guest email address has already been sent an email!");
                    } else if (!isValidEmail(individualGuests.getEmail().toString())) {
                        logger.log(Level.INFO, "Guest email address is not valid!");
                    } else {
                        personsToSendEmail.add(individualGuests.getEmail().toString());
                    }
                }
            }
        }

        // If no guests have the specified tags, throw a Command Exception
        if (personsToSendEmail.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_NO_PERSONS_WITH_TAGS);
        }

        return personsToSendEmail;
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
        if (!(other instanceof EmailSpecificCommand)) {
            return false;
        }

        // state check
        EmailSpecificCommand e = (EmailSpecificCommand) other;
        return tagsToSend.equals(e.tagsToSend);
    }
}
