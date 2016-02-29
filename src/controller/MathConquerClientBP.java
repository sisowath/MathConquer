package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MathConquerClientBP {
        // attribut(s)
    JFrame frame;
    JPanel panel;
    WaitingScreen ws;
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
        frame.setSize(400, 300);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        panel = new JPanel(new GridLayout(5, 5));
        buttons = new JButton[25];
        for(int i=0; i<25; i++) {
            buttons[i] = new JButton("Case " + (i+1));
            buttons[i].setFont(new Font("ARIAL", Font.PLAIN, 12));
            buttons[i].setActionCommand(String.valueOf(i));
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int operande1 = new Random().nextInt(10);
                    int operande2 = new Random().nextInt(10);
                    int resultat = 0;
                    int reponse = 0;
                    switch(new Random().nextInt((3-1)+1) + 1) {
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
                        //JOptionPane.showMessageDialog(null, "BRAVO !", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                        out.println(identifiant);
                        out.flush();
                        out.println(couleur);
                        out.flush();
                        out.println(String.valueOf(e.getActionCommand()));
                        out.flush();
                        System.out.println("Message sent to server : [" + identifiant + " :: " + couleur + " :: " + String.valueOf(e.getActionCommand()) + "].");
                    } else {
                        //JOptionPane.showMessageDialog(null, "ECHEC !", "FAIL", JOptionPane.ERROR_MESSAGE);
                        buttons[Integer.parseInt(e.getActionCommand())].setBackground(Color.GRAY);
                        buttons[Integer.parseInt(e.getActionCommand())].setFont(new Font("ARIAL", Font.CENTER_BASELINE, 14));
                        buttons[Integer.parseInt(e.getActionCommand())].setForeground(Color.RED);
                    }
                }                
            });
            panel.add(buttons[i]);
        }
        panel.setVisible(false);
        ws = new WaitingScreen();
        frame.add(ws, BorderLayout.CENTER);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ws.run();
            }
        });
        frame.revalidate();
        frame.repaint();
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
                out.println(identifiant = JOptionPane.showInputDialog(null, "Veuillez saisir un identifiant : ", "Question", JOptionPane.QUESTION_MESSAGE));
                out.flush();
            } else if(messageFromServer.equalsIgnoreCase("NAMEACCEPTED")) {
                break;
            }
        }
        while(true) {
            String messageFromServer = in.readLine();
            System.out.println("Message from server : " + messageFromServer);
            if(messageFromServer.equalsIgnoreCase("SUBMITCOLOR")) {
                couleur = JOptionPane.showInputDialog(frame, "Veuillez saisir une couleur : ", "Question", JOptionPane.QUESTION_MESSAGE);
                couleur = couleur.toUpperCase();
                out.println(couleur);
                out.flush();
            } else if(messageFromServer.equalsIgnoreCase("COLORACCEPTED")) {
                break;
            }
        }
        frame.remove(ws);
        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(475, 325);
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
                case "BLUE" :
                    buttons[Integer.parseInt(m)].setBackground(Color.BLUE);
                    break;
                case "PINK" :
                    buttons[Integer.parseInt(m)].setBackground(Color.PINK);                    
                    break;
                case "BLACK" :
                    buttons[Integer.parseInt(m)].setBackground(Color.BLACK);
                    break;
            }
            buttons[Integer.parseInt(m)].setFont(new Font("ARIAL", Font.BOLD, 14));
            buttons[Integer.parseInt(m)].setForeground(Color.WHITE);            
        }
    }
    public static void main(String args[]) throws IOException {
        MathConquerClientBP mcc = new MathConquerClientBP();
        mcc.seConnecter();
    }
    // classe(s) interne(s)
    class WaitingScreen extends JPanel implements Runnable {
        Image image;  
        public WaitingScreen() {
            image = Toolkit.getDefaultToolkit().createImage("src/images/gangnamStyleWaiting.gif");
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, this);
            }
        }
        public void run() {
            while(true) {
                try {
                    this.repaint();
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MathConquerClientBP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }        
    }
}