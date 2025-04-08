package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CAPTAIN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_VICE_PRESIDENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CCA_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddRoleToStudentCommand;
import seedu.address.model.cca.CcaName;
import seedu.address.model.role.Role;
import seedu.address.testutil.TypicalCcas;


public class AddRoleToStudentCommandParserTest {

    private static final String CCA_NAME_DESC_BASKETBALL = " " + PREFIX_CCA_NAME
            + TypicalCcas.CCA_NAME_BASKETBALL.fullCcaName;
    private static final String CCA_NAME_DESC_TENNIS = " " + PREFIX_CCA_NAME
            + TypicalCcas.CCA_NAME_TENNIS.fullCcaName;

    private static final String ROLE_DESC_CAPTAIN = " " + PREFIX_ROLE + VALID_ROLE_CAPTAIN;
    private static final String ROLE_DESC_VICE_PRESIDENT = " " + PREFIX_ROLE + VALID_ROLE_VICE_PRESIDENT;

    private static final String INVALID_CCA_NAME_DESC = " " + PREFIX_CCA_NAME + "Basket*ball";
    private static final String INVALID_ROLE_DESC = " " + PREFIX_ROLE + "Captain*";

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddRoleToStudentCommand.MESSAGE_USAGE);

    private AddRoleToStudentCommandParser parser = new AddRoleToStudentCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, CCA_NAME_DESC_BASKETBALL, MESSAGE_INVALID_FORMAT);

        // no cca prefix specified
        assertParseFailure(parser, "1 " + PREFIX_ROLE, MESSAGE_INVALID_FORMAT);

        // no cca name specified (PREFIX_CCA_NAME followed by nothing)
        assertParseFailure(parser, "1 " + PREFIX_ROLE + Role.DEFAULT_ROLE_NAME + "  " + PREFIX_CCA_NAME,
                CcaName.MESSAGE_CONSTRAINTS);

        // no role prefix specified
        assertParseFailure(parser, "1 " + PREFIX_CCA_NAME + "Basketball", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + CCA_NAME_DESC_BASKETBALL + ROLE_DESC_CAPTAIN;
        CcaName expectedCcaName = TypicalCcas.CCA_NAME_BASKETBALL;
        Role roleToAdd = new Role(VALID_ROLE_CAPTAIN);

        AddRoleToStudentCommand expectedCommand = new AddRoleToStudentCommand(targetIndex, expectedCcaName, roleToAdd);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;

        String userInput = "  " + targetIndex.getOneBased() + "   "
                + CCA_NAME_DESC_BASKETBALL + "   " + ROLE_DESC_CAPTAIN;
        CcaName expectedCcaName = TypicalCcas.CCA_NAME_BASKETBALL;
        Role roleToAdd = new Role(VALID_ROLE_CAPTAIN);

        AddRoleToStudentCommand expectedCommand =
                new AddRoleToStudentCommand(targetIndex, expectedCcaName, roleToAdd);

        assertParseSuccess(parser, userInput, expectedCommand);
    }


    @Test
    public void parse_duplicateFields_failure() {
        // Duplicate CCA prefixes
        String userInput = INDEX_FIRST_PERSON.getOneBased() + CCA_NAME_DESC_BASKETBALL + CCA_NAME_DESC_TENNIS
                + ROLE_DESC_CAPTAIN;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CCA_NAME));

        // Duplicate role prefixes
        userInput = INDEX_FIRST_PERSON.getOneBased() + CCA_NAME_DESC_BASKETBALL + ROLE_DESC_VICE_PRESIDENT
                + ROLE_DESC_CAPTAIN;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        // missing cca prefix
        assertParseFailure(parser,
                String.valueOf(INDEX_FIRST_PERSON.getOneBased()) + ROLE_DESC_CAPTAIN, MESSAGE_INVALID_FORMAT);

        // missing role prefix
        assertParseFailure(parser,
                String.valueOf(INDEX_FIRST_PERSON.getOneBased()) + CCA_NAME_DESC_BASKETBALL, MESSAGE_INVALID_FORMAT);

        // missing index
        assertParseFailure(parser, CCA_NAME_DESC_BASKETBALL + ROLE_DESC_CAPTAIN, MESSAGE_INVALID_FORMAT);
    }

}
