package seedu.address.model.event;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.text.ParseException;
import java.util.*;

import seedu.address.model.tag.Tag;

/**
 * Represents an event.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Event {

    //Identity fields
    private EventName eventName;
    private EventDate eventDate;
    private EventVenue eventVenue;
    private EventStartTime eventStartTime;
    private Set<Tag> eventTags = new HashSet<>();
    private boolean isNotInitialisedByUser;
    /**
     * Every field must be present and not null.
     */
    public Event(EventName eventName, EventDate eventDate, EventVenue eventVenue,
                 EventStartTime eventStartTime, Set<Tag> eventTags) {
        requireAllNonNull(eventName, eventDate, eventVenue, eventStartTime, eventTags);
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventVenue = eventVenue;
        this.eventStartTime = eventStartTime;
        this.eventTags.addAll(eventTags);
        this.isNotInitialisedByUser = false;
    }
    public Event(EventName eventName, EventDate eventDate, EventVenue eventVenue,
                 EventStartTime eventStartTime, Set<Tag> eventTags, Boolean eventIsNotInitialisedByUser) {
        requireAllNonNull(eventName, eventDate, eventVenue, eventStartTime, eventTags, eventIsNotInitialisedByUser);
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventVenue = eventVenue;
        this.eventStartTime = eventStartTime;
        this.eventTags.addAll(eventTags);
        this.isNotInitialisedByUser = eventIsNotInitialisedByUser;
    }
    public Event() {
        EventName eventName = new EventName("event not created yet");
        this.eventName = eventName;
        EventDate eventDate = new EventDate("01/10/2018");
        this.eventDate = eventDate;
        EventVenue eventVenue = new EventVenue("NA");
        this.eventVenue = eventVenue;
        EventStartTime eventStartTime = new EventStartTime("1:00 pm");
        this.eventStartTime = eventStartTime;
        Tag tag = new Tag("NA");
        this.eventTags.add(tag);
        this.isNotInitialisedByUser = true;
    }

    public String getName() {
        return eventName.getEventName();
    }

    public String getDate() {
        return eventDate.getEventDate();
    }

    public Date getFullDate() {
        return eventDate.getFullEventDate();
    }

    public String getVenue() {
        return eventVenue.getEventVenue();
    }

    public String getStartTime() {
        return eventStartTime.getEventStartTime();
    }

    private EventName getEventName() {
        return eventName;
    }

    private EventDate getEventDate() {
        return eventDate;
    }

    private EventVenue getEventVenue() {
        return eventVenue;
    }

    private EventStartTime getEventStartTime() {
        return eventStartTime;
    }

    public void setEvent(Event event) {
        if (!this.equals(event)) {
            this.eventName.setEventName(event.getName());
            this.eventDate.setEventDate(event.getDate());
            this.eventVenue.setEventVenue(event.getVenue());
            this.eventStartTime.setEventStartTime(event.getStartTime());
            this.eventTags = event.eventTags;
            this.isNotInitialisedByUser = !event.isUserInitialised();
        }
    }

    /** Adds user-given details of the event. */
    public void addEvent(Event event) {
        if (!this.equals(event)) {
            this.eventName.setEventName(event.getName());
            this.eventDate.setEventDate(event.getDate());
            this.eventVenue.setEventVenue(event.getVenue());
            this.eventStartTime.setEventStartTime(event.getStartTime());
            this.eventTags = event.eventTags;
        }
        this.isNotInitialisedByUser = false;
    }
    /** Deletes user-given details of the event. */
    public void deleteEvent() {
        Event event = new Event();
        this.eventName.setEventName(event.getName());
        this.eventDate.setEventDate(event.getDate());
        this.eventVenue.setEventVenue(event.getVenue());
        this.eventStartTime.setEventStartTime(event.getStartTime());
        this.eventTags = event.eventTags;
        this.isNotInitialisedByUser = true;
    }

    public boolean isUserInitialised() {
        return !isNotInitialisedByUser;
    }
    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getEventTags() {
        return Collections.unmodifiableSet(eventTags);
    }

    /**
     * Returns true if both events of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two events.
     */
    public boolean isSameEvent(seedu.address.model.event.Event otherEvent) {
        if (otherEvent == this) {
            return true;
        }

        return otherEvent != null
                && otherEvent.getName().equals(getName())
                && otherEvent.getDate().equals(getDate())
                && otherEvent.getVenue().equals(getVenue())
                && otherEvent.getStartTime().equals(getStartTime());
    }

    /**
     * Returns true if both events have the same identity and data fields.
     * This defines a stronger notion of equality between two events.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof seedu.address.model.event.Event)) {
            return false;
        }

        seedu.address.model.event.Event otherEvent = (seedu.address.model.event.Event) other;
        return otherEvent.getEventName().equals(getEventName())
                && otherEvent.getEventDate().equals(getEventDate())
                && otherEvent.getEventVenue().equals(getEventVenue())
                && otherEvent.getEventStartTime().equals(getEventStartTime())
                && otherEvent.getEventTags().equals(getEventTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(eventName, eventDate, eventVenue, eventStartTime, eventTags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Date: ")
                .append(getDate())
                .append(" Venue: ")
                .append(getVenue())
                .append(" Start Time: ")
                .append(getStartTime())
                .append(" Tags: ");
        getEventTags().forEach(builder::append);
        return builder.toString();
    }

}
