package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
public class MailCommand extends Command {

    public static final String COMMAND_WORD = "email";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sends an email to the specified person "
            + "provided by INDEX.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "Example: " + COMMAND_WORD + " 1 ";

    private static final String MESSAGE_MAIL_PERSON_SUCCESS = "Successfully sent email!";
    private static Logger logger = Logger.getLogger("execute");
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

        assert index != null;
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToMail = lastShownList.get(index.getZeroBased());
        assert personToMail != null;

        Properties props = createPropertiesConfiguration();
        EmailPasswordAuthenticator authenticate = new EmailPasswordAuthenticator();
        Session session = Session.getDefaultInstance(props, authenticate);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("eventmanager2k18@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(personToMail.getEmail().toString()));
            message.setSubject("Booking confirmation for Avengers Infinity War Part 3");
            message.setText("Dear valued customer,\n\nYou are our lucky customer! "
                    + "We hope you will continue to support Invites and remain a "
                    + "loyal customer. Please accept this gold-plated AddressBook as "
                    + "a token of our appreciation.\n\nYours Sincerely,\nThe Invites Team");

            Transport.send(message);
        } catch (MessagingException mex) {
            logger.log(Level.SEVERE, "Error: could not send email, have you\n"
                    + "given Invites application access to your Gmail account?");
            mex.printStackTrace();
        }

        logger.log(Level.INFO, "Email sent successfully");
        return new CommandResult(MESSAGE_MAIL_PERSON_SUCCESS);
    }

    /**
     * Creates a connection to the host gmail account via gmail's smtp port
     * @return
     */
    private static Properties createPropertiesConfiguration() {
        return new Properties() {
            {
                put("mail.smtp.auth", "true");
                put("mail.smtp.starttls.enable", "true");
                put("mail.smtp.host", "smtp.gmail.com");
                put("mail.smtp.port", "587");
            }
        };
    }

    /**
     * Authenticates the user account based on the credentials provided
     */
    private static class EmailPasswordAuthenticator extends Authenticator {
        private String username = "eventmanager2k18@gmail.com";
        private String password = "cs2113t2018";

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
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
