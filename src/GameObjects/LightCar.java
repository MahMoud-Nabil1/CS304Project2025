package GameObjects;

import java.awt.*;

public class LightCar extends Car{

    //--------------!!!!!!!!!!!!!!!!DONT ADD X AND Y USE posx posy!!!!!!!!!!!!!!!!!!!!!!!--------------------------
    public LightCar(float posX, float posY) {
        super(posX,posY,.3,20,100);

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

    public Rectangle getBounds() {
        // -----------------------------------------------------------
        // 1. SIZE ADJUSTMENTS (Make hitbox smaller than the image for better gameplay)
        // -----------------------------------------------------------
        int hitboxWidth =  width ;   // Make it 2 pixels narrower than the image
        int hitboxHeight = height ;  // Make it 4 pixels shorter than the image

        // -----------------------------------------------------------
        // 2. POSITION OFFSETS (Shift the box relative to the sprite center)
        // -----------------------------------------------------------
        // Remember: posX and posY are the CENTER of the sprite.

        // To center the box, we start at (posX - hitboxWidth/2).
        // Then we add 'manualShiftX' to move it Left (-) or Right (+)
        int manualShiftX = 0;

        // To center the box, we start at (posY - hitboxHeight/2).
        // Then we add 'manualShiftY' to move it Down (-) or Up (+)
        int manualShiftY = 0;

        // -----------------------------------------------------------
        // 3. CALCULATE FINAL COORDINATES
        // -----------------------------------------------------------
        int finalX = (int)(posX - (hitboxWidth / 2)) + manualShiftX;
        int finalY = (int)(posY - (hitboxHeight / 2)) + manualShiftY;

        return new Rectangle(finalX, finalY, hitboxWidth, hitboxHeight);
    }
}
