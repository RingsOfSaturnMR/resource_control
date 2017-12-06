package animation_work_and_driver;

import catchgame.Constants;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class SimpleFishingPane extends Pane
{
	// temp for now to show professor miller where we are going with this.
	// Pane explainPane = new Pane();
	// Pane fishPane = new Pane();

	// private int numCreaturesOnScreen = 0;
	// private Button btnExtractFishAction = new Button("Extract Fish");
	// private ArrayList<SeaCreature> creaturesOnScreen = new ArrayList<>();
	private Label labelExplanation = new Label(
			"Right now the fish are shown as dots. These dots are from SeaCreature objects, extracted " + "from an Ocean object belonging the server and sent to this client. We plan to animate them, and make it so that when they are " +
					"clicked (or however else caught), they are added (By calling the function attatched to the button) to the players resouces array, " +
					"which they can sell, to get better fishing equipment");

	public SimpleFishingPane()
	{

		this.setMinWidth(Constants.INITIAL_SIMPLE_FISHING_PANE_WIDTH);
		this.setMinHeight(Constants.INITIAL_SIMPLE_FISHING_PANE_HEIGHT);
		labelExplanation.setMaxWidth(Constants.INITIAL_SIMPLE_FISHING_PANE_WIDTH);
		labelExplanation.setWrapText(true);
		// labelExplanation.setTranslateY(50);
		// btnExtractFishAction.setOnAction(extractFishAction);
		this.getChildren().addAll(labelExplanation);

	}
}