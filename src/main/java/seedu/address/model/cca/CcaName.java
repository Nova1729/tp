package seedu.address.model.cca;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a CCA's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidCcaName(String)}
 */
public class CcaName {

    public static final String MESSAGE_CONSTRAINTS =
        "Cca names must consist of alphanumeric characters and can only include spaces or hyphens between words."
        + " For example, R0ck-n-R0ll is a valid CCA name, but -Basketball- is not.";
    public static final String VALIDATION_REGEX = "^[A-Za-z0-9]+(?:[ -][A-Za-z0-9]+)*$";

    public final String fullCcaName;

    /**
     * Constructs a {@code CcaName}.
     *
     * @param ccaName A valid cca name.
     */
    public CcaName(String ccaName) {
        requireNonNull(ccaName);
        checkArgument(isValidCcaName(ccaName), MESSAGE_CONSTRAINTS);
        fullCcaName = ccaName;
    }

    /**
     * Returns true if a given string is a valid cca name.
     */
    public static boolean isValidCcaName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullCcaName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CcaName)) {
            return false;
        }

        CcaName otherName = (CcaName) other;
        return fullCcaName.equals(otherName.fullCcaName);
    }

    @Override
    public int hashCode() {
        return fullCcaName.hashCode();
    }

}
