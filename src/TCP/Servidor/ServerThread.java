package TCP.Servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.time.Instant;

public class ServerThread extends Thread{

    private Socket clientSocket;
    private int threadId;
    private ServerSocket communicationServerSocket;
    private int randomIndex;
    private File[] files;
    private File folder;
    private File logFile;
    private static Object lock = new Object();

    public ServerThread(Socket sc, int id, ServerSocket cms, int index, File[] f, File fol, File lf){
        try{
            this.clientSocket = sc;
            this.threadId = id;
            this.communicationServerSocket = cms;
            this.randomIndex = index;
            this.files = f;
            this.folder = fol;
            this.logFile = lf;
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void run(){
        try (InputStream is = clientSocket.getInputStream()){
            DataInputStream dis = new DataInputStream(is);

            String filename = files[randomIndex].getName();
            File file = new File(folder + "/" + filename);

            // calculate the hash of the file
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // starts the process of sending the file to the client
            OutputStream os = clientSocket.getOutputStream();
            FileInputStream fis = new FileInputStream(file);

            byte[] buffer = new byte[8192];
            int bytesRead;

            // loads the file in chunks to be sent by the buffer
            Instant start = Instant.now();
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
                digest.update(buffer, 0,bytesRead);
            }
            Instant end = Instant.now();

            // Close streams
            dis.close();
            os.close();
            is.close();
            fis.close();

            Socket communicationSocket;
            PrintWriter out;

            communicationSocket = communicationServerSocket.accept();
            out = new PrintWriter(communicationSocket.getOutputStream());

            // Calculates the final hash and sends it to the client
            byte[] hash = digest.digest();
            String hashCode  = new String(hash);
            out.println(hashCode);

            // Closes the missing streams
            out.close();
            communicationSocket.close();


            // Writes in the log
            synchronized (lock){
                FileWriter writer = new FileWriter(logFile, true);
                writer.write("Archivo enviado: " + filename + "\n");
                writer.write("Tamaño de archivo: " + file.length() + " bytes\n");
                writer.write("Dirección IP del cliente: " + clientSocket.getInetAddress().toString() + "\n");
                writer.write("CLiente: " + threadId + "\n");
                writer.write("Éxito de entrega: " + true + "\n");
                writer.write("Tiempo de transferencia: " + (end.toEpochMilli() - start.toEpochMilli()) + " ms\n");
                writer.write("------------------------------------------\n");
                writer.close();
            }

            clientSocket.close();


        }catch (Exception e) {e.printStackTrace();}
    }

}
