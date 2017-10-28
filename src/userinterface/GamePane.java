package userinterface;

import java.util.ArrayList;
import java.util.Random;

import catchgame.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import resources.SeaCreature;

public class GamePane extends VBox
{
	private Player player;

	private StackPane primaryPane = new StackPane();
	private ActionVBox actionHBox = new ActionVBox();
	private MyStatsPane myStatsPane;

	public SimpleFishingPane simpleFishingPane;
	private MarketsPane marketsPane;

	public GamePane(EventHandler<ActionEvent> extractFishAction, EventHandler<ActionEvent> sellFishAction, Player player)
	{
		this.player = player;
		myStatsPane = new MyStatsPane();
		
		simpleFishingPane = new SimpleFishingPane(extractFishAction);
		marketsPane = new MarketsPane(sellFishAction);

		primaryPane.getChildren().add(myStatsPane);

		this.getChildren().addAll(primaryPane, actionHBox);

	}

	private class ActionVBox extends VBox
	{
		private Text txtTitle = new Text("Primary Actions - What would you like to do?");

		// buttons
		private Button btnGoFishing = new Button("Go Fishing");
		private Button btnGoToMarket = new Button("Go to Market");
		private Button btnCheckMyResources = new Button("View My Resouces");

		// containers
		private HBox buttonHBox = new HBox();

		public ActionVBox()
		{
			// set button actions
			btnGoFishing.setOnAction(e -> {
				primaryPane.getChildren().clear();
				primaryPane.getChildren().add(simpleFishingPane);
			});

			btnCheckMyResources.setOnAction(e -> {
				primaryPane.getChildren().clear();
				primaryPane.getChildren().add(myStatsPane);
			});

			btnGoToMarket.setOnAction(e -> {
				primaryPane.getChildren().clear();
				primaryPane.getChildren().add(marketsPane);
			});

			// put buttons in their container
			buttonHBox.getChildren().addAll(btnGoFishing, btnGoToMarket, btnCheckMyResources);

			// set spacing and alignment elements
			this.setSpacing(10);
			buttonHBox.setSpacing(10);
			this.setAlignment(Pos.CENTER);
			buttonHBox.setAlignment(Pos.CENTER);

			this.getChildren().addAll(txtTitle, buttonHBox);

		}

	}

	///////////////////////////////////////////////
	// Definitons for Panes that go in 'primaryPane'
	///////////////////////////////////////////////

	private class MyStatsPane extends StackPane
	{
		// Labels
		Label lblName = new Label("Name: ");
		Label lblCashOnHand = new Label("Available $: ");
		Label lblSkillLevel = new Label("Skill Level");

		// Text Fields
		Text txtName = new Text(player.getName());
		Text txtCashOnHand = new Text(Double.toString(player.getCashOnHand()));
		Text txtSkillLevel = new Text(Integer.toString(player.getSkillLevel()));

		GridPane statsGridPane = new GridPane();

		public MyStatsPane()
		{
			this.setWidth(200);
			this.setHeight(200);
			// node, col, row
			statsGridPane.add(lblName, 0, 0);
			statsGridPane.add(txtName, 1, 0);
			statsGridPane.add(lblCashOnHand, 0, 1);
			statsGridPane.add(txtCashOnHand, 1, 1);
			statsGridPane.add(lblSkillLevel, 0, 2);
			statsGridPane.add(txtSkillLevel, 1, 2);

			this.getChildren().addAll(statsGridPane);
		}
	}

	public class SimpleFishingPane extends VBox
	{
		// temp for now to show professor miller where we are going with this.
		Pane explainPane = new Pane();
		Pane fishPane = new Pane();

		private Random rand = new Random();
		private int numCreaturesOnScreen = 0;
		private Button btnExtractFishAction = new Button("Extract Fish");
		private ArrayList<SeaCreature> creaturesOnScreen = new ArrayList<>();
		private Label labelExplanation = new Label("Right now the fish are shown as dots. These dots are from SeaCreature objects, extracted " 
				+ "from an Ocean object belonging the server and sent to this client. We plan to animate them, and make it so that when they are "
				+ "clicked (or however else caught), they are added (By calling the function attatched to the button) to the players resouces array, "
				+ "which they can sell, to get better fishing equipment");

		public SimpleFishingPane(EventHandler<ActionEvent> extractFishAction)
		{
			//this.setMinWidth(300);
			//this.setMinHeight(400);

			labelExplanation.setWrapText(true);
			
			btnExtractFishAction.setOnAction(extractFishAction);
			btnExtractFishAction.setAlignment(Pos.CENTER);
			this.getChildren().addAll(btnExtractFishAction, labelExplanation);
			
		}

		public void addCreature(SeaCreature creature)
		{
			numCreaturesOnScreen++;
			creaturesOnScreen.add(creature);

//			creaturesOnScreen.get(numCreaturesOnScreen - 1).getBody().setCenterX(getRandomDouble(0, getMinWidth()));
//			creaturesOnScreen.get(numCreaturesOnScreen - 1).getBody().setCenterY(getRandomDouble(100, getMinHeight()));
			
			this.getChildren().add(creaturesOnScreen.get(numCreaturesOnScreen - 1).getBody());
			
			creaturesOnScreen.get(numCreaturesOnScreen - 1).getBody().setCenterX(27);
			creaturesOnScreen.get(numCreaturesOnScreen - 1).getBody().setCenterY(50);
		}

		private double getRandomDouble(double max, double min)
		{
			double randomDouble = (max - min) * rand.nextDouble() + min;
			return randomDouble;
		}
	}

	private class BoatFishingPane extends Pane
	{
		private Button btnExtractFishAction = new Button("Extract Fish");

		public BoatFishingPane(EventHandler<ActionEvent> extractFishAction)
		{
			btnExtractFishAction.setOnAction(extractFishAction);
			this.getChildren().addAll(btnExtractFishAction);
		}
	}

	private class LobsterTrappingPane extends Pane
	{

	}

	private class MarketsPane extends StackPane
	{
		private Button btnSellFishAction = new Button("Sell Fish");

		public MarketsPane(EventHandler<ActionEvent> sellFishAction)
		{
			this.setWidth(200);
			this.setHeight(200);

			btnSellFishAction.setOnAction(sellFishAction);
			this.getChildren().addAll(btnSellFishAction);
		}
	}

}
