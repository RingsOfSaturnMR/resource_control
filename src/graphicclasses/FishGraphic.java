package graphicclasses;

import catchgame.Constants;
<<<<<<< HEAD
import resources.Fish;
=======
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import resources.Fish;
import resources.FishSpecies;
>>>>>>> master

public class FishGraphic extends AbstractSeaCreatureGraphic{
	private Fish fish;
	private FishImageView fishImageView;
	
	public FishGraphic(Fish fish){
		this.fish=fish;
		
		fishImageView=new FishImageView(fish);
		super.seaCreatureImage=AbstractSeaCreatureGraphic.getImage(fish.getSpecies());
		System.out.println("got image");
		fishImageView.setImage(super.seaCreatureImage);
		System.out.println("set image");
		short WEIGHT_GRAPHIC_MULTIPLE=10;
		switch (fish.getSpecies()){
		case COD:
			WEIGHT_GRAPHIC_MULTIPLE=Constants.SEACREATURE_WEIGHT_GRAPHIC_MULTIPLE;
			break;
		case SALMON:
			WEIGHT_GRAPHIC_MULTIPLE=Constants.SEACREATURE_WEIGHT_GRAPHIC_MULTIPLE;
			break;
		case TUNA:
			WEIGHT_GRAPHIC_MULTIPLE=Constants.SEACREATURE_WEIGHT_GRAPHIC_MULTIPLE;
			break;
		}
<<<<<<< HEAD
		fishImageView.setFitWidth(fish.getWeight()*WEIGHT_GRAPHIC_MULTIPLE);
=======
		//double fishImageViewWidth=fish.getWeight()*Constants.COD_WEIGHT_GRAPHIC_MULTIPLE;
		fishImageView.setFitWidth(fish.getWeight()*WEIGHT_GRAPHIC_MULTIPLE);
		//fishImageView.setPreserveRatio(true);
>>>>>>> master
		fishImageView.setFitHeight(this.seaCreatureImage.getHeight()*fishImageView.getFitWidth()/this.seaCreatureImage.getWidth());
		fishImageView.setSmooth(true);
		fishImageView.setCache(true);
		
<<<<<<< HEAD
=======
		/*
		seaCreatureImage=getImage(fish.getSpecies());
		System.out.println("got image");
		seaCreatureImageView.setImage(seaCreatureImage);
		System.out.println("set image");
		*/
>>>>>>> master
	}
	
	public FishImageView getFishImageView(){
		return fishImageView;
	}
	
<<<<<<< HEAD
=======
	/*
	protected ImageView seaCreatureImageView=new ImageView();
	protected Image seaCreatureImage;
	
	public static final Image getImage(final Enum<?> e)
	{
		for(int i = 0; i < Constants.SUPPORTED_SPECIES.length; i++ )
		{
			if(e == Constants.SUPPORTED_SPECIES[i])
			{
				return (new Image("img/" + Constants.SUPPORTED_SPECIES[i].toString().toLowerCase() + ".png"));
			}
		}
		
		return null;	
	}
	
	public ImageView getSeaCreatureImageView(){
		return seaCreatureImageView;
	}
	*/
>>>>>>> master
}
