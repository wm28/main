package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    //@@author aaryamNUS
    /**
     * The following string array represents different tag colours associated with each guest in the list.
     * Each colour represents a characteristic of the guest, as summarised below:
     * Orange - Halal, Yellow - Vegan, Green - Vegetarian, Light Blue - VIP, Red - Bride or Groom,
     * Black - Guest, White - Guest Speaker, Anything else - Pink
     *
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    private static final String[] TAG_COLORS = {"orange", "yellow", "green", "lightblue",
                                                "red", "white", "black", "purple", "grey"};
    //@@author

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
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

    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        attendance.setText(person.getAttendance().attendanceValue);
        email.setText(person.getEmail().value);
        payment.setText(person.getPayment().paymentValue);
        createTags(person);
    }

    //@@author aaryamNUS
    /**
        Method getTagColor returns the specific color style for {@code tagName}'s label.
        Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    private String getTagColor(String tagName) {
        /**
         * Using the hashcode of the tag name ensures the color of the tag remains consistent
         * during different iterations of the code by generating a random color
         */
        int number = Math.abs(tagName.hashCode()) % TAG_COLORS.length;
        return TAG_COLORS[number];
    }

    /**
        Method createTags initialises the tag labels for {@code person}
        Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    private void createTags(Person person) {
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }
    //@@author

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }
}
