package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ConexionCliente extends Thread{

    private DatagramPacket peticion;

    public ConexionCliente(DatagramPacket peticion){
        this.peticion = peticion;
    }

    public void run(){
        try {
            // Se obtiene el mensaje del cliente
            String mensaje = new String(peticion.getData());
            System.out.println("Mensaje recibido: " + mensaje);
            // Se obtiene la dirección del cliente
            String ipCliente = peticion.getAddress().getHostAddress();
            int puertoCliente = peticion.getPort();
            // Se prepara el mensaje de respuesta
            String respuesta = "Hola cliente " + ipCliente + ":" + puertoCliente;
            // Se prepara el datagrama para enviar la respuesta
            DatagramPacket paquete = new DatagramPacket(respuesta.getBytes(), respuesta.getBytes().length, peticion.getAddress(), peticion.getPort());
            // Se obtiene el socket del servidor
            DatagramSocket socketUDP = new DatagramSocket();
            // Se envía la respuesta
            socketUDP.send(paquete);
            // Se cierra el socket
            socketUDP.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
