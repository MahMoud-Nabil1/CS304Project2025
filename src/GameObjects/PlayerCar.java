package GameObjects;
import GameController.GameController;

import java.util.ArrayList;

public class PlayerCar extends Car{
    protected int nitro=100;
    ArrayList<Bullet> bullets;
    boolean doubleBulletActive;
    int score=0;
    public PlayerCar(int posX, int posY) {
        super(posX,posY,0.1,20,100);
    }
    PlayerCar(int posX, int posY, int speed, int damage, int health) {
        super(posX,posY,speed, damage, health);
    }
    private boolean nitroActive = false;

    public void nitroOn() {
        if(nitro > 0 && !nitroActive) {
            this.damage += 50;
            GameController.gameSpeed = 2 ;
            damageFactor = 1.5;
            nitro--;
            nitroActive = true;
        }
        if (nitro<=0&&!nitroActive) {
            GameController.gameSpeed =1;
            nitroOff();
            System.out.println("nitroOff");
        }
    }

    public void nitroOff() {
        if(nitroActive) {
            this.speed -= 50;
            this.damage -= 50;
            nitroActive = false;

        }
    }
    public void shoot() {
        if(doubleBulletActive) {
            bullets.add(new Bullet(posX - 5, posY, damage));
            bullets.add(new Bullet(posX + 5, posY, damage));
        } else {
            bullets.add(new Bullet(posX, posY, damage));
        }
    }
}
