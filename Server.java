import java.net.*;
import java.io.*;

public class Server {

	private final int screen_size = 1600;
	
    public static void main(String[] args) throws IOException {
        int portNumber = 1023;
		ServerSocket serverSocket = new ServerSocket(portNumber);
        ClientManager manager = new ClientManager();


		//This loop will run and wait for one connection at a time.
		while(true){
			System.out.println("Waiting for a connection");

			//Wait for a connection.
			Socket clientSocket = serverSocket.accept();

			//Once a connection is made, run the socket in a ServerThread.
			Thread thread = new Thread(new ServerThread(clientSocket, manager));
			thread.start();
		}
    }

	private class Board {
		
		private int[][] foodGrid = new int[screen_size/25][screen_size/25];
		
		private Board(){
			developFood();
		}

		private void developFood() {
			for (int i = 0; i < foodGrid.length; i++){
				for (int j = 0; j < foodGrid[i].length; j++){
					double rand = Math.random();
					if (rand < 0.2){
						foodGrid[i][j] = 0;
					}else{
						foodGrid[i][j] = 1;
					}
				}
			}
		}
		
		public String toString() {
			String ans = "F";
			for (int i = 0; i < foodGrid.length; i++) {
				for (int j = 0; j < foodGrid[i].length; j++) {
					ans += foodGrid[i][j];
				}
			}
			return ans;
		}

		private void foodPopulation() {
			// filling up 40% of unfilled food spaces
		}

	}

}