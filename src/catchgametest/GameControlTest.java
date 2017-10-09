package catchgametest;

import catchgame.GameControl;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameControlTest extends Application
{

	@Override
	public void start(Stage primaryStage)
	{
		GameControl gameControl = new GameControl();

		for (int i = 0; i < gameControl.getOcean().getCodPopulation().size(); i++)
		{
			System.out.println(i+1 + ".) " + "'toString()': " + gameControl.getOcean().getCodPopulation().get(i).toString());
			System.out.println("Weight: " + gameControl.getOcean().getCodPopulation().get(i).getWeight() + "\n");
		}
		
		for (int i = 0; i < gameControl.getOcean().getSalmonPopulation().size(); i++)
		{
			System.out.println(i+1 + ".) " + "'toString()': " + gameControl.getOcean().getSalmonPopulation().get(i).toString());
			System.out.println("Weight: " + gameControl.getOcean().getSalmonPopulation().get(i).getWeight() + "\n");
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}

}
