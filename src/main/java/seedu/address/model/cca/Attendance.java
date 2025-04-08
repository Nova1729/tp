package seedu.address.model.cca;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a person's attendance in a specific CCA.
 * Guarantees: immutable; is valid as declared in {@link #isValidAttendance(SessionCount, SessionCount)}
 */
public class Attendance {

    public static final String MESSAGE_CONSTRAINTS =
            "Attended sessions cannot be higher than the total sessions of the cca!";

    private final SessionCount sessionsAttended;
    private final SessionCount totalSessions;

    /**
     * Constructs an Attendance object.
     *
     * @param sessionsAttended Number of attended sessions (must be non-negative).
     * @param tota  lSessions The total number of sessions in the CCA.
     */
    public Attendance(SessionCount sessionsAttended, SessionCount totalSessions) {
        requireNonNull(sessionsAttended);
        requireNonNull(totalSessions);
        checkArgument(isValidAttendance(sessionsAttended, totalSessions), MESSAGE_CONSTRAINTS);
        this.sessionsAttended = sessionsAttended;
        this.totalSessions = totalSessions;
    }

    /**
     * Returns true if the given attendance is valid where the sessions attended are less than the
     * total number of Cca sessions.
     */
    public boolean isValidAttendance(SessionCount sessionsAttended, SessionCount totalSessions) {
        return sessionsAttended.getSessionCount() <= totalSessions.getSessionCount();
    }

    /**
     * Returns the SessionCount class of sessions attended.
     */
    public SessionCount getSessionsAttended() {
        return sessionsAttended;
    }

    /**
     * Returns the SessionCount class of total sessions.
     */
    public SessionCount getTotalSessions() {
        return totalSessions;
    }

    /**
     * Returns true if the amount of sessions attended is less than or equal to the total number of sessions.
     *
     * @param amount The amount of sessions to be added.
     * @return true if the amount of sessions attended is valid, false otherwise.
     */
    public boolean canAttend(Amount amount) {
        return isValidAttendance(sessionsAttended.addAmount(amount), totalSessions);
    }

    /**
     * Returns a new Attendance object with the amount added.
     */
    public Attendance attend(Amount amount) {
        return new Attendance(sessionsAttended.addAmount(amount), totalSessions);
    }

    /**
     * Return a new Attendance object with updated total sessions.
     * If the current sessions attended more than the new total sessions, it lowers the session attended
     * to match the updated total sessions.
     *
     * @param updatedTotalSessions The total sessions to update.
     * @return a new Attendance object with the updated total sessions.
     */
    public Attendance updateTotalSessions(SessionCount updatedTotalSessions) {
        if (sessionsAttended.getSessionCount() > updatedTotalSessions.getSessionCount()) {
            return new Attendance(updatedTotalSessions, updatedTotalSessions);
        }
        return new Attendance(sessionsAttended, updatedTotalSessions);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Attendance)) {
            return false;
        }
        Attendance otherAttendance = (Attendance) other;
        return sessionsAttended.equals(otherAttendance.sessionsAttended)
                && totalSessions.equals(otherAttendance.totalSessions);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(sessionsAttended.getSessionCount()) + totalSessions.hashCode();
    }

    @Override
    public String toString() {
        return "Attendance: " + sessionsAttended.getSessionCount() + "/" + totalSessions.getSessionCount();
    }
}
