package GameObjects;

import GameController.GameController;

public abstract class PowerUp extends GameObject {

    public boolean isCollected = false;
    public int durationFrames = 0;
    public double speed = GameController.gameSpeed;

    public PowerUp(float x, float y) {
        super(x, y);
        this.width = 8;
        this.height = 2;
    }

    // --- THE UPDATE METHOD ---
    public void update(PlayerCar car) {
        if (!isCollected) {
            // PHASE 1: Falling down the screen
            setPosY((float) (getPosY() - GameController.gameSpeed));
        }
        else {
            // PHASE 2: Collected & Active (In Inventory/Effect active)
            if (durationFrames > 0) {
                durationFrames--;
            } else {
                // Time is up!
                remove(car);   // Deactivate effect
                alive = false; // Mark for deletion by GameController
            }
        }
    }

    // This ensures children implement these
    public abstract void apply(PlayerCar car);
    public abstract void remove(PlayerCar car);

    // Getters for hitbox
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}