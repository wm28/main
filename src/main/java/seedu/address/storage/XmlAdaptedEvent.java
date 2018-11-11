package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;
import seedu.address.model.event.EventStartTime;
import seedu.address.model.event.EventVenue;
import seedu.address.model.tag.Tag;

/**
 * JAXB-friendly version of the Event.
 */
public class XmlAdaptedEvent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Event's %s field is missing!";

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private String date;

    @XmlElement(required = true)
    private String venue;

    @XmlElement(required = true)
    private String startTime;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    @XmlElement
    private String isNotInitialisedByUser;

    /**
     * Constructs an XmlAdaptedEvent.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedEvent() {}

    /**
     * Constructs an {@code XmlAdaptedEvent} with the given event details.
     */
    public XmlAdaptedEvent(String name, String date, String venue,
                           String startTime, List<XmlAdaptedTag> tagged, String isNotInitialisedByUser) {
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.startTime = startTime;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
        this.isNotInitialisedByUser = isNotInitialisedByUser;
    }

    /**
     * Converts a given Event into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedEvent
     */
    public XmlAdaptedEvent(Event source) {
        name = source.getName();
        date = source.getDate();
        venue = source.getVenue();
        startTime = source.getStartTime();
        tagged = source.getEventTags().stream()
                .map(XmlAdaptedTag::new)
                .collect(Collectors.toList());
        if (source.isUserInitialised() == true) {
            isNotInitialisedByUser = "false";
        } else {
            isNotInitialisedByUser = "true";
        }
    }

    /**
     * Converts this jaxb-friendly adapted event object into the model's Event object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted event
     */
    public Event toModelType() throws IllegalValueException {
        final List<Tag> eventTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            eventTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format
                    (MISSING_FIELD_MESSAGE_FORMAT, EventName.class.getSimpleName()));
        }
        if (!EventName.isValidEventName(name)) {
            throw new IllegalValueException(EventName.MESSAGE_EVENTNAME_CONSTRAINTS);
        }
        final EventName modelName = new EventName(name);

        if (date == null) {
            throw new IllegalValueException(String.format
                    (MISSING_FIELD_MESSAGE_FORMAT, EventDate.class.getSimpleName()));
        }
        if (!EventDate.isValidEventDate(date)) {
            throw new IllegalValueException(EventDate.MESSAGE_EVENTDATE_CONSTRAINTS);
        }
        final EventDate modelDate = new EventDate(date);

        if (venue == null) {
            throw new IllegalValueException(String.format
                    (MISSING_FIELD_MESSAGE_FORMAT, EventVenue.class.getSimpleName()));
        }
        if (!EventVenue.isValidEventVenue(venue)) {
            throw new IllegalValueException(EventVenue.MESSAGE_EVENTVENUE_CONSTRAINTS);
        }
        final EventVenue modelVenue = new EventVenue(venue);

        if (startTime == null) {
            throw new IllegalValueException(String.format
                    (MISSING_FIELD_MESSAGE_FORMAT, EventStartTime.class.getSimpleName()));
        }
        if (!EventStartTime.isValidEventStartTime(startTime)) {
            throw new IllegalValueException(EventStartTime.MESSAGE_EVENTSTARTTIME_CONSTRAINTS);
        }
        final EventStartTime modelStartTime = new EventStartTime(startTime);

        final Set<Tag> modelTags = new HashSet<>(eventTags);

        boolean modelIsNotInitialisedByUser;
        if (isNotInitialisedByUser == "true") {
            modelIsNotInitialisedByUser = true;
        } else {
            modelIsNotInitialisedByUser = false;
        }

        return new Event(modelName, modelDate, modelVenue, modelStartTime, modelTags, modelIsNotInitialisedByUser);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedEvent)) {
            return false;
        }

        XmlAdaptedEvent otherEvent = (XmlAdaptedEvent) other;
        return Objects.equals(name, otherEvent.name)
                && Objects.equals(date, otherEvent.date)
                && Objects.equals(venue, otherEvent.venue)
                && Objects.equals(startTime, otherEvent.startTime)
                && tagged.equals(otherEvent.tagged);
    }
}
