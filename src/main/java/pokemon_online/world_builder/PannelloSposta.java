package pokemon_online.world_builder;


import java.beans.*;
import java.io.Serializable;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

/**
 * @author Ric
 */
public class PannelloSposta extends JPanel implements Serializable {
    
    public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";
    
    private String sampleProperty;
    
    private PropertyChangeSupport propertySupport;
    
    public PannelloSposta() {
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
        grap.setColor(Color.GRAY);
        grap.fillRect(0,0, 1000, 1000);
            grap.setColor(Color.BLUE);
            grap.fillRect(this.xSpostamento + 24, this.ySpostamento + 24, 48, 48);
            grap.setColor(Color.WHITE);
            grap.drawLine(24, 24, 24, 1000);
            grap.drawLine(24, 24, 1000, 24);
    }

    /**
     * Holds value of property xSpostamento.
     */
    private int xSpostamento = 0;

    /**
     * Getter for property xSpostamento.
     * @return Value of property xSpostamento.
     */
    public int getXSpostamento() {
        return this.xSpostamento;
    }

    /**
     * Setter for property xSpostamento.
     * @param xSpostamento New value of property xSpostamento.
     */
    public void setXSpostamento(int xSpostamento) {
        this.xSpostamento = xSpostamento;
    }

    /**
     * Holds value of property ySpostamento.
     */
    private int ySpostamento = 0;

    /**
     * Getter for property ySpostamento.
     * @return Value of property ySpostamento.
     */
    public int getYSpostamento() {
        return this.ySpostamento;
    }
    
    public void setYSpostamento(int ySpostamento) {
        this.ySpostamento = ySpostamento;
    }
}
