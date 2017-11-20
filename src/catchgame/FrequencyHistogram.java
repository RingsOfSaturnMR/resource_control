package catchgame;

import catchgame.Catch.LoginHandler;
import catchgame.Catch.NewUserHandler;
import catchgame.Catch.NewServerHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.FrequencyHistogramPane;
import userinterface.LoginPane;

public class FrequencyHistogram {

	Ocean ocean;
	
	FrequencyHistogramPane fhPane;
	
	public FrequencyHistogram(Ocean ocean){
		this.ocean=ocean;
		loadFHPane();
	}
	
	public void loadFHPane()
	{
		Stage fh_Stage=new Stage();
		fhPane = new FrequencyHistogramPane(ocean);
		Scene loginScene = new Scene(fhPane, Constants.FREQUENCY_HISTOGRAM_PANE_WIDTH, Constants.FREQUENCY_HISTOGRAM_PANE_HEIGHT);
		fh_Stage.setScene(loginScene);
		fh_Stage.setTitle("Frequency Histogram of Ocean");
		fh_Stage.centerOnScreen();
		fh_Stage.show();
		fh_Stage.requestFocus();
	}
}