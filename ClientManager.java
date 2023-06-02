

public class ClientManager {

    private MyArrayList<ServerThread> list = new MyArrayList<ServerThread>();
    private Board board;
    private int updateCount;
    
    public ClientManager(Board board) {
        this.board = board;
    }

    // broadcast grid
    public void broadcast(String update) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).send(update);
        }
    }

    public void updateBoard() {
        for (int i = 0; i < list.size(); i++){
            list.get(i).send(this.board.toString());
        }
    }

    // change to MyArrayList<Pair<Integer, Integer>>
    public void updateBoard(String update) {
        updateCount = (updateCount + 1) % 100;
        update = update.substring(update.indexOf(" ") + 1);
        int numUpdates = Integer.parseInt(update.substring(0, update.indexOf(" ")));
        update = update.substring(update.indexOf(" ") + 1);
        for (int i = 0; i < numUpdates; i++) {
            int y = Integer.parseInt(update.substring(0, update.indexOf(" ")));
            update = update.substring(update.indexOf(" ") + 1);
            int x = Integer.parseInt(update.substring(0, update.indexOf(" ")));
            if (i != numUpdates - 1) {
                update = update.substring(update.indexOf(" ") + 1);
            }
            this.board.remove(y, x);
        }
        if (updateCount == 0) this.board.foodPopulation();
        this.updateBoard();
    }

    // starting screen
    public void start() {
        
    }

    public void addThread(ServerThread s) {
        list.add(s);
    }

}