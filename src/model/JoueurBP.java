package model;

public class JoueurBP {
        // attribut(s)
    private String alias;
    private String couleur;
    private int deplacement;
        // methode(s)
    // constructeur(s)
    public JoueurBP() {
        this.alias = "no alias";
        this.couleur = "no coleur";
        this.deplacement = 0;
    }
    // accesseur(s)
    public String getAlias() { return alias; }
    public String getCouleur() { return couleur; }
    public int getDeplacement() { return deplacement; }
    // mutateur(s)
    public void setAlias(String alias) { this.alias = alias; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    public void setDeplacement(int deplacement) { this.deplacement = deplacement; }
    // autre(s)
    
}