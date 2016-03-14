/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/**
 *
 * @author usager
 */
public class MathConquerServer2 {
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(10000);
        System.out.println("MathConquer is up!");
        try {
            while(true) {
                Game game = new Game();
                Game.Player player1 = game.new Player (listener.accept(), "RED");
                Game.Player player2 = game.new Player (listener.accept(), "YELLOW");
                player1.start();
                player2.start();
            }
        } finally {
            listener.close();
        }
    }
}
class Game {
    private static int CASES = 5;
    private String[] board = new String[CASES];
    private HashSet<PrintWriter> writer = new HashSet<PrintWriter>();
    
    public boolean isWinner() {
        for (String s:board)
        {
            if (s == null)
            {
                return false;
            }            
        }
        return true;
    }
    class Player extends Thread {
        String color;           
        Socket socket;
        BufferedReader input;
        PrintWriter output;

        public Player(Socket socket, String color) {
            this.socket = socket;
            this.color = color;
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                writer.add(output);
                output.println("WELCOME " + color);
                output.println("MESSAGE En attente d'un joueur");
            }
            catch (IOException e)
            {
                System.out.println("Joueur déconnecté: " + e);
            }
        }
        public void run() {
            try {                
                output.println("MESSAGE Partie commencée");
                output.println("READY");
                while (true) {
                    String command = input.readLine();
                    if (command.startsWith("MOVE"))
                    {
                        String c = input.readLine(); 
                        String m = input.readLine();
                        System.out.println("Message received : [" + c + " :: " + m + "].");               
                        if(c == null || m == null) {
                            return;
                        }
                        board[Integer.parseInt(m)] = c;
                        for (PrintWriter p:writer)
                        {
                            p.println("MOVE");
                            p.flush();
                            p.println(c);
                            p.flush();
                            p.println(m);
                            p.flush();
                        }
                        if (isWinner())
                        {
                            int red = 0, yellow = 0;                            
                            for (String s:board)
                            {
                                switch (s)
                                {
                                    case "RED" : red++;
                                                 break;
                                    case "YELLOW" : yellow++;
                                                    break;
                                }
                            }
                            for (PrintWriter p:writer)
                            {
                                p.println("WINNER");
                                p.flush();
                                if (red > yellow)
                                {
                                    p.println("RED");
                                    p.flush();
                                } else if (red == yellow) {
                                    p.println("TIE");
                                    p.flush();
                                } else {
                                    p.println("YELLOW");
                                    p.flush();
                                }
                            }
                        }
                    } else if (command.startsWith("QUIT"))
                    {
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Joueur déconnecté: " + e);
            } finally {
                try {
                    socket.close();
                }  catch (IOException e) {}
            }
        }
    }
}

