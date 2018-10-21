package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.event.Event;

public class EventDetailsChangedEvent extends BaseEvent {
    public final Event data;

    public EventDetailsChangedEvent(Event event) { this.data = event;}

    @Override
    public String toString() {
        return "Event Details: " + data.toString();
    }

    public Event getNewDetails() { return this.data; }
}
