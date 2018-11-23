package application;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class MainController {

	@FXML
	private Canvas histogram;

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
	DoubleProperty sprop = new SimpleDoubleProperty();

	@FXML
	private Button save;

	@FXML
	private Slider highlightsSlider;

	@FXML
	private Slider shadowsSlider;

	@FXML
	private Slider gammaSlider;
	
    @FXML
    private ComboBox<String> presetPicker;

	private double saturationValue;

	private double contrastValue;

	private double brightnessValue;

	private double shadowsValue;

	private double gammaValue;

	private double highlightsValue;

	private Image img;

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

	public void setShadowsValue(double shadowsValue) {
		this.shadowsValue = shadowsValue;
		updateSettings();
	}

	public void setHighlightsValue(double highlightsValue) {
		this.highlightsValue = highlightsValue;
		updateSettings();
	}

	public void setGammaValue(double gammaValue) {
		this.gammaValue = 1 - gammaValue;
		updateSettings();
	}

	public double getShadowsValue() {
		return shadowsValue;
	}

	public double getHighlightsValue() {
		return highlightsValue;
	}

	private void updateSettings() {
		ImageAdjust ia = new ImageAdjust(saturationValue, contrastValue, brightnessValue, shadowsValue, highlightsValue,
				gammaValue, img);
		viewer.setImage(ia.getImage());
		int[] bins = ia.getBins();
		GraphicsContext gc = histogram.getGraphicsContext2D();
		gc.clearRect(0, 0, histogram.getWidth(), histogram.getHeight());
		int total = ia.getTotal();
		for (int i = 0; i < 16; i++) {
			double height = (double) bins[i] / (double) total * 100;
			height *= 5;
			gc.fillRoundRect(i * 10, 100 - height, 10, height, 3, 3);
		}
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
			contrastSlider.setValue(1);
			this.setContrastValue(1);
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
		double saturation = saturationSlider.getValue();
		this.setSaturationValue(saturation);
	}

	@FXML
	void contrastChange(MouseEvent event) {
		double contrast = contrastSlider.getValue();
		this.setContrastValue(contrast);
	}

	@FXML
	void sliderChange(MouseEvent event) {
		double brightness = brightnessSlider.getValue();
		this.setBrightnessValue(brightness);
	}

	@FXML
	void gammaChange(MouseEvent event) {
		this.setGammaValue(gammaSlider.getValue());
	}

	@FXML
	void shadowsChange(MouseEvent event) {
		this.setShadowsValue(shadowsSlider.getValue());
	}

	@FXML
	void resetShadowsSlider(MouseEvent event) {
		if (event.getClickCount() == 2) {
			shadowsSlider.setValue(0);
			this.setShadowsValue(0);
		}
	}

	@FXML
	void resetGammaSlider(MouseEvent event) {
		if (event.getClickCount() == 2) {
			gammaSlider.setValue(1.0);
			this.setGammaValue(1.0);
		}
	}

	@FXML
	void resetHighlightsSlider(MouseEvent event) {
		if (event.getClickCount() == 2) {
			highlightsSlider.setValue(0);
			this.setHighlightsValue(0);
		}
	}

	@FXML
	void highlightsChange(MouseEvent event) {
		this.setHighlightsValue(highlightsSlider.getValue());
	}

	@FXML
	void openFile(MouseEvent event) {
		System.out.println(contrastSlider.getValue());
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open File");
		File file = chooser.showOpenDialog(ap.getScene().getWindow());

		String name = "file:" + file.getAbsolutePath();
		path.setText(name);
		Image image = new Image(name);

		viewer.setImage(image);

		img = image;

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

		saturationValue = 0.0;
		brightnessValue = 0.0;
		contrastValue = 1.0;
		this.setGammaValue(0.0);

		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
		
		presetPicker.getItems().add("Choice 1");		
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