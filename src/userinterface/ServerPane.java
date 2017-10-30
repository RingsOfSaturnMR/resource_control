package userinterface;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

public class ServerPane extends Pane
{
	private TextArea taOutput = new TextArea("");
	
	public ServerPane()
	{
		getChildren().addAll(taOutput);
	}
	
	public void appendToOutput(String str)
	{
		taOutput.appendText(str + "\n");
	}
	
}
