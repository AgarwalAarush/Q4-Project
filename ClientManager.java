import java.util.*;
import java.io.*;

public class ClientManager {

    private ArrayList<ServerThread> list = new ArrayList<ServerThread>();
    private Board board;
    
    public ClientManager(Board board) {
        this.board = board;
    }

    // broadcast grid
    public void broadcast(String update) {
        for (ServerThread s : list) {
            s.send(update);
        }
    }

    public void updateBoard() {
        for (ServerThread s : list) {
            s.send(this.board.toString());
        }
    }

    // change to ArrayList<Pair<Integer, Integer>>
    public void updateBoard(String update) {
        int numUpdates = update.charAt(2) - 48;
        update = update.substring(4);
        for (int i = 0; i < numUpdates; i++) {
            int y = Integer.parseInt(update.substring(0,update.indexOf(" ")));
            update = update.substring(update.indexOf(" ") + 1);
            int x = Integer.parseInt(update.substring(0,update.indexOf(" ")));
            if (i != numUpdates - 1) {
                update = update.substring(update.indexOf(" ") + 1);
            }
            this.board.remove(y, x);
        }
        this.updateBoard();
    }

    // starting screen
    public void start() {
        
    }

    public void addThread(ServerThread s) {
        list.add(s);
    }

}