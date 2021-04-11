package pokemon_online.world_builder;
/*
 * PannelloStrumenti.java
 *
 * Created on 11 aprile 2007, 19.04
 */


import java.beans.*;
import java.io.Serializable;
import javax.swing.JPanel;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

/**
 * @author Ric
 */
public class PannelloStrumenti extends JPanel implements Serializable {
    
    public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";
    
    private String sampleProperty;
    
    private PropertyChangeSupport propertySupport;
    
    public PannelloStrumenti() {
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
     * Holds value of property componenti.
     */
    private Componente[] componenti = null;
    
    private int xMouse = 0;
    private int yMouse = 0;
    
    private int xScroll = 0;
    private int yScroll = 0;

    
    private Componente componenteSelezionato = null;
    
    

    /**
     * Getter for property componenti.
     * @return Value of property componenti.
     */
    public Componente[] getComponenti() {
        return this.componenti;
    }

    /**
     * Setter for property componenti.
     * @param componenti New value of property componenti.
     */
    public void setComponenti(Componente[] componenti) {
        this.componenti = componenti;
    }
    
    public void paintComponent (Graphics g){
        Graphics2D grap = (Graphics2D) g;
        super.paintComponent(g);
        if (this.componenti != null){            
            boolean fineArray = false;
            int indexRiga = 0;
            int indexColonna = 0;
            int index = 0;
            while ((index < this.componenti.length)&&(fineArray == false)){
                grap.drawImage(this.componenti[index].immagine, indexColonna * 32, indexRiga * 32 - this.yScroll, this);
                index++;
                if (componenti[index] == null)
                    fineArray = true;
                else{
                    indexColonna++;
                    if (indexColonna == 4){
                        indexColonna = 0;
                        indexRiga++;
                    }
                }
            }
        }
        grap.setColor(Color.YELLOW);
        grap.drawRect(this.xMouse * 32, this.yMouse * 32 - this.yScroll, 31, 31);
        grap.drawRect(this.xMouse * 32 + 1, (this.yMouse * 32 + 1) - this.yScroll, 29, 29);

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
     * Getter for property componenteSelezionato.
     * @return Value of property componenteSelezionato.
     */
    public Componente getComponenteSelezionato() {
        if ((this.xMouse + (this.yMouse * 4)) < this.componenti.length)
            this.componenteSelezionato = componenti[this.xMouse + (this.yMouse * 4)]; 
        else
            this.componenteSelezionato = null;
        return this.componenteSelezionato;
    }

    /**
     * Setter for property componenteSelezionato.
     * @param componenteSelezionato New value of property componenteSelezionato.
     */
    public void setComponenteSelezionato(Componente componenteSelezionato) {
        this.componenteSelezionato = componenteSelezionato;
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

    /**
     * Getter for property yScroll.
     * @return Value of property yScroll.
     */
    public int getYScroll() {
        return this.yScroll;
    }

    /**
     * Setter for property yScroll.
     * @param yScroll New value of property yScroll.
     */
    public void setYScroll(int yScroll) {
        this.yScroll = yScroll;
    }
}

