/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aldro
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class PallaNetServer extends JFrame {

    public PallaNetServer(ThreadGestisciSingolaConnessioni c1, ThreadGestisciSingolaConnessioni c2) {
        super("PallaNet - Server");
        this.setSize(500, 400);            //setto la grandezza della finestra
        this.setLocationRelativeTo(null); //setto la posizione al centro dello schermo
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //instanzio un oggetto per attendere la connessione di un client	

        this.setVisible(true);
        //inizio l'animazione su un oggetto di classe PannelloAnimazione
        PannelloAnimazione pan = new PannelloAnimazione(c1, c2, this.getSize());

        //lo aggiungo alla JFrame
        this.add(pan);
    }//fine costruttore

}
/* --Fine classe PallaNetServer */
