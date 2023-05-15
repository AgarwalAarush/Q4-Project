import java.net.*;
import java.io.*;

public class ServerThread {

    private ClientManager manager;
    private Socket socket;
    private OjectOutputStream out;
    
    public ServerThread(Socket _socket, ClientManager _manager) {
        this.socket = _socket;
        this.manager = _manager;
        manager.addThread(this);
    }
    }

}