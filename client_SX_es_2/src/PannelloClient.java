
import javax.swing.JOptionPane;

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

class PannelloClient extends JPanel implements ActionListener {

    private PallaNetClient finestra;
    private Dimension dimensioni;
    private Image bufferVirtuale;
    private Graphics offScreen;
    private Timer tim = null;
    private int xPallina = 0;
    private int yPallina = 0;
    private int diametroPallina = 40;
    private int spostamento = 3;
    private int timerDelay = 10;
    private boolean destra, basso, pallinaPresente, arrivoComunicato;

    public PannelloClient(PallaNetClient finestra, Dimension dimensioni) {
        super();
        this.finestra = finestra;     // riferimento alla finestra corrente
        this.setSize(dimensioni);     //setto la grandezza 
        destra = false;  //direzioni iniziali della pallina
        //la pallina parte dalla mio schermo
        pallinaPresente = false;
        arrivoComunicato = false;
        xPallina = 500;
        tim = new Timer(timerDelay, this);  //instanzio il Timer
        tim.start();  //attivo il Timer
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        super.paint(g);
        //definisce lo spazio esterno per il doppioBuffering
        bufferVirtuale = createImage(getWidth(), getHeight());
        offScreen = bufferVirtuale.getGraphics();
        Graphics2D screen = (Graphics2D) g;

        //setto il colore dello sfondo
        offScreen.setColor(new Color(183, 255, 255));
        //setto il colore della palla
        offScreen.setColor(new Color(254, 138, 22));
        //disegno la palla
        if (pallinaPresente) {
            offScreen.fillOval(xPallina, yPallina, diametroPallina, diametroPallina);
        }
        //disegno l'immagine modificata "bufferVirtuale" sul Component
        screen.drawImage(bufferVirtuale, 0, 0, this);
        offScreen.dispose();
    }
    //evento generato dal Timer

    public void actionPerformed(ActionEvent e) {
        if (pallinaPresente) {
            /*direzione in verticale*/
            if (basso) {
                //si muove verso il basso           
                if (yPallina > (this.getHeight() - diametroPallina)) {
                    //cambia direzione
                    basso = false;
                    yPallina -= spostamento;
                } else {
                    yPallina += spostamento;
                }
            } else {
                //si muove verso l'alto         
                if (yPallina <= 0) {
                    //cambio direzione
                    basso = true;
                    yPallina += spostamento;
                } else {
                    yPallina -= spostamento;
                }
            }

            /*direzione orizzontale*/
            if (destra) {
                if ((!arrivoComunicato) && (xPallina >= this.getWidth() - diametroPallina )) {
                    /*comunico al client che deve far entrare la pallina da sinistra
          * comunico il verso di direzione verticale e le coordinate
          * verticali in cui la pallina si trova attualmente              */
                    try {
                        finestra.getOutput().writeBoolean(basso);
                        finestra.getOutput().writeInt(yPallina);
                        
                        arrivoComunicato = true;
                    } catch (Exception ecc) {
                        JOptionPane.showMessageDialog(null, ecc.toString());
                        System.exit(-1);
                    }
                } else {
                    //System.out.println(xPallina);
                    xPallina += spostamento;

                    if (xPallina >= this.getWidth()) {
                        pallinaPresente = false;
                        arrivoComunicato = false;
                    }
                }

            } else {

                if (xPallina <= 0) {
                    destra = true;
                    xPallina += spostamento;
                } else {
                    xPallina -= spostamento;
                }
            }
        } else {
            /* rimango in attesa del ritorno della pallina nel mio schermo */
            boolean direzione = false;
            int y = 0;
            try {
                
                direzione = finestra.getInput().readBoolean();
                y = finestra.getInput().readInt();
                basso = direzione;
                destra = false;
                yPallina = y;
                xPallina = 500 ;
                pallinaPresente = true;
            } catch (Exception ecc) {
                JOptionPane.showMessageDialog(null, ecc.toString());
                System.exit(-1);
            }
        }
        repaint();
    }
}
