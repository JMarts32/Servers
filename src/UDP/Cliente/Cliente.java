package UDP.Cliente;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cliente extends Thread {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 9999;
    private static final String RECEIVED_FILES_DIR = "src/ArchivosRecibidos/";
    private static final String LOG_DIR = "src/UDP/Cliente/logs/";
    private final Lock lock = new ReentrantLock();
    private int id;
    private int totalConnections;
    private String nombrePrueba;
    private String nombreArchivoLog;
    private Boolean allDatagramsReceived;


    public Cliente(int id, int totalConnections, String nombreArchivoLog) {
        this.id = id;
        this.totalConnections = totalConnections;
        this.nombrePrueba = "Cliente"+id+"-Prueba-"+totalConnections+".txt";
        this.nombreArchivoLog = nombreArchivoLog;
        this.allDatagramsReceived = false;
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

            /* Se prepara el archivo donde se escribiran los datagramas recibidos
            *  Se crea un archivo con el nombre de la prueba que tiene el sgte formato:
            *  [Número cliente]–Prueba- [Cantidad de conexiones].txt
            *  Por ejemplo: Cliente1-Prueba-5.txt
            */
            File file = new File(RECEIVED_FILES_DIR + nombrePrueba);
            // Se crea el archivo
            file.createNewFile();

            // Se prepara el datagrama para recibir el numero de partes
            byte[] bufer = new byte[4];
            DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
            // Se recibe el numero de partes
            socketUDP.receive(respuesta);
            // Se obtiene el numero de partes
            int numChunks = ByteBuffer.wrap(respuesta.getData()).getInt();
            // Se escribe el numero de partes en el archivo log
            writeFile(LOG_DIR, nombreArchivoLog, "El cliente " + id + " espera recibir " + numChunks + " chunks");
            // Se obtienen las partes
            for (int i = 0; i < numChunks; i++) {
                // Se prepara el datagrama para recibir la parte
                bufer = new byte[65507];
                respuesta = new DatagramPacket(bufer, bufer.length);
                // Se establece un timeout de 5 segundos para que no se quede esperando indefinidamente por paquetes
                socketUDP.setSoTimeout(5000);
                // Se recibe la parte
                socketUDP.receive(respuesta);
                // Se obtiene la parte
                byte[] chunk = respuesta.getData();
                // Se obtiene el texto de la parte
                String chunkText = new String(chunk);
                // Se escribe la parte en el archivo
                writeFile(RECEIVED_FILES_DIR, nombrePrueba, chunkText);
            }
            // Si se llega a este punto, se recibieron todas las partes
            allDatagramsReceived = true;
            writeFile(LOG_DIR, nombreArchivoLog, "El cliente " + id + " recibió todos los datagramas");
            // Se imprime que el cliente termino
            writeFile(LOG_DIR, nombreArchivoLog, "Cliente"+id+" Terminado");
        } catch (SocketTimeoutException e) {
            // Imprime que se agoto el tiempo de espera
            System.out.println(("Cliente"+id+": Se agotó el tiempo de espera para el paquete/datagrama. Error: " + e.getMessage()));
        } catch (Exception e) {
            System.out.println(("Cliente"+id+": Error: " + e.getMessage()));
        } finally {
            // Se verifica que el cliente haya recibido todos los datagramas
            if (allDatagramsReceived) {
                writeFile(LOG_DIR, nombreArchivoLog, "El cliente " + id + " recibió todos los datagramas");
            } else {
                writeFile(LOG_DIR, nombreArchivoLog, "El cliente " + id + " no recibió todos los datagramas");
            }
            // Se imprime que el cliente termino
            System.out.println("Cliente"+id+" Terminado");
            this.
            writeFile(LOG_DIR, nombreArchivoLog, "Cliente"+id+" Terminado");
        }
    }

        public void writeFile(String DIR, String nombreArchivo, String message) {
        lock.lock();
        try (FileWriter file = new FileWriter(DIR + nombreArchivo, true)) {
            file.write(message + "\n");
        } catch (IOException e){
            System.out.println("No se pudo escribir en el archivo " + nombreArchivo +". Error: " + e.getMessage());
            System.exit(1);
        } finally {
            lock.unlock(); // Libera el bloqueo al finalizar la escritura
        }
    }

}