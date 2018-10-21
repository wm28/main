package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.event.EventName;
import seedu.address.model.event.Event;
import seedu.address.model.tag.Tag;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedEvent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Event's %s field is missing!";

    @XmlElement(required = true)
    private String name;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    @XmlElement
    private String isNotInitialisedByUser;

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedEvent() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given person details.
     */
    public XmlAdaptedEvent(String name, List<XmlAdaptedTag> tagged, String isNotInitialisedByUser) {
        this.name = name;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
        this.isNotInitialisedByUser = isNotInitialisedByUser;
    }

    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedEvent(Event source) {
        name = source.getName();
        tagged = source.getEventTags().stream()
                .map(XmlAdaptedTag::new)
                .collect(Collectors.toList());
        if (source.isUserInitialised() == true)
            isNotInitialisedByUser = "false";
        else
            isNotInitialisedByUser = "true";
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Event toModelType() throws IllegalValueException {
        final List<Tag> eventTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            eventTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, EventName.class.getSimpleName()));
        }
        if (!EventName.isValidEventName(name)) {
            throw new IllegalValueException(EventName.MESSAGE_EVENTNAME_CONSTRAINTS);
        }
        final EventName modelName = new EventName(name);

        //@@author SE-EDU
        final Set<Tag> modelTags = new HashSet<>(eventTags);

        Boolean modelisNotInitialisedByUser;
        if (isNotInitialisedByUser == "true")
            modelisNotInitialisedByUser = true;
        else
            modelisNotInitialisedByUser = false;

        return new Event(modelName, modelTags, modelisNotInitialisedByUser);
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
                && tagged.equals(otherEvent.tagged);
    }
}
