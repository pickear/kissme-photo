package com.kissme.photo.domain.photo;

/**
 * 
 * @author loudyn
 * 
 */
public class PhotoThumbConf {
	private int width = -1;
	private int height = -1;
	private double rotate = -1.0d;
	private float quality = -1.0f;

	private int cropX = -1;
	private int cropY = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getRotate() {
		return rotate;
	}

	public void setRotate(double rotate) {
		this.rotate = rotate;
	}

	public float getQuality() {
		return quality;
	}

	public void setQuality(float quality) {
		this.quality = quality;
	}

	public int getCropX() {
		return cropX;
	}

	public void setCropX(int cropX) {
		this.cropX = cropX;
	}

	public int getCropY() {
		return cropY;
	}

	public void setCropY(int cropY) {
		this.cropY = cropY;
	}

	public boolean requiredResize() {
		return getWidth() > 0 && getHeight() > 0;
	}

	public boolean requiredRotate() {
		return getRotate() > 0.0d && getRotate() <= 360.0d;
	}

	public boolean requiredQuality() {
		return getQuality() > 0f && getQuality() <= 1.0f;
	}

	public boolean requiredCrop() {
		return getCropX() >= 0 && getCropY() >= 0 && requiredResize();
	}
}
