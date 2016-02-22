package model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
        // attribut(s)
    ServerSocket serverSocket;
    int numPort;
    int maxConnexions;
    Socket connexions[];
    int compteurConnexions;
    PrintWriter os[];
    BufferedInputStream is[];
    VerifierConnexion vc;
    VerifierServeur vs;
        // methode(s)
    // constructeur(s)
    public Server(int p) { 
        this.numPort = p; 
        this.maxConnexions = 4;
        this.compteurConnexions = -1;
        this.connexions = new Socket[this.maxConnexions];
        this.os = new PrintWriter[this.maxConnexions];
        this.is = new BufferedInputStream[this.maxConnexions];
    }    
    // accesseur(s)
    public int getPort() { return this.numPort; }
    // mutateur(s)
    public void setPort(int p) { this.numPort = p; }
    // autre(s)
    public void envoyer(String message, int numClient) {
        if( os[numClient] == null ) {
            return;
        } else {
            os[numClient].print(message);
            os[numClient].flush();
        }
    }
    public void seConnecter() {
        try { 
            this.serverSocket = new ServerSocket(this.numPort);
            this.vc = new VerifierConnexion(this);
            vc.start();
            System.out.println( "Serveur " + this.serverSocket + " est a l'ecoute sur le port #" + this.serverSocket.getLocalPort() );
        } catch (IOException ex) {
            System.out.println( "ERREUR ! Un autre serveur ecoute deja sur le port #" + this.serverSocket.getLocalPort() );
        }        
    }
    public void attendreUneConnexion() {
        try {
            this.compteurConnexions++;
            Socket socket = this.serverSocket.accept();
            this.connexions[this.compteurConnexions] = socket;
            this.os[this.compteurConnexions] = new PrintWriter(this.connexions[this.compteurConnexions].getOutputStream() );
            this.is[this.compteurConnexions] = new BufferedInputStream(this.connexions[this.compteurConnexions].getInputStream() );
            if( this.compteurConnexions == 0 ) {
                this.vs = new VerifierServeur(this);
                this.vs.start();
            }
            this.envoyer(String.valueOf(this.compteurConnexions), this.compteurConnexions);
            this.compteurConnexions++;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void lire() {
        try {
            byte buf[] = new byte[500];
            String texte = "";
            int provenance = 0;
            for(int i=0; i <= this.compteurConnexions; i++) {
                if(this.is[i] != null && this.is[i].available() > 0 ) {
                    this.is[i].read(buf);
                    texte = (new String(buf)).trim();
                    provenance = Integer.parseInt(texte.substring(0, texte.indexOf("|") ) );
                    for(int z=0; z <= this.compteurConnexions; z++) {
                        if(z != provenance) {
                            this.envoyer(texte.substring(texte.indexOf("|")+1), z);
                        }
                    }                
                }
                String message = texte.substring(texte.indexOf(">>") + 2);
                if( message.equals("STOP") ) {
                    this.is[provenance].close();
                    this.os[provenance].close();
                    this.connexions[provenance].close();
                    this.is[provenance] = null;
                    this.os[provenance] = null;
                    this.connexions[provenance] = null;
                    System.out.println("Un client s'est deconnecte.");
                }
            }
        } catch (IOException ex) {
            System.out.println("Exception rencontree dans la methode lire() de la classe Server.");
        }
    }
    public static void main(String args[]) {
        Server server = new Server(5555);
        server.seConnecter();
    }
    // classe(s) interne(s)
    class VerifierConnexion extends Thread {
            // attribut(s)
        Server ref;
            // methode(s)
        // constructeur(s)
        public VerifierConnexion(Server s) {
            this.ref = s;
        }
        // accesseur(s)
        
        // mutateur(s)
        
        // autre(s)
        @Override
        public void run() {
            while(true) {
                try {
                    this.ref.attendreUneConnexion();
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    class VerifierServeur extends Thread {
            // attribut(s)
        Server ref;
            // methode(s)
        // constructeur(s)
        public VerifierServeur(Server s) {
            this.ref = s;
        }
        // accesseur(s)
        
        // mutateur(s)
        
        // autre(s)
        @Override
        public void run() {
            while(true) {
                try {
                    this.ref.lire();
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}