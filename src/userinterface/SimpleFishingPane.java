package userinterface;



import catchgame.Constants;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

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
		
		Rectangle clippingRectangle=new Rectangle(Constants.INITIAL_SIMPLE_FISHING_PANE_WIDTH,
				Constants.INITIAL_SIMPLE_FISHING_PANE_HEIGHT);
		ImageView oceanImageView=new ImageView();
		Image oceanImage=new Image("img/ocean.png");
		oceanImageView.setImage(oceanImage);
		oceanImageView.setFitWidth(Constants.INITIAL_SIMPLE_FISHING_PANE_WIDTH);
		oceanImageView.setFitHeight(Constants.INITIAL_SIMPLE_FISHING_PANE_HEIGHT*Constants.TOP_COEFFICIENT);
		Rectangle rocksRectangle=new Rectangle(Constants.INITIAL_SIMPLE_FISHING_PANE_WIDTH,
				Constants.INITIAL_SIMPLE_FISHING_PANE_HEIGHT*Constants.BOTTOM_COEFFICIENT);
		rocksRectangle.setTranslateY(Constants.INITIAL_SIMPLE_FISHING_PANE_HEIGHT*Constants.TOP_COEFFICIENT);
		rocksRectangle.setFill(Color.LIGHTGREY);
		this.getChildren().addAll(oceanImageView, rocksRectangle);
		this.setClip(clippingRectangle);
	}
}