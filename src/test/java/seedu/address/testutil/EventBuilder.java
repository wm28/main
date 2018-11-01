package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;
import seedu.address.model.event.EventStartTime;
import seedu.address.model.event.EventVenue;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class EventBuilder {

    public static final String DEFAULT_NAME = "Conference";
    public static final String DEFAULT_DATE = "24/10/2019";
    public static final String DEFAULT_VENUE = "SRC 1";
    public static final String DEFAULT_STARTTIME = "10:00 AM";

    private EventName eventName;
    private EventDate eventDate;
    private EventVenue eventVenue;
    private EventStartTime eventStartTime;
    private Set<Tag> eventTags;

    public EventBuilder() {
        eventName = new EventName(DEFAULT_NAME);
        eventDate = new EventDate(DEFAULT_DATE);
        eventVenue = new EventVenue(DEFAULT_VENUE);
        eventStartTime = new EventStartTime(DEFAULT_STARTTIME);
        eventTags = new HashSet<>();
    }

    /**
     * Initializes the EventBuilder with the data of {@code eventToCopy}.
     */
    public EventBuilder(Event eventToCopy) {
        eventName = new EventName(eventToCopy.getName());
        eventDate = new EventDate(eventToCopy.getDate());
        eventVenue = new EventVenue(eventToCopy.getVenue());
        eventStartTime = new EventStartTime(eventToCopy.getStartTime());
        eventTags = new HashSet<>(eventToCopy.getEventTags());
    }

    /**
     * Sets the {@code EventName} of the {@code Event} that we are building.
     */
    public EventBuilder withEventName(String name) {
        this.eventName = new EventName(name);
        return this;
    }

    /**
     * Sets the {@code EventDate} of the {@code Event} that we are building
     */
    public EventBuilder withEventDate(String date) {
        this.eventDate = new EventDate(date);
        return this;
    }

    /**
     * Sets the {@code EventVenue} of the {@code Event} that we are building
     */
    public EventBuilder withEventVenue(String venue) {
        this.eventVenue = new EventVenue(venue);
        return this;
    }

    /**
     * Sets the {@code EventStartTime} of the {@code Event} that we are building
     */
    public EventBuilder withEventStartTime(String startTime) {
        this.eventStartTime = new EventStartTime(startTime);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Event} that we are building.
     */
    public EventBuilder withEventTags(String ... tags) {
        this.eventTags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    public Event build() {
        return new Event(eventName, eventDate, eventVenue, eventStartTime, eventTags);
    }

}
