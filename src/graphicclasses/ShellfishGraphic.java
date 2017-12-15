package graphicclasses;

import catchgame.Constants;
<<<<<<< HEAD
=======
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import resources.Fish;
import resources.FishSpecies;
>>>>>>> master
import resources.Shellfish;

public class ShellfishGraphic extends AbstractSeaCreatureGraphic{
	private Shellfish shellfish;
	private ShellfishImageView shellfishImageView;
	
	public ShellfishGraphic(Shellfish shellfish){
		this.shellfish=shellfish;
		
		shellfishImageView=new ShellfishImageView(shellfish);
		super.seaCreatureImage=AbstractSeaCreatureGraphic.getImage(shellfish.getSpecies());
		System.out.println("got image");
		shellfishImageView.setImage(super.seaCreatureImage);
		System.out.println("set image");
		short WEIGHT_GRAPHIC_MULTIPLE=10;
		switch (shellfish.getSpecies()){
		case LOBSTER:
			WEIGHT_GRAPHIC_MULTIPLE=Constants.SEACREATURE_WEIGHT_GRAPHIC_MULTIPLE;
			break;
		case CRAB:
			WEIGHT_GRAPHIC_MULTIPLE=Constants.SEACREATURE_WEIGHT_GRAPHIC_MULTIPLE;
			break;
		case OYSTER:
			WEIGHT_GRAPHIC_MULTIPLE=Constants.SEACREATURE_WEIGHT_GRAPHIC_MULTIPLE;
			break;
		}
		shellfishImageView.setFitWidth(shellfish.getWeight()*WEIGHT_GRAPHIC_MULTIPLE);
		shellfishImageView.setPreserveRatio(true);
		shellfishImageView.setSmooth(true);
		shellfishImageView.setCache(true);
		
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
	
	public ShellfishImageView getShellfishImageView(){
		return shellfishImageView;
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
