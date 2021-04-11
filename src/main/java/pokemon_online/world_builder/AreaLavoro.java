package pokemon_online.world_builder;
/*
 * AreaLavoro.java
 *
 * Created on 3 aprile 2007, 19.38
 */


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import javax.swing.JPanel;

import pokemon_online.land.Land;


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
        Graphics2D grap = (Graphics2D) g;
        super.paintComponent(g);
        //Disegno il rettangolo grigio di sfondo
        grap.setColor(Color.GRAY);
        grap.fillRect(0, 0 , 1800, 1800);
        if (this.land != null){ 
            //Disegno il rettangolo bianco
            grap.setColor(Color.WHITE);
            grap.fillRect(0 - this.xScroll, 0 - this.yScroll, land.getLarghezza() * 32, land.getAltezza() * 32);
            //Disegno la griglia
            grap.setColor(Color.GRAY);
            for (int c = 0; c < land.getColsCount(); c++){
                for (int r = 0; r < land.getRowsCount(); r++){
                    grap.drawRect(c * 32 - this.xScroll, r * 32 - yScroll, 32, 32);
                }
            }
            //Disegno i componenti
            for (int c = 0; c < land.getColsCount(); c++){//Per ogni colonna
                for (int r = 0; r < land.getRowsCount(); r++){//Per ogni righa
                    if (land.componenti[c][r] != null){
                        grap.drawImage(land.componenti[c][r].immagine, c * 32 - this.xScroll, r * 32 - this.yScroll, this);
                    }
                }
            }
            
            //Disegno il componente in uso alle coordinate del mouse
            if (this.componenteInUso != null){
                grap.drawImage(componenteInUso.immagine, xMouse - 16, yMouse - 16, this);
            }
        }
    }


    private Land land = null;
    private Componente componenteInUso = null;
    private int xMouse = 0;
    private int yMouse = 0;
    
    private int yScroll = 0;
    private int xScroll = 0;

    /**
     * Getter for property land.
     * @return Value of property land.
     */
    public Land getLand() {
        return this.land;
    }

    /**
     * Setter for property land.
     * @param land New value of property land.
     */
    public void setLand(Land land) {
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
