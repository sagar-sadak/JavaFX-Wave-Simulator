// import libraries
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.Random;
import javafx.beans.value.*;

public class WaveControlPane extends Pane {

	// declare private variables
	private WaveDisplayPane wavePane;
	private int width, height;
	private Color color;
	private ColorPicker picker;

	private Button startButton;
	private Button stopButton;
	private Button clearButton;
	private Button surpriseButton;
	
	private Slider speedSlider;
	private Slider widthSlider;
	private Slider heightSlider;
	
	private Label speedLabel;
	private Label widthLabel;
	private Label heightLabel;

	// constructor to create all components, set their handler/listener,
	// and arrange them using layout panes.
	public WaveControlPane(int h, int w, Color initialColor) {
		this.color = initialColor;
		this.width = (int) (h * 0.68);
		this.height = w - 10;

		// creates a pane to display waves with the specified color
		wavePane = new WaveDisplayPane(width, color);
		wavePane.setMinSize(width, height);
		wavePane.setMaxSize(width, height);

		// create a color picker with the specified initial color
		picker = new ColorPicker(color);
		picker.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		// create buttons, adjust their width, add to a VBox
		startButton = new Button("Start");
		startButton.setMaxWidth(Double.MAX_VALUE);
		stopButton = new Button("Stop");
		stopButton.setMaxWidth(Double.MAX_VALUE);
		clearButton = new Button("Clear");
		clearButton.setMaxWidth(Double.MAX_VALUE);
		surpriseButton = new Button("Surprise!");
		surpriseButton.setMaxWidth(Double.MAX_VALUE);

		VBox buttonPane = new VBox(startButton, stopButton, clearButton, surpriseButton, picker);
		
		buttonPane.setPrefSize(100, 100);
		buttonPane.setAlignment(Pos.CENTER);

		// create sliders and labels, and add them to the VBox
		speedLabel = new Label("Speed");
		widthLabel = new Label("Width");
		heightLabel = new Label("Height");
		
		// customize the speedslider
		speedSlider = new Slider(5, 100, 20);
		speedSlider.setOrientation(Orientation.VERTICAL);
		speedSlider.setShowTickMarks(true);
		speedSlider.setShowTickLabels(true);
		speedSlider.setMinorTickCount(5);
		speedSlider.setMajorTickUnit(10);
		
		// customize the widthslider
		widthSlider = new Slider(5, 100, 50);
		widthSlider.setOrientation(Orientation.VERTICAL);
		widthSlider.setShowTickMarks(true);
		widthSlider.setShowTickLabels(true);
		widthSlider.setMinorTickCount(5);
		widthSlider.setMajorTickUnit(10);
		
		// customize the heightslider
		heightSlider = new Slider(5, 100, 100);
		heightSlider.setOrientation(Orientation.VERTICAL);
		heightSlider.setMinorTickCount(5);
		heightSlider.setShowTickMarks(true);
        heightSlider.setShowTickLabels(true);
		heightSlider.setMajorTickUnit(10);

		// add to VBox
		VBox speedSliderPane = new VBox(speedLabel, speedSlider);
		VBox waveLengthSliderPane = new VBox(widthLabel, widthSlider);
		VBox waveAmplitudeSliderPane = new VBox(heightLabel, heightSlider);

		TilePane sliderPane = new TilePane();
		sliderPane.setPrefColumns(3);
		sliderPane.setPadding(new Insets(5, 5, 5, 5));
		sliderPane.setAlignment(Pos.CENTER);
		sliderPane.getChildren().addAll(speedSliderPane, waveLengthSliderPane, waveAmplitudeSliderPane);

		HBox controls = new HBox(buttonPane, sliderPane);
		controls.setAlignment(Pos.CENTER);

		BorderPane controlsAndWaves = new BorderPane();
		controlsAndWaves.setLeft(controls);
		controlsAndWaves.setCenter(wavePane);

		this.getChildren().add(controlsAndWaves);

		// Register buttons, sliders, and color picker to appropriate handlers
		startButton.setOnAction(new ButtonHandler());
		stopButton.setOnAction(new ButtonHandler());
		clearButton.setOnAction(new ButtonHandler());
		surpriseButton.setOnAction(new ButtonHandler());
		
		picker.setOnAction(new ColorHandler());
		
		speedSlider.valueProperty().addListener(new SpeedHandler());
		widthSlider.valueProperty().addListener(new WaveLengthHandler());
		heightSlider.valueProperty().addListener(new WaveAmplitudeHandler());
		
	}

	// Event handler for the 4 buttons
	private class ButtonHandler implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			// check the source and act accordingly
			if (e.getSource() == startButton)
			{
				wavePane.resume();
			}
			else if (e.getSource() == stopButton)
			{
				wavePane.suspend();
			}
			else if (e.getSource() == clearButton)
			{
				wavePane.clearPane();
			}
			else if (e.getSource() == surpriseButton)
			{
				// update variables with random numbers
				wavePane.suspend();
				Random rand = new Random();
				int amp = rand.nextInt(100) + 1;
				int len = rand.nextInt(100) + 1;
				int rate = rand.nextInt(100) + 1;
				Color color = new Color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), 1);
				
				wavePane.setWaveAmplitude(amp);
				wavePane.setWaveLength(len);
				wavePane.setRate(rate);
				wavePane.changeColor(color);
				
				speedSlider.setValue(rate);
				widthSlider.setValue(len);
				heightSlider.setValue(amp);
				picker.setValue(color);
				wavePane.resume();
			}
		}
	}
	
	// Event handler for the color picker
	private class ColorHandler implements EventHandler<ActionEvent>
	{
		public void handle(ActionEvent e)
		{
			// update wave color
			wavePane.changeColor(picker.getValue());
		}
	}
	
	// Event handler for speed slider
	private class SpeedHandler implements ChangeListener<Number>
	{
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
		{
			// set the new rate 
			wavePane.setRate(newValue.intValue());
		}
	}
	
	// Event handler for width slider
	private class WaveLengthHandler implements ChangeListener<Number>
	{
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
		{
			// set new width
			wavePane.suspend();
			wavePane.setWaveLength(newValue.intValue());
		}
	}
	
	// Event handler for height slider
	private class WaveAmplitudeHandler implements ChangeListener<Number>
	{
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
		{
			// set new height
			wavePane.suspend();
			wavePane.setWaveAmplitude(newValue.intValue());
		}
	}
}
