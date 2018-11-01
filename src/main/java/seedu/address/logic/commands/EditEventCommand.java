package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VENUE;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;
import seedu.address.model.event.EventStartTime;
import seedu.address.model.event.EventVenue;

import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditEventCommand extends Command {

    public static final String COMMAND_WORD = "edit_event";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the event details specified. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "EVENT NAME] "
            + "[" + PREFIX_DATE + "EVENT DATE] "
            + "[" + PREFIX_VENUE + "VENUE] "
            + "[" + PREFIX_START_TIME + "START TIME] "
            + "[" + PREFIX_TAG + "EVENT TAGS]...\n"
            + "Example: " + COMMAND_WORD
            + PREFIX_DATE + "12/01/2019 "
            + PREFIX_VENUE + "SRC-RM2";

    public static final String MESSAGE_EDIT_EVENT_SUCCESS = "Edited event details: %1$s";
    public static final String MESSAGE_NOT_EDITED_EVENT = "At least one field to edit must be provided.";
    public static final String MESSAGE_NO_DETAILS = "No details about the event have been put in.";
    public static final String MESSAGE_SAME_EVENT = "These event details are already present.";

    private final EditEventDetails editEventDetails;

    /**
     * @param editEventDetails details to edit the person with
     */
    public EditEventCommand(EditEventDetails editEventDetails) {
        requireNonNull(editEventDetails);
        this.editEventDetails = new EditEventDetails(editEventDetails);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        Event existingEvent = model.getEventDetails();

        if (!model.hasEvent()) {
            throw new CommandException(MESSAGE_NO_DETAILS);
        }

        Event editedEvent = createEditedEvent(existingEvent, editEventDetails);

        if (existingEvent.isSameEvent(editedEvent)) {
            throw new CommandException(MESSAGE_SAME_EVENT);
        }

        model.updateEvent(editedEvent);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_EDIT_EVENT_SUCCESS, editedEvent));
    }

    /**
     * Creates and returns a {@code Event} with the details of {@code existingEvent}
     * edited with {@code editEventDetails}.
     */
    private static Event createEditedEvent(Event existingEvent, EditEventDetails editEventDetails) {
        assert existingEvent != null;

        EventName updatedEventName = editEventDetails.getEventName().orElse(
                new EventName(existingEvent.getName()));
        EventDate updatedEventDate = editEventDetails.getEventDate().orElse(
                new EventDate(existingEvent.getDate()));
        EventVenue updatedEventVenue = editEventDetails.getEventVenue().orElse(
                new EventVenue(existingEvent.getVenue()));
        EventStartTime updatedEventStartTime = editEventDetails.getEventStartTime()
                .orElse(new EventStartTime(existingEvent.getStartTime()));
        Set<Tag> updatedEventTags = editEventDetails.getEventTags()
                .orElse(existingEvent.getEventTags());
        Boolean eventIsNotInitialisedByUser = !existingEvent.isUserInitialised();

        return new Event(updatedEventName, updatedEventDate, updatedEventVenue,
                updatedEventStartTime, updatedEventTags, eventIsNotInitialisedByUser);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditEventCommand)) {
            return false;
        }

        // state check
        EditEventCommand e = (EditEventCommand) other;
        return editEventDetails.equals(e.editEventDetails);
    }

    /**
     * Stores the event details to be edited. Each non-empty field value will replace the
     * corresponding field value of the event.
     */
    public static class EditEventDetails {
        private EventName eventName;
        private EventDate eventDate;
        private EventVenue eventVenue;
        private EventStartTime eventStartTime;
        private Set<Tag> eventTags;

        public EditEventDetails() {}

        /**
         * Copy constructor.
         */
        public EditEventDetails(EditEventDetails toCopy) {
            setEventName(toCopy.eventName);
            setEventDate(toCopy.eventDate);
            setEventVenue(toCopy.eventVenue);
            setEventStartTime(toCopy.eventStartTime);
            setEventTags(toCopy.eventTags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(eventName, eventDate,
                    eventVenue, eventStartTime, eventTags);
        }

        public void setEventName(EventName eventName) {
            this.eventName = eventName;
        }

        public Optional<EventName> getEventName() {
            return Optional.ofNullable(eventName);
        }

        public void setEventDate(EventDate eventDate) {
            this.eventDate = eventDate;
        }

        public Optional<EventDate> getEventDate() {
            return Optional.ofNullable(eventDate);
        }

        public void setEventVenue(EventVenue eventVenue) {
            this.eventVenue = eventVenue;
        }

        public Optional<EventVenue> getEventVenue() {
            return Optional.ofNullable(eventVenue);
        }

        public void setEventStartTime(EventStartTime eventStartTime) {
            this.eventStartTime = eventStartTime;
        }

        public Optional<EventStartTime> getEventStartTime() {
            return Optional.ofNullable(eventStartTime);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         */
        public void setEventTags(Set<Tag> eventTags) {
            this.eventTags = (eventTags != null) ? new HashSet<>(eventTags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getEventTags() {
            return (eventTags != null) ? Optional.of(Collections.unmodifiableSet(eventTags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditEventDetails)) {
                return false;
            }

            // state check
            EditEventDetails e = (EditEventDetails) other;

            return getEventName().equals(e.getEventName())
                    && getEventDate().equals(e.getEventDate())
                    && getEventVenue().equals(e.getEventVenue())
                    && getEventStartTime().equals(e.getEventStartTime())
                    && getEventTags().equals(e.getEventTags());
        }
    }
}
