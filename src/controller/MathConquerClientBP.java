package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MathConquerClientBP {
        // attribut(s)
    JFrame frame;
    JPanel panel;
    JButton buttons[];
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String identifiant;
    String couleur;
        // methode(s)
    // constructeur(s)
    public MathConquerClientBP() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        panel = new JPanel(new GridLayout(5, 5));
        buttons = new JButton[25];
        for(int i=0; i<25; i++) {
            buttons[i] = new JButton("Case " + (i+1));
            buttons[i].setFont(new Font("ARIAL", Font.PLAIN, 16));
            buttons[i].setActionCommand(String.valueOf(i));
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int operande1 = new Random().nextInt(10);
                    int operande2 = new Random().nextInt(10);
                    int resultat = 0;
                    int reponse = 0;
                    switch(new Random().nextInt(4) + 1) {
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
                        JOptionPane.showMessageDialog(null, "BRAVO !", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                        out.println(identifiant);
                        out.flush();
                        out.println(couleur);
                        out.flush();
                        out.println(String.valueOf(e.getActionCommand()));
                        out.flush();
                        System.out.println("Message sent to server : [" + identifiant + " :: " + couleur + " :: " + String.valueOf(e.getActionCommand()) + "].");
                    } else {
                        JOptionPane.showMessageDialog(null, "ECHEC !", "FAIL", JOptionPane.ERROR_MESSAGE);
                    }
                }                
            });
            panel.add(buttons[i]);
        }
        panel.setVisible(false);
        frame.add(panel, BorderLayout.CENTER);
    }
    // accesseur(s)

    // mutateur(s)

    // autre(s)
    public void seConnecter() throws IOException {
        socket = new Socket("localhost", 10300);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
        while(true) {
            String messageFromServer = in.readLine();
            System.out.println("Message from server : " + messageFromServer);
            if(messageFromServer.equalsIgnoreCase("SUBMITNAME")) {
                out.println(identifiant = JOptionPane.showInputDialog(null, "Veuillez sasir un identifiant : ", "Question", JOptionPane.QUESTION_MESSAGE));
                out.flush();
            } else if(messageFromServer.equalsIgnoreCase("NAMEACCEPTED")) {
                break;
            }
        }
        while(true) {
            String messageFromServer = in.readLine();
            System.out.println("Message from server : " + messageFromServer);
            if(messageFromServer.equalsIgnoreCase("SUBMITCOLOR")) {
                couleur = JOptionPane.showInputDialog(null, "Veuillez saisir une couleur : ", "Question", JOptionPane.QUESTION_MESSAGE);
                couleur = couleur.toUpperCase();
                out.println(couleur);
                out.flush();
            } else if(messageFromServer.equalsIgnoreCase("COLORACCEPTED")) {
                break;
            }
        }
        panel.setVisible(true);
        frame.setTitle("Math & Conquer - " + identifiant);
        frame.revalidate();
        frame.repaint();
        while(true) {
            System.out.println("Waiting for server message...");
            String n = in.readLine();
            String c = in.readLine();
            String m = in.readLine();
            System.out.println("Message received : [" + n + " :: " + c + " :: " + m + "].");
            switch(c) {
                case "RED" :
                    buttons[Integer.parseInt(m)].setBackground(Color.RED);
                    break;
                case "ORANGE" :
                    buttons[Integer.parseInt(m)].setBackground(Color.ORANGE);
                    break;
                case "YELLOW" :
                    buttons[Integer.parseInt(m)].setBackground(Color.YELLOW);
                    break;
                case "GREEN" :
                    buttons[Integer.parseInt(m)].setBackground(Color.GREEN);
                    break;
            }            
        }
    }
    public static void main(String args[]) throws IOException {
        MathConquerClientBP mcc = new MathConquerClientBP();
        mcc.seConnecter();
    }
}