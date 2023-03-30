package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Cliente extends Thread{

        private static final String SERVER_IP = "192.168.184.133";
        private static final int SERVER_PORT = 9999;
        private int id;


        public Cliente(int id){
            this.id = id;
        }

        public void run(){
            try {

                // Se crea el socket UDP
                DatagramSocket socketUDP = new DatagramSocket();
                // Se prepara el mensaje
                String mensaje = "Hola servidor soy el cliente " + id;
                // Se prepara el datagrama para enviar el mensaje
                DatagramPacket paquete = new DatagramPacket(mensaje.getBytes(), mensaje.getBytes().length, InetAddress.getByName(SERVER_IP), SERVER_PORT);
                // Se envía el datagrama
                socketUDP.send(paquete);
                // Se prepara el datagrama para recibir la respuesta
                byte[] bufer = new byte[1024];
                DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
                // Se recibe la respuesta
                socketUDP.receive(respuesta);
                // Se cierra el socket
                socketUDP.close();
                // Se muestra la respuesta
                System.out.println(new String(respuesta.getData()));
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        }
}
