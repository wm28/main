package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMultiset;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.event.Event;

/**
 * A handler for the {@code BrowserPanel} of the UI.
 */
public class BrowserPanelHandle extends NodeHandle<Node> {

    public static final String BROWSER_PANEL_ID = "#browserPanel";
    private static final String EVENT_NAME_FIELD_ID = "#name";
    private static final String EVENT_DATE_FIELD_ID = "#date";
    private static final String EVENT_VENUE_FIELD_ID = "#venue";
    private static final String EVENT_STARTTIME_FIELD_ID = "#startTime";
    private static final String EVENT_TAGS_FIELD_ID = "#tags";

    private final Label nameLabel;
    private final Label dateLabel;
    private final Label venueLabel;
    private final Label startTimeLabel;
    private final List<Label> tagLabels;

    public BrowserPanelHandle(Node panelNode) {
        super(panelNode);

        nameLabel = getChildNode(EVENT_NAME_FIELD_ID);
        dateLabel = getChildNode(EVENT_DATE_FIELD_ID);
        venueLabel = getChildNode(EVENT_VENUE_FIELD_ID);
        startTimeLabel = getChildNode(EVENT_STARTTIME_FIELD_ID);

        Region tagsContainer = getChildNode(EVENT_TAGS_FIELD_ID);
        tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getEventName() {
        return nameLabel.getText();
    }

    public String getEventDate() {
        return dateLabel.getText();
    }

    public String getEventVenue() {
        return venueLabel.getText();
    }

    public String getEventStartTime() {
        return startTimeLabel.getText();
    }

    public List<String> getEventTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }

    /**
     * Returns true if this handle contains {@code event}.
     */
    public boolean equals(Event event) {
        return getEventName().equals(event.getName())
                && getEventDate().equals(event.getDate())
                && getEventVenue().equals(event.getVenue())
                && getEventStartTime().equals(event.getStartTime())
                && ImmutableMultiset.copyOf(getEventTags())
                .equals(ImmutableMultiset.copyOf(event.getEventTags().stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.toList())));
    }
}
