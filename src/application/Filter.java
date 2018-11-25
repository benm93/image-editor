package application;

public class Filter {
	
	private String name;
	private double contrast;
	private double brightness;
	private double saturation;
	private double shadows;
	private double highlights;
	private double gamma;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getContrast() {
		return contrast;
	}

	public void setContrast(double contrast) {
		this.contrast = contrast;
	}

	public double getBrightness() {
		return brightness;
	}

	public void setBrightness(double brightness) {
		this.brightness = brightness;
	}

	public double getSaturation() {
		return saturation;
	}

	public void setSaturation(double saturation) {
		this.saturation = saturation;
	}

	public double getShadows() {
		return shadows;
	}

	public void setShadows(double shadows) {
		this.shadows = shadows;
	}

	public double getHighlights() {
		return highlights;
	}

	public void setHighlights(double highlights) {
		this.highlights = highlights;
	}

	public double getGamma() {
		return gamma;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}
	
	Filter(String name, double contrast, double brightness, double saturation, double shadows, double highlights, double gamma) {
		this.name = name;
		this.contrast = contrast;
		this.brightness = brightness;
		this.saturation = saturation;
		this.shadows = shadows;
		this.highlights = highlights;
		this.gamma = gamma;		
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	
}
