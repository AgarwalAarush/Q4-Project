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
    private final int screen_size = 1600;
    
    private boolean started;
    
    private JButton start;

    public ClientScreen(){
        setLayout(null);

        this.started = false;

        start = new JButton("Start");
        start.setBounds(300,300,100,30);
        start.addActionListener(this);
        this.add(start);

        backgroundPos = new int[2];
        player = new Player(screen_size);

        addKeyListener(this);
        this.setFocusable(true);
    }
    
    public Dimension getPreferredSize(){
		return new Dimension(800,800);
	}
	
	public void paintComponent(Graphics graphics){
		
        super.paintComponent(graphics);


        checkButtons();

        if (started) {

            backgroundPos[0] = 400 - player.getX();
            backgroundPos[1] = 400 - player.getY();
            
            // 10,000 by 10,000 grid
            // 25 by 25 squares
            // 400 squares -> 401 horizontal + vertical lines
            for (int i = 0; i < 1 + screen_size / 25; i++) {
                // horizontal lines
                graphics.drawLine(backgroundPos[0], i * 25 + backgroundPos[1], screen_size + backgroundPos[0], i * 25 + backgroundPos[1]);
                // vertical lines
                graphics.drawLine(i * 25 + backgroundPos[0], 0 + backgroundPos[1], i * 25 + backgroundPos[0], screen_size);
            }
            
            // player.drawMe(graphics);
            
            otherPlayers = new ArrayList<>();

            for (int i = 0; i < log.size(); i++) {
                String currentLog = log.get(i);
                int x = Integer.parseInt(currentLog.substring(0,currentLog.indexOf(" ")));
                currentLog = currentLog.substring(currentLog.indexOf(" ") + 1);
                int y = Integer.parseInt(currentLog.substring(0,currentLog.indexOf(" ")));
                currentLog = currentLog.substring(currentLog.indexOf(" ") + 1);
                int radius = Integer.parseInt(currentLog.substring(0,currentLog.indexOf(" ")));
                currentLog = currentLog.substring(currentLog.indexOf(" ") + 1);
                int red = Integer.parseInt(currentLog.substring(0,currentLog.indexOf(" ")));
                currentLog = currentLog.substring(currentLog.indexOf(" ") + 1);
                int green = Integer.parseInt(currentLog.substring(0,currentLog.indexOf(" ")));
                currentLog = currentLog.substring(currentLog.indexOf(" ") + 1);
                int blue = Integer.parseInt(currentLog);
                graphics.setColor(new Color (red,green,blue));
                if (new Color(red, green, blue).equals(player.getColor())) {
                    continue;
                } else {
                    otherPlayers.add(new Player(x, y, radius, new Color(red, green, blue)));
                }
                graphics.fillOval(x, y, radius * 2, radius * 2);   
            }

            log.clear();

            for (int i = 0; i < otherPlayers.size(); i++) {
                if (collisionDetected(this.player, otherPlayers.get(i))) {
                    // collision detected code
                }
            }
        }
    }

    private boolean collisionDetected(Player p1, Player p2) {
        int distance = (int) Math.pow(Math.pow(p1.getX() - p2.getY(), 2) + Math.pow(p1.getY() - p2.getY(), 2), 0.5)
        if (distance < (p1.getRadius() / 2) || distance < (p2.getRadius() / 2)) {
            return true;
        }
        return false;
    }

    private void checkButtons() {

        if (started) {
            start.setVisible(false);
        } else {
            start.setVisible(true);
        }

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==start){
            started=true;
        }
        repaint();
    }

}