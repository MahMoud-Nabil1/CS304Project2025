package GameObjects;

import java.awt.*;

public class LightCar extends Car{

    //--------------!!!!!!!!!!!!!!!!DONT ADD X AND Y USE posx posy!!!!!!!!!!!!!!!!!!!!!!!--------------------------
    public LightCar(float posX, float posY) {
        super(posX,posY,.1,20,100);

        // Set specific size for Light Car
        this.width = 12;
        this.height = 12;
    }
    LightCar(int posX, int posY, int speed, int damage, int health) {
        super(posX,posY,speed, damage, health);
        this.width = 12;
        this.height = 12;

    }

    //-----Hit Box--------

    public Rectangle getBounds() {
        // FIXED: Subtract half width/height to center the box on posX, posY
        return new Rectangle(
                (int)(posX - width / 2),
                (int)(posY - height / 2),
                width,
                height
        );
    }
}
