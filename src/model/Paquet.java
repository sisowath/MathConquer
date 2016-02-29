package model;

import java.awt.Color;
import java.io.Serializable;

public class Paquet implements Serializable {
        // attribut(s)
    private int numeroCase;
    private Color couleurJoueur;
        // methode(s)
    // constructeur(s)
    public Paquet(int n, Color c) {
        this.numeroCase = n;
        this.couleurJoueur = c;
    }
    // accesseur(s)
    public int getNumeroCase() { return this.numeroCase; }
    public Color getCouleurJoueur() { return this.couleurJoueur; }
    // mutateur(s)

    // autre(s)

}