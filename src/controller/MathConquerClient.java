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
import model.Joueur;

public class MathConquerClient {
        // attribut(s)
    JFrame frame;
    JButton buttons[];
    JPanel map;
    BufferedReader in;
    PrintWriter out;
    Joueur joueur;
    Joueur adversaire;
    Socket socket;
        // methode(s)
    // constructeur(s)
    public MathConquerClient() {
        frame = new JFrame("Math & Conquer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        map = new JPanel(new GridLayout(5, 5));
        buttons = new JButton[25];
        for(int i=0; i < 25; i++) {
            buttons[i] = new JButton("Case " + (i+1));
            buttons[i].setActionCommand(String.valueOf(i));
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int operande1 = (new Random()).nextInt(10);
                    int operande2 = (new Random()).nextInt(10);
                    int resultat = 0;
                    int reponse = 0;
                    switch((new Random()).nextInt(4)) {
                        case 1 :
                            resultat = operande1 + operande2;
                            reponse = Integer.parseInt( JOptionPane.showInputDialog(null, operande1 + " + " + operande2 + " = ?", "Question", JOptionPane.QUESTION_MESSAGE) );
                            break;
                        case 2 :
                            resultat = operande1 - operande2;
                            reponse = Integer.parseInt( JOptionPane.showInputDialog(null, operande1 + " - " + operande2 + " = ?", "Question", JOptionPane.QUESTION_MESSAGE) );
                            break;
                        case 3 :
                            resultat = operande1 * operande2;
                            reponse = Integer.parseInt( JOptionPane.showInputDialog(null, operande1 + " * " + operande2 + " = ?", "Question", JOptionPane.QUESTION_MESSAGE) );
                            break;
                    }
                    if(resultat == reponse) {
                        JOptionPane.showMessageDialog(null, "SUCCES !", "Info", JOptionPane.INFORMATION_MESSAGE);
                        try {
                            joueur.setDeplacement(Integer.parseInt( e.getActionCommand()) );
                            joueur.writeObject();
                        } catch (IOException ex) {
                            Logger.getLogger(MathConquerClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "ECHEC", "Info", JOptionPane.ERROR_MESSAGE);
                    }                    
                }                
            });
            map.add(buttons[i]);
        }
        frame.add(map);        
        map.setVisible(false);
        joueur = new Joueur();
        adversaire = new Joueur();
    }
    // accesseur(s)

    // mutateur(s)

    // autre(s)
    public void run() throws IOException, ClassNotFoundException {
        socket = new Socket("localhost", 10100);
        System.out.println("Connexion etablie.");                               
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));                
        out = new PrintWriter(socket.getOutputStream());
        adversaire.setOis(new ObjectInputStream(socket.getInputStream()));
        joueur.setOos(new ObjectOutputStream(socket.getOutputStream()));
        System.out.println("Les flux sont crees.");
        while(true) {
            System.out.println("En attente d'un message du serveur.");
            String messageFromServer = in.readLine();
            System.out.println("Message received from server : " + messageFromServer);
            if(messageFromServer.equalsIgnoreCase("LOGIN")) {                
                joueur.setIdentifiant(JOptionPane.showInputDialog(null, "Veuillez saisir un identifiant : ", "Question", JOptionPane.QUESTION_MESSAGE));
                joueur.setCouleur(JColorChooser.showDialog(null, "Choisissez une couleur", Color.WHITE));
                joueur.writeObject();                
                System.out.println("Player login sent : " + joueur);
            } else if(messageFromServer.equalsIgnoreCase("SUCCESS")) {
                System.out.println("Inside success .");
                map.setVisible(true);
                frame.setTitle("Math & Conquer - " + joueur.getIdentifiant());
                frame.revalidate();
                frame.repaint();
            } else if(messageFromServer.equalsIgnoreCase("MOVE")) {                
                System.out.println("inside readObject().");
                adversaire.readObject();
                System.out.println("Adversaire move : " + adversaire.getDeplacement());
                buttons[adversaire.getDeplacement()].setBackground(adversaire.getCouleur());
            }
        }
    }
    public static void main(String argsp[]) throws IOException, ClassNotFoundException {
        MathConquerClient mcc = new MathConquerClient();
        mcc.run();
    }
}