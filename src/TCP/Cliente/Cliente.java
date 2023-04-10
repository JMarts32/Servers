package TCP.Cliente;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try{

            Scanner sc = new Scanner(System.in);

            System.out.println("Enter the number of clients: ");
            int numConexiones = sc.nextInt();

            // Creates the log entry
            Date n = new Date();
            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String logName = form.format(n) + "-log.txt";
            File logFile = new File("src/TCP/Cliente/Logs",logName);
            logFile.createNewFile();

            for (int i = 1; i <= numConexiones; i++){
                ClientThread client = new ClientThread(numConexiones,logFile, i);
                client.start();
            }


        }catch (Exception e) {e.printStackTrace();}

    }
}
