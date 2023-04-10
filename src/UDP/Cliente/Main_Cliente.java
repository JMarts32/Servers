package UDP.Cliente;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main_Cliente {
    private static final String LOG_DIRECTORY = "src/UDP/Cliente/logs/";
    private static final String DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    private static Scanner input;
    private static int numClientes;
    private static String nombreArchivoLog;
    private static FileWriter archivoLog;

    public static void main(String[] args) throws IOException {
        // Pregunta al usuario cuantos clientes quiere crear
        input = new Scanner(System.in);
        System.out.println("¿Cuantos clientes quieres crear?");
        numClientes = input.nextInt();
        /* Se crea un archivo de log para todos los clientes
         * El nombre del archivo de log es la fecha y hora en la que se inicia el servidor
         */

        // Se obtiene la fecha y hora actual
        Date fechaActual = new Date();
        // Se define el formato de la fecha y hora
        SimpleDateFormat formato = new SimpleDateFormat(DATE_FORMAT);
        // Se formatea la fecha y hora según el formato definido
        String fechaFormateada = formato.format(fechaActual);
        // Se utiliza la fecha formateada para nombrar el archivo log
        nombreArchivoLog = fechaFormateada + "-log.txt";
        // Se crea el archivo del log de los clientes
        File archivo = new File(LOG_DIRECTORY + nombreArchivoLog);
        archivo.createNewFile();
        archivoLog = new FileWriter(archivo);
        // Se escribe en el archivo de log el numero de clientes que se van a crear
        archivoLog.write("Numero de clientes: " + numClientes + "\n");
        // Se cierra el archivo de log
        archivoLog.close();
        input.nextLine(); //
        // Crea los clientes
        for (int i = 0; i < numClientes; i++) {
            Cliente cliente = new Cliente(i, numClientes, nombreArchivoLog);
            cliente.start();
        }
    }
}

