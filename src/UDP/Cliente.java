package UDP;

import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class Cliente extends Thread {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 9999;
    private int id;


    public Cliente(int id) {
        this.id = id;
    }

    public void run() {
        try {

            // Se crea el socket UDP
            DatagramSocket socketUDP = new DatagramSocket();
            // Se envia el id del cliente
            byte[] mensaje = ByteBuffer.allocate(4).putInt(id).array(); // Se convierte el id a bytes, se pone 4 porque es el tamaño de un int
            // Se prepara el datagrama para enviar el mensaje
            DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length, InetAddress.getByName(SERVER_IP), SERVER_PORT);
            // Se envía el datagrama
            socketUDP.send(paquete);

            // Se prepara el datagrama para recibir el numero de partes
            byte[] bufer = new byte[4];
            DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
            // Se recibe el numero de partes
            socketUDP.receive(respuesta);
            // Se obtiene el numero de partes
            int numChunks = ByteBuffer.wrap(respuesta.getData()).getInt();
            // Se obtienen las partes
            for (int i = 0; i < numChunks; i++) {
                // Se prepara el datagrama para recibir la parte
                bufer = new byte[65507];
                respuesta = new DatagramPacket(bufer, bufer.length);
                // Se establece un timeout de 5 segundos para que no se quede esperando indefinidamente
                socketUDP.setSoTimeout(5000);
                // Se recibe la parte
                socketUDP.receive(respuesta);
                // Se obtiene la parte
                byte[] chunk = respuesta.getData();
                // TODO: Se escribe la parte en el archivo
                // writeLog(new String(chunk, 0, chunk.length));
            }

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}