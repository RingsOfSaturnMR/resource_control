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

public class ClientSubOceanAnimator {

	SimpleFishingPane simpleFishingPane;

	ArrayList<Fish> codPopulation = new ArrayList<>();
	ArrayList<Fish> salmonPopulation = new ArrayList<>();
	ArrayList<Fish> tunaPopulation = new ArrayList<>();

	// resounces.ShellFishSpecies
	ArrayList<Shellfish> lobsterPopulation = new ArrayList<>();
	ArrayList<Shellfish> crabPopulation = new ArrayList<>();
	ArrayList<Shellfish> oysterPopuliation = new ArrayList<>();

	Timeline timeline;

	ClientSubOceanAnimator(SimpleFishingPane simpleFishingPane) {
		this.simpleFishingPane = simpleFishingPane;
	}

	void doBasicClientSubOceanAnimation() {
		Timeline timeline = new Timeline();
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.05), event -> {
			makeSeaCreaturesOnScreenGo();
		});

		timeline.getKeyFrames().addAll(keyFrame); // add frame to the timeline
													// KeyFrames
		timeline.setCycleCount(Timeline.INDEFINITE);

		timeline.play();
	}

	void stopAnimation() {
		if (timeline != null) {
			timeline.stop();
		} else {
			System.out.println("Timeline was null.");
		}
	}

	private void makeSeaCreaturesOnScreenGo() {
		makeFishOnScreenGo(codPopulation);
		makeFishOnScreenGo(salmonPopulation);
		makeFishOnScreenGo(tunaPopulation);
		makeShellfishOnScreenGo(lobsterPopulation);
		makeShellfishOnScreenGo(crabPopulation);
		makeShellfishOnScreenGo(oysterPopuliation);

	}

	private void makeFishOnScreenGo(ArrayList<Fish> fishPopulation) {
		for (Fish fish : fishPopulation) {
			FishImageView fishImageView = fish.getFishGraphic().getFishImageView();
			fishImageView.setTranslateX(fishImageView.getTranslateX() 
					+ fish.getSpeed() * fish.getDirecTion());
			if ((fishImageView.getTranslateX() > simpleFishingPane.getWidth() 
					&& fish.getDirecTion() == Constants.RIGHT)
					|| (fishImageView.getTranslateX() < -fishImageView.getFitWidth()
							&& fish.getDirecTion() == Constants.LEFT)) {
				makeFishAppearFromOffScreen(fish);
			}
		}
	}

	private void makeShellfishOnScreenGo(ArrayList<Shellfish> shellfishPopulation) {
		for (Shellfish shellFish : shellfishPopulation) {
			ShellfishImageView shellfishImageView = shellFish.getShellfishGraphic().getShellfishImageView();
			shellfishImageView.setTranslateX(
					shellfishImageView.getTranslateX() + shellFish.getSpeed() * shellFish.getDirecTion());
			if ((shellfishImageView.getTranslateX() > simpleFishingPane.getWidth()
					&& shellFish.getDirecTion() == Constants.RIGHT)
					|| (shellfishImageView.getTranslateX() < -shellfishImageView.getImage().getWidth()
							&& shellFish.getDirecTion() == Constants.LEFT)) {
				makeShellfishAppearFromOffScreen(shellFish);
			}
		}
	}

	private void makeFishAppearFromOffScreen(Fish fish) {
		int leftOrRight = NumberUtilities.getRandomInt(0, 1);
		FishImageView fishImageView = fish.getFishGraphic().getFishImageView();
		if (leftOrRight == 1) {
			fishImageView.setScaleX(1);
			// move fish to far left
			fishImageView.setTranslateX(-fishImageView.getFitWidth() - 20);
			// set direction to right
			fish.setDirecTion(true);
		}
		// go left
		else {
			fishImageView.setScaleX(-1);
			// move fish to far right
			fishImageView.setTranslateX(simpleFishingPane.getWidth() + 20);
			// set direction to left
			fish.setDirecTion(false);
		}
		// get a random height within proper range
		fishImageView.setTranslateY(NumberUtilities.getRandomDouble(Constants.DISTANCE_FROM_TOP,
				(int) simpleFishingPane.getHeight() * Constants.TOP_COEFFICIENT - fishImageView.getFitHeight()
						- Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT));
	}

	private void makeShellfishAppearFromOffScreen(Shellfish shellfish) {
		int leftOrRight = NumberUtilities.getRandomInt(0, 1);
		ShellfishImageView shellfishImageView = shellfish.getShellfishGraphic().getShellfishImageView();
		// go right
		if (leftOrRight == 1) {
			// set image to rightward-facing
			shellfishImageView.setScaleX(1);
			// move fish to far left
			shellfishImageView.setTranslateX(-shellfishImageView.getFitWidth() - 20);
			// set direction to right
			shellfish.setDirecTion(true);
		}
		// go left
		else {
			shellfishImageView.setScaleX(-1);
			// move fish to far right
			shellfishImageView.setTranslateX(simpleFishingPane.getWidth() + 20);
			// set direction to left
			shellfish.setDirecTion(false);
		}
		// get a random height within proper range
		shellfishImageView.setTranslateY(NumberUtilities.getRandomDouble(
				((int) simpleFishingPane.getHeight() * Constants.TOP_COEFFICIENT
						+ Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT),
				(int) simpleFishingPane.getHeight() - shellfishImageView.getFitHeight()
						- Constants.DISTANCE_FROM_BOTTOM));
	}
}
