package GameObjects;

import GameController.GameController;

public abstract class PowerUp extends GameObject {

    // Fields to track state
    public boolean isCollected = false; // Is it currently active on the player?
    public int durationFrames = 0;      // How long the effect lasts (0 for instant items)
    public double speed = GameController.gameSpeed;// How fast it falls down the screen
   // public float y;
   // public float x;

    public PowerUp(float x, float y) {
        super(x, y);
        this.width = 8;
        this.height = 2;

    }

    // --- THE UPDATE METHOD ("The Brain") ---
    public void update(PlayerCar car) {
        if (!isCollected) {
            // PHASE 1: Falling
            // It moves down based on its own speed + the game speed (so it moves with the road)
           // y -= (float) GameController.gameSpeed;

            // FIX 2: Update the PARENT variables (posY/posX) so getBounds() works -- Shehab Fix
            setPosY((float) (getPosY() - GameController.gameSpeed));

            // Sync posX if needed (though it doesn't change usually)
            // setPosX(...)

        } else {
            // PHASE 2: Active Effect (Counting down)
            if (durationFrames > 0) {
                durationFrames--;

                // If time runs out, remove effect
                if (durationFrames <= 0) {
                    remove(car);
                    alive = false; // Remove from game
                    // We will remove it from the list in the Main Class loop
                } else {alive = false;}
            }
        }
    }


    // Shehab Hit box
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Abstract methods children must implement
    public abstract void apply(PlayerCar car);
    public abstract void remove(PlayerCar car);

}