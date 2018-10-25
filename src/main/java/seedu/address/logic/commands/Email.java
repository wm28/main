package seedu.address.logic.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;
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

//@@author aaryamNUS
/**
 * This abstract class is inherited by Mail, EMailAll, and ForceEmail commands,
 * in order to reduce code duplicity.
 */
public abstract class Email extends Command {

    /**
     * Checks whether username, password, email subject and email message are
     * provided by the user. If any of the parameters are either null or an
     * empty string, the respective command exception is throw.
     * @throws CommandException whenever a field in the email of credentials is missing
     */
    public void checkFields(String username, String password,
                            String emailSubject, String emailMessage) throws CommandException {
        if (username == null || username.replaceAll("\\s+", "").equals("")) {
            throw new CommandException(Messages.MESSAGE_USERNAME_NOT_PROVIDED);
        } else if (password == null || password.replaceAll("\\s+", "").equals("")) {
            throw new CommandException(Messages.MESSAGE_PASSWORD_NOT_PROVIDED);
        } else if (emailSubject == null || emailSubject.replaceAll("\\s+", "").equals("")) {
            throw new CommandException(Messages.MESSAGE_EMAIL_SUBJECT_NOT_PROVIDED);
        } else if (emailMessage == null || emailMessage.replaceAll("\\s+", "").equals("")) {
            throw new CommandException(Messages.MESSAGE_EMAIL_MESSAGE_NOT_PROVIDED);
        }
    }

    /**
     * Reads and parses the files Credentials.txt and Message.txt to retrieve
     * username, password, email message and email subject. Error handling is also performed
     * through the try-catch block, which details with FileNotFoundExceptions as well as
     * General Exceptions. Once parsed, the private global variables in the MailCommand username,
     * password, emailSubject, and emailMessage are set with the strings parsed from the .txt files
     */
    public String[] retrieveInformation() throws FileNotFoundException {
        String[] information = new String[4];

        try {
            File credentials = new File("src/main/resources/EmailData/Credentials.txt")
                    .getAbsoluteFile();
            Scanner credentialsScanner = new Scanner(credentials);

            // Retrieve the two strings in Credentials.txt
            String unmodifiedUsername = credentialsScanner.nextLine();
            String unmodifiedPassword = credentialsScanner.nextLine();

            // Parse the strings to retrieve the username and password within quotation marks
            information[0] = unmodifiedUsername.split("\"")[1];
            information[1] = unmodifiedPassword.split("\"")[1];

        } catch (FileNotFoundException fe) {
            throw new FileNotFoundException("Error: The file Credentials.txt was not found!");
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new ArrayIndexOutOfBoundsException(Messages.MESSAGE_PARSE_ERROR_MESSAGE);
        } catch (NoSuchElementException ne) {
            throw new NoSuchElementException("Error: Please specify your credentials, email message, "
                    + "and email subject in Credentials.txt and Message.txt");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            File message = new File("src/main/resources/EmailData/Message.txt")
                    .getAbsoluteFile();
            Scanner messageScanner = new Scanner(message);

            String unmodifiedSubject = messageScanner.nextLine();
            StringBuilder unmodifiedMessage = new StringBuilder();

            while (messageScanner.hasNextLine()) {
                unmodifiedMessage.append(messageScanner.nextLine());
                unmodifiedMessage.append("\n");
            }

            information[2] = unmodifiedSubject.split("\"")[1];
            information[3] = unmodifiedMessage.toString().split("\"")[1];

        } catch (FileNotFoundException fe) {
            throw new FileNotFoundException("Error: The file Message.txt was not found!");
        } catch (NoSuchElementException ne) {
            throw new NoSuchElementException("Error: Please specify your credentials, email message, "
                    + "and email subject in Credentials.txt and Message.txt");
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new ArrayIndexOutOfBoundsException(Messages.MESSAGE_PARSE_ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
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
                              String recipient, Session session) throws CommandException {
        try {
            // Creates a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set the email of the host
            message.setFrom(new InternetAddress(username));

            // Set the email of the guest
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            // Set email subject and message
            message.setSubject(emailSubject);
            message.setText(emailMessage);

            Transport.send(message);
        } catch (MessagingException mex) {
            throw new CommandException("Error: could not send email, please ensure you have strong "
                    + "internet connectivity.");
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
