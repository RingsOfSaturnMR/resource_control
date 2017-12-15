package graphicclasses;

import catchgame.Constants;
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
		
	}
	
	public ShellfishImageView getShellfishImageView(){
		return shellfishImageView;
	}
	
}
