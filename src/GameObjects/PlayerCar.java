package GameObjects;
import GameController.*;

import java.awt.*;
import java.util.ArrayList;

public class PlayerCar extends Car{
    protected int nitro=200;
    public ArrayList<Bullet> bullets;
    int score=0;
    public int firerate = 0;
    public int invincibilityTimer = 0; // For Colligions
    public double nitro1PositionX=1000;
    public double nitro2PositionX=1000;
    public double nitroPositionY=1000;

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
    public boolean nitroActive = false;

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
            nitro1PositionX=1000;
            nitro2PositionX=1000;
            nitroPositionY=1000;
        }
    }
    // Inside PlayerCar.java
    public void shoot() {
        if (firerate <= 0) {
            // Recycle a bullet instead of making a new one
            if (GameController.doubleBulletActive) {
                // Grab two from the pool
                Bullet b1 = BulletPool.getBullet((float) (posX - 2), posY, damage);
                Bullet b2 = BulletPool.getBullet(posX + 2, posY, damage);

                // Add them to your active list ONLY if pool returned something
                if (b1 != null) bullets.add(b1);
                if (b2 != null) bullets.add(b2);

            } else {
                Bullet b = BulletPool.getBullet(posX, posY, damage);
                if (b != null) bullets.add(b);
            }
            firerate = 15;
        }
    }
    // Add this to your updateMovement or a new update() method
    public void updateInvincibility() {
        if (invincibilityTimer > 0) {
            invincibilityTimer--;
        }
    }
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
