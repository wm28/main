package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionClearedEvent;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a selected {@code Person} on the MainWindow.
 */
public class PersonDisplay extends UiPart<Region> {
    private static final String FXML = "PersonDisplay.fxml";

    //@@author aaryamNUS
    /**
     * The following string array represents different tag colours associated with each guest in the list.
     * Each colour represents a charecteristic of the guest, as summarised below:
     * Orange - Absent, Yellow - Present, Green - Vegetarian, Light Blue - VIP, Red - Bride or Groom,
     * White - Guest Speaker, Black - Not Paid, Purple - Paid
     *
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    private static final String[] TAG_COLORS = {"orange", "yellow", "green", "lightblue",
                                                "red", "white", "black", "purple"};
    //@@author

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label attendance;
    @FXML
    private Label email;
    @FXML
    private Label payment;
    @FXML
    private FlowPane tags;

    public PersonDisplay() {
        super(FXML);
        registerAsAnEventHandler(this);
    }

    //@@author aaryamNUS
    /**
     * Method getTagColor returns the specific color style for {@code tagName}'s label.
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    private String getTagColor(String tagName) {
        /**
         * Using the hashcode of the tag name ensures the color of the tag remains consistent
         * during different iterations of the code by generating a random color
         */

        return TAG_COLORS[Math.abs(tagName.hashCode()) % TAG_COLORS.length];
    }

    /**
     * Method createTags initialises the tag labels for {@code person}
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    private void createTags(Person person) {
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }
    //@@author

    //@@author wm28
    /**
     * Removes all tags from the PersonDisplay Ui component
     */
    private void removeTags() {
        tags.getChildren().clear();
    }

    /**
     * Fills in details of the selected {@code person} to the PersonDisplay Ui component
     */
    private void fillInPersonDetails(Person person) {
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        attendance.setText(person.getAttendance().attendanceValue);
        email.setText(person.getEmail().value);
        payment.setText(person.getPayment().paymentValue);
        removeTags();
        createTags(person);
    }

    /**
     * Removes details of the previously selected {@code Person} displayed in the PersonDisplay Ui component
     */
    private void removePersonDetails() {
        name.setText("");
        phone.setText("");
        attendance.setText("");
        email.setText("");
        payment.setText("");
        removeTags();
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        fillInPersonDetails(event.getNewSelection());
    }

    /**
     * Clears {@code Person} details in PersonDisplay Ui component when selection is cleared in the PersonListPanel
     */
    @Subscribe
    public void handlePersonPanelSelectionClearedEvent(PersonPanelSelectionClearedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event,
                "Local data changed, clearing selected guest details "));
        removePersonDetails();
    }
    //@@author
}
