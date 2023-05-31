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
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;


public class ClientScreen extends JPanel implements ActionListener, KeyListener {
    
    Player player;
    int[] backgroundPos;
    ArrayList<Player> otherPlayers;
    SoundPlayer s;

    private JTextArea leaderboard;

    private String leaderboardString;
    
    private JTextField nameInput; 
    
    private ObjectOutputStream out;
    private ObjectInputStream in;
    // private ArrayList<String> log = new ArrayList<String>();
    private ArrayList<String> foodLog;
    
    private final int screen_size = 1600;
    
    private boolean started;
    
    private JButton start;

    public ClientScreen(){

        setLayout(null);
        
        s = new SoundPlayer();

        this.started = false;

        this.otherPlayers = new ArrayList<>();
        this.foodLog = new ArrayList<>();

        leaderboard = new JTextArea(500,250); //sets the location and size
		leaderboard.setText("");
		leaderboard.setEditable(false);
		
		//JScrollPane
        
		JScrollPane scrollPane = new JScrollPane(leaderboard); 
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(600,0,200,300);

		this.add(scrollPane);

        start = new JButton("Start");
        start.setBounds(300,300,100,30);
        start.addActionListener(this);
        this.add(start);

        nameInput = new JTextField();
        nameInput.setBounds(200,200,100,20);
        add(nameInput);

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
            
            drawGrid(graphics);
            
            player.drawMe(graphics);
            
            drawPlayers(graphics);

            checkCollisions(graphics);
            
        } else {

        }
    }

    private void drawPlayers(Graphics graphics) {
        for (int i = 0; i < otherPlayers.size(); i++) {
            Player cPlayer = otherPlayers.get(i);
            graphics.setColor(cPlayer.getColor());
            graphics.fillOval(cPlayer.getX() + backgroundPos[0] - cPlayer.getRadius(), cPlayer.getY() + backgroundPos[1] - cPlayer.getRadius(), cPlayer.getRadius() * 2, cPlayer.getRadius() * 2);
        }
    }

    private void checkCollisions(Graphics graphics) {
        for (int i = 0; i < otherPlayers.size(); i++) {
            if (collisionDetected(this.player, otherPlayers.get(i))) {
                // collision detected code
                if (this.player.getRadius()>otherPlayers.get(i).getRadius()){
                    player.increaseSize(otherPlayers.get(i).getRadius());
                    s.playChompSound();
                    leaderboard.setText(getLeaderboard());
                }
                else if (this.player.getRadius()<otherPlayers.get(i).getRadius()){
                    s.playGameOverSound();
                    started = false;

                    player.reset();
                }
                
            }
        }

        //drawing food -> change? -> can just send x,y coordinates of update food and flip the boolean value
        if (foodLog.size() > 0){
            String currentFood = foodLog.get(foodLog.size()-1);
            //goes through food string
            for (int i = 0; i < currentFood.length(); i++){
                //check if 1 or 0
                //draw food -i%Screen_size for row, i-i%screen_size for column
                if (currentFood.charAt(i) == 1){
                    int col = i % ((int) Math.sqrt(screen_size) / 25);
                    int row = i - i % ((int) Math.sqrt(screen_size) / 25);
                    graphics.setColor(Color.red);
                    graphics.fillOval(row*25,col*25,4,4);
                }
            }
            int foodCollision = foodCollisionDetected(player, currentFood);
        }
    }

    private void drawGrid(Graphics graphics) {
        backgroundPos[0] = 400 - player.getX();
        backgroundPos[1] = 400 - player.getY();
        // 10,000 by 10,000 grid
        // 25 by 25 squares
        // 400 squares -> 401 horizontal + vertical lines
        for (int i = 0; i < 1 + screen_size / 25; i++) {
            // horizontal lines
            graphics.drawLine(backgroundPos[0], i * 25 + backgroundPos[1], screen_size + backgroundPos[0], i * 25 + backgroundPos[1]);
            // vertical lines
            graphics.drawLine(i * 25 + backgroundPos[0], 0 + backgroundPos[1], i * 25 + backgroundPos[0], screen_size + backgroundPos[1]);
        }
    }
    

    private void checkButtons() {

        if (started) {
            start.setVisible(false);
        } else {
            start.setVisible(true);
        }

    }

    private boolean collisionDetected(Player p1, Player p2) {
        int distance = (int) Math.pow(Math.pow(p1.getX() - p2.getY(), 2) + Math.pow(p1.getY() - p2.getY(), 2), 0.5);
        if (distance < (p1.getRadius() / 2) || distance < (p2.getRadius() / 2)) {
            return true;
        }
        return false;
    }
    //returns 0 if no collision, otherwise the number in the foodString that should become 0
    private int foodCollisionDetected(Player p,String foodString ){
        int playerX = p.getX();
        int playerY = p.getY();
        int playerRadius = p.getRadius();
        //row1 = Math.sqrt(screenSize)/25, for each y one of those is added
        //col, just add x/25
        //position in terms of the foodgrid for the player is py/25*Math.sqrt(screenSize)/25 + x/25;
        int playerPosNum = (playerY/25)*((int)Math.sqrt(screen_size)/25 )+playerX/25;
        if (foodString.charAt(playerPosNum) == '1'){
            return playerPosNum;
        }
        // if foodString
        return 0;
    }
    
    public void keyPressed (KeyEvent e) {
        boolean playerMove = false;
        
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            player.moveUp();
            playerMove = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            player.moveDown();
            playerMove = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            player.moveLeft();
            playerMove = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            player.moveRight();
            playerMove = true;
        }
        
        if (playerMove) {
            try {
                out.writeObject(player.getX() + " " + player.getY() + " " + player.getRadius() + " " + player.getR() + " "+ player.getG() + " " + player.getB());
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
        
        repaint();
    }

    private String compressGraph() {
        // compress graph into String format
        return null;
    }
    
    public void poll() throws IOException {
        
        // String hostName = "10.11.115.207";
        String hostName = "localhost";
        int portNumber = 1023;
        Socket serverSocket = new Socket(hostName, portNumber);
        out = new ObjectOutputStream(serverSocket.getOutputStream());
        in = new ObjectInputStream(serverSocket.getInputStream());

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
                        // determine authenticity
                        Player otherPlayer = determineAuthenticity(message);
                        if (otherPlayer != null) {
                            boolean found = false;
                            for (int i = 0; i < otherPlayers.size(); i++) {
                                if (otherPlayers.get(i).getColor().equals(otherPlayer.getColor())) {
                                    otherPlayers.set(i, otherPlayer); found = true;
                                }
                            }
                            if (!found) otherPlayers.add(otherPlayer);
                            repaint();
                        }
                        // send as well?
                        // repaint?
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

    private Player determineAuthenticity(String message) {
        int x = Integer.parseInt(message.substring(0,message.indexOf(" ")));
        message = message.substring(message.indexOf(" ") + 1);
        int y = Integer.parseInt(message.substring(0,message.indexOf(" ")));
        message = message.substring(message.indexOf(" ") + 1);
        int radius = Integer.parseInt(message.substring(0,message.indexOf(" ")));
        message = message.substring(message.indexOf(" ") + 1);
        int red = Integer.parseInt(message.substring(0,message.indexOf(" ")));
        message = message.substring(message.indexOf(" ") + 1);
        int green = Integer.parseInt(message.substring(0,message.indexOf(" ")));
        message = message.substring(message.indexOf(" ") + 1);
        int blue = Integer.parseInt(message);
        if ((new Color(red, green, blue)).equals(player.getColor())) {
            return null;
        }
        return new Player(x, y, radius, new Color(red, green, blue));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start){
            started = true;
            nameInput.setVisible(false);
            player.setName(nameInput.getText());
        }
    }

    public String getLeaderboard(){
        sortPlayers();
        String s = "";
        for (int i = 0; i < otherPlayers.size(); i++){
            s += otherPlayers.get(i).getName() + " - " + otherPlayers.get(i).getRadius() + "/n";
        }
        return s;
    }

    public void sortPlayers(){
        //insert sorting algo
        for (int i = 0; i < otherPlayers.size() - 1; i++){
            for (int j = i + 1; j < otherPlayers.size(); j++){
                if (otherPlayers.get(j).getRadius() > otherPlayers.get(i).getRadius()){
                    Player temp = otherPlayers.get(j);
                    otherPlayers.set(j,otherPlayers.get(i));
                    otherPlayers.set(i,temp);
                }
            }
        }
    }

    public void keyReleased(KeyEvent e){

    }
    
    public void keyTyped(KeyEvent e){

    }

}
