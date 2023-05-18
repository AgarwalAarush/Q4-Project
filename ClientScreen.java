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

public class ClientScreen extends JPanel implements ActionListener, KeyListener{
    Player player;
    int[] backgroundPos;
    ArrayList<Player> otherPlayers;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ArrayList<String> log = new ArrayList<String>();

    public ClientScreen(){
        setLayout(null);

        backgroundPos = new int[2];
        player = new Player();

        addKeyListener(this);
        this.setFocusable(true);
    }
    
    public Dimension getPreferredSize(){
		return new Dimension(800,800);
	}
	
	public void paintComponent(Graphics graphics){
		
        super.paintComponent(graphics);

        backgroundPos[0] = player.getX();
        backgroundPos[1] = player.getY();
        
        // 10,000 by 10,000 grid
        // 25 by 25 squares
        // 400 squares -> 401 horizontal + vertical lines
        for (int i = 0; i < 401; i++) {
            // horizontal lines
            graphics.drawLine(0 - backgroundPos[0], i * 25 - backgroundPos[1], 10000 - backgroundPos[0], i * 25 - backgroundPos[1]);
            // vertical lines
            graphics.drawLine(i * 25 - backgroundPos[0], 0 - backgroundPos[1], i * 25 - backgroundPos[0], 10000);
        }

        System.out.println(backgroundPos[0] + " " + backgroundPos[1]);
        
        player.drawMe(graphics);
        
        if(log.size() > 0){
            String latestLog = log.get(log.size()-1);
            int x = Integer.parseInt(latestLog.substring(0,latestLog.indexOf(" ")));
            latestLog = latestLog.substring(latestLog.indexOf(" ") + 1);
            int y = Integer.parseInt(latestLog.substring(0,latestLog.indexOf(" ")));
            latestLog = latestLog.substring(latestLog.indexOf(" ") + 1);
            int radius = Integer.parseInt(latestLog.substring(0,latestLog.indexOf(" ")));
            latestLog = latestLog.substring(latestLog.indexOf(" ") + 1);
            int red = Integer.parseInt(latestLog.substring(0,latestLog.indexOf(" ")));
            latestLog = latestLog.substring(latestLog.indexOf(" ") + 1);
            int green = Integer.parseInt(latestLog.substring(0,latestLog.indexOf(" ")));
            latestLog = latestLog.substring(latestLog.indexOf(" ") + 1);
            int blue = Integer.parseInt(latestLog);
            graphics.setColor(new Color (red,green,blue));
            graphics.fillOval(x, y, radius * 2, radius * 2);
        }
	
    }
	
	public void actionPerformed(ActionEvent e){
        
	}
    public void keyPressed (KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            player.moveUp();
            try {
                //x, y, size, R, G, B
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
        repaint();
    }

    private String compressGraph() {
        // compress graph into String format
        return null;
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
            while (true) {
                String message;
                try {
                    message = (String) (in.readObject());
                    // different message cases
                    if (message.charAt(0) == 's') {

                    } else if (message.charAt(0) == 'e') {
                        
                    } else {
                        log.add(message);
                    }
                } catch (ClassNotFoundException exc) {
                    exc.printStackTrace();
                }
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

    private String objToString(Object o){
        return o.toString();
    }

}