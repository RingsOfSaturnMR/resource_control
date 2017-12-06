package animation_work_and_driver;

import java.util.ArrayList;

import catchgame.Constants;
import graphicclasses.FishImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import resources.Fish;
import resources.SeaCreature;
import resources.Shellfish;
import utilities.NumberUtilities;

public class ClientFishingActivityFishManager {
		SimpleFishingPane simpleFishingPane;
	
		ArrayList<Fish>codPopulation = new ArrayList<>();
		ArrayList<Fish>salmonPopulation = new ArrayList<>(); 
		ArrayList<Fish>tunaPopulation = new ArrayList<>();
		
		// resounces.ShellFishSpecies
		ArrayList<Shellfish>lobsterPopulation = new ArrayList<>();
		ArrayList<Shellfish>crabPopulation = new ArrayList<>(); 
		ArrayList<Fish>oysterPopuliation = new ArrayList<>();
		
		ArrayList<Fish>offScreenCod= new ArrayList<>();
	
		ClientFishingActivityFishManager(SimpleFishingPane simpleFishingPane){
			this.simpleFishingPane=simpleFishingPane;
		}

	 void doBasicClientSubOceanAnimation(){
		Timeline timeline = new Timeline();
		KeyFrame frame = new KeyFrame(Duration.seconds(0.1),event->{
			makeSeaCreaturesOnScreenGo();
		});
			    		  
		timeline.getKeyFrames().addAll(frame); // add frame to the timeline KeyFrames
        timeline.setCycleCount(Timeline.INDEFINITE);
		
        timeline.play();
	}
	
	private void makeSeaCreaturesOnScreenGo(){
		//ArrayList<Fish> justOffScreenCod=new ArrayList<>();
		for (Fish cod : codPopulation){
			FishImageView codImageView=cod.getFishGraphic().getFishImageView();
			codImageView.setTranslateX(codImageView.getTranslateX()+cod.getSpeed()*cod.getDirecTion());
			//System.out.println(codImageView.getTranslateX());
			//System.out.println(simpleFishingPane.getWidth());
			//System.out.println(cod.getDirecTion()==Constants.RIGHT);
			if ((codImageView.getTranslateX()>simpleFishingPane.getWidth()&&cod.getDirecTion()==Constants.RIGHT)
					||(codImageView.getTranslateX()<-codImageView.getImage().getWidth()&&cod.getDirecTion()==Constants.LEFT)){
				makeAppearFromOffScreen(cod);
				System.out.println("hello");
				//codImageView.setTranslateX(0);
				//justOffScreenCod.add(cod);
			}
		}
		//offScreenCod.addAll(justOffScreenCod);
		//codPopulation.removeAll(justOffScreenCod);
	}
	
	/*
	fish.getFishGraphic().getFishImageView().setTranslateX(NumberUtilities.getRandomDouble(
			0, width-fish.getFishGraphic().getFishImageView().getImage().getWidth()));
	fish.getFishGraphic().getFishImageView().setTranslateY(NumberUtilities.getRandomDouble(
			75 + topOffset, 
			height - bottomOffset-fish.getFishGraphic().getFishImageView().getImage().getHeight()));
	*/
	
	private void makeAppearFromOffScreen(Fish fish){
		int leftOrRight=NumberUtilities.getRandomInt(0, 1);
		FishImageView fishImageView=fish.getFishGraphic().getFishImageView();
		System.out.println(leftOrRight);
		//go right
		if (leftOrRight==1){
			System.out.println("should go right");
			
			/*
			//if image direction was already right, keep it
			if (fish.getDirecTion()==Constants.RIGHT){
			}
			//if image direction was left, flip it
			else{
				fishImageView.setScaleX(1);
				System.out.println("should flip");
				//fishImageView.setRotate(180);
			}
			*/
			
			//set image to rightward-facing
			fishImageView.setScaleX(1);
			//move fish to far left
			fishImageView.setTranslateX(-fishImageView.getImage().getWidth()-20);
			//set direction to right
			fish.setDirecTion(true);
		}
		//go left
		else{
			System.out.println("should go left");
			
			/*
			//if image direction was already left, keep it
			if (fish.getDirecTion()==Constants.LEFT){
			}
			//if image direction was right, flip it
			else{
				System.out.println("should flip");
				fishImageView.setScaleX(-1);
				//fishImageView.setRotate(180);
			}
			*/
			
			fishImageView.setScaleX(-1);
			//move fish to far right
			fishImageView.setTranslateX(simpleFishingPane.getWidth()+20);
			//set direction to left
			fish.setDirecTion(false);
		}
		//get a random height within proper range
		fish.getFishGraphic().getFishImageView().setTranslateY(NumberUtilities.getRandomDouble(
				75 , 
				simpleFishingPane.getHeight()-fish.getFishGraphic().getFishImageView().getImage().getHeight()));
	}
}
