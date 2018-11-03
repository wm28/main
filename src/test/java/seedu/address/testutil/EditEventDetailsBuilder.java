package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditEventCommand;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;
import seedu.address.model.event.EventStartTime;
import seedu.address.model.event.EventVenue;

import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditEventDetails objects.
 */
public class EditEventDetailsBuilder {

    private EditEventCommand.EditEventDetails descriptor;

    public EditEventDetailsBuilder() {
        descriptor = new EditEventCommand.EditEventDetails();
    }

    public EditEventDetailsBuilder(EditEventCommand.EditEventDetails descriptor) {
        this.descriptor = new EditEventCommand.EditEventDetails(descriptor);
    }

    /**
     * Returns an {@code EditEventDetails} with fields containing {@code event}'s details
     */
    public EditEventDetailsBuilder(Event event) {
        descriptor = new EditEventCommand.EditEventDetails();
        descriptor.setEventName(new EventName(event.getName()));
        descriptor.setEventDate(new EventDate(event.getDate()));
        descriptor.setEventVenue(new EventVenue(event.getVenue()));
        descriptor.setEventStartTime(new EventStartTime(event.getStartTime()));
        descriptor.setEventTags(event.getEventTags());
    }

    /**
     * Sets the {@code EventName} of the {@code EditEventDetails} that we are building.
     */
    public EditEventDetailsBuilder withEventName(String eventName) {
        descriptor.setEventName(new EventName(eventName));
        return this;
    }

    /**
     * Sets the {@code EventDate} of the {@code EditEventDetails} that we are building.
     */
    public EditEventDetailsBuilder withEventDate(String eventDate) {
        descriptor.setEventDate(new EventDate(eventDate));
        return this;
    }

    /**
     * Sets the {@code EventVenue} of the {@code EditEventDetails} that we are building.
     */
    public EditEventDetailsBuilder withEventVenue(String eventVenue) {
        descriptor.setEventVenue(new EventVenue(eventVenue));
        return this;
    }

    /**
     * Sets the {@code EventStartTime} of the {@code EditEventDetails} that we are building.
     */
    public EditEventDetailsBuilder withEventStartTime(String eventStartTime) {
        descriptor.setEventStartTime(new EventStartTime(eventStartTime));
        return this;
    }

    /**
     * Parses the {@code EventTags} into a {@code Set<Tag>} and set it to the {@code EditEventDetails}
     * that we are building.
     */
    public EditEventDetailsBuilder withEventTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setEventTags(tagSet);
        return this;
    }

    public EditEventCommand.EditEventDetails build() {
        return descriptor;
    }

}
