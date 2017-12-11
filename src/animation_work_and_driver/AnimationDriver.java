package animation_work_and_driver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.sun.javafx.geom.Rectangle;

import catchgame.Constants;
import catchgame.Catch.LoginHandler;
import catchgame.Catch.NewServerHandler;
import catchgame.Catch.NewUserHandler;
import catchgame.Packets.NewUserPacket;
import catchgame.Packets.ResultPacket;
import catchgame.Player;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.LoginPane;
import userinterface.NewUserPane;
import userinterface.SimpleFishingPane;

public class AnimationDriver extends Application{

	SimpleFishingPane simpleFishingPane;
	@Override
	public void start(Stage primaryStage)
	{
		loadSimpleFishingPanePane(primaryStage);
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}	
	
	public void loadSimpleFishingPanePane(Stage primaryStage)
	{
		simpleFishingPane = new SimpleFishingPane();
		Scene simpleFishingScene = new Scene(simpleFishingPane, Constants.INITIAL_SIMPLE_FISHING_PANE_WIDTH, 
				Constants.INITIAL_SIMPLE_FISHING_PANE_HEIGHT);
		primaryStage.setScene(simpleFishingScene);
		primaryStage.setTitle("Fishing Activity");
		FishingActivityV2 fishingActivityV2=new FishingActivityV2(simpleFishingPane, new Player("JaneFisher"));
		primaryStage.show();
		primaryStage.requestFocus();
		
	}
}