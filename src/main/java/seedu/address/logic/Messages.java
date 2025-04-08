package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.cca.Amount;
import seedu.address.model.cca.Cca;
import seedu.address.model.cca.CcaInformation;
import seedu.address.model.cca.CcaName;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.role.Role;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The student index provided is invalid";
    public static final String MESSAGE_INVALID_CCA_DISPLAYED_INDEX = "The CCA index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d students listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_DUPLICATE_PERSON =
                "A student with the same name, phone number, or email already exists in the student list";
    public static final String MESSAGE_DUPLICATE_CCA = "A CCA with the same name already exists in the CCA list";
    public static final String MESSAGE_CCA_NOT_FOUND = "The CCA does not exist in the CCA list";
    public static final String MESSAGE_ROLE_NOT_FOUND = "The role does not exist in the CCA";
    public static final String MESSAGE_CCA_NOT_IN_PERSON = "The student is not in the CCA";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; CcaInformation: ");
        for (CcaInformation ccaInformation : person.getCcaInformations()) {
            builder.append(ccaInformation)
                    .append(", ");
        }
        builder.delete(builder.length() - 2, builder.length());
        return builder.toString();
    }

    /**
     * Formats the {@code cca} for display to the user.
     */
    public static String format(Cca cca) {
        final StringBuilder builder = new StringBuilder();
        builder.append(cca.getCcaName())
                .append("; Roles: ")
                .append(cca.getRoles())
                .append("; Total sessions: ")
                .append(cca.getTotalSessions());
        return builder.toString();
    }

    /**
     * Formats the {@code ccaName} for display to the user.
     */
    public static String format(CcaName ccaName) {
        return ccaName.fullCcaName;
    }

    /**
     * Formats the {@code amount} for display to the user.
     */
    public static String format(Amount amount) {
        return amount.toString();
    }

    /**
     * Formats the {@code name} for display to the user.
     */
    public static String format(Name name) {
        return name.fullName;
    }

    public static String format(Role role) {
        return role.roleName;
    }
}
