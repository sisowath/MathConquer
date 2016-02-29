package controller;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.Paquet;

public class MathConquerClient_old {
        // attribut(s)
    Socket socket;
    ObjectInputStream objIn;
    ObjectOutputStream objOut;
    JFrame frame;
    JPanel map;
    JButton buttons[];
    String playerName = null;
    Color playerColor = null;
    EtablirConnexion ec;
        // methode(s)
    // constructeur(s)
    public MathConquerClient_old() {
        this.frame = new JFrame("Math & Conquer");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(500, 600);
        this.frame.setVisible(true);
        this.frame.setLocationRelativeTo(null);
        this.map = new JPanel(new GridLayout(5, 5));
        this.buttons = new JButton[25];
        for(int i = 0; i < 25; i++) {
            this.buttons[i] = new JButton("Case " + (i+1));
            this.buttons[i].setActionCommand(String.valueOf(i));
            this.buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int operande1 = (new Random()).nextInt((12-0)+1);
                    int operande2 = (new Random()).nextInt((12-0)+1);
                    double resultat = 0, reponse = 0;                    
                    switch((new Random(4).nextInt())) {
                        case 1 :
                                resultat = operande1 + operande2;
                                reponse = Double.parseDouble(JOptionPane.showInputDialog(null, operande1 + " + " + operande2 + " = ?", "Question", JOptionPane.QUESTION_MESSAGE));
                                break;
                        case 2 :
                                resultat = operande1 + operande2;
                                reponse = Double.parseDouble(JOptionPane.showInputDialog(null, operande1 + " + " + operande2 + " = ?", "Question", JOptionPane.QUESTION_MESSAGE));
                                break;
                        case 3 :
                                resultat = operande1 + operande2;
                                reponse = Double.parseDouble(JOptionPane.showInputDialog(null, operande1 + " + " + operande2 + " = ?", "Question", JOptionPane.QUESTION_MESSAGE));
                                break;
                    }
                    if(resultat == reponse) {
                        try {
                            objOut.writeObject(new Paquet(Integer.parseInt(e.getActionCommand()), playerColor));
                        } catch (IOException ex) {
                            Logger.getLogger(MathConquerClient_old.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Fail !", "Info", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            this.map.add(this.buttons[i]);
        }
        this.frame.add(this.map);
        this.frame.validate();
        this.frame.repaint();
        this.ec = new EtablirConnexion(this);
    }
    // accesseur(s)
    private String getServerAddresse() {
        return JOptionPane.showInputDialog(
                null,
                "Veuillez saisir l'adresse IP du serveur : ",
                "Question",
                JOptionPane.QUESTION_MESSAGE);
    }
    private int getServerPort() {
        return Integer.parseInt(JOptionPane.showInputDialog(
                this.frame,
                "Veuillez saisir le port du serveur : ",
                "Question",
                JOptionPane.QUESTION_MESSAGE));
    }
    // mutateur(s)

    // autre(s)
    public void seConnecter() throws IOException {
        this.socket = new Socket(getServerAddresse(),getServerPort()); 
        System.out.println("Connexion etablie !!");                
        this.ec.start();
        /*
        this.objIn = new ObjectInputStream(this.socket.getInputStream());
        this.objOut = new ObjectOutputStream(this.socket.getOutputStream());
        // valider l'identifiant du joueur aupres du serveur
        while(true) {
            while((this.playerName = JOptionPane.showInputDialog(null, "Veuillez saisir un identifiant : ", "Question", JOptionPane.QUESTION_MESSAGE)) == null || this.playerName.equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(null, "Attention ! Votre identifiant ne peut pas etre vide.");
            }            
            this.objOut.writeObject(this.playerName);
            this.objOut.flush();// INSTRUCTION TRES IMPORTANTE !!!
            String answerFromServer = null;
            try {
                answerFromServer = (String)this.objIn.readObject();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MathConquerClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("answerFromServer : " + answerFromServer);
            if( answerFromServer.equalsIgnoreCase("NAMEACCEPTED")) {
                break;
            }
        }        
        // valider la couleur du joueur aupres du serveur
        while(true) {
            try {
                while((this.playerColor = JColorChooser.showDialog(this.frame, "Choisir une couleur ", Color.WHITE)) != null && this.playerColor == Color.WHITE) {
                    JOptionPane.showMessageDialog(this.frame, "Attention ! Veuillez saisir une autre couleur que le blanc.");
                }
                this.objOut = new ObjectOutputStream(this.socket.getOutputStream());
                this.objOut.writeObject(this.playerColor);
                this.objOut.flush();
                this.objIn = new ObjectInputStream(this.socket.getInputStream());
                String answerFromServer = this.objIn.readObject().toString();
                if( answerFromServer.equalsIgnoreCase("COLORACCEPTED")) {
                    break;
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MathConquerClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        (new EcouteurEnnemi(this)).start();
        */
    }
    public static void main(String argsp[]) {
        try {
            MathConquerClient_old mcc = new MathConquerClient_old();
            mcc.seConnecter();            
        } catch (IOException ex) {
            Logger.getLogger(MathConquerClient_old.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // classe(s) interne(s)
    class EtablirConnexion extends Thread {
        MathConquerClient_old mcc;
        public EtablirConnexion(MathConquerClient_old mcc) {
            this.mcc = mcc;
        }
        @Override
        public void run() {
            try {
                this.mcc.objIn = new ObjectInputStream(this.mcc.socket.getInputStream());
                this.mcc.objOut = new ObjectOutputStream(this.mcc.socket.getOutputStream());
                // valider l'identifiant du joueur aupres du serveur
                while(true) {
                    while((this.mcc.playerName = JOptionPane.showInputDialog(null, "Veuillez saisir un identifiant : ", "Question", JOptionPane.QUESTION_MESSAGE)) == null || this.mcc.playerName.equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(null, "Attention ! Votre identifiant ne peut pas etre vide.");
                    }            
                    this.mcc.objOut.writeObject(this.mcc.playerName);
                    this.mcc.objOut.flush();// INSTRUCTION TRES IMPORTANTE !!!
                    String answerFromServer = null;
                    try {
                        answerFromServer = (String)this.mcc.objIn.readObject();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(MathConquerClient_old.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                    if( answerFromServer.equalsIgnoreCase("NAMEACCEPTED")) {
                        System.out.println("answerFromServer : " + answerFromServer);
                        break;
                    }
                } 
            } catch (IOException ex) {
                Logger.getLogger(MathConquerClient_old.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    class EcouteurEnnemi extends Thread {
        MathConquerClient_old client;
        public EcouteurEnnemi(MathConquerClient_old c) {
            this.client = c;
        }
        public void run() {
            while(true) {
                try {
                    Paquet temp = (Paquet)this.client.objIn.readObject();
                    if( temp != null ){
                        this.client.buttons[temp.getNumeroCase()].setBackground(temp.getCouleurJoueur());
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MathConquerClient_old.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MathConquerClient_old.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}