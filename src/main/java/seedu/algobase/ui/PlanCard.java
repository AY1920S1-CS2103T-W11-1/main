package seedu.algobase.ui;

import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.algobase.commons.core.LogsCenter;
import seedu.algobase.logic.parser.ParserUtil;
import seedu.algobase.model.Id;
import seedu.algobase.model.ModelType;
import seedu.algobase.model.gui.WriteOnlyTabManager;
import seedu.algobase.model.plan.Plan;

/**
 * An UI component that displays information of a {@code Plan}.
 */
public class PlanCard extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(PlanCard.class);


    private static final String FXML = "PlanListCard.fxml";

    public final Plan plan;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label planName;
    @FXML
    private Label planDescription;
    @FXML
    private Label startDate;
    @FXML
    private Label endDate;

    public PlanCard(Plan plan, int displayedIndex, WriteOnlyTabManager writeOnlyTabManager) {
        super(FXML);
        this.plan = plan;
        id.setText(displayedIndex + ". ");
        planName.setText(plan.getPlanName().fullName + " (" + plan.getTasks().size() + " tasks)");
        planDescription.setText(plan.getPlanDescription().value);
        startDate.setText(plan.getStartDate().format(ParserUtil.FORMATTER));
        endDate.setText(plan.getEndDate().format(ParserUtil.FORMATTER));
        addMouseClickListener(writeOnlyTabManager.addDetailsTabConsumer(ModelType.PLAN));
    }

    @Override
    public boolean equals(Object other) {
        // checks if same object
        if (other == this) {
            return true;
        }

        // checks if object of same class
        // handles null
        if (!(other instanceof PlanCard)) {
            return false;
        }

        // check fields equality
        PlanCard card = (PlanCard) other;
        return id.getText().equals(card.id.getText())
                && plan.equals(card.plan);
    }

    /**
     * Spawns a new Tab when the cardPane registers a double click event.
     *
     * @param addDetailsTabConsumer
     */
    public void addMouseClickListener(Consumer<Id> addDetailsTabConsumer) {
        cardPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        logger.fine("Double Clicked");
                        addDetailsTabConsumer.accept(plan.getId());
                    }
                }
            }
        });
    }
}
