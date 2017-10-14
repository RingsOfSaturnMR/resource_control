package catchgame;

import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.GamePane;
import userinterface.LoginPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GameControl
{
	private GamePane gamePane;
// private, stream, socket? network stuff 
	private Player player;
	private String socket;

	private Stage gameStage = new Stage();

	public GameControl(Player player)
	{
		this.player = player;
		loadGamePane();
	}

	public void loadGamePane()
	{
		// the height and width
		int GAME_WIDTH = 400;
		int GAME_HEIGHT = 400;

		gamePane = new GamePane(new ExtractFishAction(), new SellFishAction());

		Scene gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);

		// show GamePane
		gameStage.setScene(gameScene);
		gameStage.setTitle("Catch!");
		gameStage.centerOnScreen();
		gameStage.show();
		gameStage.requestFocus();
	}

	private class ExtractFishAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			System.out.println("Extract fish action triggered(fish caught)");
		};
	}
	
	private class SellFishAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			System.out.println("Sell Fish action triggered(fish sold to market)");
		};
	}
}
