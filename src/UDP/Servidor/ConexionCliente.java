package UDP.Servidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConexionCliente extends Thread{

    private static final String FILE_DIR = "src/files/";
    private static final String LOG_DIR = "src/UDP/Servidor/logs/";
    private static final int MESSAGE_SIZE = 64000;
    private DatagramPacket peticion;
    private String nombreArchivo;
    private String nombreArchivoLog;

    private final Lock lock = new ReentrantLock();

    public ConexionCliente(DatagramPacket peticion, String nombreArchivo, String nombreArchivoLog){
        this.peticion = peticion;
        this.nombreArchivo = nombreArchivo;
        this.nombreArchivoLog = nombreArchivoLog;
    }

    public void run(){
        try {
            // Se obtiene el primer paquete del cliente
            byte[] idClienteData = peticion.getData();
            // Se obtiene el id del cliente
            int idCliente = ByteBuffer.wrap(idClienteData).getInt();
            // Se escribe en la consola que se recibió el cliente
            System.out.println("Cliente recibido: " + idCliente);
            // Se obtiene la dirección del cliente
            InetAddress ipCliente = peticion.getAddress();
            // Se obtiene el puerto del cliente
            int puertoCliente = peticion.getPort();
            // Se prepara el archivo a enviar
            File file = new File(FILE_DIR + nombreArchivo);
            FileInputStream fis = new FileInputStream(file);
            // Se divide el archivo en partes
            int numChunks = (int) Math.ceil((double) file.length() / MESSAGE_SIZE);
            // Se prepara el número de partes para enviarlo
            byte[] numChunksData = ByteBuffer.allocate(4).putInt(numChunks).array();
            // Se prepara el datagrama para enviar el número de partes
            DatagramPacket numChunksPacket = new DatagramPacket(numChunksData, numChunksData.length, ipCliente, puertoCliente);
            // Se obtiene el socket del servidor
            DatagramSocket socketUDP = new DatagramSocket();
            // Se empieza a contar el tiempo
            Instant start = Instant.now();
            // Se envía el número de partes
            socketUDP.send(numChunksPacket);
            // Se envía el archivo por partes
            byte[] fileBytes = new byte[MESSAGE_SIZE];
            int bytesRead = 0;
            for (int i = 0; i < numChunks; i++) {
                bytesRead = fis.read(fileBytes, 0, MESSAGE_SIZE);
                DatagramPacket sendPacket = new DatagramPacket(fileBytes, bytesRead, peticion.getAddress(), peticion.getPort());
                System.out.println("Conexion " + idCliente + " enviando parte " + (i + 1) + " de " + numChunks);
                socketUDP.send(sendPacket);
            }
            // Se cierra el socket
            socketUDP.close();
            // Se termina de contar el tiempo
            Instant end = Instant.now();
            // Se escribe en el log el tiempo de envío total del archivo
            writeLog("Cliente " + idCliente + "-" + "Tiempo de envío: " + (end.toEpochMilli() - start.toEpochMilli()) + " ms");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void writeLog(String message) throws IOException, IOException {
        lock.lock(); // Adquiere el bloqueo para sincronizar el acceso al archivo
        try (FileWriter logFile = new FileWriter(LOG_DIR + nombreArchivoLog, true)) {
            logFile.write(message + "\n");
        } finally {
            lock.unlock(); // Libera el bloqueo al finalizar la escritura
        }
    }
}
