package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.NewResultAvailableEvent;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    public static final String ERROR_IN_COMMAND = "error";
    private static final Logger logger = LogsCenter.getLogger(ResultDisplay.class);
    private static final String FXML = "ResultDisplay.fxml";

    private final StringProperty displayed = new SimpleStringProperty("");

    @FXML
    private TextArea resultDisplay;

    public ResultDisplay() {
        super(FXML);
        resultDisplay.textProperty().bind(displayed);
        registerAsAnEventHandler(this);
    }

    @Subscribe
    private void handleNewResultAvailableEvent(NewResultAvailableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> {
            displayed.setValue(event.message);

            if (event.isCorrectCommand) {
                setStyleforSuccessfulCommand();
            }
            else {
                setStyleforIncorrectCommand();
            }
        });
    }

    /**
     * Sets the {@code ResultDisplay} style to display the default style
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */

    private void setStyleforSuccessfulCommand() {
        resultDisplay.getStyleClass().remove(ERROR_IN_COMMAND);
    }

    /**
     * Sets the {@code ResultDisplay} style to display an incorrect command
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */

    private void setStyleforIncorrectCommand() {
        ObservableList<String> styleClass = resultDisplay.getStyleClass();

        if (styleClass.contains(ERROR_IN_COMMAND)) {
            return;
        }

        styleClass.add(ERROR_IN_COMMAND);
    }

}
