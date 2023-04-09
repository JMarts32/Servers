package TCP.Servidor;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogGenerator {

    /* long transferTime*/
    public static void generateLog(String filename, long fileSize, String clientAdd, boolean success, long transferTime){
        Date n = new Date();
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String logName = form.format(n) + "-log.txt";

        try{
            // Se crea el archivo y se agrega en la carpeta de logs
            File logFile = new File("src/ClientServer/newServer/Logs",logName);
            logFile.createNewFile();

            // Se escriben los logs dentro del archivo
            FileWriter writer = new FileWriter(logFile);
            writer.write("Archivo enviado: " + filename + "\n");
            writer.write("Tamaño de archivo: " + fileSize + " bytes\n");
            writer.write("Dirección IP del cliente: " + clientAdd + "\n");
            writer.write("Éxito de entrega: " + success + "\n");
            writer.write("Tiempo de transferencia: " + transferTime + " ms\n");
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
