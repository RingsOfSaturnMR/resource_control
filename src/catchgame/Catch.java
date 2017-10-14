package catchgame;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import javafx.stage.Stage;
import userinterface.LoginPane;

public class Catch extends Application
{
	public final static double TUNA_PRICE_PER_POUND = 6.40;
	public final static double SALMON_PRICE_PER_POUND = 12.0;
	public final static double COD_PRICE_PER_POUND = 17.0;
	
	private final int LOGIN_WIDTH = 400;
	private final int LOGIN_HEIGHT = 400;

	private LoginPane loginPane;

	@Override
	public void start(Stage primaryStage)
	{
		loadLoginPane();
	}

	public void loadLoginPane()
	{

		Stage loginStage = new Stage();

		loginPane = new LoginPane(new LoginHandler(), new NewUserHandler());

		Scene loginScene = new Scene(loginPane, LOGIN_WIDTH, LOGIN_HEIGHT);

		// show LoginPane
		loginStage.setScene(loginScene);
		loginStage.setTitle("Catch! Log-in");
		loginStage.centerOnScreen();
		loginStage.show();
		loginStage.requestFocus();
	}

	public class LoginHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			GameControl gameControl = new GameControl(new Player());
		};
	}

	public class NewUserHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			System.out.println("New User Clicked");
		};
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
