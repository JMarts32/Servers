package TCP.Servidor;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{

    private Socket serverSocket;
    private int threadId;
    private ServerSocket communicationSocket;
    private int randomIndex;
    private File[] files;
    private File folder;

    public ServerThread(Socket sc, int id, ServerSocket cms, int index, File[] f, File fol){
        try{
            this.serverSocket = sc;
            this.threadId = id;
            this.communicationSocket = cms;
            this.randomIndex = index;
            this.files = f;
            this.folder = fol;
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void run(){
        
    }

}
