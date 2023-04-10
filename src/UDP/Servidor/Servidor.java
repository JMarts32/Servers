package UDP.Servidor;

import java.io.FileWriter;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Servidor {

    private static final int PORT = 9999;
    private static final int BUFFER_SIZE = 64000;
    private static final String LOG_DIRECTORY = "src/UDP/Servidor/logs/";
    private static final String DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    private static Scanner input;
    private static FileWriter archivoLog;

    public static void main(String[] args) {

        try {
            // Se inicializa el servidor UDP en el puerto establecido
            DatagramSocket socketUDP = new DatagramSocket(PORT);
            System.out.println("Servidor UDP iniciado en el puerto " + PORT);
            // Se pregunta al usuario cual archivo quiere enviar
            System.out.println("¿Que archivo quieres enviar a los clientes?");
            System.out.println("1. Archivo 100MB");
            System.out.println("2. Archivo 250MB");
            input = new Scanner(System.in);
            int opcion = input.nextInt();
            String nombreArchivo = "";
            switch (opcion) {
                case 1:
                    nombreArchivo = "100MB.txt";
                    break;
                case 2:
                    nombreArchivo = "250MB.txt";
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
            /* Se crea un archivo de log para el servidor
             * El nombre del archivo de log es la fecha y hora en la que se inicia el servidor
             */

            // Se obtiene la fecha y hora actual
            Date fechaActual = new Date();
            // Se define el formato de la fecha y hora
            SimpleDateFormat formato = new SimpleDateFormat(DATE_FORMAT);
            // Se formatea la fecha y hora según el formato definido
            String fechaFormateada = formato.format(fechaActual);
            // Se utiliza la fecha formateada para nombrar el archivo log
            String nombreArchivoLog = fechaFormateada + "-log.txt";
            // Se crea el archivo del log del servidor
            archivoLog = new FileWriter(LOG_DIRECTORY + nombreArchivoLog);
            // Se escribe en el archivo de log el nombre del archivo que se va a enviar y el tamaño de los mensajes
            archivoLog.write("Archivo a enviar: " + nombreArchivo + "\n");
            // Se cierra el archivo de log
            archivoLog.close();

            // Se crea una conexión para cada cliente que se conecte
            while (true) {
                // Se prepara el paquete para recibir los datos
                byte[] bufer = new byte[BUFFER_SIZE];
                // Se crea el datagrama para recibir los datos
                DatagramPacket peticion = new DatagramPacket(bufer, bufer.length);
                // Se establece un timeout de 15 segundos para que no se quede esperando indefinidamente
                socketUDP.setSoTimeout(15000);
                // Se recibe la petición del cliente
                socketUDP.receive(peticion);
                // Se procesa la petición del cliente
                ConexionCliente conexion = new ConexionCliente(peticion, nombreArchivo, nombreArchivoLog);
                conexion.start();
            }

        } catch (SocketTimeoutException e) {
            System.out.println("Se ha agotado el tiempo de espera");
            System.out.println("Cerrando el servidor...");
            System.exit(0);
        } catch (SocketException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}