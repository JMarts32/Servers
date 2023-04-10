package TCP.Cliente;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.time.Instant;

public class ClientThread extends Thread{

    private static String serverAddress = "localhost";
    private static int serverport = 23;
    private int clientId;
    private int numConexiones;
    private File logFile;

    private static Object lock = new Object();

    public ClientThread(int n, File lf, int id){
        this.numConexiones = n;
        this.logFile = lf;
        this.clientId = id;
    }

    @Override
    public void run(){

        try (Socket socket = new Socket(serverAddress,serverport)){

            // Starts the process of recieving the file from the server
            InputStream is = socket.getInputStream();
            String filename = "Cliente" + this.clientId + "-Prueba-" + numConexiones + ".txt";
            FileOutputStream fos = new FileOutputStream("src/TCP/Cliente/files/" + filename);
            byte[] buffer = new byte[8192];
            int bytesRead;

            // calculate the hash of the file
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // loads the file in chunks to be received by the buffer
            Instant start = Instant.now();
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                digest.update(buffer, 0, bytesRead);
            }
            Instant end = Instant.now();

            // Starts the socket where the hash code has been sent by the server
            Socket communicationSocket = new Socket(serverAddress,8000);
            BufferedReader br = new BufferedReader(new InputStreamReader(communicationSocket.getInputStream()));

            // Receives the hash code from the server and closes the streams
            String hashReceived = br.readLine();

            // Now calculates the hash code from the file received
            byte[] hash = digest.digest();
            String hashCode  = new String(hash);
            boolean success;

            //now we verify the integrity of the file
            if(hashReceived.equals(hashCode)){
                success = true;
            }else {
                success = false;
            }

            // Opens the file to verify its length
            File file = new File("src/TCP/Cliente/files/" + filename);

            // Writes in the log
            synchronized (lock){
                FileWriter writer = new FileWriter(logFile, true);
                writer.write("Archivo recibido: " + filename + "\n");
                writer.write("Tamaño de archivo: " + file.length() + " bytes\n");
                writer.write("Cliente: " + clientId + "\n");
                writer.write("Éxito de entrega: " + success+ "\n");
                writer.write("Tiempo de transferencia: " + (end.toEpochMilli() - start.toEpochMilli()) + " ms\n");
                writer.write("------------------------------------------\n");
                writer.flush();
                writer.close();
            }

            // close the streams
            fos.flush();
            fos.close();
            is.close();
            br.close();
            communicationSocket.close();

        }catch (Exception e) {e.printStackTrace();}
    }
}
