import java.util.*;
import java.io.*;

public class ClientManager {

    private ArrayList<ServerThread> list = new ArrayList<ServerThread>();
    
    public ClientManager() {

    }

    // broadcast grid
    public void broadcast() {
        for (ServerThread s : list) {
            s.send()
        }
    }

    // broadcast positions and size
    public void broadcast() {
        for (ServerThread s : list) {

        }
    }

    public void start() {
        
    }

}