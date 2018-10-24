package seedu.address.commons.events.ui;

import java.util.List;

import seedu.address.commons.events.BaseEvent;
import seedu.address.logic.converters.ImportError;

/**
 * An event requesting to show the import report
 */
public class ShowImportReportEvent extends BaseEvent {
    public final List<ImportError> errors;

    public ShowImportReportEvent(List<ImportError> errors){
        this.errors = errors;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
