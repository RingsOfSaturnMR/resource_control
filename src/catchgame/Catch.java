package catchgame;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
	private LoginPane loginPane;
	private Stage loginStage = new Stage();
	private GameControl gameControl;

	@Override
	public void start(Stage primaryStage)
	{
		launchLoginPane();
	}

	public void launchLoginPane()
	{
		loginPane = new LoginPane(new LoginHandler(), new NewUserHandler(), new NewUserServerHandler());
		Scene loginScene = new Scene(loginPane, Constants.LOGIN_PANE_WIDTH, Constants.LOGIN_PANE_HEIGHT);
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
			gameControl = new GameControl(new Player());
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
	
	public class NewUserServerHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			System.out.println("New Server Clicked");
			CatchServer catchServer = new CatchServer();
		};
	}
	public static void main(String[] args)
	{
		launch(args);
	}
}
