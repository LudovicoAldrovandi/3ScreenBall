
import java.io.IOException;
import java.net.ServerSocket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aldro
 */
public class ProvaPallaServer {

    public static void main(String args[]) throws IOException {
        new ThreadConnessioni(2);
    }
}
