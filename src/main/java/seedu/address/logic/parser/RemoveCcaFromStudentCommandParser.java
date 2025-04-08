package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA_NAME;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemoveCcaFromStudentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.cca.CcaName;

/**
 * Parses input arguments and creates a new RemoveCcaFromStudentCommand object.
 */
public class RemoveCcaFromStudentCommandParser implements Parser<RemoveCcaFromStudentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RemoveCcaFromStudentCommand
     * and returns an RemoveCcaFromStudentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemoveCcaFromStudentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CCA_NAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_CCA_NAME)
                || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, RemoveCcaFromStudentCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CCA_NAME);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveCcaFromStudentCommand.MESSAGE_USAGE), pe);
        }

        CcaName ccaName = ParserUtil.parseCcaName(argMultimap.getValue(PREFIX_CCA_NAME).get());
        return new RemoveCcaFromStudentCommand(index, ccaName);

    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
