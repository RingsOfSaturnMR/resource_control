package userinterface;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class GamePane extends StackPane
{
	private Text newGameTXT = new Text("New Game!");

	public GamePane()
	{
		this.getChildren().add(newGameTXT);
	}
}
