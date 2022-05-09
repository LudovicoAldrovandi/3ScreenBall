/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aldro
 */
import java.awt.List;
import java.net.*;
import javax.swing.JOptionPane;

public class ThreadConnessioni implements Runnable {

    private int numeroMaxConnessioni;

    private ThreadGestisciSingolaConnessioni[] listaConnessioni;
    Thread me;
    private ServerSocket serverChat;

    public ThreadConnessioni(int numeroMaxConnessioni) {
        this.numeroMaxConnessioni = numeroMaxConnessioni;

        this.listaConnessioni = new ThreadGestisciSingolaConnessioni[this.numeroMaxConnessioni];
        me = new Thread(this);
        me.start();
    }

    public void run() {
        boolean continua = true;
        //instanzio la connessione del server per la chat
        try {

            serverChat = new ServerSocket(6789);
            System.out.println("Server aperto: ");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile instanziare il server");
            continua = false;
        }

        if (continua) {
            //accetto le connessioni chat
            try {
                for (int xx = 0; xx < numeroMaxConnessioni; xx++) {
                    Socket tempo = null;
                    tempo = serverChat.accept();
                    System.out.println("client " + xx + " accettato.");
                    listaConnessioni[xx] = new ThreadGestisciSingolaConnessioni(this, tempo);
                }
                System.out.println("server chiuso.");
                serverChat.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Impossibile instanziare server chat");
            }
        }
        new PallaNetServer(listaConnessioni[0], listaConnessioni[1]);

    }//fine metodo "run"

}//fine classe ThreadGestioneServizioChat

