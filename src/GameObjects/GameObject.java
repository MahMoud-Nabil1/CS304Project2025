package GameObjects;
import java.awt.Rectangle;

public class GameObject {
    public double posX;
    public double posY;

    // --------------------Shehab Colligion Related-------------------------------
    public int width = 10;
    public int height = 10;

    public int health = 100; // Default health
    public boolean alive=true;

    //Constructor
    GameObject(double posX, double posY){
        this.posX = posX;
        this.posY = posY;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    // --------------------Shehab Colligion Related-------------------------------
    // Helper to get the rectangle for collision math

    public Rectangle getBounds() {
        // TWEAK THESE NUMBERS
        int xOffset = 0;   // Change this to move Left(-) or Right(+)
        int yOffset = 0;  // Change this to move Down(-) or Up(+)

        // Reduce width/height if the box is too big
        int hitboxWidth = 13;
        int hitboxHeight = 10;

        return new Rectangle((int)posX + xOffset, (int)posY + yOffset, hitboxWidth, hitboxHeight);
    }

    // General method to take damage
    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health <= 0) {
            this.alive = false;
        }
    }


}
