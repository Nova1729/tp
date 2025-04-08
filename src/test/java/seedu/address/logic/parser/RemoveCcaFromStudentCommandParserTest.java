package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.RemoveCcaFromStudentCommand;
import seedu.address.model.cca.CcaName;
import seedu.address.testutil.TypicalCcas;

public class RemoveCcaFromStudentCommandParserTest {

    private static final String CCA_NAME_DESC_BASKETBALL = " " + PREFIX_CCA_NAME
            + TypicalCcas.CCA_NAME_BASKETBALL.fullCcaName;
    private static final String CCA_NAME_DESC_TENNIS = " " + PREFIX_CCA_NAME
            + TypicalCcas.CCA_NAME_TENNIS.fullCcaName;

    private static final String INVALID_CCA_NAME_DESC = " " + PREFIX_CCA_NAME + "Basket*ball";

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemoveCcaFromStudentCommand.MESSAGE_USAGE);

    private RemoveCcaFromStudentCommandParser parser = new RemoveCcaFromStudentCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, CCA_NAME_DESC_BASKETBALL, MESSAGE_INVALID_FORMAT);

        // no cca prefix specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no cca name specified
        assertParseFailure(parser, "1 " + PREFIX_CCA_NAME, CcaName.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 " + PREFIX_CCA_NAME + " ", CcaName.MESSAGE_CONSTRAINTS);


        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + CCA_NAME_DESC_BASKETBALL;
        CcaName expectedCcaName = TypicalCcas.CCA_NAME_BASKETBALL;

        RemoveCcaFromStudentCommand expectedCommand = new RemoveCcaFromStudentCommand(targetIndex, expectedCcaName);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_fieldsWithWhitespace_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = "  " + targetIndex.getOneBased() + "   " + CCA_NAME_DESC_BASKETBALL + "   ";
        CcaName expectedCcaName = TypicalCcas.CCA_NAME_BASKETBALL;

        RemoveCcaFromStudentCommand expectedCommand = new RemoveCcaFromStudentCommand(targetIndex, expectedCcaName);

        assertParseSuccess(parser, userInput, expectedCommand);
    }


    @Test
    public void parse_duplicateFields_failure() {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + CCA_NAME_DESC_BASKETBALL + CCA_NAME_DESC_TENNIS;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CCA_NAME));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        // missing cca prefix
        assertParseFailure(parser, String.valueOf(INDEX_FIRST_PERSON.getOneBased()), MESSAGE_INVALID_FORMAT);

        // missing index
        assertParseFailure(parser, CCA_NAME_DESC_BASKETBALL, MESSAGE_INVALID_FORMAT);
    }

}
