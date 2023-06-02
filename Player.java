import java.awt.*;
import javax.swing.*;

public class Player {
    
    private double radius;
    private int xPos, yPos, speed;
    private Color color;
    private String name;

    private int _screen_size;

    public Player(int screen_size) {
        this.name = "";
        this._screen_size = screen_size;
        this.radius = 15; 
        this.speed = 15;

        // this.xPos = 0; this.yPos = 0;
        this.xPos = (int) (Math.random() * _screen_size);
        this.yPos = (int) (Math.random() * _screen_size);
        this.color = new Color( (int) (Math.random() * 252), (int) (Math.random() * 252), (int) (Math.random()));
    }
    
    public String getName(){
        return name;
    }

    public Player(int _x, int _y, int _radius, Color _color) {
        this.xPos = _x; this.yPos = _y;
        this.radius = _radius; this.color = _color;
    }
    
    public void drawMe(Graphics graphics){
        graphics.setColor(color);
        graphics.fillOval(400 - (int) radius, 400 - (int) radius, (int) radius * 2, (int) radius * 2);
        graphics.setColor(new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue()));
        graphics.drawString(this.name, 400 - (int) this.radius, 400 - (int) this.radius);
    }

    public void setName(String _name) {
        this.name = _name;
    }
    
    public int getX() {
        return xPos;
    }
    
    public int getY() {
        return yPos;
    }
    
    public int getRadius() {
        return (int) radius;
    }
    
    public void moveLeft() {
        if (xPos - speed - (int) radius >= 0){
            xPos -= speed;
        }
    }
    
    public void moveRight() {
        if (xPos + speed + (int) radius <= _screen_size) { // adjust
            xPos += speed;
        }
    }
    
    public void moveUp() {
        if (yPos - speed - (int) radius >= 0)
            yPos -= speed;
    }
    
    public void moveDown() {
        if (yPos + speed + (int) radius <= _screen_size)
            yPos += speed;
    }
    
    public int getR() {
        return color.getRed();
    }
    
    public int getG() {
        return color.getGreen();
    }
    
    public int getB() {
        return color.getBlue();
    }

    public Color getColor() {
        return this.color;
    }

    public void increaseSize(int r) {
        if (r > 0) {
            // radius += (0.25 + ((int) ((1.0 / Math.pow(radius, 1.1)) * (Math.log(r)) * 100)) / 100.);
            radius += (0.25 + ((int) ((Math.log(r)) * 100)) / 100.);
        }
        this.speed = (int) ((1.0 / radius) * 225);
    }

    public void reset() {
        this.radius = 15; 
        // this.speed = (int)((1/radius)*225);

        this.xPos = (int) (Math.random() * _screen_size);
        this.yPos = (int) (Math.random() * _screen_size);
        this.color = new Color( (int) (Math.random() * 252), (int) (Math.random() * 252), (int) (Math.random()));
    }

}