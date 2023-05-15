import java.util.*;
import java.io.*;

public class ClientManager {

    private ArrayList<ServerThread> list = new ArrayList<ServerThread>();
    
    public ClientManager() {

    }

    // broadcast grid
    public void broadcast(String update) {
        for (ServerThread s : list) {
            s.send(update);
        }
    }

    // starting screen
    public void start() {
        
    }

    public void addThread(ServerThread s) {
        list.add(s);
    }

}