package userinterface;

import catchgame.Constants;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import resources.Fish;
import resources.FishSpecies;

public class FishGraphic extends AbstractSeaCreatureGraphic{
	public Fish fish;
	public FishGraphic(Fish fish){
		this.fish=fish;
		
		
		super.seaCreatureImage=AbstractSeaCreatureGraphic.getImage(fish.getSpecies());
		System.out.println("got image");
		super.seaCreatureImageView.setImage(super.seaCreatureImage);
		System.out.println("set image");
		
		/*
		seaCreatureImage=getImage(fish.getSpecies());
		System.out.println("got image");
		seaCreatureImageView.setImage(seaCreatureImage);
		System.out.println("set image");
		*/
	}
	
	
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
}
