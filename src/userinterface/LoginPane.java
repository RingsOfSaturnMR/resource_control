package userinterface;

import catchgame.GameControl;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import userinterface.LoginPane;

public class LoginPane extends StackPane {

    //add code--should set window width/height max/min to height and width
	public LoginPane(GameControl.LoginHandler loginHandler){
	Button loginBtn=new Button("Login");

	
	loginBtn.setOnAction(loginHandler);


    this.getChildren().add(loginBtn);
	}
}
