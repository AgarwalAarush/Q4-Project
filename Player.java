import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Player{
    
    private int radius, xPos, yPos, speed;
    private Color color;
    private int numPieces;

    public Player(){
        this.radius = 15;
        this.xPos = (int)(Math.random()*10000);
        this.yPos = (int)(Math.random()*10000);
        this.numPieces = 1;
        this.speed = 10;
        this.color = new Color((int)(Math.random()*252),(int)(Math.random()*252),(int)(Math.random()));
    }
    
    public void drawMe(Graphics g){
        g.setColor(color);
        g.fillOval(400, 400, radius * 2, radius * 2);        
    }
    
    public int getX(){
        return xPos+radius;
    }
    
    public int getY(){
        return yPos+radius;
    }
    
    public int getRadius(){
        return radius;
    }
    
    public void moveLeft(){
        if (xPos!=0){
            xPos -= speed;
        }
    }
    
    public void moveRight(){
        if (xPos != 10000) { // adjust bounds
            xPos += speed;
        }
    }
    
    public void moveUp() {
        if (yPos!=0)
            yPos -= speed;
    }
    
    public void moveDown() { // adjust bounds
        if (yPos!=10000)
            yPos += speed;
    }
    
    public int getR() {
        return color.getRed();
    }
    
    public int getG(){
        return color.getGreen();
    }
    
    public int getB(){
        return color.getBlue();
    }

}