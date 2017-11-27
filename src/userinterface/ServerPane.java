package userinterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ServerPane extends VBox
{
	private TextArea taOutput = new TextArea("");

	// MenuBar related nodes
	public MenuBar menuBar = new MenuBar();
	private Menu fileMenu = new Menu("File");
	private MenuItem dbManipulatorItem = new MenuItem("DB Manipulator");
	private MenuItem exitMenuItem = new MenuItem("Exit");
	
	// buttons
	private Button btnFH = new Button("Frequency Histogram Of Ocean");

	public ServerPane(EventHandler<ActionEvent> launchDbManipulator, EventHandler<ActionEvent> closeProgramHandler, EventHandler launch_FH_Action)
	{

		// set menu actions
		dbManipulatorItem.setOnAction(launchDbManipulator);
		exitMenuItem.setOnAction(closeProgramHandler);
		
		// set button actions
		btnFH.setOnAction(launch_FH_Action);

		// add menu items to menu and
		fileMenu.getItems().addAll(dbManipulatorItem, exitMenuItem);
		menuBar.getMenus().add(fileMenu);
	
		this.setSpacing(10);
		
		getChildren().addAll(menuBar, taOutput, btnFH);
	}

	public void appendToOutput(String str)
	{
		taOutput.appendText(str + "\n");
	}

}
