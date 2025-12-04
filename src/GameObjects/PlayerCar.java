package GameObjects;
import GameController.GameController;

import java.util.ArrayList;

public class PlayerCar extends Car{
    protected int nitro=100;
    public ArrayList<Bullet> bullets;
    boolean doubleBulletActive;
    int score=0;
    public int firerate = 0;
    public PlayerCar(float posX, float posY) {
        super(posX,posY,.7,20,100);
        bullets=new ArrayList<>();
    }
    PlayerCar(float posX, float posY, int speed, int damage, int health) {
        super(posX,posY,speed, damage, health);
        bullets=new ArrayList<>();
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
        if (firerate<=0) {
            if(doubleBulletActive) {
                bullets.add(new Bullet(posX - 5, posY, damage));
                bullets.add(new Bullet(posX + 5, posY, damage));
            } else {
                bullets.add(new Bullet(posX, posY, damage));
            }
            firerate=30;
        }
    }
}
