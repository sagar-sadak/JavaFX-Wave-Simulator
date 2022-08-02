// import libraries
import javafx.animation.*;
import javafx.scene.shape.Circle;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class WaveDisplayPane extends Pane {

	//	declare private variables
	private Timeline timeline;
	private int time;
	private int waveLength;
	private int waveAmplitude;
	private int paneWidth;
	private Color color;

	// constructor
	public WaveDisplayPane(int x, Color color)
	{
		waveAmplitude = 100;
		waveLength = 50;
		paneWidth = x;
		this.color = color;
		time = 0;
		this.setStyle("-fx-background-color: white; -fx-border-color: black");
		KeyFrame kf = new KeyFrame(Duration.millis(500), new WaveHandler());
		timeline = new Timeline(kf);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setRate(20);
		timeline.play();
	}

	// resume animation
	public void resume()
	{
		timeline.play();
	}
	
	// pause animation
	public void suspend()
	{
		timeline.pause();
		time = 0;
	}
	
	// change color of wave
	public void changeColor(Color color)
	{
		this.color = color;
	}
	
	// clear pane
	public void clearPane()
	{
		this.getChildren().clear();
		this.suspend();
	}
	
	// change wave length
	public void setWaveLength(int length)
	{
		this.waveLength = length;
	}
	
	// change wave amplitude
	public void setWaveAmplitude(int amp)
	{
		this.waveAmplitude = amp;
	}
	
	// change wave drawing speed
	public void setRate(int rate)
	{
		timeline.setRate(rate);
	}

	// defines an event listener to draw a new point
	private class WaveHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			time++;
			int x = (waveLength * time) / 100;
			int y = (int) (waveAmplitude * Math.sin((0.0174533) * time) + 115);

			if (x < paneWidth) {
				Circle dot = new Circle(x, y, 2);
				dot.setFill(color);
				getChildren().add(dot);
			} else suspend();
		}
	}
}
