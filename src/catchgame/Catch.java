package catchgame;

import java.io.IOException;
import java.io.Serializable;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

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

	static int numTries = 0;

	public class LoginHandler implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			try
			{
				GameControl gameControl = new GameControl(loginPane.getServerIpAddress(), loginPane.getClientPort(), loginPane.getPlayerName(), loginPane.getPlayerPassword());
			}
			catch (Exception ex )
			{
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		

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

	public static class LoginPacket implements Serializable
	{
		public LoginPacket(String name, String password)
		{
			this.enteredName = name;
			this.enteredPassword = password;
		}

		public String enteredName;
		public String enteredPassword;
	}

	public static void main(String[] args)
	{
		launch(args);
	}

}
