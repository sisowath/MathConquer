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
import model.Joueur;

public class MathConquerServer {
        // attribut(s)
    ServerSocket serverSocket;    
    HashSet<ObjectInputStream> ins;
    HashSet<PrintWriter> outs;
    HashSet<Joueur> joueurs; 
        // methode(s)
    // constructeur(s)
    public MathConquerServer() {
        ins = new HashSet<ObjectInputStream>();
        outs = new HashSet<PrintWriter>();
        joueurs = new HashSet<Joueur>();
    }
    // accesseur(s)

    // mutateur(s)

    // autre(s)
    public void attendreConnexion() throws IOException {
        serverSocket = new ServerSocket(10100);
        System.out.println("Le serveur est a l'ecoute sur le port #" + serverSocket.getLocalPort());
        while(true) {
            new  EcouteurJoueurs(serverSocket.accept()).start();        
        }
    }
    public static void main(String args[]) throws IOException {
        MathConquerServer mcs = new MathConquerServer();
        mcs.attendreConnexion();
    }
    // classe(s) interne(s)
    class EcouteurJoueurs extends Thread {
        Socket socket;
        PrintWriter out;
        BufferedReader in;
        Joueur joueur;
        public EcouteurJoueurs(Socket socket) {
            this.socket = socket;
            joueur = new Joueur();
            System.out.println("Un joueur s'est connecte.");
        }
        public void run() {
            try {                
                System.out.println("Inside run()");                
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));                
                out = new PrintWriter(socket.getOutputStream());
                joueur.setOos(new ObjectOutputStream(socket.getOutputStream()));
                joueur.setOis(new ObjectInputStream(socket.getInputStream()));                
                System.out.println("Les flux sont crees.");
                while(true) {
                    out.println("LOGIN");
                    out.flush();
                    System.out.println("Login request sent to client");
                    try {                        
                        joueur.readObject();
                        System.out.println("Player login received from client : " + joueur);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(MathConquerServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(joueur == null) {
                        System.out.println("Inside if().");
                        return;
                    }
                    synchronized(joueur) {
                        System.out.println("Inside syncronized().");
                        if(!joueurs.contains(joueur)) {
                            joueurs.add(joueur);
                            break;
                        }
                    }
                }
                out.println("SUCCESS");   
                out.flush();
                outs.add(out);
                System.out.println("Login success sent to client.");
                while(true) {
                    try {
                        System.out.println("Inside readObject().");
                        joueur.readObject();
                        if(joueur == null) 
                            return;
                        else {
                            System.out.println("Player " + joueur.getIdentifiant() + " conquer cell #" + joueur.getDeplacement());
                            synchronized(joueur) {
                                if(joueurs.contains(joueur)) {
                                    System.out.println("Inside contains()");
                                    joueurs.remove(joueur);
                                    joueurs.add(joueur);
                                }
                            }
                            for(PrintWriter p : outs) {
                                p.println("MOVE");
                                p.flush();
                            }
                            for(Joueur j : joueurs) {
                                //j.writeObject(joueur);
                                j.writeObject();
                            }
                        }
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(MathConquerServer.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                }
            } catch (IOException ex) {
                Logger.getLogger(MathConquerServer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    joueurs.remove(joueur);                    
                    outs.remove(out);                    
                    out.close();
                    socket.close();
                    joueur = null;
                    out = null;
                    socket = null;
                } catch (IOException ex) {
                    Logger.getLogger(MathConquerServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}