package seedu.address.model.cca;

import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the number of sessions for a CCA in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidSessions(int)}
 */
public class SessionCount {

    public static final String MESSAGE_CONSTRAINTS =
            "The number of sessions must be a non-negative integer and must not exceed 999.";

    private final int sessionCount;

    /**
     * Constructs a {@Code Sessions}
     *
     * @param sessionCount A valid number of sessions
     */
    public SessionCount(int sessionCount) {
        checkArgument(isValidSessions(sessionCount), MESSAGE_CONSTRAINTS);
        this.sessionCount = sessionCount;
    }

    /**
     * Returns true if the given integer is a valid session count.
     */
    public static boolean isValidSessions(int test) {
        return (test >= 0) && (test <= 999);
    }

    /**
     * Returns the session count as an integer.
     */
    public int getSessionCount() {
        return sessionCount;
    }

    /**
     * Returns a new SessionCount object with the amount added.
     */
    public SessionCount addAmount(Amount amount) {
        return new SessionCount(sessionCount + amount.getAmount());
    }

    @Override
    public String toString() {
        return Integer.toString(sessionCount);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SessionCount)) {
            return false;
        }

        SessionCount otherSessions = (SessionCount) other;
        return sessionCount == otherSessions.sessionCount;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(sessionCount);
    }
}
