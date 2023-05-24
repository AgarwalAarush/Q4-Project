import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Player{
    
    private int radius, xPos, yPos, speed;
    private Color color;

    private int _screen_size;;

    public Player(int screen_size) {
        this._screen_size = screen_size;
        this.radius = 15; this.speed = 15;
        this.xPos = 0; this.yPos = 0;
        // this.xPos = (int) (Math.random() * _screen_size);
        // this.yPos = (int) (Math.random() * _screen_size);
        this.color = new Color( (int) (Math.random() * 252), (int) (Math.random() * 252), (int) (Math.random()));
    }

    public Player(int _x, int _y, int _radius, Color _color) {
        this.xPos = _x; this.yPos = _y;
        this.radius = _radius; this.color = _color;
    }
    
    public void drawMe(Graphics graphics){
        graphics.setColor(color);
        graphics.fillOval(400 - radius, 400 - radius, radius * 2, radius * 2);        
    }
    
    public int getX(){
        return xPos + radius;
    }
    
    public int getY(){
        return yPos + radius;
    }
    
    public int getRadius(){
        return radius;
    }
    
    public void moveLeft(){
        if (xPos >= 0){
            xPos -= speed;
        }
    }
    
    public void moveRight(){
        if (xPos <= _screen_size) { // adjust
            xPos += speed;
        }
    }
    
    public void moveUp(){
        if (yPos >= 0)
            yPos -= speed;
    }
    
    public void moveDown(){
        if (yPos <= _screen_size)
            yPos += speed;
    }
    
    public int getR(){
        return color.getRed();
    }
    
    public int getG(){
        return color.getGreen();
    }
    
    public int getB(){
        return color.getBlue();
    }

    public Color getColor() {
        return this.color;
    }

}