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
	private LoginPane loginPane;
	private Ocean ocean = new Ocean();
	
	private Stage gameStage = new Stage();
	
	public Ocean getOcean()
	{
		return ocean;
	}

	public void setOcean(Ocean ocean)
	{
		this.ocean = ocean;
	}
	
	public void loadGamePane(){

        //the height and width
        int GAME_WIDTH=800;
        int GAME_HEIGHT=800;

        //add code--should set window width/height max/min to height and width

        gamePane=new GamePane();

        Scene gameScene=new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);

        //show GamePane
        gameStage.setScene(gameScene);
        gameStage.setTitle("Catch!");
        gameStage.centerOnScreen();
        gameStage.show();
        gameStage.requestFocus();
    }
	
	public void loadLoginPane(){
        //the height and width
        int GAME_WIDTH=400;
        int GAME_HEIGHT=400;

        //add code--should set window width/height max/min to height and width

        loginPane=new LoginPane(new LoginHandler());

        Scene loginScene=new Scene(loginPane, GAME_WIDTH, GAME_HEIGHT);

        //show LoginPane
        gameStage.setScene(loginScene);
        gameStage.setTitle("Catch! Log-in");
        gameStage.centerOnScreen();
        gameStage.show();
        gameStage.requestFocus();
    }
	
	public class LoginHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			loadGamePane();
		};
	}
}
