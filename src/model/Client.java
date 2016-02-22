package model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Observable {
        // attribut(s)
    Socket                  clientSocket;
    PrintWriter             os;
    BufferedInputStream     is;
    String                  etat;
    String                  adr;
    int                     numPort;
    int                     numConnexion;
    String                  alias;
    VerifierClient          vc;
    String                  texteFromServer;
        // methode(s)
    // constructeur(s)
    public Client(String adr, String nom, int numPort) {
        this.adr = adr;
        this.alias = nom;
        this.numPort = numPort;
    }
    // accesseur(s)
    public boolean estConnecte() { return (this.clientSocket != null); }
    public String getEtat() { return this.etat; }
    public String getAlias() { return this.alias; }
    public String getAdrServeur() { return this.adr; }
    public int getPortServeur() { return this.numPort; }
    // mutateur(s)
    public void setAlias(String a) { this.alias = a; }
    public void setEtat(String e) { this.etat = e; setChanged(); notifyObservers(); }    
    public void setPortServer(int p) { this.numPort = p; setChanged(); notifyObservers(); }
    public void serAdrServeur(String a) { this.adr = a; setChanged(); notifyObservers(); }
    // autre(s)
    public void envoyer(String message) {
        this.os.print(String.valueOf(this.numConnexion) + "|" + alias + ">>" + message);
        this.os.flush();
    }
    public void lire() {
        try {
            byte buf[] = new byte[500];
            String message;
            this.is.read(buf);
            message = (new String(buf)).trim();
            if( this.getEtat().equals("waiting") ) {
                this.numConnexion = Integer.parseInt(message);
                this.setEtat("connected");
                this.envoyer("Bonjour !");
            } else {
                this.texteFromServer = message;
                setChanged();
                notifyObservers(this.texteFromServer);
            }
            buf = null;
        } catch (IOException ex) {
            System.out.println("Problème avec la méthode lire() de la classe Client");
        }
    }
    public boolean seConnecter() {
        if( !this.estConnecte() ) {
            try {
                this.clientSocket = new Socket(this.adr, this.numPort);
                this.is = new BufferedInputStream( this.clientSocket.getInputStream() );
                this.os = new PrintWriter( this.clientSocket.getOutputStream() );
                this.vc = new VerifierClient(this);
                this.vc.start();
            } catch (IOException ex) {
                System.out.println("\nErreur ! Aucun serveur trouvé...");
            }
            return true;
        } else {
            return false;
        }
    }
    public boolean seDeconnecter() {
        if( !this.estConnecte() ) {
            return true;
        } else {            
            try {
                this.envoyer("STOP");
                this.vc.interrupt();
                this.clientSocket.close();
                this.clientSocket = null;
                return true;
            } catch (IOException ex) {
                return false;
            }            
        }
    }
    // classe(s) interne(s)
    class VerifierClient extends Thread {
            // attribut(s)
        Client ref;
            // methode(s)
        // constructeur(s)
        public VerifierClient(Client c) {
            this.ref = c;
        }
        // accesseur(s)
        
        // mutateur(s)
        
        // autre(s)        
        @Override
        public void run() {
            while( true && !interrupted() ) {
                try {
                    ref.lire();
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}