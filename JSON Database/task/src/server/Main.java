package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    boolean keepRunning = true;

    public static void main(String[] args) throws IOException {

        final String localIP = "127.0.0.1";
        final int portNr = 31337;

        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        SocketThread socketThread;
        Main main = new Main();
        System.out.println("Server started!");

        ServerSocket serverSocket = new ServerSocket(portNr, 50, InetAddress.getByName(localIP));

        while (main.isKeepRunning()) {
            //try (ServerSocket serverSocket = new ServerSocket(portNr, 50, InetAddress.getByName(localIP))) {
                //System.out.println("Begin of 'TRY' statement");
                socketThread = new SocketThread(serverSocket, main);
                threadPool.execute(socketThread);
                //System.out.println("End of 'TRY' statement");
           // }
        }
        threadPool.shutdown();
    }

    boolean isKeepRunning() {
        return keepRunning;
    }
}
