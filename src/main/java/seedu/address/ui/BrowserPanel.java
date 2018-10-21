package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.model.event.Event;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private seedu.address.model.event.Event event;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;

    @FXML
    private FlowPane tags;

    public BrowserPanel(seedu.address.model.event.Event event) {
        super(FXML);
        fillInEventDetails(event);
        registerAsAnEventHandler(this);
    }

    /**
     * Fills in details of the selected {@code person} to the PersonDisplay Ui component
     */
    private void fillInEventDetails(Event event) {
        name.setText(event.getName());
        removeTags();
        createTags(event);
    }

    //@@author aaryamNUS
    /**
     * Method createTags initialises the tag labels for {@code person}
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    private void createTags(seedu.address.model.event.Event event) {
        event.getEventTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    //@@author wm28
    /**
     * Removes all tags from the PersonDisplay Ui component
     */
    private void removeTags() {
        tags.getChildren().clear();
    }

    @Subscribe
    private void handleAddressBookChangedEvent(AddressBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        fillInEventDetails(event.getNewDetails());
    }
}
