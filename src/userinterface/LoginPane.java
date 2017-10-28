package userinterface;

import catchgame.CatchServer;
import catchgame.Constants;
import catchgame.GameControl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import userinterface.LoginPane;

public class LoginPane extends VBox
{
	// title
	private Text txtTitle = new Text("Welcome to " + Constants.APPLICATION_NAME);
	
	// labels for fields
	private Label lblName = new Label("Username: ");
	private Label lblPassword = new Label("Password: ");
	private Label lblServerIp = new Label("Server ip: ");
	private Label lblClientPort = new Label("Client Port: ");

	// fields for entry, filled with default values for easy testing
	private TextField tfName = new TextField("");
	private TextField pfPassword = new TextField("");
	private TextField tfServerIp = new TextField("localhost");
	private TextField tfClientPort = new TextField("8000");
	
	// containers
	private GridPane loginGridPane = new GridPane();
	private HBox buttonHBox = new HBox();
	private StackPane titleStackPane = new StackPane();

	// buttons
	private Button btnLogin = new Button("Login");
	private Button btnNewUser = new Button("New User");
	private Button btnNewServer = new Button("New Server");
	
	// text to display errors
	private Text txtError = new Text();

	public LoginPane(EventHandler<ActionEvent> loginAction, EventHandler<ActionEvent> newUserAction, EventHandler<ActionEvent> newServerAction)
	{		
		// put spacing around title container
		titleStackPane.setPadding(new Insets(10, 10, 10, 10));
		// node, col, row
		loginGridPane.add(lblServerIp, 0, 0);
		loginGridPane.add(tfServerIp, 1, 0);
		loginGridPane.add(lblClientPort, 0, 1);
		loginGridPane.add(tfClientPort, 1, 1);
		loginGridPane.add(lblName, 0, 2);
		loginGridPane.add(tfName, 1, 2);
		loginGridPane.add(lblPassword, 0, 3);
		loginGridPane.add(pfPassword, 1, 3);

		// add vertical spacing, put in center and add padding to top/bottom
		loginGridPane.setVgap(5);
		loginGridPane.setAlignment(Pos.CENTER);
		loginGridPane.setPadding(new Insets(10, 0, 10, 0));
		
		// put in center and set spacing
		buttonHBox.setAlignment(Pos.CENTER);
		buttonHBox.setSpacing(10);
		
		// add everything to its appropriate node
		titleStackPane.getChildren().add(txtTitle);
		buttonHBox.getChildren().addAll(btnLogin, btnNewUser, btnNewServer);
		this.getChildren().addAll(titleStackPane, loginGridPane, txtError, buttonHBox);
		
		// set actions
		btnLogin.setOnAction(loginAction);
		btnNewUser.setOnAction(newUserAction);
		btnNewServer.setOnAction(newServerAction);
	}
	
	public String getPlayerName()
	{
		return tfName.getText();
	}
	
	public String getPlayerPassword()
	{
		return pfPassword.getText();
	}
	
	public String getServerIpAddress()
	{
		return tfServerIp.getText();
	}
	
	public void setErrorText(String str)
	{
		this.txtError.setText(str);
	}
	
	//NEED TO ADD check for exceptions!!!
	public int getClientPort()
	{
		return Integer.parseInt((tfClientPort.getText()));
	}
	
}
