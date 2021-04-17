package pokemon_online.world_builder;
/*
 * PannelloStrumenti.java
 *
 * Created on 11 aprile 2007, 19.04
 */

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.StrokeBorder;

import pokemon_online.game.utils.GameUtils;
import pokemon_online.game.utils.GraphicsUtils;
import pokemon_online.land.TileImage;

/**
 * @author Ric
 */
public class PannelloStrumenti extends JPanel implements Serializable {

	private static final int MIN_WIDTH = 128;
	
	private static final int MIN_HEIGHT = 64;
	
	private static final int MAX_WIDTH = 516;
	
	private static final int MAX_HEIGHT = 256;
	
	private static final long serialVersionUID = 580832731138412029L;

	public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";

	private String sampleProperty;


	protected void mouseClicked(MouseEvent e) {
		
	}

	protected void mouseDragged(MouseEvent e) {
		
	}


	private final JScrollBar jScrollH;
	private final JScrollBar jScrollV;


	private Image tileSheet;

	private int xMouse;
	private int yMouse;

	private int xScroll;
	private int yScroll;

	private Componente componenteSelezionato;
	
	public PannelloStrumenti() {
		
		//setSize(3*128, 64);
		//setPreferredSize(new Dimension(3*128, 64));

		
		setLayout(new BorderLayout(8, 8));
		
		JPanel viewport = new JPanel() {

			private static final long serialVersionUID = 2926741864929049618L;

			public void paintComponent(Graphics g) {
				Graphics2D grap = (Graphics2D) g;
				super.paintComponent(g);
				
				if (tileSheet != null) {
					grap.drawImage(tileSheet, 0, yScroll, this);
				}
				

				grap.setColor(Color.RED);
				grap.setStroke(new BasicStroke(2));
				grap.drawRect(xMouse * 32 - jScrollH.getValue(), yMouse * 32 - yScroll, 31, 31);
				//grap.drawRect(xMouse * 32 + 1, (yMouse * 32 + 1) - yScroll, 29, 29);
			}
		};
		viewport.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		add(viewport, BorderLayout.CENTER);
		
		// Scrollbars
		jScrollH = new JScrollBar(javax.swing.JScrollBar.HORIZONTAL);
		jScrollH.setEnabled(false);
		jScrollH.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				repaint();
			}
		});
		add(jScrollH, BorderLayout.SOUTH);
		
		jScrollV = new JScrollBar(javax.swing.JScrollBar.VERTICAL);
		jScrollV.setEnabled(false);
		add(jScrollV, BorderLayout.EAST);

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
		
		safeResize(null, null);
	}
	
	private void safeResize(Integer width, Integer height) {
		int newWidth = Math.max(MIN_WIDTH, Math.min(MAX_WIDTH, ((width == null) ? getWidth() : width)));
		int newHeight = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, ((height == null) ? getHeight() : height)));
		
		setPreferredSize(new Dimension(newWidth, newHeight));
		
		if (tileSheet != null) {
			jScrollH.setEnabled(tileSheet.getWidth(null) > getWidth());
			jScrollV.setEnabled(tileSheet.getHeight(null) > getHeight());
		} else {
			jScrollH.setEnabled(false);
			jScrollV.setEnabled(false);
		}
	}
	
	public void setTilesheet(Image img) {
		tileSheet = img;
		
		// Resize
		safeResize(img.getWidth(null), img.getHeight(null));
		revalidate();
		repaint();
	}

//	public void paintComponent(Graphics g) {
//		Graphics2D grap = (Graphics2D) g;
//		super.paintComponent(g);
//		
//		if (tileSheet != null) {
//			grap.drawImage(tileSheet, 0, yScroll, this);
//		}
//		
////		int currX = 0;
////		int currY = 0;
////		for (Componente comp : componenti) {
////			if (comp.getType() != 0) {
////				continue;
////			}
////			
////			TileImage tileImg = comp.getTile().getImage(0);
////			int compWidth = (int)Math.ceil(tileImg.getWidth()*tileImg.getScaleFactor());
////		}
////		
////			boolean fineArray = false;
////			int indexRiga = 0;
////			int indexColonna = 0;
////			int index = 0;
////			while ((index < this.componenti.length) && (fineArray == false)) {
////				// TODO
//////                grap.drawImage(this.componenti[index].immagine, indexColonna * 32, indexRiga * 32 - this.yScroll, this);
////				index++;
////				if (componenti[index] == null)
////					fineArray = true;
////				else {
////					indexColonna++;
////					if (indexColonna == 4) {
////						indexColonna = 0;
////						indexRiga++;
////					}
////				}
////			}
//		
//		grap.setColor(Color.YELLOW);
//		grap.drawRect(this.xMouse * 32, this.yMouse * 32 - this.yScroll, 31, 31);
//		grap.drawRect(this.xMouse * 32 + 1, (this.yMouse * 32 + 1) - this.yScroll, 29, 29);
//
//	}

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
