package userinterface;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ServerPane extends Pane
{
	private TextArea taOutput = new TextArea("");
	
	private Button btnFH=new Button("Frequency Histogram Of Ocean");
	
	private VBox vB=new VBox();
	
	public ServerPane(EventHandler launch_FH_Action)
	{
		btnFH.setOnAction(launch_FH_Action);
		vB.getChildren().addAll(taOutput, btnFH);
		getChildren().addAll(vB);
	}
	
	public void appendToOutput(String str)
	{
		taOutput.appendText(str + "\n");
	}
	
}
