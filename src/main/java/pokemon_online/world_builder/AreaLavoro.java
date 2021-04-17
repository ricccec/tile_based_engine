package pokemon_online.world_builder;
/*
 * AreaLavoro.java
 *
 * Created on 3 aprile 2007, 19.38
 */


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
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
public class AreaLavoro extends JPanel implements Serializable{
    
    
    public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";
    
    private String sampleProperty;
    
    private PropertyChangeSupport propertySupport;
    
    public AreaLavoro() {

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
    
        
    public void paintComponent (Graphics g){
    	
    	super.paintComponent(g);
        
        Rectangle bounds = new Rectangle(-xScroll, -yScroll, Math.max(0, getWidth() + xScroll), Math.max(0, getHeight() + yScroll));
        
        if (land != null) {
        	land.paint((Graphics2D) g, bounds, 2);
        }
        
    }


    private RawLand land = null;
    private Componente componenteInUso = null;
    private int xMouse = 0;
    private int yMouse = 0;
    
    private int yScroll = 0;
    private int xScroll = 0;

    /**
     * Setter for property land.
     * @param land New value of property land.
     */
    public void setLand(RawLand land) {
        this.land = land;
    }

    /**
     * Getter for property componenteInUso.
     * @return Value of property componenteInUso.
     */
    public Componente getComponenteInUso() {
        return this.componenteInUso;
    }

    /**
     * Setter for property componenteInUso.
     * @param componenteInUso New value of property componenteInUso.
     */
    public void setComponenteInUso(Componente componenteInUso) {
        this.componenteInUso = componenteInUso;
    }

    /**
     * Getter for property xMouse.
     * @return Value of property xMouse.
     */
    public int getXMouse() {
        return this.xMouse;
    }

    /**
     * Setter for property xMouse.
     * @param xMouse New value of property xMouse.
     */
    public void setXMouse(int xMouse) {
        this.xMouse = xMouse;
    }


    /**
     * Getter for property yMouse.
     * @return Value of property yMouse.
     */
    public int getYMouse() {
        return this.yMouse;
    }

    /**
     * Setter for property yMouse.
     * @param yMouse New value of property yMouse.
     */
    public void setYMouse(int yMouse) {
        this.yMouse = yMouse;
    }

    /**
     * Getter for property YScroll.
     * @return Value of property YScroll.
     */
    public int getYScroll() {
        return this.yScroll;
    }

    /**
     * Setter for property YScroll.
     * @param YScroll New value of property YScroll.
     */
    public void setYScroll(int yScroll) {
        this.yScroll = yScroll;
    }

    /**
     * Getter for property xScroll.
     * @return Value of property xScroll.
     */
    public int getXScroll() {
        return this.xScroll;
    }

    /**
     * Setter for property xScroll.
     * @param xScroll New value of property xScroll.
     */
    public void setXScroll(int xScroll) {
        this.xScroll = xScroll;
    }

}
