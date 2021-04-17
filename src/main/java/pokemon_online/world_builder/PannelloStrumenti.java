package pokemon_online.world_builder;
/*
 * PannelloStrumenti.java
 *
 * Created on 11 aprile 2007, 19.04
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JPanel;

import pokemon_online.land.TileImage;

/**
 * @author Ric
 */
public class PannelloStrumenti extends JPanel implements Serializable {

	private static final int MIN_WIDTH = 128;
	
	private static final int MIN_HEIGHT = 64;
	
	private static final int MAX_WIDTH = 516;
	
	private static final int MAX_HEIGHT = Integer.MAX_VALUE;
	
	private static final long serialVersionUID = 580832731138412029L;

	public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";

	private String sampleProperty;

	private PropertyChangeSupport propertySupport;

	protected void mouseClicked(MouseEvent e) {
		
	}

	protected void mouseDragged(MouseEvent e) {
		
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

	private Image tileSheet;

	private int xMouse;
	private int yMouse;

	private int xScroll;
	private int yScroll;

	private Componente componenteSelezionato;
	
	public PannelloStrumenti() {
		propertySupport = new PropertyChangeSupport(this);
		
		//setSize(3*128, 64);
		//setPreferredSize(new Dimension(3*128, 64));
		resize(null, null);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PannelloStrumenti.this.mouseClicked(e);
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e){
				PannelloStrumenti.this.mouseDragged(e);
			}
		});
	}
	
	private void resize(Integer width, Integer height) {
		int newWidth = Math.max(MIN_WIDTH, Math.min(MAX_WIDTH, ((width == null) ? getWidth() : width)));
		int newHeight = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, ((height == null) ? getHeight() : height)));
		
		setPreferredSize(new Dimension(newWidth, newHeight));
	}
	
	public void setTilesheet(Image img) {
		tileSheet = img;
		
		// Resize
		int newWidth = Math.min(MAX_WIDTH, img.getWidth(null));
		resize(newWidth, null);
		revalidate();
		repaint();
	}

	public void paintComponent(Graphics g) {
		Graphics2D grap = (Graphics2D) g;
		super.paintComponent(g);
		
		if (tileSheet != null) {
			grap.drawImage(tileSheet, 0, yScroll, this);
		}
		
//		int currX = 0;
//		int currY = 0;
//		for (Componente comp : componenti) {
//			if (comp.getType() != 0) {
//				continue;
//			}
//			
//			TileImage tileImg = comp.getTile().getImage(0);
//			int compWidth = (int)Math.ceil(tileImg.getWidth()*tileImg.getScaleFactor());
//		}
//		
//			boolean fineArray = false;
//			int indexRiga = 0;
//			int indexColonna = 0;
//			int index = 0;
//			while ((index < this.componenti.length) && (fineArray == false)) {
//				// TODO
////                grap.drawImage(this.componenti[index].immagine, indexColonna * 32, indexRiga * 32 - this.yScroll, this);
//				index++;
//				if (componenti[index] == null)
//					fineArray = true;
//				else {
//					indexColonna++;
//					if (indexColonna == 4) {
//						indexColonna = 0;
//						indexRiga++;
//					}
//				}
//			}
		
		grap.setColor(Color.YELLOW);
		grap.drawRect(this.xMouse * 32, this.yMouse * 32 - this.yScroll, 31, 31);
		grap.drawRect(this.xMouse * 32 + 1, (this.yMouse * 32 + 1) - this.yScroll, 29, 29);

	}

	/**
	 * Getter for property xMouse.
	 * 
	 * @return Value of property xMouse.
	 */
	public int getXMouse() {
		return this.xMouse;
	}

	/**
	 * Setter for property xMouse.
	 * 
	 * @param xMouse New value of property xMouse.
	 */
	public void setXMouse(int xMouse) {
		this.xMouse = xMouse;
	}

	/**
	 * Getter for property yMouse.
	 * 
	 * @return Value of property yMouse.
	 */
	public int getYMouse() {
		return this.yMouse;
	}

	/**
	 * Setter for property yMouse.
	 * 
	 * @param yMouse New value of property yMouse.
	 */
	public void setYMouse(int yMouse) {
		this.yMouse = yMouse;
	}

	/**
	 * Getter for property componenteSelezionato.
	 * 
	 * @return Value of property componenteSelezionato.
	 */
	public Componente getComponenteSelezionato() {
//		if ((this.xMouse + (this.yMouse * 4)) < this.componenti.length)
//			this.componenteSelezionato = componenti[this.xMouse + (this.yMouse * 4)];
//		else
//			this.componenteSelezionato = null;
//		return this.componenteSelezionato;
		return null;
	}

	/**
	 * Setter for property componenteSelezionato.
	 * 
	 * @param componenteSelezionato New value of property componenteSelezionato.
	 */
	public void setComponenteSelezionato(Componente componenteSelezionato) {
		this.componenteSelezionato = componenteSelezionato;
	}

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
