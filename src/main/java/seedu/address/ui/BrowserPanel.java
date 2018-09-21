package seedu.address.ui;

import java.net.URL;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.Person;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;

    @FXML
    private TabPane tabs;

    @FXML
    private StackPane personListPanelPlaceholder;

    public BrowserPanel(ObservableList<Person> personList) {
        super(FXML);
        personListPanel = new PersonListPanel(personList);
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());
        registerAsAnEventHandler(this);
    }



    /*
    /**
     * Frees resources allocated to the browser.
     */
    /*
    public void freeResources() {
        browser = null;
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection());
    }
    */
}
