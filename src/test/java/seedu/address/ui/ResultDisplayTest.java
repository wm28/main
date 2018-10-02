package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.EventsUtil.postNow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.ResultDisplayHandle;
import seedu.address.commons.events.ui.NewResultAvailableEvent;

public class ResultDisplayTest extends GuiUnitTest {

    private static final NewResultAvailableEvent NEW_RESULT_SUCCESS_EVENT_STUB = new NewResultAvailableEvent(
            "success", true);

    private static final NewResultAvailableEvent NEW_RESULT_FAILURE_EVENT_STUB = new NewResultAvailableEvent(
            "failure", true);

    private List<String> defaultStyleWhenCorrect;
    private List<String> errorStyleWhenFailure;

    private ResultDisplayHandle resultDisplayHandle;

    @Before
    public void setUp() {
        ResultDisplay resultDisplay = new ResultDisplay();
        uiPartRule.setUiPart(resultDisplay);

        resultDisplayHandle = new ResultDisplayHandle(getChildNode(resultDisplay.getRoot(),
                ResultDisplayHandle.RESULT_DISPLAY_ID));

        defaultStyleWhenCorrect = new ArrayList<>(resultDisplayHandle.getStyleClass());

        errorStyleWhenFailure = new ArrayList<>(defaultStyleWhenCorrect);
        errorStyleWhenFailure.add(ResultDisplay.ERROR_IN_COMMAND);
    }

    @Test
    public void display() {
        // default result text
        guiRobot.pauseForHuman();
        assertEquals("", resultDisplayHandle.getText());
        assertEquals(defaultStyleWhenCorrect, resultDisplayHandle.getStyleClass());

        // new result received
        assertResultDisplay(NEW_RESULT_SUCCESS_EVENT_STUB);
        assertResultDisplay(NEW_RESULT_FAILURE_EVENT_STUB);
    }

    /**
     * Posts the {@code event} to the {@code EventsCenter}, then verifies the <br>
     *     - the text on the result display matches the {@code event}'s message <br>
     *     - the result display's style is the same as {@code defaultStyleOfResultDisplay} if event is successful,
     *     {@code errorStyleOfResultDisplay} otherwise.
     *
     * Note: This code was adapted from the example implementation provide by @yamgent from SE-EDU
     */
    private void assertResultDisplay(NewResultAvailableEvent event) {
        postNow(event);
        guiRobot.pauseForHuman();

        List<String> expectedStyle = event.isCorrectCommand ? defaultStyleWhenCorrect : errorStyleWhenFailure;

        assertEquals(event.message, resultDisplayHandle.getText());
        assertEquals(expectedStyle, resultDisplayHandle.getStyleClass());
    }
}
