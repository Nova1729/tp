package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CCA;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteCcaCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCcaCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCcaCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCcaCommandParserTest {

    private DeleteCcaCommandParser parser = new DeleteCcaCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCcaCommand(INDEX_FIRST_CCA));
    }
}
