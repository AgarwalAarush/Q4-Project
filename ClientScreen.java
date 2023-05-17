import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ClientScreen extends JPanel implements ActionListener,KeyListener{
    Player player;
    int[] backgroundPos;
    ArrayList<Player> otherPlayers;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ArrayList<String> log = new ArrayList<String>();

    public ClientScreen(){
        setLayout(null);

        backgroundPos = new int[2];
        Player player = new Player();

        this.setFocusable(true);
    }
    
    public Dimension getPreferredSize(){
		return new Dimension(800,800);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
        backgroundPos[0]=player.getX();
        backgroundPos[1]=player.getY();
        for (int i = 0; i < 200 ; i++){
            g.drawLine(i * 25 - backgroundPos[0], i * 25 - backgroundPos[0],0,800);
            g.drawLine(0, 800, i * 50 - backgroundPos[1],i * 50 - backgroundPos[1]);
        }
        player.drawMe(g);
        String latestLog = log.get(log.size()-1);
        int numCharacters = Integer.parseInt(latestLog.substring(0,latestLog.indexOf(" ")));
        
	}
	
	public void actionPerformed(ActionEvent e){
        
	}
    public void keyPressed (KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP){
            player.moveUp();
            try {
                out.writeObject(player.getX()+" "+player.getY()+" "+player.getRadius()+" "+player.getR()+" "+player.getG()+" "+player.getB());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            player.moveDown();
            try {
                out.writeObject(player.getX()+" "+player.getY()+" "+player.getRadius()+" "+player.getR()+" "+player.getG()+" "+player.getB());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            player.moveLeft();
            try {
                out.writeObject(player.getX()+" "+player.getY()+" "+player.getRadius()+" "+player.getR()+" "+player.getG()+" "+player.getB());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            player.moveRight();
            try {
                out.writeObject(player.getX()+" "+player.getY()+" "+player.getRadius()+" "+player.getR()+" "+player.getG()+" "+player.getB());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    public void keyReleased(KeyEvent e){

    }
    public void keyTyped(KeyEvent e){

    }
    public void poll() throws IOException {
        
        // String hostName = "10.11.115.207";
        String hostName = "localhost";
        int portNumber = 1023;
        Socket serverSocket = new Socket(hostName, portNumber);
        out = new ObjectOutputStream(serverSocket.getOutputStream());
        in = new ObjectInputStream(serverSocket.getInputStream());
        
        
        repaint();

        // listens for inputs
        try {
            String message;
            try {
                message = (String) (in.readObject());
                // different message cases
                
            }
        } catch (UnknownHostException e) {
            System.err.println("Host unkown: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
        serverSocket.close();
    }

}