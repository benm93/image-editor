package application;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	
	@FXML
    void saturationChange(MouseEvent event) {
		double saturation = saturationSlider.getValue() / 500;
		path.setText(Double.toString(saturation));
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setSaturation(saturation);
		viewer.setEffect(colorAdjust);
	}
	
	@FXML
    void saveFile(MouseEvent event) {
		path.setText("clicked on save");
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");        
        File file = fileChooser.showSaveDialog(ap.getScene().getWindow());
        if (file != null) {
            try {
                //ImageIO.write(SwingFXUtils.fromFXImage(viewer.getImage(),
            	ImageIO.write(SwingFXUtils.fromFXImage(viewer.getImage(),
                        null), "png", file);
            } catch (IOException ex) {
                System.out.println("nope");
            }
        }
    }
	
	@FXML
    void contrastChange(MouseEvent event) {
		double contrast = contrastSlider.getValue() / 2000;
		path.setText(Double.toString(contrast));
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setContrast(contrast);
		viewer.setEffect(colorAdjust);
	}
	
	@FXML
    void sliderChange(MouseEvent event) {
		double brightness = brightnessSlider.getValue() / 100;		
		path.setText(Double.toString(brightness));
		//Image image = viewer.getImage();
		//PixelReader pixelReader = image.getPixelReader();
		ColorAdjust colorAdjust = new ColorAdjust();
		//colorAdjust.setContrast(0.1);
		//colorAdjust.setHue(-0.05);
		colorAdjust.setBrightness(brightness);
		//colorAdjust.setSaturation(0.2);
		viewer.setEffect(colorAdjust);
		/*
		for (int readY = 0; readY < image.getHeight(); readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {
                Color color = pixelReader.getColor(readX, readY);
                System.out.println("\nPixel color at coordinates ("
                        + readX + "," + readY + ") "
                        + color.toString());
                System.out.println("R = " + color.getRed());
                System.out.println("G = " + color.getGreen());
                System.out.println("B = " + color.getBlue());
                System.out.println("Opacity = " + color.getOpacity());
                System.out.println("Saturation = " + color.getSaturation());
            }
        }
        */
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
    }

}