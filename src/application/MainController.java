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

	private Image img;

	private void updateSettings() {
		ImageAdjust ia = new ImageAdjust(saturationSlider.getValue(), contrastSlider.getValue(),
				brightnessSlider.getValue(), shadowsSlider.getValue(), highlightsSlider.getValue(),
				gammaSlider.getValue(), img);
		System.out.println(
				saturationSlider.getValue() + " " + contrastSlider.getValue() + " " + brightnessSlider.getValue() + " "
						+ shadowsSlider.getValue() + " " + highlightsSlider.getValue() + " " + gammaSlider.getValue());
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
		}
	}

	@FXML
	void resetContrastSlider(MouseEvent event) {
		if (event.getClickCount() == 2) {
			contrastSlider.setValue(1.0);
		}
	}

	@FXML
	void resetSatSlider(MouseEvent event) {
		if (event.getClickCount() == 2) {
			saturationSlider.setValue(0);
		}
	}

	@FXML
	void resetShadowsSlider(MouseEvent event) {
		if (event.getClickCount() == 2) {
			shadowsSlider.setValue(0);
		}
	}

	@FXML
	void resetGammaSlider(MouseEvent event) {
		if (event.getClickCount() == 2) {
			gammaSlider.setValue(0.0);
		}
	}

	@FXML
	void resetHighlightsSlider(MouseEvent event) {
		if (event.getClickCount() == 2) {
			highlightsSlider.setValue(0);
		}
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

		saturationSlider.setValue(0.0);
		brightnessSlider.setValue(0.0);
		contrastSlider.setValue(1.0);
		gammaSlider.setValue(0.0);
		highlightsSlider.setValue(0.0);
		shadowsSlider.setValue(0.0);

		saturationSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
			updateSettings();
		});

		brightnessSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
			updateSettings();
		});

		contrastSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
			updateSettings();
		});

		gammaSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
			updateSettings();
		});

		highlightsSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
			updateSettings();
		});

		shadowsSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
			updateSettings();
		});

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