package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA_NAME;

import java.util.stream.Stream;

import seedu.address.logic.commands.CreateCcaCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.cca.Cca;
import seedu.address.model.cca.CcaName;

/**
 * Parses input arguments and creates a new CreateCcaCommand object.
 */
public class CreateCcaCommandParser implements Parser<CreateCcaCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CreateCcaCommand
     * and returns a CreateCcaCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CreateCcaCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CCA_NAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_CCA_NAME) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateCcaCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CCA_NAME);
        CcaName ccaName = ParserUtil.parseCcaName(argMultimap.getValue(PREFIX_CCA_NAME).get());
        Cca cca = new Cca(ccaName);
        return new CreateCcaCommand(cca);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
