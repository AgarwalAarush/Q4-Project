import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) throws IOException {
        int portNumber = 1024;
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            ClientManager manager = new ClientManager();
            while (true) {
                System.out.println("Waiting for a connection");
                Socket clientSocket = serverSocket.accept();
                Thread newThread = new Thread(new ServerThread(clientSocket, manager));
                manager.add(newThread, new ServerThread(clientSocket, manager));
            }
        }
    }
}
