package model;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Joueur implements Serializable, Comparable{
        // attribut(s)
    private String identifiant;    
    private Color couleur;
    private int deplacement;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
        // methode(s)
    // constructeur(s)
    public Joueur() {
        this.identifiant = "no identifiant";
        this.couleur = Color.RED;        
    }
    // accesseur(s)
    public String getIdentifiant() { return identifiant; }
    public Color getCouleur() {return couleur; }
    public int getDeplacement() {return deplacement; }
    public ObjectOutputStream getOos() { return oos; }
    public ObjectInputStream getOis() { return ois; }
    // mutateur(s)
    public void setIdentifiant(String identifiant) { this.identifiant = identifiant; }
    public void setCouleur(Color couleur) { this.couleur = couleur; }
    public void setDeplacement(int deplacement) { this.deplacement = deplacement; }
    public void setOos(ObjectOutputStream oos) { this.oos = oos; }
    public void setOis(ObjectInputStream ois) { this.ois = ois; }    
    // autre(s)
    @Override
    public int compareTo(Object o) {
        if(o instanceof Joueur) {
            Joueur temp = (Joueur)o;
            if(this.identifiant.equals(temp.getIdentifiant()) && this.couleur == temp.getCouleur())
                return 0;
            else if(this.identifiant.equals(temp.getIdentifiant()))
                return 1;
            else 
                return -1;
        }
        return -1;
    }
    public void readObject() throws IOException, ClassNotFoundException {
        this.identifiant = ois.readUTF();
        this.couleur = (Color) ois.readObject();
        this.deplacement = ois.readInt();
    }
    public void writeObject() throws IOException {
        oos.writeUTF(this.identifiant);
        oos.writeObject(this.couleur);
        oos.writeInt(this.deplacement);
        oos.flush();// source de : http://stackoverflow.com/questions/5658089/java-creating-a-new-objectinputstream-blocks
    }
    public void writeObject(Joueur j) throws IOException {
        j.oos.writeUTF(j.getIdentifiant());
        j.oos.writeObject(j.getCouleur());
        j.oos.writeInt(j.getDeplacement());
        j.oos.flush();
    }
    // readObject() et writeObject() : http://blog.paumard.org/cours/java/chap10-entrees-sorties-serialization.html
}