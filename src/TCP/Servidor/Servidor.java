package TCP.Servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
        }catch (Exception e ){e.printStackTrace();}

        // it opens and selects randomly a file from the folder
        File folder = new File("src/TCP/Servidor/files");
        File[] files = folder.listFiles();
        Random random = new Random();
        int randomIndex = random.nextInt(files.length);

        // Here, the server accepts the clients and start each connection to a delegate
        while (true) {
            try{
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

                ServerThread delegate = new ServerThread(clientSocket, threadId, communicationSocket,randomIndex, files, folder);
                threadId++;
                delegate.start();

            }catch (Exception e) {e.printStackTrace();}
        }

    }
}
