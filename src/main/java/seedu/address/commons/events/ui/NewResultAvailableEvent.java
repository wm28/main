package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates that a new result is available.
 */
public class NewResultAvailableEvent extends BaseEvent {

    public final String message;
    public final boolean isCorrectCommand;

    public NewResultAvailableEvent(String message, boolean isCorrectCommand) {
        this.message = message;
        this.isCorrectCommand = isCorrectCommand;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
