package application;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageAdjust {
	private double saturation;
	
	private double contrast;

	private double brightness;
	
	private double shadows;
	
	private double highlights;
	
	private Image image;
	
	private int[] bins;
	
	private int total;
	
	public int[] getBins() {
		return bins;
	}

	public int getTotal() {
		return total;
	}
	
	public void setSaturation(double saturation) {
		this.saturation = saturation;
		updateImage();
	}

	public void setContrast(double contrast) {
		this.contrast = contrast;
		updateImage();
	}

	public void setBrightness(double brightness) {
		this.brightness = brightness;
		updateImage();
	}

	public Image getImage() {
		return this.image;
	}
	
	ImageAdjust(double saturation, double contrast, double brightness, double shadows, double highlights, Image image) {
		this.saturation = saturation;
		this.contrast = contrast;
		this.brightness = brightness;
		this.image = image;
		this.shadows = shadows;
		this.highlights = highlights;
		updateImage();
	}
	
	private void updateImage() {
		PixelReader pixelReader = image.getPixelReader();
		
		//do all adjustments here simultaneously
		//saturation: 
		//contrast: coefficient
		//brightness: offset
		//place these in their own methods
		
//		saturation = 0.1; // -.2 to .2
//		brightness = 0.05; //-.3 to .3
//		contrast = 1.3; //.8 to 1.2
		
		WritableImage wImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
		PixelWriter pw = wImage.getPixelWriter();
		bins = new int[16];
		total = 0;
		for (int readY = 0; readY < image.getHeight(); readY++) {
			for (int readX = 0; readX < image.getWidth(); readX++) {
				total++;
				Color color = pixelReader.getColor(readX, readY);
				double r = color.getRed();
				double g = color.getGreen();
				double b = color.getBlue();
				
				double s = saturation;

				// if all channels are similar, make no adjustment
				if (!(Math.abs(r - b) < 0.1 && Math.abs(r - g) < 0.1 && Math.abs(b - g) < 0.1)) {
					if (g < b && g < r) {
						g -= s;
					} else if (b < g && b < r) {
						b -= s;
					} else if (r < g && r < b) {
						r -= s;
					}
				}
				
				//apply brightness and contrast
				r = r * contrast + brightness;
				g = g * contrast + brightness;
				b = b * contrast + brightness;
				
				//apply adjustments to shadows and highlights
				if (((r + b + g) /3) > 0.7) {
					r += highlights;
					g += highlights;
					b += highlights;
				} else if (((r + b + g) /3) < 0.3) {
					r += shadows;
					g += shadows;
					b += shadows;
				}				
				
				//check for clipping - white
				if (r > 1.0) {
					r = 1.0;
				}
				if (g > 1.0) {
					g = 1.0;
				}
				if (b > 1.0) {
					b = 1.0;
				}
				
				//check for clipping - black
				if (r < 0.0) {
					r = 0.0;
				}
				if (g < 0.0) {
					g = 0.0;
				}
				if (b < 0.0) {
					b = 0.0;
				}
				
				Color color2 = Color.color(r, g, b);

				pw.setColor(readX, readY, color2);
				Double brightness = ((r + g + b) / 3) * 16;
				if (brightness < 16) {
					bins[brightness.intValue()]++;
				}
			}
		}
		image = wImage;
		
		//pass histo data to a different object which will be in charge of drawing the histo
		
		//should make a histogram class which extends ImageView and which has a drawHisto method
		
		
	}

}
