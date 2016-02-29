package controller;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.JoueurBP;

public class MathConquerServerBP {
        // attribut(s)
    static HashSet<String> playerNames;
    static HashSet<String> playerColors;
    static HashSet<PrintWriter> writers;
        // methode(s)
    // constructeur(s)
    public MathConquerServerBP() {
        this.playerNames = new HashSet<String>();
        this.playerColors = new HashSet<String>();
        this.writers = new HashSet<PrintWriter>();
    }
    // accesseur(s)

    // mutateur(s)

    // autre(s)
    public static void main(String args[]) throws IOException {
        MathConquerServerBP mcs = new MathConquerServerBP();
        ServerSocket serverSocket = new ServerSocket(10300);
        System.out.println("Le serveur est a l'ecoute sur le port #" + serverSocket.getLocalPort());
        while(true) {
            new EcouteurJoueursBP(serverSocket.accept()).start();
        }
    }
    // classe(s) interne(s)
    static class EcouteurJoueursBP extends Thread {
        JoueurBP joueurBP;
        Socket socket;
        BufferedReader in;
        PrintWriter out;
        String name;
        String color;
        public EcouteurJoueursBP(Socket s) { 
            this.socket = s; 
            this.joueurBP = new JoueurBP();
        }
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
                while(true) {
                    out.println("SUBMITNAME");
                    out.flush();
                    name = in.readLine();
                    if(name == null) {
                        return;
                    }else {
                        synchronized(name) {
                            if(!playerNames.contains(name)) {
                                playerNames.add(name);
                                break;
                            }
                        }
                    }
                }
                out.println("NAMEACCEPTED");
                out.flush();
                while(true) {
                    out.println("SUBMITCOLOR");
                    out.flush();
                    color = in.readLine();
                    if(color == null) {
                        return;                        
                    } else {
                        Set<String> listeCouleursValides = new HashSet<String>(Arrays.asList("RED", "ORANGE", "YELLOW", "GREEN", "BLUE", "PINK", "BLACK"));
                        if(listeCouleursValides.contains(color.toUpperCase())) {
                            synchronized(color) {
                                if(!playerColors.contains(color)) {
                                    playerColors.add(color);
                                    break;
                                }
                            }
                        }
                    }
                }
                out.println("COLORACCEPTED");
                out.flush();
                writers.add(out);
                while(true) {
                    String n = in.readLine();
                    String c = in.readLine();
                    String move = in.readLine();
                    System.out.println("Message from client : " + n + " :: " + c + " :: " + move + "].");
                    if(n == null || c == null || move == null) {
                        return;
                    } else {
                        for(PrintWriter p : writers) {
                            p.println(n);
                            p.flush();
                            p.println(c);
                            p.flush();
                            p.println(move);
                            p.flush();
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(MathConquerServerBP.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    playerNames.remove(name);
                    playerColors.remove(color);
                    writers.remove(out);
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(MathConquerServerBP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}