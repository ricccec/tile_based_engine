package pokemon_online.world_builder;


import java.io.*;
import java.awt.Image;

public class Componente implements Serializable{
    
    String nome;
    int tipo;
    
    transient Image immagine = null;
    
    String testo; //Utilizzato per i cartelli
    
    //Utilizzate per le porte
    String landArrivo = null;
    int colonnaArrivo = 0;
    int righaArrivo = 0;
    
    int prezzo = 0;//Utilizzato per i check point
   
    public Componente(String nm, int tp) {
        nome = nm;
        tipo = tp;
    }
    
    public Componente(Componente cmp) {
        this.nome = cmp.nome;
        this.tipo = cmp.tipo;
        this.immagine = cmp.immagine;
        this.testo = cmp.testo;
        this.landArrivo = cmp.landArrivo;
        this.colonnaArrivo = cmp.colonnaArrivo;
        this.righaArrivo = cmp.righaArrivo;
        this.prezzo = 0;
    }
}
