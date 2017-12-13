package catchgame;

import java.util.ArrayList;

import graphicclasses.FishImageView;
import graphicclasses.ShellfishImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import resources.Fish;
import resources.Shellfish;
import userinterface.SimpleFishingPane;
import utilities.NumberUtilities;

public class ClientFishingActivityFishManager {
	
	
	SimpleFishingPane simpleFishingPane;
	
	ArrayList<Fish>codPopulation = new ArrayList<>();
	ArrayList<Fish>salmonPopulation = new ArrayList<>(); 
	ArrayList<Fish>tunaPopulation = new ArrayList<>();
	
	// resounces.ShellFishSpecies
	ArrayList<Shellfish>lobsterPopulation = new ArrayList<>();
	ArrayList<Shellfish>crabPopulation = new ArrayList<>(); 
	ArrayList<Shellfish>oysterPopuliation = new ArrayList<>();
	
	ArrayList<Fish>offScreenCod= new ArrayList<>();
	
	Timeline timeline;

	ClientFishingActivityFishManager(SimpleFishingPane simpleFishingPane){
		this.simpleFishingPane=simpleFishingPane;
	}

 void doBasicClientSubOceanAnimation(){
	Timeline timeline = new Timeline();
	KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.05),event->{
		makeSeaCreaturesOnScreenGo();
	});
	//}
		    		  
	timeline.getKeyFrames().addAll(keyFrame); // add frame to the timeline KeyFrames
    timeline.setCycleCount(Timeline.INDEFINITE);
	
    timeline.play();
}
 
 void stopAnimation(){
	 timeline.stop();
 }

 private void makeSeaCreaturesOnScreenGo(){
	 makeFishOnScreenGo(codPopulation);
	 makeFishOnScreenGo(salmonPopulation);
	 makeFishOnScreenGo(tunaPopulation);
	 makeShellfishOnScreenGo(lobsterPopulation);
	 makeShellfishOnScreenGo(crabPopulation);
	 makeShellfishOnScreenGo(oysterPopuliation);
	 
 }
 /*
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
			makeFishAppearFromOffScreen(cod);
			System.out.println("hello");
			//codImageView.setTranslateX(0);
			//justOffScreenCod.add(cod);
		}
	}
	//offScreenCod.addAll(justOffScreenCod);
	//codPopulation.removeAll(justOffScreenCod);
}
*/
private void makeFishOnScreenGo(ArrayList<Fish> fishPopulation){
	//ArrayList<Fish> justOffScreenCod=new ArrayList<>();
	for (Fish fish : fishPopulation){
		FishImageView fishImageView=fish.getFishGraphic().getFishImageView();
		fishImageView.setTranslateX(fishImageView.getTranslateX()+fish.getSpeed()*fish.getDirecTion());
		//System.out.println(codImageView.getTranslateX());
		//System.out.println(simpleFishingPane.getWidth());
		//System.out.println(cod.getDirecTion()==Constants.RIGHT);
		if ((fishImageView.getTranslateX()>simpleFishingPane.getWidth()&&fish.getDirecTion()==Constants.RIGHT)
				||(fishImageView.getTranslateX()<-fishImageView.getFitWidth()&&fish.getDirecTion()==Constants.LEFT)){
			makeFishAppearFromOffScreen(fish);
			//System.out.println("hello");
			//codImageView.setTranslateX(0);
			//justOffScreenCod.add(cod);
		}
	}
	//offScreenCod.addAll(justOffScreenCod);
	//codPopulation.removeAll(justOffScreenCod);
}

private void makeShellfishOnScreenGo(ArrayList<Shellfish> shellfishPopulation){
	//ArrayList<Fish> justOffScreenCod=new ArrayList<>();
	for (Shellfish shellFish : shellfishPopulation){
		ShellfishImageView shellfishImageView=shellFish.getShellfishGraphic().getShellfishImageView();
		shellfishImageView.setTranslateX(shellfishImageView.getTranslateX()+shellFish.getSpeed()*shellFish.getDirecTion());
		//System.out.println(codImageView.getTranslateX());
		//System.out.println(simpleFishingPane.getWidth());
		//System.out.println(cod.getDirecTion()==Constants.RIGHT);
		if ((shellfishImageView.getTranslateX()>simpleFishingPane.getWidth()&&shellFish.getDirecTion()==Constants.RIGHT)
				||(shellfishImageView.getTranslateX()<-shellfishImageView.getImage().getWidth()&&shellFish.getDirecTion()==Constants.LEFT)){
			makeShellfishAppearFromOffScreen(shellFish);
			//System.out.println("hello");
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

private void makeFishAppearFromOffScreen(Fish fish){
	int leftOrRight=NumberUtilities.getRandomInt(0, 1);
	FishImageView fishImageView=fish.getFishGraphic().getFishImageView();
	//System.out.println(leftOrRight);
	//go right
	if (leftOrRight==1){
		//System.out.println("should go right");
		
		//set image to rightward-facing
		fishImageView.setScaleX(1);
		//move fish to far left
		fishImageView.setTranslateX(-fishImageView.getFitWidth()-20);
		//set direction to right
		fish.setDirecTion(true);
	}
	//go left
	else{
		//System.out.println("should go left");
		
		fishImageView.setScaleX(-1);
		//move fish to far right
		fishImageView.setTranslateX(simpleFishingPane.getWidth()+20);
		//set direction to left
		fish.setDirecTion(false);
	}
	//get a random height within proper range
	fishImageView.setTranslateY(NumberUtilities.getRandomDouble(
			Constants.DISTANCE_FROM_TOP, 
			(int)simpleFishingPane.getHeight()*Constants.TOP_COEFFICIENT
			-fishImageView.getFitHeight()-Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT));
}

private void makeShellfishAppearFromOffScreen(Shellfish shellfish){
	int leftOrRight=NumberUtilities.getRandomInt(0, 1);
	ShellfishImageView shellfishImageView=shellfish.getShellfishGraphic().getShellfishImageView();
	//System.out.println(leftOrRight);
	//go right
	if (leftOrRight==1){
		//System.out.println("should go right");
		
		//set image to rightward-facing
		shellfishImageView.setScaleX(1);
		//move fish to far left
		shellfishImageView.setTranslateX(-shellfishImageView.getFitWidth()-20);
		//set direction to right
		shellfish.setDirecTion(true);
	}
	//go left
	else{
		//System.out.println("should go left");
		
		
		shellfishImageView.setScaleX(-1);
		//move fish to far right
		shellfishImageView.setTranslateX(simpleFishingPane.getWidth()+20);
		//set direction to left
		shellfish.setDirecTion(false);
	}
	//get a random height within proper range
	shellfishImageView.setTranslateY(NumberUtilities.getRandomDouble(
			((int)simpleFishingPane.getHeight()*Constants.TOP_COEFFICIENT+Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT),
			(int)simpleFishingPane.getHeight()-shellfishImageView.getFitHeight()-Constants.DISTANCE_FROM_BOTTOM));
}
}
