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
public class PallaNetClient extends JFrame{
  private Socket connessione = null;
  private DataOutputStream out = null;  
  private DataInputStream input = null; 
  public PallaNetClient(){
    super("PallaNet - Client - DX");            // costruttore della super classe
    this.setSize(500,400);                 // grandezza della finestra 
    this.setLocationRelativeTo(null);      // posizione al centro schermo
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);//modalità di chiusura
    connetti();                                  //mi connetto al server
    this.setVisible(true);                       //rendo visibile la finestra
  }//fine costruttore
    
  public void connetti(){
    try {
      //mi connetto al server sul socket(<nomecomputer>,<porta>)
      connessione = new Socket("localhost",6789);
     
      //ottengo lo stream di input dal server e di output verso il server
      out = new DataOutputStream(connessione.getOutputStream());
      input = new DataInputStream(connessione.getInputStream());           
    }catch(Exception e){
      JOptionPane.showMessageDialog(null,e.toString());
      System.exit(-1);
    }
    //inizio l'animazione
    Container contenitore = this.getContentPane();      
    PannelloClient pan = new PannelloClient(this,contenitore.getSize());
    contenitore.add(pan);    //lo aggiungo alla JFrame
  }
    
  public DataInputStream getInput(){
   return input;        
  }
  public DataOutputStream getOutput(){
    return out;     
  } 
}/* --Fine classe PallaNetClient */
