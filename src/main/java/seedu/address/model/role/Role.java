package seedu.address.model.role;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Role a person holds in a cca in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidRoleName(String)}
 */
public class Role {

    public static final String MESSAGE_CONSTRAINTS = "Role names should be alphanumeric, allowing the use of hyphens, "
            + "and must be at most 50 characters long (including spaces and hyphens)";
    public static final String VALIDATION_REGEX = "^(?=.{1,50}$)[A-Za-z0-9]+(?:[ -][A-Za-z0-9]+)*$";
    public static final String DEFAULT_ROLE_NAME = "Member";

    public static final Role DEFAULT_ROLE = new Role(DEFAULT_ROLE_NAME);

    public final String roleName;

    /**
     * Constructs a {@code Role}.
     *
     * @param roleName A valid role name.
     */
    public Role(String roleName) {
        requireNonNull(roleName);
        checkArgument(isValidRoleName(roleName), MESSAGE_CONSTRAINTS);
        this.roleName = roleName;
    }

    /**
     * Returns true if a given string is a valid role name.
     */
    public static boolean isValidRoleName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if this object is a default role.
     */
    public boolean isDefaultRole() {
        return this.equals(Role.DEFAULT_ROLE);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Role)) {
            return false;
        }

        Role otherRole = (Role) other;
        return roleName.equals(otherRole.roleName);
    }

    @Override
    public int hashCode() {
        return roleName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + roleName + ']';
    }

}
