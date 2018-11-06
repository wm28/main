package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

//@@author Sarah
/**
 * Represents a Person's payment in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPayment(String)}
 */
public class Payment {
    public static final String MESSAGE_PAYMENT_CONSTRAINTS =
            "Payment should only contain alphanumeric characters and . such as N.A. , "
                    + ", it should not be blank and should not have spaces."
                    + " The following words are accepted (ignoring case): "
                    + "\"PAID\", \"NOTPAID\", \"PENDING\", \"N.A.\""
                    + " Any words besides these will not be accepted and a blank field will be seen.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String PAYMENT_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum}.-]*";

    public final String paymentValue;

    /**
     * Constructs a {@code Payment}.
     *
     * @param payment A valid payment.
     */
    public Payment(String payment) {
        requireNonNull(payment);
        checkArgument(isValidPayment(payment), MESSAGE_PAYMENT_CONSTRAINTS);
        String paymentChecker = null;
        if (payment.equalsIgnoreCase("PAID")
                || payment.equalsIgnoreCase("NOTPAID")
                || payment.equalsIgnoreCase("N.A.")
                || payment.equalsIgnoreCase("PENDING")) {
            paymentChecker = payment;
        }

        paymentValue = paymentChecker;
    }

    /**
     * Returns true if a given string is a valid attendance.
     */
    public static boolean isValidPayment(String test) {
        return test.matches(PAYMENT_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return paymentValue;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Payment // instanceof handles nulls
                && paymentValue.equals(((Payment) other).paymentValue)); // state check
    }

    @Override
    public int hashCode() {
        return paymentValue.hashCode();
    }

}
