package UDP;

import java.io.File;
import java.io.FileWriter;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Servidor {

    private static final int PORT = 9999;
    private static final int BUFFER_SIZE = 64;
    private static final String LOG_DIRECTORY = "logs/";
    private static final String DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    private static Scanner input;
    private static String nombreArchivoLog;
    private static FileWriter archivoLog;

    public static void main(String[] args) {

        try {
            // Se inicializa el servidor UDP en el puerto establecido
            DatagramSocket socketUDP = new DatagramSocket(PORT);
            System.out.println("Servidor UDP iniciado en el puerto " + PORT);
            // Se pregunta al usuario cual archivo quiere enviar
            System.out.println("¿Que archivo quieres enviar?");
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
            // Se pregunta al usuario cual es el tamaño de los mensajes en que se va a dividir el archivo
            System.out.println("¿Cual es el tamaño de los mensajes en que se va a dividir el archivo?");
            int messageSize = input.nextInt();
            // Se verifica que el buffer tenga un tamaño entre 1 y 64000
            if (BUFFER_SIZE < 1 || BUFFER_SIZE > 64000) {
                System.out.println("El tamaño del buffer debe estar entre 1 y 64000");
                return;
            }
            // Se obtiene la fecha y hora actual
            Date fechaActual = new Date();
            // Se define el formato de la fecha y hora
            SimpleDateFormat formato = new SimpleDateFormat(DATE_FORMAT);
            // Se formatea la fecha y hora según el formato definido
            String fechaFormateada = formato.format(fechaActual);
            // Se utiliza la fecha formateada para nombrar el archivo log
            String nombreArchivoLog = fechaFormateada + "-log";
            // Crea el directorio si no existe
            File directory = new File(LOG_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Se crea el archivo del log del servidor
            FileWriter archivoLog = new FileWriter(LOG_DIRECTORY + nombreArchivoLog + ".txt");
            // Se escribe en el archivo de log el nombre del archivo que se va a enviar y el tamaño de los mensajes
            archivoLog.write("Archivo a enviar: " + nombreArchivo + " Tamaño de los mensajes: " + messageSize);
            // Se cierra el archivo de log
            archivoLog.close();

            // Se crea una conexión para cada cliente que se conecte
            while (true) {
                // Se prepara el paquete para recibir los datos
                byte[] bufer = new byte[BUFFER_SIZE];
                // Se crea el datagrama para recibir los datos
                DatagramPacket peticion = new DatagramPacket(bufer, bufer.length);
                // Se recibe la petición del cliente
                socketUDP.receive(peticion);
                // Se procesa la petición del cliente
                ConexionCliente conexion = new ConexionCliente(peticion, nombreArchivo, nombreArchivoLog, messageSize);
                conexion.start();
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

}