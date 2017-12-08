package userinterface;

import catchgame.Constants;
import catchgame.GameControl.IsValidQuantityListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


/**
 * This class is the GUI for the "market" for selling SeaCreatures.
 */
public class SeafoodMarketPane extends VBox
{
	// for titles
	private Label lblPrices = new Label("Current Prices");
	private Label lblMyResources = new Label("My Sea Creatures");
	private Label lblToSell = new Label("Resources To Sell");

	// for the name of the market
	public Text txtMarketName;

	// to display current prices
	private GridPane priceGridPane = new GridPane();

	// to display the players resources
	private GridPane myResourcesGridPane = new GridPane();

	// Containers, public for intuitive binding with parent class
	public HBox transactionContainer = new HBox();
	public VBox priceBox = new VBox();
	public VBox usersResourcesBox = new VBox();
	public VBox itemsToSellBox = new VBox();

	// to fire event to do the transaction
	private Button btnDoTransaction = new Button("Sell My Catch!");

	// for entry of how many SeaCreatures the player wants to sell
	private TextField[] numCreaturesToSellTextFields = new TextField[Constants.SUPPORTED_SPECIES.length];
	// shows how many of a type of SeaCreature player has
	private Text[] creaturesOnHandTextArray = new Text[Constants.SUPPORTED_SPECIES.length];
	// shows the current price for each type of SeaCreature
	private Text[] currentPricesTextArray = new Text[Constants.SUPPORTED_SPECIES.length];

	public SeafoodMarketPane(String name, EventHandler<ActionEvent> sellFishAction)
	{
		// set the title
		txtMarketName = new Text(name);

		// initialize price GridGane and  myResourcesGridPane GridPane
		for (int i = 0; i < Constants.SUPPORTED_SPECIES.length; i++)
		{
			Enum<?> currentSpecies = Constants.SUPPORTED_SPECIES[i];

			// add name of species
			priceGridPane.add(new Label(currentSpecies.toString()), 0, i);
			// initialize current price
			currentPricesTextArray[i] = new Text("Prices Not Set");
			priceGridPane.add(currentPricesTextArray[i], 1, i);
			// add picture of that SeaCreature
			ImageView curImage = new ImageView(Constants.getImage(currentSpecies));
			curImage.setFitHeight(20);
			curImage.setFitWidth(20);
			priceGridPane.add(curImage, 2, i);
			
			// init params for MyResoucesGridPane
			creaturesOnHandTextArray[i] = new Text();
			TextField textField = new TextField();
			textField.setMaxWidth(50);
			numCreaturesToSellTextFields[i] = textField;

			// add species label
			myResourcesGridPane.add(new Label(Constants.SUPPORTED_SPECIES[i].toString()), 0, i);
			// add text field for number to sell
			myResourcesGridPane.add(textField, 1, i);
			// add text show how many player has
			myResourcesGridPane.add(creaturesOnHandTextArray[i], 2, i);
		}

		// set up the left box to show prices
		priceBox.getChildren().addAll(lblPrices, priceGridPane);

		// set the middle box to show what I have/want to sell
		usersResourcesBox.getChildren().addAll(lblMyResources, myResourcesGridPane);

		// set the main container to show the two boxes from above
		transactionContainer.getChildren().addAll(priceBox, usersResourcesBox);

		// centering
		priceGridPane.setAlignment(Pos.CENTER);
		priceBox.setAlignment(Pos.CENTER);
		myResourcesGridPane.setAlignment(Pos.CENTER);
		usersResourcesBox.setAlignment(Pos.CENTER);
		
		this.setAlignment(Pos.CENTER);
		

		this.getChildren().addAll(txtMarketName, transactionContainer, btnDoTransaction);

		// spacing
		priceGridPane.setHgap(5);
		myResourcesGridPane.setHgap(5);

		priceGridPane.setVgap(5);
		myResourcesGridPane.setVgap(5);
		
		this.setSpacing(5);

		// set the buttons action
		btnDoTransaction.setOnAction(sellFishAction);

	}

	public void setCreaturesOnHandTextAt(int i, String str)
	{
		this.creaturesOnHandTextArray[i].setText(str);
	}

	public void setSpeciesToSellFfAt(int i, String str)
	{
		this.numCreaturesToSellTextFields[i].setText(str);
	}
	
	public void addNumToSellTfListener(int i, IsValidQuantityListener isIntegerTextFieldListener)
	{
		isIntegerTextFieldListener.setTextField(numCreaturesToSellTextFields[i]);
		numCreaturesToSellTextFields[i].textProperty().addListener(isIntegerTextFieldListener);
	}
	

	public TextField[] getNumCreaturesToSellTextFields()
	{
		return this.numCreaturesToSellTextFields;
	}

	public void setCurrentPricesTextAt(int i, String str)
	{
		this.currentPricesTextArray[i].setText("$" + str);
	}

	public Text[] getCurrentPricesTextArray()
	{
		return this.currentPricesTextArray;
	}


}
