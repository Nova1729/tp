package seedu.address.ui;

import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.cca.Cca;

/**
 * An UI component that displays information of a {@code Cca}.
 */
public class CcaCard extends UiPart<Region> {

    private static final String FXML = "CcaListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Cca cca;

    @FXML
    private HBox ccaCardPane;
    @FXML
    private Label id;
    @FXML
    private Label ccaName;
    @FXML
    private Label roles;
    @FXML
    private Label sessionCount;

    /**
     * Creates a {@code CcaCard} with the given {@code Cca} and index to display.
     */
    public CcaCard(Cca cca, int displayedIndex) {
        super(FXML);
        this.cca = cca;
        ccaCardPane.getStyleClass().add("cca-box");
        id.setText(displayedIndex + ". ");
        ccaName.setText(cca.getCcaName().fullCcaName);
        if (cca.getRoles().isEmpty()) {
            roles.setText("Roles: None");
        } else {
            String rolesString = cca.getRoles().stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            roles.setText("Roles: " + rolesString);
        }
        sessionCount.setText("Total sessions: "
                + cca.getTotalSessions().getSessionCount());
    }
}
