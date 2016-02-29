package controller;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Paquet;

public class MathConquerServer_old {
        // attribut(s)
    ServerSocket serverSocket;
    Socket socket;
    int Port;
    ObjectInputStream objIn[];
    ObjectOutputStream objOut[];
    int compteurJoueurs;
    HashSet<String> playerNames;
    HashSet<Color> playerColors;
    EcouteurConnexion ec;
    EcouteurJoueurs ej;
        // methode(s)
    // constructeur(s)
    public MathConquerServer_old(int port) {
        this.Port = port;
        this.objIn = new ObjectInputStream[2];
        this.objOut = new ObjectOutputStream[2];
        this.compteurJoueurs = 0;        
        this.playerNames = new HashSet<String>();
        this.playerColors = new HashSet<Color>();
        this.ec = new EcouteurConnexion(this);
        this.ej = new EcouteurJoueurs(this);
    }
    // accesseur(s)

    // mutateur(s)

    // autre(s)
    public void AttendreConnexions() {
            try {
                this.serverSocket = new ServerSocket(this.Port);
            } catch (IOException ex) {
                Logger.getLogger(MathConquerServer_old.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Un serveur est a l'ecoute :" + this.serverSocket.getLocalPort());
            try {       
                this.socket = this.serverSocket.accept();                
            } catch (IOException ex) {
                Logger.getLogger(MathConquerServer_old.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.ec.start();
            /*
            this.objIn[this.compteurJoueurs] = new ObjectInputStream( this.socket.getInputStream() );
            this.objOut[this.compteurJoueurs] = new ObjectOutputStream( this.socket.getOutputStream());
            //(new EcouteurConnexions(this)).start();
            do {
            String playerName = null;
            Color playerColor = null;
            // demander l'identifiant du nouveau joueur
            while(true) {
            try {
            playerName = (String)this.objIn[this.compteurJoueurs].readObject();
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(MathConquerServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            synchronized(playerName) {
            if(playerName != null && !this.playerNames.contains(playerName)) {
            this.playerNames.add(playerName);
            System.out.println(playerName + " s'est connecte !");
            this.objOut[this.compteurJoueurs].writeObject("NAMEACCEPTED");
            this.objOut[this.compteurJoueurs].flush();
            break;
            }
            }
            }
            // demander la couleur du nouveau joueur
            while(true) {
            try {
            playerColor = (Color) this.objIn[this.compteurJoueurs].readObject();
            synchronized(playerColor) {
            if(!this.playerColors.contains(playerColor)) {
            this.playerColors.add(playerColor);
            System.out.println(playerName + " utilise la couleur " + playerColor);
            break;
            }
            }
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(MathConquerServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(MathConquerServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            this.compteurJoueurs++;
            System.out.println("En attent d'un autre joueur...");
            } while(this.compteurJoueurs < 1);
            this.eJ.start();
            System.out.println("Une partie vient de demarrer !");
            } catch (IOException ex) {
            Logger.getLogger(MathConquerServer.class.getName()).log(Level.SEVERE, null, ex);
            }*/        
    }
    public static void main(String args[]) {
        int port = 10100;
        MathConquerServer_old mcs = new MathConquerServer_old(port);
        mcs.AttendreConnexions();        
    }
    // classe(s) interne(s)  
    class EcouteurConnexion extends Thread {
        MathConquerServer_old mcs;
        public EcouteurConnexion(MathConquerServer_old mcs) {
            this.mcs = mcs;
        }
        public void run() {
            try {
                this.mcs.objIn[this.mcs.compteurJoueurs] = new ObjectInputStream(this.mcs.socket.getInputStream());
                this.mcs.objOut[this.mcs.compteurJoueurs] = new ObjectOutputStream(this.mcs.socket.getOutputStream());
                String playerName = (String) this.mcs.objIn[this.mcs.compteurJoueurs].readObject();
                synchronized(playerName) {
                    if(playerName != null && !this.mcs.playerNames.contains(playerName)) {
                        this.mcs.playerNames.add(playerName);
                        this.mcs.objOut[this.mcs.compteurJoueurs].writeObject("NAMEACCEPTED");
                        this.mcs.objOut[this.mcs.compteurJoueurs].flush();
                    }
                }
                Color playerColor = (Color)this.mcs.objIn[this.mcs.compteurJoueurs].readObject();
                synchronized(playerColor) {
                    if(playerColor != null && !this.mcs.playerColors.contains(playerColor)) {
                        this.mcs.playerColors.add(playerColor);
                        this.mcs.objOut[this.mcs.compteurJoueurs].writeObject("COLORACCEPTED");
                        this.mcs.objOut[this.mcs.compteurJoueurs].flush();
                    }
                }
                System.out.println(playerName + " utilise la couleur " + playerColor);
            } catch (IOException ex) {
                Logger.getLogger(MathConquerServer_old.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MathConquerServer_old.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
    }
    class EcouteurJoueurs extends Thread  {
        MathConquerServer_old mcs;
        public EcouteurJoueurs(MathConquerServer_old mcs) {
            this.mcs = mcs;
        }
        @Override
        public void run() {            
            while(true) {
                for(int i=0; i < this.mcs.objIn.length; i++) {                                            
                    Paquet paquetADispatch = null;
                    try {
                        paquetADispatch = (Paquet)this.mcs.objIn[i].readObject();
                        if(paquetADispatch != null) {
                            for (int j=0; j < this.mcs.objOut.length; j++) {
                                try {                            
                                    this.mcs.objOut[j].writeObject(paquetADispatch);
                                } catch (IOException ex) {
                                    Logger.getLogger(MathConquerServer_old.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(MathConquerServer_old.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                }        
            }
        }
    }
}