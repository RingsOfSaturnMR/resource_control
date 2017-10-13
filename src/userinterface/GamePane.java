package userinterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GamePane extends VBox
{
	private Pane outputPane = new Pane();
	private HBox actionHBox = new HBox();
	
	private Button btnGoFishing = new Button("Go Fishing");
	private Button btnGoToMarket = new Button("Go to Market");
	
	private Button btnExtractFishAction = new Button("Extract Fish");
	private Button btnSellFishAction = new Button("Sell Fish");
	
	private TextArea taMessage = new TextArea("Welcome!");
	
	public GamePane(EventHandler<ActionEvent> extractFishAction, EventHandler<ActionEvent> sellFishAction)
	{
		
		btnExtractFishAction.setOnAction(extractFishAction);
		btnSellFishAction.setOnAction(sellFishAction);
		
		outputPane.getChildren().addAll(taMessage);
		actionHBox.getChildren().addAll(btnGoFishing, btnGoToMarket);
		
		btnGoFishing.setOnAction(e -> setViewForFishing());
		btnGoToMarket.setOnAction(e -> setViewForSelling());

		
		this.getChildren().addAll(outputPane, actionHBox);
	}

	private void setViewForFishing()
	{
		outputPane.getChildren().clear();
		outputPane.getChildren().add(btnExtractFishAction);
	}
	
	private void setViewForSelling()
	{
		outputPane.getChildren().clear();
		outputPane.getChildren().add(btnSellFishAction);
	}
	
}
