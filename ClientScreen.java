import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

import java.io.*;
import java.net.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;


public class ClientScreen extends JPanel implements ActionListener, KeyListener {
    
    Player player;
    int[] backgroundPos;
    MyArrayList<Player> otherPlayers;
    SoundPlayer soundPlayer;
    String winnerName ="";

    private JTextArea leaderboard;
    
    private JTextField nameInput; 
    
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    private final int screen_size = 1600;
    
    private boolean started;
    
    private JButton start;

    private int[][] foodGrid;

    public ClientScreen() {

        setLayout(null);
        
        soundPlayer = new SoundPlayer();

        this.foodGrid = new int[screen_size / 25][screen_size / 25];

        this.started = false;

        this.otherPlayers = new MyArrayList<>();

        leaderboard = new JTextArea(500,250); //sets the location and size
		leaderboard.setText("");
		leaderboard.setEditable(false);
		
		//JScrollPane
        
		JScrollPane scrollPane = new JScrollPane(leaderboard); 
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(600,0,200,200);

		this.add(scrollPane);

        start = new JButton("Start");
        start.setBounds(300,300,100,30);
        start.addActionListener(this);
        this.add(start);

        nameInput = new JTextField();
        nameInput.setBounds(300,350,100,30);
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
            drawFood(graphics);
            
            player.drawMe(graphics);
            
            drawPlayers(graphics);

            checkCollisions(graphics);

            updateLeaderboard();

            if (player.getRadius() > 150){
                winnerName = player.getName();
                resetGame();
            }
            
        } else {

            graphics.drawString("Name: ", 250, 375);
            nameInput.setVisible(true);
            start.setVisible(true);

            graphics.drawString("Move using arrow keys", 50, 50);
            graphics.drawString("Collect tiny orbs and eat other players to grow bigger", 50, 100);
            graphics.drawString("First player to 150 wins!", 50, 150);
            graphics.drawString("The leaderboard is in the top right corner", 50, 200);

            if (!winnerName.equals("")){
                graphics.drawString(winnerName+" won by reaching 150 size!",100,300);
            }

        }
    }

    private void updateLeaderboard() {
        leaderboard.setText(getLeaderboard());
    }

    private void drawFood(Graphics graphics) {
        for (int i = 0; i < screen_size / 25; i++) {
            for (int j = 0; j < screen_size / 25; j++) {
                if (foodGrid[i][j] == 1) {
                    Color randColor = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
                    graphics.setColor(randColor);
                    graphics.fillOval(9 + j * 25 + backgroundPos[0], 9 + i * 25 + backgroundPos[1], 6, 6);
                }
            }
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
                if (this.player.getRadius() > otherPlayers.get(i).getRadius()){
                    player.increaseSize(otherPlayers.get(i).getRadius());
                    SoundPlayer.playChompSound();
                } else if (this.player.getRadius() < otherPlayers.get(i).getRadius()){

                    System.out.println("here");
                    
                    SoundPlayer.playGameOverSound();
                    started = false;
                    
                    try {
                        out.writeObject("e " + player.getR() + " " + player.getG() + " " + player.getB() + " ");                    
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }

                    player.reset();

                    repaint();
                    
                }
            }
        }

        MyArrayList<Pair<Integer, Integer>> foodCollisions = foodCollisionsDetected();
        player.increaseSize(foodCollisions.size());

        if (foodCollisions.size() > 0) {
            String update = "U ";
            update += foodCollisions.size() + " ";
            for (int i = 0; i < foodCollisions.size(); i++) {
                update += foodCollisions.get(i).getK() + " " + foodCollisions.get(i).getV() + " ";
            }

            try {
                out.writeObject(update);
            } catch (IOException exc) {
                exc.printStackTrace();
            }

        }

    }

    private boolean collisionDetected(Player p1, Player p2) {
        int distance = (int) Math.pow(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2), 0.5);
        if (distance < Math.abs(p1.getRadius() - p2.getRadius()) && (p1.getRadius() > p2.getRadius() * 1.4 || p1.getRadius() * 1.4 < p2.getRadius())) {
            return true;
        }
        return false;
    }
    //returns 0 if no collision, otherwise the number in the foodString that should become 0
    private MyArrayList<Pair<Integer, Integer>> foodCollisionsDetected() {
        
        MyArrayList<Pair<Integer, Integer>> foodCollisions = new MyArrayList<>();
        
        int playerX = player.getX();
        int playerY = player.getY();
        int playerRadius = player.getRadius();
        
        int gridXStart = (playerX - playerRadius) / 25;
        int gridXEnd = (playerX + playerRadius) / 25;
        
        int gridYStart = (playerY - playerRadius) / 25;
        int gridYEnd = (playerY + playerRadius) / 25;

        gridXStart = Math.max(gridXStart, 0);
        gridYStart = Math.max(gridYStart, 0);

        gridXEnd = Math.min(gridXEnd, screen_size / 25 - 1);
        gridYEnd = Math.min(gridYEnd, screen_size / 25 - 1);

        for (int i = gridYStart; i < gridYEnd; i++) {
            for (int j = gridXStart; j < gridXEnd; j++) {
                if (foodGrid[i][j] == 1) {
                    foodCollisions.add(new Pair<Integer, Integer>(i, j));
                }
            }
        }

        return foodCollisions;

    }

    public void resetGame(){
            try {
                out.writeObject("E ALL " + player.getName());
            } catch (IOException exc) {
                exc.printStackTrace();
            }
            started = false;
            player.reset();
            repaint();
        }

    private void drawGrid(Graphics graphics) {
        backgroundPos[0] = 400 - player.getX();
        backgroundPos[1] = 400 - player.getY();
        // 10,000 by 10,000 grid
        // 25 by 25 squares
        // 400 squares -> 401 horizontal + vertical lines
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < 1 + (screen_size / 25); i++) {
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
    
    public void keyPressed (KeyEvent e) {

        if (started) {
            winnerName="";
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
                    out.writeObject(player.getX() + " " + player.getY() + " " + player.getRadius() + " " + player.getR() + " "+ player.getG() + " " + player.getB() + " " + player.getName());
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
            
            repaint();
        }
        
    }
    
    public void poll() throws IOException {
        
        // String hostName = "10.11.115.207";
        String hostName = "10.210.116.2";
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
                        System.out.println("here: removal");
                        message = message.substring(message.indexOf(" ") + 1);
                        int red = Integer.parseInt(message.substring(0, message.indexOf(" ")));
                        message = message.substring(message.indexOf(" ") + 1);
                        int green = Integer.parseInt(message.substring(0, message.indexOf(" ")));
                        message = message.substring(message.indexOf(" ") + 1);
                        int blue = Integer.parseInt(message.substring(0, message.indexOf(" ")));
                        for (int i = 0; i < otherPlayers.size(); i++) {
                            Player temp = otherPlayers.get(i);
                            if (temp.getR() == red && temp.getG() == green && temp.getB() == blue) {
                                otherPlayers.remove(temp);
                            }
                        }
                        repaint();
                    } else if (message.charAt(0) == 'F') {
                        for (int i = 0; i < screen_size / 25; i++) {
                            for (int j = 0; j < screen_size / 25; j++) {
                                foodGrid[i][j] = message.charAt(1 + j + i * (screen_size / 25)) - 48;
                            }
                        }
                    } else if (message.charAt(0)=='E'){
                        if (message.substring(2,5).equals("ALL")){
                            otherPlayers = new MyArrayList<>();
                            winnerName = message.substring(5);
                            player.reset();
                            started = false;
                        }
                        repaint();
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
        int blue = Integer.parseInt(message.substring(0,message.indexOf(" ")));
        message = message.substring(message.indexOf(" ") + 1);
        String name = message;
        if ((new Color(red, green, blue)).equals(player.getColor())) {
            return null;
        }
        Player temp = new Player(x, y, radius, new Color(red, green, blue));
        temp.setName(name);
        return temp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == start){
            started = true;
            nameInput.setVisible(false);
            player.setName(nameInput.getText());
        }
        
        repaint();
    }

    public String getLeaderboard() {
        MyArrayList<Player> allPlayers = new MyArrayList<>();
        for (int i = 0; i < otherPlayers.size(); i++) {
            allPlayers.add(otherPlayers.get(i));
        }
        allPlayers.add(this.player);
        sortPlayers(allPlayers);
        String s = "";
        for (int i = 0; i < allPlayers.size(); i++){
            s += allPlayers.get(i).getName() + " - " + allPlayers.get(i).getRadius() + "\n";
        }
        return s;
    }

    public void sortPlayers(MyArrayList<Player> allPlayers){
        //insert sorting algo
        for (int i = 0; i < allPlayers.size() - 1; i++){
            for (int j = i + 1; j < allPlayers.size(); j++){
                if (allPlayers.get(j).getRadius() > allPlayers.get(i).getRadius()){
                    Player temp = allPlayers.get(j);
                    allPlayers.set(j,allPlayers.get(i));
                    allPlayers.set(i,temp);
                }
            }
        }
    }

    public void keyReleased(KeyEvent e){

    }
    
    public void keyTyped(KeyEvent e){

    }

}
