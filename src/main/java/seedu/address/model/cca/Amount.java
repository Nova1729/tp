package seedu.address.model.cca;

import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the amount of sessions to be added to a CCA.
 * Guarantees: immutable; is valid as declared in {@link #isValidAmount(int)}
 */
public class Amount {

    public static final String MESSAGE_CONSTRAINTS =
            "Amount should be a positive integer.";

    private final int amount;

    /**
     * Constructs a {@code Amount}.
     *
     * @param amount A valid amount.
     */
    public Amount(int amount) {
        checkArgument(isValidAmount(amount), MESSAGE_CONSTRAINTS);
        this.amount = amount;
    }

    /**
     * Returns true if a given amount is a valid amount.
     */
    public static boolean isValidAmount(int test) {
        return test > 0;
    }

    /**
     * Returns the amount as an integer.
     */
    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return Integer.toString(amount);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Amount)) {
            return false;
        }

        Amount otherAmount = (Amount) other;
        return amount == otherAmount.amount;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(amount);
    }

}
