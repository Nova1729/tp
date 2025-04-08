package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TOTAL_SESSIONS;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCcaCommand;
import seedu.address.logic.commands.EditCcaCommand.EditCcaDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.role.Role;

/**
 * Parses input arguments and creates a new EditCcaCommand object.
 */
public class EditCcaCommandParser implements Parser<EditCcaCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCcaCommand
     * and returns an EditCcaCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public EditCcaCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CCA_NAME, PREFIX_ROLE, PREFIX_TOTAL_SESSIONS);

        if (argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCcaCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CCA_NAME, PREFIX_TOTAL_SESSIONS);
        EditCcaDescriptor editCcaDescriptor = new EditCcaDescriptor();

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCcaCommand.MESSAGE_USAGE), pe);
        }

        if (argMultimap.getValue(PREFIX_CCA_NAME).isPresent()) {
            editCcaDescriptor.setCcaName(ParserUtil.parseCcaName(argMultimap.getValue(PREFIX_CCA_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_TOTAL_SESSIONS).isPresent()) {
            editCcaDescriptor.setTotalSessions(
                    ParserUtil.parseTotalSessions(argMultimap.getValue(PREFIX_TOTAL_SESSIONS).get()));
        }
        parseRolesForEdit(argMultimap.getAllValues(PREFIX_ROLE)).ifPresent(editCcaDescriptor::setRoles);

        if (!editCcaDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCcaCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCcaCommand(index, editCcaDescriptor);
    }

    /**
     * Parses {@code Collection<String> roles} into a {@code Set<Role>} if {@code roles} is non-empty.
     * If {@code roles} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Role>} containing zero roles.
     */
    private Optional<Set<Role>> parseRolesForEdit(Collection<String> roles) throws ParseException {
        assert roles != null;

        if (roles.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> roleSet = roles.size() == 1 && roles.contains("") ? Collections.emptySet() : roles;
        return Optional.of(ParserUtil.parseRoles(roleSet));
    }
}
