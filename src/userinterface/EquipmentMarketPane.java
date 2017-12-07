package userinterface;

import catchgame.Constants;
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

public class EquipmentMarketPane extends VBox
{
		// for title
		private Label lblPrices = new Label("Items For Sale");

		// for the name of the market
		public Text txtMarketName;

		// to display current prices
		private GridPane saleGridPane = new GridPane();

		// shows the current price for each type of SeaCreature
		private Text[] currentPricesTextArray = new Text[Constants.SUPPORTED_EQUIPMENT.length];
		// holds the buttons to buy the items
		private Button[] purchaseBtnArray = new Button[Constants.SUPPORTED_EQUIPMENT.length];
		// shows how many of a type of SeaCreature player has
		private Text[] numOnHandTextArray = new Text[Constants.SUPPORTED_EQUIPMENT.length];
		
		public EquipmentMarketPane(String name)//, EventHandler<ActionEvent> buyEquipmentAction)
		{
			// set the title
			txtMarketName = new Text(name);

			// initialize price GridGane for each row
			for (int i = 0; i < Constants.SUPPORTED_EQUIPMENT.length; i++)
			{
				Enum<?> currentItem = Constants.SUPPORTED_EQUIPMENT[i];

				// get the thumbnail img
				ImageView curImage = new ImageView(Constants.getImage(currentItem));
				curImage.setFitHeight(20);
				curImage.setFitWidth(20);
				
				// get the items name
				Text curNameText = new Text(currentItem.toString());
				
				//initialize the item's price text
				currentPricesTextArray[i] = new Text("price not set yet");
				
				// initialize the items button
				purchaseBtnArray[i] = new Button("Buy");
				
				// initialize the num on hand text
				numOnHandTextArray[i] = new Text("Number Not Set");
				
				saleGridPane.add(curImage, 0, i);
				saleGridPane.add(curNameText, 1, i);
				saleGridPane.add(currentPricesTextArray[i], 2, i);
				saleGridPane.add(purchaseBtnArray[i], 3, i);
				saleGridPane.add(numOnHandTextArray[i], 4, i);
			}

			// set up the left box to show prices
			this.getChildren().addAll(lblPrices, saleGridPane);

			this.setAlignment(Pos.CENTER);

			// spacing
			saleGridPane.setHgap(5);
			saleGridPane.setVgap(5);
			this.setSpacing(5);

		}

		public void setEquipOnHandTextAt(int i, String str)
		{
			this.numOnHandTextArray[i].setText(str);
		}

		public void setBtnSellActionAt(int i, EventHandler<ActionEvent> action)
		{
			this.purchaseBtnArray[i].setOnAction(action);
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
