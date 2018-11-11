package application;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class MainController {

	@FXML
	private AnchorPane ap;

	@FXML
	private ImageView viewer;

	@FXML
	private Button open;

	@FXML
	private Label path;

	@FXML
	private Slider brightnessSlider;

	@FXML
	private Slider contrastSlider;

	@FXML
	private Slider saturationSlider;

	@FXML
	private Button save;

	private double saturationValue;

	private double contrastValue;

	private double brightnessValue;

	public void setSaturationValue(double saturationValue) {
		this.saturationValue = saturationValue;
		updateSettings();
	}

	public void setContrastValue(double contrastValue) {
		this.contrastValue = contrastValue;
		updateSettings();
	}

	public void setBrightnessValue(double brightnessValue) {
		this.brightnessValue = brightnessValue;
		updateSettings();
	}

	private void updateSettings() {
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setContrast(contrastValue);
		colorAdjust.setSaturation(saturationValue);
		colorAdjust.setBrightness(brightnessValue);
		viewer.setEffect(colorAdjust);
	}
	
	@FXML
	void resetBrightnessSlider(MouseEvent event) {
		if (event.getClickCount() == 2) {
			brightnessSlider.setValue(0);
			this.setBrightnessValue(0);
		}
	}

	@FXML
	void resetContrastSlider(MouseEvent event) {
		if (event.getClickCount() == 2) {
			contrastSlider.setValue(0);
			this.setContrastValue(0);
		}
	}

	@FXML
	void resetSatSlider(MouseEvent event) {
		if (event.getClickCount() == 2) {
			saturationSlider.setValue(0);
			this.setSaturationValue(0);
		}
	}

	@FXML
	void saturationChange(MouseEvent event) {
		double saturation = saturationSlider.getValue() / 400;
		this.setSaturationValue(saturation);
	}
	
	@FXML
	void contrastChange(MouseEvent event) {
		double contrast = contrastSlider.getValue() / 800;
		this.setContrastValue(contrast);
	}

	@FXML
	void sliderChange(MouseEvent event) {
		double brightness = brightnessSlider.getValue() / 100;
		this.setBrightnessValue(brightness);
	}

	@FXML
	void openFile(MouseEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open File");
		File img = chooser.showOpenDialog(ap.getScene().getWindow());
		String name = "file:" + img.getAbsolutePath();
		path.setText(name);
		Image image = new Image(name);
		viewer.setImage(image);

		if (image != null) {
			double w = 0;
			double h = 0;

			double ratioX = viewer.getFitWidth() / image.getWidth();
			double ratioY = viewer.getFitHeight() / image.getHeight();

			double reducCoeff = 0;
			if (ratioX >= ratioY) {
				reducCoeff = ratioY;
			} else {
				reducCoeff = ratioX;
			}

			w = image.getWidth() * reducCoeff;
			h = image.getHeight() * reducCoeff;

			viewer.setX((viewer.getFitWidth() - w) / 2);
			viewer.setY((viewer.getFitHeight() - h) / 2);
		}
	}
	
	@FXML
	void saveFile(MouseEvent event) {
		path.setText("clicked on save");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Image");
		File file = fileChooser.showSaveDialog(ap.getScene().getWindow());
		if (file != null) {
			try {
				// ImageIO.write(SwingFXUtils.fromFXImage(viewer.getImage(),
				ImageIO.write(SwingFXUtils.fromFXImage(viewer.getImage(), null), "png", file);
			} catch (IOException ex) {
				System.out.println("nope");
			}
		}
	}
}