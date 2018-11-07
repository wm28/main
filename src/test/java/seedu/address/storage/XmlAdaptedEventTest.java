package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.XmlAdaptedEvent.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalPersons.typicalEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;
import seedu.address.model.event.EventStartTime;
import seedu.address.model.event.EventVenue;
import seedu.address.testutil.Assert;

public class XmlAdaptedEventTest {
    private static final String INVALID_EVENT_NAME = "W@dding";
    private static final String INVALID_EVENT_DATE = "29/02/2019";
    private static final String INVALID_EVENT_VENUE = "PLACE %99";
    private static final String INVALID_EVENT_STARTTIME = "00:00";
    private static final String INVALID_EVENT_TAG = "#Casual";

    private static final String VALID_EVENT_NAME = typicalEvent.getName();
    private static final String VALID_EVENT_DATE = typicalEvent.getDate();
    private static final String VALID_EVENT_VENUE = typicalEvent.getVenue();
    private static final String VALID_EVENT_STARTTIME = typicalEvent.getStartTime();
    private static final List<XmlAdaptedTag> VALID_EVENT_TAGS = typicalEvent.getEventTags().stream()
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validEventDetails_returnsEvent() throws Exception {
        XmlAdaptedEvent event = new XmlAdaptedEvent(typicalEvent);
        assertEquals(typicalEvent, event.toModelType());
    }

    @Test
    public void toModelType_invalidEventName_throwsIllegalValueException() {
        XmlAdaptedEvent event =
                new XmlAdaptedEvent(INVALID_EVENT_NAME, VALID_EVENT_DATE, VALID_EVENT_VENUE,
                        VALID_EVENT_STARTTIME, VALID_EVENT_TAGS, "false");
        String expectedMessage = EventName.MESSAGE_EVENTNAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_nullEventName_throwsIllegalValueException() {
        XmlAdaptedEvent event = new XmlAdaptedEvent(null, VALID_EVENT_DATE, VALID_EVENT_VENUE,
                VALID_EVENT_STARTTIME, VALID_EVENT_TAGS, "TRUE");
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, EventName.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_invalidEventDate_throwsIllegalValueException() {
        XmlAdaptedEvent event =
                new XmlAdaptedEvent(VALID_EVENT_NAME, INVALID_EVENT_DATE, VALID_EVENT_VENUE,
                        VALID_EVENT_STARTTIME, VALID_EVENT_TAGS, "false");
        String expectedMessage = EventDate.MESSAGE_EVENTDATE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_nullEventDate_throwsIllegalValueException() {
        XmlAdaptedEvent event = new XmlAdaptedEvent(VALID_EVENT_NAME, null, VALID_EVENT_VENUE,
                VALID_EVENT_STARTTIME, VALID_EVENT_TAGS, "TRUE");
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, EventDate.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_invalidEventVenue_throwsIllegalValueException() {
        XmlAdaptedEvent event =
                new XmlAdaptedEvent(VALID_EVENT_NAME, VALID_EVENT_DATE, INVALID_EVENT_VENUE,
                        VALID_EVENT_STARTTIME, VALID_EVENT_TAGS, "false");
        String expectedMessage = EventVenue.MESSAGE_EVENTVENUE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_nullEventVenue_throwsIllegalValueException() {
        XmlAdaptedEvent event = new XmlAdaptedEvent(VALID_EVENT_NAME, VALID_EVENT_DATE, null,
                VALID_EVENT_STARTTIME, VALID_EVENT_TAGS, "TRUE");
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, EventVenue.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_invalidEventStartTime_throwsIllegalValueException() {
        XmlAdaptedEvent event =
                new XmlAdaptedEvent(VALID_EVENT_NAME, VALID_EVENT_DATE, VALID_EVENT_VENUE,
                        INVALID_EVENT_STARTTIME, VALID_EVENT_TAGS, "false");
        String expectedMessage = EventStartTime.MESSAGE_EVENTSTARTTIME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_nullEventStartTime_throwsIllegalValueException() {
        XmlAdaptedEvent event = new XmlAdaptedEvent(VALID_EVENT_NAME, VALID_EVENT_DATE, VALID_EVENT_VENUE,
                null, VALID_EVENT_TAGS, "TRUE");
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, EventStartTime.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_EVENT_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_EVENT_TAG));
        XmlAdaptedEvent event =
                new XmlAdaptedEvent(VALID_EVENT_NAME, VALID_EVENT_DATE, VALID_EVENT_VENUE, VALID_EVENT_STARTTIME,
                        invalidTags, "FALSE");
        Assert.assertThrows(IllegalValueException.class, event::toModelType);
    }
    /*
    @Test
    public void toModelType_invalidEventStartTime_throwsIllegalValueException() {
        XmlAdaptedEvent event =
                new XmlAdaptedEvent(VALID_EVENT_NAME, VALID_EVENT_DATE, VALID_EVENT_VENUE,
                        INVALID_EVENT_STARTTIME, VALID_EVENT_TAGS, "false");
        String expectedMessage = EventStartTime.MESSAGE_EVENTSTARTTIME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_nullEventStartTime_throwsIllegalValueException() {
        XmlAdaptedEvent event = new XmlAdaptedEvent(VALID_EVENT_NAME, VALID_EVENT_DATE, VALID_EVENT_VENUE,
                null, VALID_EVENT_TAGS, "TRUE");
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, EventStartTime.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }*/

}
