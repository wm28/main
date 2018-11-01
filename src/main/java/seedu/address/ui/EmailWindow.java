package seedu.address.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.Email;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

//@@author aaryamNUS
/**
 * Controller for an email window.
 */
public class EmailWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(EmailWindow.class);
    private static final String FXML = "EmailWindow.fxml";
    private String[] information = new String[4];
    private boolean isStillOpen = false;
    private boolean isSendButtonPressed = false;
    private boolean isQuitButtonPressed = false;
    private Email email = new Email() {
        @Override
        public CommandResult execute(Model model, CommandHistory history) throws CommandException {
            return null;
        }
    };

    public EmailWindow() {
        super(FXML, new Stage());
    }

    @FXML
    private Button quitButton;

    @FXML
    private Button sendButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextArea messageField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField subjectField;

    @FXML
    private VBox emailContainer;

    /**
     * This is the event handler when the Send Email Button is pressed.
     * Checks whether username, password, email subject and email message are
     * provided by the user. If any of the parameters are either null or an
     * empty string, the respective alert is thrown. Moreover, if all the fields are
     * correct, the information String array is returned back to the Email abstract class
     * to construct the email message and connect to the Gmail smtp server
     */
    @FXML
    public void handleSendEmailButtonAction() {
        information[0] = emailField.getText();
        information[1] = passwordField.getText();
        information[2] = subjectField.getText();
        information[3] = messageField.getText();

        if (emailField.getText() == null
                || emailField.getText().replaceAll("\\s+", "").equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, Messages.MESSAGE_USERNAME_NOT_PROVIDED, ButtonType.OK);
            alert.showAndWait();
            isStillOpen = true;
        } else if (passwordField.getText() == null
                || passwordField.getText().replaceAll("\\s+", "").equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, Messages.MESSAGE_PASSWORD_NOT_PROVIDED, ButtonType.OK);
            alert.showAndWait();

            isStillOpen = true;
        } else if (subjectField.getText() == null
                || subjectField.getText().replaceAll("\\s+", "").equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, Messages.MESSAGE_EMAIL_SUBJECT_NOT_PROVIDED, ButtonType.OK);
            alert.showAndWait();

            isStillOpen = true;
        } else if (messageField.getText() == null
                || messageField.getText().replaceAll("\\s+", "").equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, Messages.MESSAGE_EMAIL_MESSAGE_NOT_PROVIDED, ButtonType.OK);
            alert.showAndWait();

            isStillOpen = true;
        } else {
            logger.log(Level.INFO, "All fields from EmailWindow received successfully!");
            isStillOpen = false;
            isSendButtonPressed = true;
        }

        if (!isStillOpen) {
            getRoot().close();
        }
    }

    /**
     * This is the event handler when the Quit Button is pressed. When done so,
     * the EmailWindow event closes and the command box provides a simple message to the user
     * indicating that they have not sent an email to any guests
     */
    @FXML
    public void handleQuitButtonAction() {
        isQuitButtonPressed = true;
        getRoot().close();
    }

    /**
     * Shows the email window
     */
    public void showAndWait() {
        logger.fine("Showing email window");
        isStillOpen = true;
        getRoot().showAndWait();
    }

    public String[] getInformation() {
        return information;
    }

    public boolean isSendButton() {
        return isSendButtonPressed;
    }

    public boolean isQuitButton() {
        return isQuitButtonPressed;
    }
}
