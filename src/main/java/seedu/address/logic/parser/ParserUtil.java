package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.cca.Amount;
import seedu.address.model.cca.Cca;
import seedu.address.model.cca.CcaName;
import seedu.address.model.cca.SessionCount;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.role.Role;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX =
            "Index is not a non-zero unsigned integer and must not exceed 999.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param oneBasedIndex The one-based index as a string.
     * @return The corresponding {@code Index} object.
     * @throws ParseException if the specified index is invalid (not a non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param name The string to be parsed as a name.
     * @return A {@code Name} object.
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param phone The string to be parsed as a phone number.
     * @return A {@code Phone} object.
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param address The string to be parsed as an address.
     * @return An {@code Address} object.
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param email The string to be parsed as an email.
     * @return An {@code Email} object.
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String role} into a {@code Role}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param role The string to be parsed as a role.
     * @return A {@code Role} object.
     * @throws ParseException if the given {@code role} is invalid.
     */
    public static Role parseRole(String role) throws ParseException {
        requireNonNull(role);
        String trimmedRole = role.trim();
        if (!Role.isValidRoleName(trimmedRole)) {
            throw new ParseException(Role.MESSAGE_CONSTRAINTS);
        }
        return new Role(trimmedRole);
    }

    /**
     * Parses a {@code Collection<String>} into a {@code Set<Role>}.
     *
     * @param roles The collection of role names as strings.
     * @return A set of {@code Role} objects.
     * @throws ParseException if any role is invalid.
     */
    public static Set<Role> parseRoles(Collection<String> roles) throws ParseException {
        requireNonNull(roles);
        final Set<Role> roleSet = new HashSet<>();
        for (String roleName : roles) {
            if (roleName.equals(Role.DEFAULT_ROLE_NAME)) {
                roleSet.add(Role.DEFAULT_ROLE);
            } else {
                roleSet.add(parseRole(roleName));
            }
        }

        roleSet.add(Role.DEFAULT_ROLE);

        return roleSet;
    }

    /**
     * Parses a {@code String amount} into an {@code Amount}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param amount The string to be parsed as an integer.
     * @return An {@code Amount} object.
     * @throws ParseException if the given {@code amount} is invalid.
     */
    public static Amount parseAmount(String amount) throws ParseException {
        requireNonNull(amount);
        String trimmedAmount = amount.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedAmount)) {
            throw new ParseException(Amount.MESSAGE_CONSTRAINTS);
        }
        return new Amount(Integer.parseInt(trimmedAmount));
    }

    /**
     * Parses a {@code String ccaName} into a {@code CcaName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param ccaName
     * @return A {@code CcaName} object.
     * @throws ParseException if the given {@code ccaName} is invalid.
     */
    public static CcaName parseCcaName(String ccaName) throws ParseException {
        requireNonNull(ccaName);
        String trimmedCcaName = ccaName.trim();

        if (!CcaName.isValidCcaName(trimmedCcaName)) {
            throw new ParseException(CcaName.MESSAGE_CONSTRAINTS);
        }
        return new CcaName(trimmedCcaName);
    }

    /**
     * Parses a {@code String totalSessions} into a {@code SessionCount}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param totalSessions The string to be parsed as a session count.
     * @return A {@code SessionCount} object.
     * @throws ParseException if the given {@code totalSessions} is invalid.
     */
    public static SessionCount parseTotalSessions(String totalSessions) throws ParseException {
        requireNonNull(totalSessions);
        String trimmedTotalSessions = totalSessions.trim();
        if (!StringUtil.isNonNegativeUnsignedInteger(trimmedTotalSessions)) {
            throw new ParseException(SessionCount.MESSAGE_CONSTRAINTS);
        }
        return new SessionCount(Integer.parseInt(trimmedTotalSessions));
    }

    /**
     * Parses a {@code CcaName} into a {@code Cca}.
     *
     * @param ccaName The {@code CcaName} object.
     * @return A {@code Cca} object.
     * @throws ParseException if the given {@code ccaName} is null.
     */
    public static Cca parseCca(String ccaName) throws ParseException {
        return new Cca(parseCcaName(ccaName));
    }

    /**
     * Parses a {@code Collection<String>} into a {@code Set<Cca>}.
     *
     * @param ccaNames The collection of CCA names as strings.
     * @return A set of {@code Cca} objects.
     * @throws ParseException if any CCA name is invalid.
     */
    public static Set<Cca> parseCcas(Collection<String> ccaNames) throws ParseException {
        requireNonNull(ccaNames);
        final Set<Cca> ccaSet = new HashSet<>();
        for (String ccaName : ccaNames) {
            ccaSet.add(parseCca(ccaName));
        }
        return ccaSet;
    }

}
