package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import model.JoueurBP;

public class MathConquerServerBP {
        // attribut(s)
    static HashSet<String> playerNames;
    static HashSet<String> playerColors;
    static HashSet<PrintWriter> writers;
    ServerSocket serverSocket;
    int numeroPort;
    JLabel lblInfo;
        // methode(s)
    // constructeur(s)
    public MathConquerServerBP(int portNumber) throws IOException {        
        this.numeroPort = portNumber;
        this.playerNames = new HashSet<String>();
        this.playerColors = new HashSet<String>();
        this.writers = new HashSet<PrintWriter>();
        JFrame frame = new JFrame("Math & Conquer - Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 330);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        ImageIcon icon = new ImageIcon("src/images/server-icon.png");
        JLabel thumb = new JLabel();
        thumb.setIcon(icon);
        panel.add(thumb);        
        lblInfo = new JLabel("Le serveur dort...", SwingConstants.CENTER);
        lblInfo.setFont(new Font("ARIAL", Font.BOLD, 13));      
        lblInfo.setForeground(Color.ORANGE);
        JMenuBar mBar = new JMenuBar();
        JMenu mGestion = new JMenu("Gestion");
        JMenuItem miDemarrer = new JMenuItem("Démarrer");
        miDemarrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                try {
                    serverSocket = new ServerSocket(numeroPort);                    
                    lblInfo.setText("Le serveur est a l'ecoute sur le port #" + serverSocket.getLocalPort());
                    System.out.println("Le serveur est a l'ecoute sur le port #" + serverSocket.getLocalPort());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(true) {
                                try {
                                    new EcouteurJoueursBP(serverSocket.accept()).start();
                                } catch (IOException ex) {
                                    Logger.getLogger(MathConquerServerBP.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }                        
                    }).start();
                } catch (IOException ex) {
                    Logger.getLogger(MathConquerServerBP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
        });
        JMenuItem miFermer = new JMenuItem("Fermer");
        miFermer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }            
        });
        mGestion.add(miDemarrer);
        mGestion.add(miFermer);
        mBar.add(mGestion);
        frame.add(lblInfo, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.setJMenuBar(mBar);
        frame.revalidate();
        frame.repaint();
    }
    // accesseur(s)

    // mutateur(s)

    // autre(s)
    public static void main(String args[]) throws IOException {
        MathConquerServerBP mcs = new MathConquerServerBP(10300);                
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