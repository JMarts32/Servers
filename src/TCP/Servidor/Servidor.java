package TCP.Servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Servidor {
    private static ServerSocket serverSocket;
    private static int port = 23;
    private static ServerSocket communicationSocket;
    public static void main(String[] args) {
        int threadId = 1;

        // Starts the sockets for conection and comunication
        try{
            serverSocket = new ServerSocket(port);
            communicationSocket = new ServerSocket(8000);
            System.out.println("Servidor TCP iniciado");

            // it opens and selects randomly a file from the folder
            File folder = new File("src/TCP/Servidor/files");
            File[] files = folder.listFiles();
            Random random = new Random();
            int randomIndex = random.nextInt(files.length);

            // Creates the log entry
            Date n = new Date();
            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String logName = form.format(n) + "-log.txt";
            File logFile = new File("src/TCP/Servidor/Logs",logName);
            logFile.createNewFile();


        // Here, the server accepts the clients and start each connection to a delegate
        while (true) {

            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

            ServerThread delegate = new ServerThread(clientSocket, threadId, communicationSocket,randomIndex, files, folder, logFile);
            threadId++;
            delegate.start();

        }

        }catch (Exception e ){e.printStackTrace();}

    }
}
