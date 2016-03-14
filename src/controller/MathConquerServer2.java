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
    private String[] board = new String[25];

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
                output.println("BONJOUR " + color);
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
                while (true) {
                    String n = input.readLine();
                    String c = input.readLine();
                    String move = input.readLine();
                    System.out.println("Message from client : " + c + " :: " + move + "].");                    
                    if(n == null || c == null || move == null) {
                        return;
                    }
                    board[Integer.parseInt(move)] = c;
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

