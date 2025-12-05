package GameObjects;

public class LightCar extends Car{

    //--------------!!!!!!!!!!!!!!!!DONT ADD X AND Y USE posx posy!!!!!!!!!!!!!!!!!!!!!!!--------------------------
    //int x;
    //int y;
    LightCar(int posX, int posY) {
        super(posX,posY,100,20,100);

        // Set specific size for Light Car
        this.width = 50;
        this.height = 100;
    }
    LightCar(int posX, int posY, int speed, int damage, int health) {
        super(posX,posY,speed, damage, health);
        this.width = 50;
        this.height = 100;

    }
}
