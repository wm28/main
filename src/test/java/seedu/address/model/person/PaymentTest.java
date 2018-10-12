package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class PaymentTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Attendance(null));
    }

    @Test
    public void constructor_invalidPayment_throwsIllegalArgumentException() {
        String invalidPayment = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Payment(invalidPayment));
    }

    @Test
    public void isValidPayment() {
        // null address
        Assert.assertThrows(NullPointerException.class, () -> Payment.isValidPayment(null));

        // invalid Payment
        assertFalse(Payment.isValidPayment("")); // empty string
        assertFalse(Payment.isValidPayment(" ")); // spaces only

        // valid Payment
        assertTrue(Payment.isValidPayment("PAID"));
        assertTrue(Payment.isValidPayment("NOT PAID"));
        assertTrue(Payment.isValidPayment("PENDING"));
    }
}
