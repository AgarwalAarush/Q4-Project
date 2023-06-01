import java.net.*;
import java.io.*;

public class ServerThread implements Runnable {

    private ClientManager manager;
    private Socket socket;
    private ObjectOutputStream out;
    
    public ServerThread(Socket _socket, ClientManager _manager) {
        this.socket = _socket;
        this.manager = _manager;
        manager.addThread(this);
    }
    
    public Socket getSocket() {
        return this.socket;
    }

    public boolean send(String update) {
        try {
            out.writeObject(update);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public void run() {
        
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            manager.updateBoard();

            while (true) {
                
                Object input;
                try {
                    input = in.readObject();
                    if (((String) input).equals("start")) {
                        manager.start();
                    } else if (((String) input).charAt(0) == 'U') {
                        manager.updateBoard((String) input);
                    } else {
                        manager.broadcast((String) input);
                    }
                } catch (ClassNotFoundException exc) {
                    exc.printStackTrace();
                }
            
            }
        } catch (IOException ex) {
            System.out.println("Error listening for a connection");
            ex.printStackTrace();
        }

    }

}