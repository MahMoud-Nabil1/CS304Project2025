package GameObjects;

import java.awt.*;

public class LightCar extends Car{

    //--------------!!!!!!!!!!!!!!!!DONT ADD X AND Y USE posx posy!!!!!!!!!!!!!!!!!!!!!!!--------------------------
    public LightCar(float posX, float posY) {
        super(posX,posY,.1,20,100);

        // Set specific size for Light Car
        this.width = 8;
        this.height = 10;
    }
    LightCar(int posX, int posY, int speed, int damage, int health) {
        super(posX,posY,speed, damage, health);
        this.width = 8;
        this.height = 10;

    }

    //-----Hit Box--------

    public Rectangle getBounds(){
        // TWEAK THESE NUMBERS
        int xOffset = -1;   // Change this to move Left(-) or Right(+)
        int yOffset = -1;  // Change this to move Down(-) or Up(+)

        // Reduce width/height if the box is too big
        int hitboxWidth = 12;
        int hitboxHeight = 12;

        return new Rectangle((int)posX + xOffset, (int)posY + yOffset, hitboxWidth, hitboxHeight);

    }
}
