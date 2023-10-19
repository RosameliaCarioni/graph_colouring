import java.util.Optional;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class Timer extends MainGui {

	static Timeline animation;
	private static int temp;
	private static int initialSeconds;

	private static int hintEnabledTime;

	/**
	* Contructor method for a timer object. Default timer is set to 0 and adds up.
	*/
	public Timer() {
		timeLabel = new Label();
		timeLabel.setFont(new Font("Arial Bold", 50));
		temp = 0;
		initialSeconds = 0;
		hintEnabledTime = 0;
		setLabelTime();

		animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateLabelGlobal()));

		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
	}

	/**
	* Constructor method for a timer object. Timer is set to entered value and counts down.
	* @param seconds the amount of seconds to count down from
	*/
	public Timer(int seconds) {
		timeLabel = new Label();
		timeLabel.setFont(new Font("Arial Bold", 50));

		temp = seconds;
		initialSeconds = seconds;
		hintEnabledTime = 0;
		setLabelTime();

		animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateLabelCountDown()));

		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
	}

	/**
	* Method to update the label for the basic timer.
	*/
	public void updateLabelGlobal() {
		temp++;
		setLabelTime();
		if (temp >= hintEnabledTime && hintEnabledTime != 0) {
			hintButton.setDisable(false);
			hintButton.setText("Hint");
		} else if (temp < hintEnabledTime && hintEnabledTime != 0) {
			hintButton.setText("Hint " + (hintEnabledTime - temp));
		}
	}

	/**
	* Method to update the label for the countdown timer.
	*/
	public void updateLabelCountDown() {
		if (temp > 0) {
			temp--;
			setLabelTime();
			if (temp <= hintEnabledTime && hintEnabledTime != 0) {
				hintButton.setDisable(false);
				hintButton.setText("Hint");
			} else if (temp > hintEnabledTime && hintEnabledTime != 0) {
				hintButton.setText("Hint " + (temp - hintEnabledTime));
			}
		} else if (temp == 0) {
			stopAnimation();
			Platform.runLater(() -> timeUp());
		}
	}

	/**
	* Method that checks the time in gamemode 2 and handles the actions after time is up
	*/
	public void timeUp() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Time is up!");
		alert.setHeaderText("Oh no! Time is up!");
		alert.setContentText("Choose your option");

		ButtonType restartType = new ButtonType("Try again");
		ButtonType homeType = new ButtonType("Main menu");

		alert.getButtonTypes().setAll(restartType, homeType);
		alert.initOwner(window);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == restartType) {
			ResetMethods.resetAllAssignedColors();
			resetAnimation();
		} else if (result.get() == homeType) {
			animation.stop();
			ResetMethods.clearData();
			HomeScreen.run();
		}
	}

	/**
	* Method that sets the cooldown timer for the hints.
	*/
	public static void setHintRestrictionGlobal() {
		hintEnabledTime = temp + 40;
		hintButton.setText("Hint 40");
	}

	/**
	* Method that sets the cooldown timer for the hint in gamemode 2.
	*/
	public static void setHintRestrictionGM2() {
		hintEnabledTime = temp - 40;
		hintButton.setText("Hint 40");
	}

	/**
	* Method that calculates the time the user took to color the graph.
	* @return the amount of seconds
	*/
	public static int getTimeItTook() {
		return Math.abs(initialSeconds - temp);
	}

	/**
	* Method that stops the timer.
	*/
	public static void stopAnimation() {
		animation.stop();
	}

	/**
	* Method that resets the timer.
	*/
	public static void resetAnimation() {
		timeLabel.setText(Integer.toString(initialSeconds));
		hintEnabledTime = 0;
		temp = initialSeconds;
		hintButton.setText("Hint");
		setLabelTime();
		animation.play();
	}

	/**
	* Method to pause the timer.
	*/
	public static void pauseAnimation() {
		animation.pause();
	}

	/**
	Method to resume the timer.
	*/
	public static void resumeAnimation() {
		animation.play();
	}

	/**
	* Method to change the label of the timer.
	*/
	private static void setLabelTime() {
		int minutes = 0;
		int seconds = temp;
		while (seconds - 60 >= 0) {
			seconds = seconds - 60;
			minutes++;
		}
		if (gameModeChosen == 2 && temp <= 10) {
			timeLabel.setTextFill(Color.RED);
		}
		if (gameModeChosen == 2 && temp == 10) {
			ChangeMusic();
		}

		if (gameModeChosen == 2 && temp == 0) {
			ContinueMusic();
		}

		timeLabel.setText(Integer.toString(minutes) + "m " + Integer.toString(seconds) + "s");
	}

	/**
	* Method that plays the warning sound when the timer runs below 10 in gamemode 2.
	*/
	public static boolean warningMusic()
	{
		if (temp <= 10)
		{
			return true;
		}
		else return false;
	}
}
