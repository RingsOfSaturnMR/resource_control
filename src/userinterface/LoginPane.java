package userinterface;

import catchgame.GameControl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import userinterface.LoginPane;

public class LoginPane extends VBox
{
	private Label lblName = new Label("Username: ");
	private Label lblPassword = new Label("Password: ");

	private PasswordField pfPassword = new PasswordField();
	private TextField tfName = new TextField();

	private GridPane loginGridPane = new GridPane();
	private HBox buttonHBox = new HBox();

	private Button btnLogin = new Button("Login");
	private Button btnNewUser = new Button("New User");

	public LoginPane(EventHandler<ActionEvent> loginAction, EventHandler<ActionEvent> newUserAction)
	{
		btnLogin.setOnAction(loginAction);
		btnNewUser.setOnAction(newUserAction);

		// node, col, row
		loginGridPane.add(lblName, 0, 0);
		loginGridPane.add(tfName, 1, 0);
		loginGridPane.add(lblPassword, 0, 1);
		loginGridPane.add(pfPassword, 1, 1);

		buttonHBox.getChildren().addAll(btnLogin, btnNewUser);

		this.getChildren().addAll(loginGridPane, buttonHBox);
	}
}
