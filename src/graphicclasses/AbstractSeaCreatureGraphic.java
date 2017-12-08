package graphicclasses;

import catchgame.Constants;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AbstractSeaCreatureGraphic {
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
	
}
