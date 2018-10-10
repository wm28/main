package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author wm28
/**
 * Indicates that selection is cleared in the Person List Panel
 */
public class PersonPanelSelectionClearedEvent extends BaseEvent {
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
//@@author

