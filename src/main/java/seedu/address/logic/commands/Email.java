package seedu.address.logic.commands;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.ui.EmailWindow;

//@@author aaryamNUS
/**
 * This abstract class is inherited by Mail, EmailAll, and ForceEmail commands,
 * in order to reduce code duplicity.
 */
public abstract class Email extends Command {

    /**
     * Creates a new EmailWindow controller which subsequently launches a GUI Window to retrieve
     * username, password, email message and email subject. Error handling is also performed
     * through the try-catch block, which details with CommandException as well as
     * General Exceptions. Once parsed, the private global variables in the MailCommand username,
     * password, emailSubject, and emailMessage are set with the strings received from the EmailWindow
     */
    public String[] retrieveInformation() throws CommandException {
        String[] information;
        EmailWindow newEmailWindow = new EmailWindow();

        newEmailWindow.showAndWait();

        if (newEmailWindow.isSendButton()) {
            information = newEmailWindow.getInformation();
            if (!isValidEmail(information[0])) {
                throw new CommandException(Messages.MESSAGE_INVALID_EMAIL);
            }
        } else if (newEmailWindow.isQuitButton()) {
            throw new CommandException(Messages.MESSAGE_NO_EMAIL_SENT_MESSAGE);
        } else {
            throw new CommandException(Messages.MESSAGE_NO_EMAIL_SENT_MESSAGE);
        }

        return information;
    }

    /**
     * Creates a connection to the host gmail account via gmail's smtp port
     * @return the properties of the host domain server, in this case gmail
     */
    public Properties createPropertiesConfiguration() {
        // Connects to Gmail using it's smtp port and previous authorization
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
     * Creates the message of the email using the emailMessage and emailSubject parameters
     * provided, and sends the email using Transport.send(). Moreover, the 'to' and 'from'
     * fields are provided by the child classes
     */
    public void createAndSendEmail(String username, String emailSubject, String emailMessage,
                                   String recipients, Session session) throws CommandException {
        try {
            // Creates a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set the email of the host
            message.setFrom(new InternetAddress(username));

            // Set the email of the guest
            message.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(recipients));

            // Set email subject and message
            message.setSubject(emailSubject);
            message.setText(emailMessage);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new CommandException(Messages.MESSAGE_NO_INTERNET_CONNECTION_OR_INVALID_CREDENTIALS);
        }
    }

    /**
     * This method checks whether a given email address has the valid format, through the use
     * of a Java Regular expression, which is a special sequence of characters that allows you
     * to match and find other strings or sets of strings
     *
     * A basic outline of the 'expression' string is given below:
     *
     * Subexpression                                Meaning
     *      ^                           Matches the beginning of the line
     *      $                           Matches the end of the line
     *    [...]                         Matches with any character in the brackets
     *     \w                           Matches any word characters
     *    {2,4}                         Matches between 2 and 4 occurrences of preceding expressions
     *
     * @param guestAddress is the address of the guest you wish to send an email to
     * @return a boolean that determines whether the given email address is of the correct format
     * The following regular expression was adapted from zParacha.com,
     * Source: http://zparacha.com/ultimate-java-regular-expression-to-validate-email-address
     */
    public boolean isValidEmail (String guestAddress) {
        String expression = "^[\\w\\-]([\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        // Create a pattern object using the expression provided
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);

        // Create the corresponding matcher object
        Matcher matcher = pattern.matcher(guestAddress);
        return matcher.matches();
    }
}
