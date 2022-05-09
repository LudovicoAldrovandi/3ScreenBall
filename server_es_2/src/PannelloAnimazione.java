
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aldro
 */
class PannelloAnimazione extends JPanel implements ActionListener {

    private ThreadGestisciSingolaConnessioni clientSX;
    private ThreadGestisciSingolaConnessioni clientDX;
    private Dimension dimensioni;
    private Image bufferVirtuale;           // per il doppio buffering
    private Graphics offScreen;
    private Timer tim = null;
    private int xPallina = 0;               // coordinate iniziali
    private int yPallina = 0;
    private int diametroPallina = 40;
    private int spostamento = 3;
    private int timerDelay = 10;
    private boolean destra, basso, pallinaPresente, arrivoComunicatoDestra, arrivoComunicatoSinistra, uscitaDestra;

    public PannelloAnimazione(ThreadGestisciSingolaConnessioni clientSX, ThreadGestisciSingolaConnessioni clientDX, Dimension dimensioni) {
        super();
        this.clientSX = clientSX;      // riferimento alla finestra corrente
        this.clientDX = clientDX;
        this.setSize(dimensioni);      // setto la grandezza	
        destra = true;                 // direzioni iniziali della pallina
        basso = true;
        pallinaPresente = true;    	   //la pallina parte dal questo schermo
        arrivoComunicatoDestra = false;
        arrivoComunicatoSinistra = false;
        tim = new Timer(timerDelay, this); // instanzio il Timer per il movimento
        tim.start(); 	                  // attivo il Timer
    }

    public void update(Graphics g) {     // aggiorno il pannello
        paint(g);                          // eseguo il disegno in Doppio Beffering
    }

    public void paint(Graphics g) {
        super.paint(g);
        //definisce lo spazio esterno per il doppioBuffering
        bufferVirtuale = createImage(getWidth(), getHeight());
        offScreen = bufferVirtuale.getGraphics();
        Graphics2D screen = (Graphics2D) g;
        //setto il colore dello sfondo
        //offScreen.setColor(new Color(183,255,255));		
        offScreen.setColor(new Color(254, 138, 22));  // setto il colore della palla
        if (pallinaPresente) {                      	// disegno la palla
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
                if (yPallina > (this.getHeight() - diametroPallina)) {// urto inferiore
                    basso = false;             	                  //cambia direzione
                    yPallina -= spostamento;
                } else {
                    yPallina += spostamento;
                }
            } else {
                //si muove verso l'alto			
                if (yPallina <= 0) {                                // urto superiore 
                    basso = true;     	                          //cambia direzione
                    yPallina += spostamento;
                } else {
                    yPallina -= spostamento;
                }
            }
            /*direzione orizzontale*/
            if (destra) {
                if ((!arrivoComunicatoDestra) && (xPallina > (this.getWidth() - diametroPallina))) {
                    /* comunico il verso di direz. verticale e la coordinata verticale
	      *  in cui la pallina si trova attualmente            */

                    try {
                        clientDX.getOutput().writeBoolean(basso);
                        clientDX.getOutput().writeInt(yPallina);
                        arrivoComunicatoDestra = true;
                        uscitaDestra = true;
                    } catch (Exception ecc) {
                        JOptionPane.showMessageDialog(null, ecc.toString());
                        System.exit(-1);
                    }
                } else {
                    xPallina += spostamento;
                    if (xPallina > this.getWidth()) {
                        pallinaPresente = false;
                        arrivoComunicatoDestra = false;
                    }
                }
            } else {                        // la pallina sta andando a sinistra

                if ((!arrivoComunicatoSinistra) && (xPallina <= (0))) {          // fine finestra a sinistra
                    //destra = true;
                    try {
                        clientSX.getOutput().writeBoolean(basso);
                        clientSX.getOutput().writeInt(yPallina);

                        arrivoComunicatoSinistra = true;
                        uscitaDestra = false;
                    } catch (Exception ecc) {
                        JOptionPane.showMessageDialog(null, ecc.toString());
                        System.exit(-1);
                    }

                } else {

                    xPallina -= spostamento;
                    if (xPallina < 0 - diametroPallina) {
                        pallinaPresente = false;
                        arrivoComunicatoSinistra = false;
                    }
                }
            }
        } else {                                 // la pallina non è presente 
            // rimango in attesa del ritorno della pallina nel mio schermo
            if (uscitaDestra == true) {

                boolean direzione = false;
                int y = 0;
                try {
                    direzione = clientDX.getInput().readBoolean();
                    y = clientDX.getInput().readInt(); // attendo che arriva la pallina 
                    basso = direzione;                 // direzione di ingresso  
                    destra = false;                    // si muove verso sinistra
                    yPallina = y;                      // coord. iniziali della pallina
                    xPallina = this.getWidth();
                    pallinaPresente = true;
                } catch (Exception ecc) {
                    JOptionPane.showMessageDialog(null, ecc.toString());
                    System.exit(-1);
                }
            } else {

                boolean direzione = false;
                int y = 0;
                try {
                    direzione = clientSX.getInput().readBoolean();
                    y = clientSX.getInput().readInt(); // attendo che arriva la pallina 
                    basso = direzione;                 // direzione di ingresso  
                    destra = true;                    // si muove verso sinistra
                    yPallina = y;                      // coord. iniziali della pallina
                    xPallina = -diametroPallina;
                    pallinaPresente = true;

                } catch (Exception ecc) {
                    JOptionPane.showMessageDialog(null, ecc.toString());
                    System.exit(-1);
                }
            }

        }
        repaint();
    }
}
