package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

import java.util.logging.Logger;

/**
 * Ui for each event tab in the EventsPanel
 */
public class EventTab extends UiPart<Region>{
    private final Logger logger = LogsCenter.getLogger(EventTab.class);

    @FXML
    private ListView<Person> personListView;

    private static final String FXML = "EventTab.fxml";

    public EventTab(ObservableList<Person> personList) {
        super(FXML);
        setConnections(personList);
    }

    private void setConnections(ObservableList<Person> personList) {
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new EventTab.PersonListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }
}
