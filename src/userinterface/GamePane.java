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
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import market.SeafoodMarket;
import catchgame.GameControl.FetchStatsHandler;
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
	public StatsVBox statsPane;

	public SimpleFishingPane simpleFishingPane;
	public SeafoodMarketPane seafoodMarketPane;
	public EquipmentMarketPane equipmentMarketPane;
	private GameControl.FishingActivityActions fishingActivityActions;
	private boolean fishingStarted = false;

	// for dropdown menu
	public MenuBar menuBar = new MenuBar();
	private Menu fileMenu = new Menu("File");
	private MenuItem accountDeleteMenuItem = new MenuItem("Delete Account");
	private MenuItem saveMenuItem = new MenuItem("Save");
	private MenuItem exitMenuItem = new MenuItem("Exit");
	
	// to trigger updating leaderboard
	private FetchStatsHandler updateStatsHandler;

	public GamePane(
			EventHandler<ActionEvent> sellFishAction,
			Player player,
			FishingActivityActions fishingActivityActions,
			EventHandler<ActionEvent> deleteAccountAction,
			EventHandler<ActionEvent> saveAction,
			EventHandler<ActionEvent> exitAction,
			String seaFoodMarketName,
			String equipMarketName,
			FetchStatsHandler updateStatsHandler)
	{
		this.player = player;
		statsPane = new StatsVBox();
		this.fishingActivityActions = fishingActivityActions;
		this.updateStatsHandler = updateStatsHandler;
		

		simpleFishingPane = new SimpleFishingPane();
		seafoodMarketPane = new SeafoodMarketPane(seaFoodMarketName, sellFishAction);
		equipmentMarketPane = new EquipmentMarketPane(equipMarketName);

		// set up menu
		fileMenu.getItems().addAll(accountDeleteMenuItem, saveMenuItem, exitMenuItem);
		menuBar.getMenus().add(fileMenu);

		primaryPane.getChildren().add(statsPane);

		this.getChildren().addAll(menuBar, primaryPane, actionHBox);

		// set actions
		accountDeleteMenuItem.setOnAction(deleteAccountAction);
		saveMenuItem.setOnAction(saveAction);
		exitMenuItem.setOnAction(exitAction);

		// make pane width the same as parent, helps with responsiveness
		seafoodMarketPane.prefWidthProperty().bind(this.widthProperty());
		equipmentMarketPane.prefWidthProperty().bind(this.widthProperty());
		statsPane.prefWidthProperty().bind(this.widthProperty());
	}

	// where user selects what they want to do
	private class ActionVBox extends VBox
	{
		// title for pane
		private Text txtTitle = new Text("Primary Actions - What would you like to do?");

		// general action output for user
		public TextArea taGameOutput = new TextArea();

		// buttons
		private Button btnGoFishing = new Button("Go Fishing");
		private Button btnGoToSeaFoodMarket = new Button("Sell Fish");
		private Button btnGoToEquipMarket = new Button("Buy Equipment");
		private Button btnCheckMyResources = new Button("View My Resouces");

		// containers
		private HBox buttonHBox = new HBox();

		public ActionVBox()
		{
			taGameOutput.setMaxHeight(75);
			taGameOutput.setEditable(false);

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
				updateStatsHandler.fetch();
				primaryPane.getChildren().add(statsPane);
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
			// this.setAlignment(Pos.CENTER);
			buttonHBox.setAlignment(Pos.CENTER);

			this.setAlignment(Pos.BOTTOM_CENTER);

			this.getChildren().addAll(txtTitle, buttonHBox, taGameOutput);

		}
	}

	public void appendOutput(String str)
	{
		if (!str.equals(""))
		{
			this.actionHBox.taGameOutput.appendText(str + "\n");
		}
	}

}
