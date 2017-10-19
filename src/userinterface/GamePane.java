package userinterface;

import catchgame.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GamePane extends VBox
{
	private Player player = new Player();
	
	private StackPane primaryPane = new StackPane();
	private ActionVBox actionHBox = new ActionVBox();
	private MyStatsPane myStatsPane = new MyStatsPane();
	
	private SimpleFishingPane simpleFishingPane;
	private MarketsPane marketsPane;
	
	public GamePane(EventHandler<ActionEvent> extractFishAction, EventHandler<ActionEvent> sellFishAction, Player player)
	{
		//this.player = player;
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
	//Definitons for Panes that go in 'primaryPane'
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
	
	private class SimpleFishingPane extends StackPane
	{
		private Button btnExtractFishAction = new Button("Extract Fish");

		public SimpleFishingPane(EventHandler<ActionEvent> extractFishAction)
		{
			this.setWidth(200);
			this.setHeight(200);
			
			btnExtractFishAction.setOnAction(extractFishAction);
			this.getChildren().addAll(btnExtractFishAction);
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
