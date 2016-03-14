package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MathConquerClient2 {
    private JFrame frame = new JFrame("Math Conquer");
    //private JPanel panel;
    //private WaitingScreen ws;
    private JButton buttons[];
    private Socket socket;
    private String couleur;
    private BufferedReader in;
    private PrintWriter out;
    private static int PORT = 10000;
    
    public MathConquerClient2(String serverAddress) throws Exception {
        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridLayout(5, 5));
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
                        out.println(couleur);
                        out.flush();
                        out.println(String.valueOf(e.getActionCommand()));
                        out.flush();
                        System.out.println("Message sent to server : [" + couleur + " :: " + String.valueOf(e.getActionCommand()) + "].");
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
        //ecranAttente();
    }
    
    public void play() throws Exception {
        String c, m;
        try {
            c = in.readLine();
            m = in.readLine();
            System.out.println("Message received : [" + c + " :: " + m + "].");
            switch(c) {
                case "RED" :
                case "ROUGE" :
                    buttons[Integer.parseInt(m)].setBackground(Color.RED);
                    buttons[Integer.parseInt(m)].setEnabled(false);
                    break;
                case "ORANGE" :
                    buttons[Integer.parseInt(m)].setBackground(Color.ORANGE);
                    buttons[Integer.parseInt(m)].setEnabled(false);
                    break;
                case "YELLOW" :
                case "JAUNE" :
                    buttons[Integer.parseInt(m)].setBackground(Color.YELLOW);
                    buttons[Integer.parseInt(m)].setEnabled(false);
                    break;
                case "GREEN" :
                case "VERT" :
                    buttons[Integer.parseInt(m)].setBackground(Color.GREEN);
                    buttons[Integer.parseInt(m)].setEnabled(false);
                    break;
                case "BLUE" :
                case "BLEU" :
                    buttons[Integer.parseInt(m)].setBackground(Color.BLUE);
                    buttons[Integer.parseInt(m)].setEnabled(false);
                    break;
                case "PINK" :
                case "ROSE" :
                    buttons[Integer.parseInt(m)].setBackground(Color.PINK);
                    buttons[Integer.parseInt(m)].setEnabled(false);
                    break;
                case "BLACK" :
                case "NOIR" :
                    buttons[Integer.parseInt(m)].setBackground(Color.BLACK);
                    buttons[Integer.parseInt(m)].setEnabled(false);
                    break;
            }
            buttons[Integer.parseInt(m)].setFont(new Font("ARIAL", Font.BOLD, 14));
            buttons[Integer.parseInt(m)].setForeground(Color.WHITE);
        } finally {
            socket.close();
        }
    }
}