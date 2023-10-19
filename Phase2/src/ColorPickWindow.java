import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ColorPickWindow extends DisplayGraph {

	static ComboBox<Color> colorCombo;

	/**
	* Method that creates the menu where the user can pick a color.
	* @param border the border of the menu
	*/
	public static void createColorMenu(int border)
	{

		Label chooseColor = new Label("Choose a color");

		colorCombo = new ComboBox<>();
    //GETTHECOLORS IS A METHOD THAT RETURNS AN OBSERVABLE LIST OF COLORS

		colorCombo.setItems(getTheColors());
		colorCombo.setPromptText("Colors");

		//TO DISPLAY THE COLORS IN THE CELLS OF THE COMBO BOX - CAN SOMEONE HELP ME TO DISPLAY IT IN THE TOP CELL - AS OF NOW IT WILL ONLY DISPLAY THE (HEX CODE?) OF THE COLOR CHOSEN
		colorCombo.setCellFactory(new Callback<ListView<Color>, ListCell<Color>>(){

			@Override
			public ListCell<Color> call(ListView<Color> param) {

				ListCell<Color> cell = new ListCell<Color>() {

					@Override
					public void updateItem(Color color, boolean empty) {
						super.updateItem(color, empty);
						if(color != null) {
							setBackground(new Background(new BackgroundFill(color,
                                    CornerRadii.EMPTY,
                                    Insets.EMPTY)));
						}else {
							setText(null);
							setBackground(Background.EMPTY);
						}
					}
				};
				return cell;
			}

		});

		colorCombo.buttonCellProperty().bind(Bindings.createObjectBinding(() -> {

			return new ListCell<Color>() {
				@Override
				public void updateItem(Color color, boolean empty) {
					super.updateItem(color, empty);
					if(color != null) {
						setBackground(new Background(new BackgroundFill(color,
                                CornerRadii.EMPTY,
                                Insets.EMPTY)));
					}else {
						setText(null);
						setBackground(Background.EMPTY);
					}
				}
			};
		}, colorCombo.valueProperty()));


		newColorButton = new Button("Add a random new color");
		newColorButton.setOnAction(e -> addAColor());

    //YAN'S COLOR PICKER
		final ColorPicker colorPicker = new ColorPicker();
		Button addColorButton = new Button("Add selected color from the color picker");


		addColorButton.setOnAction(e -> {
			boolean duplicate = true;
			for (int i = 0; i < theColors.size(); i++)
			{
				if (theColors.get(i) == colorPicker.getValue())
				{
					duplicate = false;
				}
			}
			if (duplicate)
			{
				theColors.add(colorPicker.getValue());
			}
			else System.out.println("This color already exists..");
		});


		colorBox = new VBox(25);
		colorBox.getChildren().addAll(chooseColor, colorCombo, newColorButton, colorPicker, addColorButton);
		colorBox.setAlignment(Pos.CENTER);

	}

	//ObservableList
	public static ObservableList<Color> getTheColors(){
		//THE DEFAULT THREE COLORS
		theColors.addAll(Color.BLUE);
		return theColors;
	}

	/**
	* Method to add a color to the list.
	*/
	public static void addAColor() {
		Color randomColor = Color.color(Math.random(),Math.random(),Math.random());
		theColors.add(randomColor);
	}

	/**
	* Method that assigns the color to the vertex.
	*/
	public static void displayColor() {
		if (colorCombo.getValue() != null)
		{
			//CHANGED A BIT - DID NOT UNDERSTAND WHY THE + 1 (IN YANS CODE) IT GAVE ME AN OUT OF BOUNDS
			int chosenColorIndex = colorCombo.getSelectionModel().getSelectedIndex();
			ColorAssignation.colorChosen(chosenColorIndex, selectedCircle, edges);
		}
	}

	/**
	* Method that resets the color of a vertex if the user chooses to do so.
	*/
	public static void resetColor() {
		ColorAssignation.colorChosen(-1, selectedCircle, edges);
	}
}
