package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddRoleToStudentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.cca.CcaName;
import seedu.address.model.role.Role;

/**
 * Parses input arguments and creates a new AddRoleToStudentCommand object
 */
public class AddRoleToStudentCommandParser implements Parser<AddRoleToStudentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddRoleToStudentCommand
     * and returns an AddRoleToStudentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddRoleToStudentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CCA_NAME, PREFIX_ROLE);

        if (!arePrefixesPresent(argMultimap, PREFIX_CCA_NAME, PREFIX_ROLE) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, AddRoleToStudentCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CCA_NAME, PREFIX_ROLE);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddRoleToStudentCommand.MESSAGE_USAGE), pe);
        }

        CcaName ccaName = ParserUtil.parseCcaName(argMultimap.getValue(PREFIX_CCA_NAME).get());
        Role role = ParserUtil.parseRole(argMultimap.getValue(PREFIX_ROLE).get());
        return new AddRoleToStudentCommand(index, ccaName, role);

    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
