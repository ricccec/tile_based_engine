package pokemon_online.world_builder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import javax.swing.JPanel;

import pokemon_online.ResourcesManager;
import pokemon_online.land.Land;
import pokemon_online.land.Tile;
import pokemon_online.land.TileImage;

/**
 * @author Ric
 */
public class PannelloAnteprimaLand extends JPanel implements Serializable {

	private Land land = null;

	private Boolean griglia = false;

	public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";

	private String sampleProperty;

	private PropertyChangeSupport propertySupport;

	public PannelloAnteprimaLand() {
		propertySupport = new PropertyChangeSupport(this);
	}

	public String getSampleProperty() {
		return sampleProperty;
	}

	public void setSampleProperty(String value) {
		String oldValue = sampleProperty;
		sampleProperty = value;
		propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(listener);
	}

	/**
	 * Getter for property land.
	 * 
	 * @return Value of property land.
	 */
	public Land getLand() {
		return this.land;
	}

	/**
	 * Setter for property land.
	 * 
	 * @param land New value of property land.
	 */
	public void setLand(Land land) {
		if (land != null) {
			Toolkit kit = Toolkit.getDefaultToolkit();
			this.land = new Land("Prova", land.getColsCount(), land.getRowsCount());
			for (int c = 0; c < this.land.getColsCount(); c++) {
				for (int r = 0; r < this.land.getRowsCount(); r++) {
					// TODO
//                    if (land.componenti[c][r] != null){
//                        this.land.componenti[c][r] = new Componente(land.componenti[c][r]);
//                        this.land.componenti[c][r].immagine = this.land.componenti[c][r].immagine.getScaledInstance(16, 16, Image.SCALE_FAST);
//                    }
				}
			}
		} else {
			this.land = null;
		}
	}

	public void paintComponent(Graphics g) {
		Graphics2D grap = (Graphics2D) g;
		super.paintComponent(g);
		grap.setColor(Color.GRAY);
		grap.fillRect(0, 0, 1000, 1000);
		if (this.land != null) {
			// Disegno il rettangolo bianco
			grap.setColor(Color.WHITE);
			grap.fillRect(this.xScroll * -1, this.yScroll * -1, this.land.getColsCount() * 16,
					this.land.getRowsCount() * 16);
			// Disegno i componenti
			for (int c = 0; c < land.getColsCount(); c++) {// Per ogni colonna
				for (int r = 0; r < land.getRowsCount(); r++) {// Per ogni righa
					Tile tile = land.getCellTile(r, c);
					if (tile != null) {
						TileImage tileImg = tile.getImage(0);
						Image img = ResourcesManager.getMgr().getTileImage(tileImg);
						grap.drawImage(img, c * 16 - this.xScroll,
								r * 16 - this.yScroll, this);
						// TODO
//						switch (this.land.componenti[c][r].tipo) {
//						case 0:
//							grap.setColor(Color.GRAY);
//							break;
//						case 1:
//							grap.setColor(Color.BLUE);
//							break;
//						case 2:
//							grap.setColor(Color.RED);
//							break;
//						case 3:
//							grap.setColor(Color.YELLOW);
//							break;
//						case 4:
//							grap.setColor(Color.GREEN);
//							break;
//						case 5:
//							grap.setColor(Color.ORANGE);
//							break;
//						case 6:
//							grap.setColor(Color.PINK);
//							break;
//						}
						if (this.griglia == true) {
							grap.drawRect(c * 16 - this.xScroll, r * 16 - this.yScroll, 15, 15);
							grap.drawRect(c * 16 + 1 - this.xScroll, r * 16 + 1 - this.yScroll, 13, 13);
						}
					}
				}
			}
		}
	}

	/**
	 * Getter for property griglia.
	 * 
	 * @return Value of property griglia.
	 */
	public Boolean getGriglia() {
		return this.griglia;
	}

	/**
	 * Setter for property griglia.
	 * 
	 * @param griglia New value of property griglia.
	 */
	public void setGriglia(Boolean griglia) {
		this.griglia = griglia;
	}

	/**
	 * Holds value of property xScroll.
	 */
	private int xScroll = 0;

	/**
	 * Getter for property xScroll.
	 * 
	 * @return Value of property xScroll.
	 */
	public int getXScroll() {
		return this.xScroll;
	}

	/**
	 * Setter for property xScroll.
	 * 
	 * @param xScroll New value of property xScroll.
	 */
	public void setXScroll(int xScroll) {
		this.xScroll = xScroll;
	}

	/**
	 * Holds value of property yScroll.
	 */
	private int yScroll = 0;

	/**
	 * Getter for property yScroll.
	 * 
	 * @return Value of property yScroll.
	 */
	public int getYScroll() {
		return this.yScroll;
	}

	/**
	 * Setter for property yScroll.
	 * 
	 * @param yScroll New value of property yScroll.
	 */
	public void setYScroll(int yScroll) {
		this.yScroll = yScroll;
	}

}
