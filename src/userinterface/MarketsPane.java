package userinterface;

import java.util.ArrayList;

import catchgame.Constants;
import catchgame.Player;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import market.SeafoodMarket;
import resources.SeaCreature;
import resources.ShellfishSpecies;

public class MarketsPane extends VBox
{
	// the market that is doing the transaction
	private SeafoodMarket seafoodMarket;

	private Player player;

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

	// to hold the resources the player wants to sell
	private ObservableList<SeaCreature> toSellList = FXCollections.observableArrayList();

	// to flag if the input is valid
	private boolean isValidTransaction = true;

	// Containers
	public HBox transactionContainer = new HBox();
	public VBox priceBox = new VBox();
	public VBox usersResourcesBox = new VBox();
	public VBox itemsToSellBox = new VBox();

	// to fire event to do the transaction
	private Button btnDoTransaction = new Button("Do Transaction");

	private TextField[] numCreaturesToSellTextFields = new TextField[Constants.supportedSpecies.length];
	private Text[] creaturesOnHandTextArray = new Text[Constants.supportedSpecies.length];

	public MarketsPane(Player player, SeafoodMarket seafoodMarket, EventHandler<ActionEvent> sellFishAction)
	{
		this.player = player;
		this.seafoodMarket = seafoodMarket;
		
		// set the market
		setSeafoodMarket(seafoodMarket);

		// set the title
		txtMarketName = new Text(seafoodMarket.getName());

		// initialize gridpanes for all species
		for (int i = 0; i < Constants.supportedSpecies.length; i++)
		{
			Enum currentSpecies = Constants.supportedSpecies[i];
			
			// node, col, row
			priceGridPane.add(new Label(currentSpecies.toString()), 0, i);
			double price = seafoodMarket.getCurrentPricePerPound(currentSpecies);
			priceGridPane.add(new Text(Double.toString(price)), 1, i);
			ImageView curImage = new ImageView(Constants.getImage(currentSpecies));
			curImage.setFitHeight(20);
			curImage.setFitWidth(20);
			priceGridPane.add(curImage, 2, i);

			int numPlayerHas = player.getNumOf(Constants.supportedSpecies[i]);
			Text text = new Text("You Have: " + Integer.toString(numPlayerHas));
			TextField textField = new TextField();
			textField.setMaxWidth(50);

			creaturesOnHandTextArray[i] = text;
			numCreaturesToSellTextFields[i] = textField;

			// species label
			myResourcesGridPane.add(new Label(Constants.supportedSpecies[i].toString()), 0, i);
			// text field for number to sell
			myResourcesGridPane.add(textField, 1, i);
			// text show how many player has
			myResourcesGridPane.add(text, 2, i);
		}

		// set up the left box to show prices
		priceBox.getChildren().addAll(lblPrices, priceGridPane);

		// set the middle box to show what resources I have
		usersResourcesBox.getChildren().addAll(lblMyResources, myResourcesGridPane);

		// set the right box to show what resources I want to sell
		itemsToSellBox.getChildren().addAll(lblToSell);

		// set the main container to show the three boxes from above
		transactionContainer.getChildren().addAll(priceBox, usersResourcesBox);
		transactionContainer.setAlignment(Pos.CENTER);
		transactionContainer.setSpacing(5);

		btnDoTransaction.setOnAction(sellFishAction);
		
		// centering
		priceGridPane.setAlignment(Pos.CENTER);
		
		
		this.getChildren().addAll(txtMarketName, transactionContainer, btnDoTransaction);

		// spacing
		transactionContainer.setSpacing(6);
		priceGridPane.setHgap(6);
		myResourcesGridPane.setHgap(6);

		priceGridPane.setVgap(3);
		myResourcesGridPane.setVgap(3);
	
		
		// padding
		priceGridPane.setPadding(new Insets(5,5,5,5));
		priceGridPane.setPadding(new Insets(5,5,5,5));

	}

	public void setSeafoodMarket(SeafoodMarket market)
	{
		this.seafoodMarket = market;
	}


	public void setCreaturesOnHandTextAt(int i, String str)
	{
		this.creaturesOnHandTextArray[i].setText(str);
	}

	public void setSpeciesToSellTextFieldAt(int i, String str)
	{
		this.numCreaturesToSellTextFields[i].setText(str);
	}
	
	public TextField[] getNumCreaturesToSellTextFields()
	{
		return this.numCreaturesToSellTextFields;
	}
}
