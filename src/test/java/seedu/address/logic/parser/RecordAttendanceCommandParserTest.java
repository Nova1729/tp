package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.AMOUNT_DESC_FOUR;
import static seedu.address.logic.commands.CommandTestUtil.AMOUNT_DESC_ONE;
import static seedu.address.logic.commands.CommandTestUtil.CCA_NAME_DESC_BASKETBALL;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_AMOUNT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CCA_NAME_BASKETBALL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.RecordAttendanceCommand;
import seedu.address.model.cca.Amount;
import seedu.address.model.cca.CcaName;


public class RecordAttendanceCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, RecordAttendanceCommand.MESSAGE_USAGE);

    private RecordAttendanceCommandParser parser = new RecordAttendanceCommandParser();

    @Test
    public void parse_validArgs_returnsRecordAttendanceCommand() {
        // no leading and trailing whitespaces
        RecordAttendanceCommand expectedRecordAttendanceCommand =
                new RecordAttendanceCommand(INDEX_FIRST_PERSON, new CcaName(VALID_CCA_NAME_BASKETBALL), new Amount(1));
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + CCA_NAME_DESC_BASKETBALL + AMOUNT_DESC_ONE,
                        expectedRecordAttendanceCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + CCA_NAME_DESC_BASKETBALL + " " + AMOUNT_DESC_ONE,
                        expectedRecordAttendanceCommand);

        // higher amount
        RecordAttendanceCommand expectedRecordAttendanceCommandHigherAmount =
                new RecordAttendanceCommand(INDEX_FIRST_PERSON, new CcaName(VALID_CCA_NAME_BASKETBALL), new Amount(4));
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + CCA_NAME_DESC_BASKETBALL + AMOUNT_DESC_FOUR,
                        expectedRecordAttendanceCommandHigherAmount);

    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // non integer amount
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + CCA_NAME_DESC_BASKETBALL + INVALID_AMOUNT_DESC,
                Amount.MESSAGE_CONSTRAINTS);

        // missing amount prefix
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + CCA_NAME_DESC_BASKETBALL + "1",
                MESSAGE_INVALID_FORMAT);

        // missing cca name prefix
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + " Basketball" + AMOUNT_DESC_ONE,
                MESSAGE_INVALID_FORMAT);
    }

}
