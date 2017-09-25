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
	@Override
	public void start(Stage primaryStage)
	{
		Button btn = new Button("Do you like fishing?");

		btn.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				System.out.println("I dont know, do you?!");
			}
		});

		StackPane root = new StackPane();
		root.getChildren().add(btn);
		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.setTitle("Catch!");
		primaryStage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}

}
