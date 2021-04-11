/**
 * 
 */
package pokemon_online.land;

import java.io.File;

/**
 * @author Cecchi
 *
 */
public class CroppedImage {
	
	private static final float DEFAULT_SCALE_FACTOR = 1f;
	
	private final File image;
	
	private final int x;
	
	private final int y;

	private final int width;
	
	private final int height;
	
	private final float scaleFactor;
	
	public CroppedImage(File image, int x, int y, int width, int height) {
		this(image, x, y, width, height, DEFAULT_SCALE_FACTOR);
	}
	
	public CroppedImage(File image, int x, int y, int width, int height, float scaleFactor) {
		this.image = image;
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
		this.scaleFactor = scaleFactor;
	}

	public File getTileSet() {
		return image;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float getScaleFactor() {
		return scaleFactor;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + Float.floatToIntBits(scaleFactor);
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + width;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CroppedImage other = (CroppedImage) obj;
		if (height != other.height)
			return false;
		if (Float.floatToIntBits(scaleFactor) != Float.floatToIntBits(other.scaleFactor))
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (width != other.width)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return image + "{" + x + ", " + y + ", " + width + "x" + height + "}x" + scaleFactor;
	}
	
}
