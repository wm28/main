package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.logic.Logic;
import seedu.address.model.person.Person;

import com.google.common.eventbus.Subscribe;

//@@author wm28

/**
 * Panel containing the event tabs.
 */
public class EventsPanel extends UiPart<Region> {
    private static final String FXML = "EventsPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(EventsPanel.class);
    private final Logic logic;

    @FXML
    private TabPane eventTabs;


    public EventsPanel(Logic logic) {
        super(FXML);
        this.logic = logic;
        initializeTabs();
    }

    /**
     * creates all existing events on start up
     */
    private void initializeTabs() {
        /**
         * To be Implemented:
         *  1. Use logic to get the different events
         *  2. Use logic to get ObservableList of these events
         *  2. Create Tabs with the ObservableList
         * For Now: Auto create the one addressbook into a tab
         */
        createTab(logic.getFilteredPersonList(), "My first event");
    }

    /**
     * Create an event tab in the tab pane
     */
    private void createTab(ObservableList<Person> personList, String eventTitle) {
        Tab tab = new Tab();
        EventTab eventTab = new EventTab(personList);
        tab.setText(eventTitle);
        tab.setContent(eventTab.getRoot());
        eventTabs.getTabs().add(tab);
    }

    /**
     * Updates the event tabs whenever a change is made to the events
     * NOTE: Tentative
     */
    @Subscribe
    private void handleAddressBookChangedEvent(AddressBookChangedEvent event) {
        //Update event tabs here
    }
}
//@@author
