package GameController;

import GameObjects.Bullet;
import java.util.ArrayList;
    public class BulletPool {
        // The "Clip" holding all bullets
        public static ArrayList<Bullet> pool = new ArrayList<>();

        // Call this in your Main Init() method!
        public static void init(int size) {
            pool.clear();
            for (int i = 0; i < size; i++) {
                // Create dead bullets way off screen
                Bullet b = new Bullet(-1000, -1000, 20);
                b.timer = -1; // Mark as "dead"
                pool.add(b);
            }
        }

        public static Bullet getBullet(float x, float y, int damage) {
            for (Bullet b : pool) {
                if (b.timer < 0) { // Found a dead bullet? Recycle it!
                    b.posX = x;
                    b.posY = y;
                    b.damage = damage;
                    b.timer = 100; // Reset life
                    return b;
                }
            }
            return null; // No bullets left in pool (maybe increase pool size)
        }
    }
