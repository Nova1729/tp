package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCcaCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCcaCommand object.
 */
public class DeleteCcaCommandParser implements Parser<DeleteCcaCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCcaCommand
     * and returns a DeleteCcaCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCcaCommand parse(String args) throws ParseException {
        Index index;
        try {
            index = ParserUtil.parseIndex(args);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            pe.getMessage() + DeleteCcaCommand.MESSAGE_USAGE), pe);
        }
        return new DeleteCcaCommand(index);
    }

}
