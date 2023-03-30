package UDP;

import java.net.*;

public class Servidor {

    private static final int PORT = 9999;
    private static final int BUFFER_SIZE = 1024; //

    public static void main(String[] args) {

        try {
            // Se inicializa el servidor UDP en el puerto establecido
            DatagramSocket socketUDP = new DatagramSocket(PORT);
            System.out.println("Servidor UDP iniciado en el puerto " + PORT);

            while (true) {
                // Se prepara el paquete para recibir los datos
                byte[] bufer = new byte[BUFFER_SIZE];
                // Se crea el datagrama para recibir los datos
                DatagramPacket peticion = new DatagramPacket(bufer, bufer.length);
                // Se recibe la petición del cliente
                socketUDP.receive(peticion);
                // Se procesa la petición del cliente
                ConexionCliente conexion = new ConexionCliente(peticion);
                conexion.start();
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

}