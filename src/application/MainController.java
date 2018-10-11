package application;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    private Slider saturationSlider;
	
	@FXML
    void saturationChange(MouseEvent event) {
		double saturation = saturationSlider.getValue() / 100;
		path.setText(Double.toString(saturation));
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setSaturation(saturation);
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