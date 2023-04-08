package TCP.Servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor iniciado");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

                InputStream inputStream = clientSocket.getInputStream();
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));

                OutputStream outputStream = clientSocket.getOutputStream();
                PrintWriter outputWriter = new PrintWriter(outputStream, true);

                String fileName = inputReader.readLine();
                System.out.println("Recibiendo archivo " + fileName);

                File file = new File(fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                fileOutputStream.close();
                System.out.println("Archivo " + fileName + " recibido");
                outputWriter.println("Archivo " + fileName + " recibido correctamente");

                clientSocket.close();
                System.out.println("Cliente desconectado");
            }

        }catch (Exception e ){e.printStackTrace();}

    }
}
