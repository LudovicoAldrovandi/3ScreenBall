/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aldro
 */
import java.net.*;
import java.io.*;

public class ThreadGestisciSingolaConnessioni {

    private ThreadConnessioni gestoreChat;
    private DataOutputStream out = null;
    private DataInputStream input = null;
    private Socket client = null;
    private PrintWriter output = null;
    Thread me;

    public ThreadGestisciSingolaConnessioni(ThreadConnessioni gestoreChat, Socket client) {
        this.gestoreChat = gestoreChat;
        this.client = client;
        try {
            this.out = new DataOutputStream(client.getOutputStream());
            this.input = new DataInputStream(client.getInputStream());
        } catch (Exception e) {
            output.println("Errore nella lettura.");
        }
        
       
    }



    public DataInputStream getInput() {
        return input;
    }

    public DataOutputStream getOutput() {
        return out;
    }

}
