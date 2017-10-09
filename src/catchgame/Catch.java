package catchgame;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import javafx.stage.Stage;

public class Catch extends Application
{
	public final static double TUNA_PRICE_PER_POUND = 6.40;
	public final static double SALMON_PRICE_PER_POUND = 12.0;
	public final static double COD_PRICE_PER_POUND = 17.0;
	
	
	@Override
	public void start(Stage primaryStage)
	{
		GameControl gameControl=new GameControl();
        gameControl.loadLoginPane();
	}

	public static void main(String[] args)
	{
		launch(args);
	}

}
