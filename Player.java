import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Player{
    private int radius, xPos, yPos;
    private Color color;
    private int numPieces;
    public Player(){
        this.radius = 5;
        this.xPos = (int)(Math.random()*10000);
        this.yPos = (int)(Math.random()*10000);
        numPieces = 1;
        color = new Color((int)(Math.random()*252),(int)(Math.random()*252),(int)(Math.random()));
    }
    public void drawMe(Graphics g){
        g.setColor(color);
        g.fillOval(radius*2,radius*2,400,400);        
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
            xPos--;
        }
    }
    public void moveRight(){
        if (xPos!=10000){
            xPos++;
        }
    }
    public void moveUp(){
        if (yPos!=0)
            yPos--;
    }
    public void moveDown(){
        if (yPos!=10000)
            yPos++;
    }
}