package userinterface;

/*
Class by Dr. Java and the JavaDocs
Nils Johnson, Caileigh Fitzgerald, Thanh Lam, and Matt Roberts
Date: 11-27-2017
*/

/*
Purpose: to let user navigate between MyStatsPane, MarketsPane,
and SimpleFishingPane
SimpleFishingPane's purpose is to launch a FishingActivity when
it is loaded if one has not already been launched
MyStatsPane just gets stats from player object
MarketsPane is in progress

Modification info:
SimpleFishingActivity launches a FishingActivity,
and only if one has not already been launched
MarketsPane is in progress
*/


import java.util.Random;
import catchgame.GameControl;
import catchgame.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import market.SeafoodMarket;
import catchgame.GameControl.FishingActivityActions;

/**
 * Primary container for all gameplay GUI components.
 */
public class GamePane extends VBox
{
	// has player from GameControl to show the player's attributes
	private Player player;

	// pane to hold the main nodes for what the player is doing
	private Pane primaryPane = new Pane();
	
	// holds nodes which fire actions to change what nodes go in primaryPane
	private ActionVBox actionHBox = new ActionVBox();

	// pane to hold readout of players attributes.
	private MyStatsPane myStatsPane;

	public SimpleFishingPane simpleFishingPane;
<<<<<<< HEAD
	public SeafoodMarketPane marketsPane;
=======
	public SeafoodMarketPane seafoodMarketPane;
	public EquipmentMarketPane equipmentMarketPane;
>>>>>>> nils_branch
	private GameControl.FishingActivityActions fishingActivityActions;
	private boolean fishingStarted = false;

	// for dropdown menu
	public MenuBar menuBar = new MenuBar();
	private Menu fileMenu = new Menu("File");
	private MenuItem accountDeleteMenuItem = new MenuItem("Delete Account");
	private MenuItem saveMenuItem = new MenuItem("Save");
	private MenuItem exitMenuItem = new MenuItem("Exit");

	public GamePane(EventHandler<ActionEvent> sellFishAction, Player player, FishingActivityActions fishingActivityActions, EventHandler<ActionEvent> deleteAccountAction, EventHandler<ActionEvent> saveAction,
<<<<<<< HEAD
			EventHandler<ActionEvent> exitAction, SeafoodMarket seafoodMarket)
=======
			EventHandler<ActionEvent> exitAction, String seaFoodMarketName)
>>>>>>> nils_branch
	{
		this.player = player;
		myStatsPane = new MyStatsPane();
		this.fishingActivityActions = fishingActivityActions;

		simpleFishingPane = new SimpleFishingPane();
<<<<<<< HEAD
		marketsPane = new SeafoodMarketPane(seafoodMarket.getName(), sellFishAction);

=======
		seafoodMarketPane = new SeafoodMarketPane(seaFoodMarketName, sellFishAction);
		equipmentMarketPane = new EquipmentMarketPane("Ye 'Ol Fishing Store");
		
>>>>>>> nils_branch
		// set up menu
		fileMenu.getItems().addAll(accountDeleteMenuItem, saveMenuItem, exitMenuItem);
		menuBar.getMenus().add(fileMenu);

		primaryPane.getChildren().add(myStatsPane);
<<<<<<< HEAD

		this.getChildren().addAll(menuBar, primaryPane, actionHBox);

		// set actions
		accountDeleteMenuItem.setOnAction(deleteAccountAction);
		saveMenuItem.setOnAction(saveAction);
		exitMenuItem.setOnAction(exitAction);
		
		// make seafoodMarketPane responsive
		marketsPane.transactionContainer.prefWidthProperty().bind(this.widthProperty());
		marketsPane.priceBox.prefWidthProperty().bind(marketsPane.transactionContainer.widthProperty().divide(2));
		marketsPane.usersResourcesBox.prefWidthProperty().bind(marketsPane.transactionContainer.widthProperty().divide(2));

	}

	// where user selects what they want to do
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
			btnGoFishing.setOnAction(e ->
			{
				primaryPane.getChildren().clear();
				primaryPane.getChildren().add(simpleFishingPane);
				if (fishingStarted)
				{
					// do nothing
				}
				else
				{
					fishingActivityActions.startFishingActivity();
					fishingStarted = true;
				}
			});

			btnCheckMyResources.setOnAction(e ->
			{
				primaryPane.getChildren().clear();
				// TODO use observables or something, so that it automatically changes.
				// this forces a refresh, dont leave this way...
				myStatsPane = new MyStatsPane();
				primaryPane.getChildren().add(myStatsPane);
			});

			btnGoToMarket.setOnAction(e ->
			{
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

=======

		this.getChildren().addAll(menuBar, primaryPane, actionHBox);

		// set actions
		accountDeleteMenuItem.setOnAction(deleteAccountAction);
		saveMenuItem.setOnAction(saveAction);
		exitMenuItem.setOnAction(exitAction);
		
		// make seafoodMarketPane responsive
		seafoodMarketPane.transactionContainer.prefWidthProperty().bind(this.widthProperty());
		seafoodMarketPane.priceBox.prefWidthProperty().bind(seafoodMarketPane.transactionContainer.widthProperty().divide(2));
		seafoodMarketPane.usersResourcesBox.prefWidthProperty().bind(seafoodMarketPane.transactionContainer.widthProperty().divide(2));
		
		// make equipmentMarketPane responsive
		equipmentMarketPane.saleGridPaneContainer.prefWidthProperty().bind(this.widthProperty());
	}

	// where user selects what they want to do
	private class ActionVBox extends VBox
	{
		private Text txtTitle = new Text("Primary Actions - What would you like to do?");

		// buttons
		private Button btnGoFishing = new Button("Go Fishing");
		private Button btnGoToSeaFoodMarket = new Button("Sell Fish");
		private Button btnGoToEquipMarket = new Button("Buy Equipment");
		private Button btnCheckMyResources = new Button("View My Resouces");

		// containers
		private HBox buttonHBox = new HBox();

		public ActionVBox()
		{
			// set button actions
			btnGoFishing.setOnAction(e ->
			{
				primaryPane.getChildren().clear();
				primaryPane.getChildren().add(simpleFishingPane);
				if (fishingStarted)
				{
					// do nothing
				}
				else
				{
					fishingActivityActions.startFishingActivity();
					fishingStarted = true;
				}
			});

			btnCheckMyResources.setOnAction(e ->
			{
				primaryPane.getChildren().clear();
				// TODO use observables or something, so that it automatically changes.
				// this forces a refresh, dont leave this way...
				myStatsPane = new MyStatsPane();
				primaryPane.getChildren().add(myStatsPane);
			});

			btnGoToSeaFoodMarket.setOnAction(e ->
			{
				primaryPane.getChildren().clear();
				primaryPane.getChildren().add(seafoodMarketPane);
			});
			
			btnGoToEquipMarket.setOnAction(e ->
			{
				primaryPane.getChildren().clear();
				primaryPane.getChildren().add(equipmentMarketPane);
			});

			// put buttons in their container
			buttonHBox.getChildren().addAll(btnGoFishing, btnGoToSeaFoodMarket, btnGoToEquipMarket, btnCheckMyResources);

			// set spacing and alignment elements
			this.setSpacing(10);
			buttonHBox.setSpacing(10);
			this.setAlignment(Pos.CENTER);
			buttonHBox.setAlignment(Pos.CENTER);

			this.getChildren().addAll(txtTitle, buttonHBox);

		}

>>>>>>> nils_branch
	}

	///////////////////////////////////////////////
	// Definitons for Panes that go in 'primaryPane'
	///////////////////////////////////////////////

	private class MyStatsPane extends StackPane
	{
		// Labels
		Label lblName = new Label("Name: ");
		Label lblCashOnHand = new Label("Available $: ");
		Label lblSkillLevel = new Label("Skill Level: ");
		Label lblNumSeaCreatures = new Label("Number SeaCreatures: ");
<<<<<<< HEAD
=======
		Label lblNumTools = new Label("Number Tools: ");
>>>>>>> nils_branch

		// Text Fields
		Text txtName = new Text(player.getUsername());
		Text txtCashOnHand = new Text(Double.toString(player.getCashOnHand()));
		Text txtSkillLevel = new Text(Integer.toString(player.getSkillLevel()));
		Text textNumSeaCreatures = new Text(player.getIceChest() == null ? "0" : Integer.toString(player.getIceChest().size()));
<<<<<<< HEAD
=======
		Text txtNumTools = new Text(player.getToolChest() == null ? "0" : Integer.toString(player.getToolChest().size()));
>>>>>>> nils_branch

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
			statsGridPane.add(lblNumSeaCreatures, 0, 3);
			statsGridPane.add(textNumSeaCreatures, 1, 3);
<<<<<<< HEAD
=======
			statsGridPane.add(lblNumTools, 0, 4);
			statsGridPane.add(txtNumTools, 1, 4);
>>>>>>> nils_branch

			this.getChildren().addAll(statsGridPane);
		}
	}

	public class SimpleFishingPane extends Pane
	{
		// temp for now to show professor miller where we are going with this.
		// Pane explainPane = new Pane();
		// Pane fishPane = new Pane();

		private Random rand = new Random();
		// private int numCreaturesOnScreen = 0;
		// private Button btnExtractFishAction = new Button("Extract Fish");
		// private ArrayList<SeaCreature> creaturesOnScreen = new ArrayList<>();
		private Label labelExplanation = new Label(
				"Right now the fish are shown as dots. These dots are from SeaCreature objects, extracted " + "from an Ocean object belonging the server and sent to this client. We plan to animate them, and make it so that when they are " +
						"clicked (or however else caught), they are added (By calling the function attatched to the button) to the players resouces array, " +
						"which they can sell, to get better fishing equipment");

		public SimpleFishingPane()
		{

			this.setMinWidth(500);
			this.setMinHeight(400);
			labelExplanation.setMaxWidth(500);
			labelExplanation.setWrapText(true);
			// labelExplanation.setTranslateY(50);
			// btnExtractFishAction.setOnAction(extractFishAction);
			this.getChildren().addAll(labelExplanation);

		}
	}

	

}
