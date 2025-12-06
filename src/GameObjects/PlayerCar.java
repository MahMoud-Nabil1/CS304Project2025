package GameObjects;
import GameController.GameController;

import java.util.ArrayList;

public class PlayerCar extends Car{
    protected int nitro=200;
    public ArrayList<Bullet> bullets;
    int score=0;
    public int firerate = 0;
    public int invincibilityTimer = 0; // For Colligions

    public PlayerCar(float posX, float posY) {
        super(posX,posY,1,20,100);
        bullets=new ArrayList<>();

        // FIX: Set the size here!
        this.width = 8;
        this.height = 10;
    }
    PlayerCar(float posX, float posY, int speed, int damage, int health) {
        super(posX,posY,speed, damage, health);
        bullets=new ArrayList<>();
        //
        this.width = 8;
        this.height = 10;
    }
    private boolean nitroActive = false;

    // Add a variable to remember normal speed
    private double normalGameSpeed = 1.0;

    public void update() {
        // --- NITRO DRAIN LOGIC ---
        // If nitro is on, drain fuel
        if (nitroActive) {
            nitro--; // Drain 1 unit per frame (adjust as needed)

            // If fuel runs out, force it off
            if (nitro <= 0) {
                nitroOff();
            }
        }
    }

    public void nitroOn() {
        // Only activate if we have fuel and aren't already boosting
        if (nitro > 0 && !nitroActive) {
            nitroActive = true;

            // 1. Save current speed so we can restore it later
            normalGameSpeed = GameController.gameSpeed;

            // 2. Set Boost Speed
            GameController.gameSpeed = normalGameSpeed * 2; // Double the current speed

            // 3. Buff Damage
            this.damage += 50;
            // damageFactor = 1.5; // Optional, depends on your logic
        }
    }

    public void nitroOff() {
        if (nitroActive) {
            nitroActive = false;

            // 1. Restore the speed we had before boosting
            GameController.gameSpeed = normalGameSpeed;

            // 2. Remove Damage Buff
            this.damage -= 50;
        }
    }
    public void shoot() {
        if (firerate<=0) {
            if(GameController.doubleBulletActive) {
                bullets.add(new Bullet(posX - 2, posY, damage));
                bullets.add(new Bullet(posX + 2, posY, damage));
            } else {
                bullets.add(new Bullet(posX, posY, damage));
            }
            firerate=10;
        }
    }
    // Add this to your updateMovement or a new update() method
    public void updateInvincibility() {
        if (invincibilityTimer > 0) {
            invincibilityTimer--;
        }
    }


}
