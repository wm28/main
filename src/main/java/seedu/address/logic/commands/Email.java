package seedu.address.logic.commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.zxing.WriterException;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.QrUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.ui.EmailWindow;

//@@author aaryamNUS
/**
 * This abstract class is inherited by Mail, EmailAll, and ForceEmail commands,
 * in order to reduce code duplicity.
 */
public abstract class Email extends Command {
    private static Logger logger = Logger.getLogger("createAndSendEmailWithTicket");
    private File qrImage;

    public Email() {}

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
     * fields are provided by the child classes. Moreover, this method is only used for the EMAIL INDEX
     * command as it attaches a QR code in the email message for the event manager to scan
     */
    public void createAndSendEmailWithTicket(String username, String emailSubject, String emailMessage,
                                   String recipients, Session session, String guestUniqueId)
                                   throws CommandException {
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

            // Generate and set the overall email message content
            Multipart multipart = createQrAndEmailMessage(emailMessage, guestUniqueId);
            message.setContent(multipart);

            // Send the email
            Transport.send(message);

            // Delete the image file once the email has been sent
            qrImage.deleteOnExit();
        } catch (MessagingException e) {
            throw new CommandException(Messages.MESSAGE_NO_INTERNET_CONNECTION_OR_INVALID_CREDENTIALS);
        }
    }

    /**
     * Generates the message of the email by generating a QR Code image and attaching it in the
     * email. This command is used when the input command is email INDEX
     * @param emailMessage text message written by the user
     * @param guestUniqueId Unique ID of the guest to be encoded by the QR Code
     * @return the multipart
     */
    private Multipart createQrAndEmailMessage(String emailMessage, String guestUniqueId) {
        // Create a multipart message to facilitate the attachment of images
        Multipart multipart = new MimeMultipart();

        try {
            // Create the message part of the Email and set the message
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(emailMessage);

            // Set the message part
            multipart.addBodyPart(messageBodyPart);

            // The second body part is the QR Code
            messageBodyPart = new MimeBodyPart();

            // Generate a QR code, which represents the guest ticket
            // QR code is generated using a guest's UID, hence it is ensured to be unique
            QrUtil getGuestTicket = new QrUtil();
            BufferedImage ticket = getGuestTicket.generateQr(guestUniqueId);

            // Create a temporary image file to store the BufferedImage and for it to be
            // read by DataSource()
            qrImage = new File("temp.png");
            ImageIO.write(ticket, "png", qrImage);
            DataSource source = new FileDataSource(qrImage);

            // Set the details of the image attachment
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("Your Ticket");
            multipart.addBodyPart(messageBodyPart);
        } catch (WriterException e) {
            logger.log(Level.SEVERE, "Error: exception when retrieving QRCode BufferedImage!");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error: exception when writing the BufferedImage!");
        } catch (MessagingException e) {
            logger.log(Level.SEVERE, "Error in generating QR code!");
        }

        return multipart;
    }

    /**
     * Creates the message of the email using the emailMessage and emailSubject parameters
     * provided, and sends the email using Transport.send(). Moreover, the 'to' and 'from'
     * fields are provided by the child classes. This method is used by the EmailALL and
     * EmailSpecific commands
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

    /**
     * Creates the recipient string based on all of the persons to send and email to
     * @param personsToSendEmail is the original list of all guests in the list
     * @return the recipients string
     */
    public String recipientCreator(HashSet<String> personsToSendEmail) {
        String recipients;
        StringBuilder recipientBuilder = new StringBuilder();

        // Create a string with all the recipients
        for (String personToEmail : personsToSendEmail) {
            String individualGuest = personToEmail + ",";
            recipientBuilder.append(individualGuest);
        }

        recipients = removeLastChar(recipientBuilder.toString());

        return recipients;
    }

    /**
     * Removes the last character out of the recipients String as it contains
     * an unwanted ',' character
     * @param string is the original recipients string
     * @return the substring
     */
    private static String removeLastChar(String string) {
        return string.substring(0, string.length() - 1);
    }
}
