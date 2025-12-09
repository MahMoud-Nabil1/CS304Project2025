import GameObjects.*;
import ScoreRelated.ScoreEntry;
import Texture.TextureReader;
import GameController.*;
import com.sun.opengl.util.j2d.TextRenderer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import GameController.TextureHandling.*;

import static GameController.GameController.obstaclesList;
import static GameController.TextureHandling.*;


public class CarGLEventListener implements MouseListener, GLEventListener, KeyListener, ActionListener, MouseMotionListener {
    int GameState = 0;
    final int Menu = 0;
    final int Game = 1;
    final int Pause = 2;
    final int End = 3;
    final int Instructions = 4;
    int windowWidth = 1;
    int windowHeight = 1;
    ArrayList<buttons> menuButtons = new ArrayList<>();
    ArrayList<buttons> pauseButtons = new ArrayList<>();
    buttons inGamePauseBtn;
    ArrayList<buttons> endButtons = new ArrayList<>();
    int mx = 0, my = 0;
    boolean clicked = false;
    static String assetsFolderName = "Assets/";




    // Score Variables
    public static int frameCounter = 0;
    int score = 0;
    int xScore = 10;
    int yScore = 85;

    // Stats Sexy
    int lightCarsKilled = 0;

    // Inside Class Variables
    int healthAnimCounter = 0; // Counts frames for the health bar
    TextRenderer renderer;

    //---------------------- For Shehab HealthBar ----------------------------------------



    int xHealthBar=50;
    int yHealthBar=50;

    // --- Variables ---
    private double glowTimer = 0;
    private final double GLOW_PERIOD = 2.0;
    private int whiteTextureId;


    //--------------------------For Shehab Collegians-----------------------------------------------------------
    public static ArrayList<GameObject> allObjects = new ArrayList<>();


    //-------------------------------------------------------------------------------------


    public BitSet keyBits = new BitSet(256);

    //---------Borders---------
    int maxWidth = 100;
    int maxHeight = 100;

    //---------initial-coordinates---------
    public static int angle = 0;
    PlayerCar player;
    float curX = maxWidth / 2.0f;
    float curY = maxHeight / 2.0f;






    //---------init-func---------
    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        drawClass.initGameLogic();


        // Disable Depth Test (Crucial for 2D games so layers stack correctly)
        // If your images are flickering or disappearing, UNCOMMENT this:
        // gl.glDisable(GL.GL_DEPTH_TEST);

        // -------------------------------------------------------------------------------------
        // 1. TEXTURE LOADING (The Clean Way)
        // -------------------------------------------------------------------------------------
        // This single line now loads: Backgrounds, Cars, Buttons, Score Numbers, AND Health Bars.
        TextureHandling.MainTextures(gld);

        // This loads the colored powerups
        TextureHandling.PowerUpTextures(gld);


        Music.playMusic("MusicAssets/MainMenuMusic.wav");


        // -------------------------------------------------------------------------------------
        // 2. OBJECT INITIALIZATION
        // -------------------------------------------------------------------------------------
        renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36));
        whiteTextureId = createBlankTexture(gl); // Keep this, it's specific to the Health Bar logic

        // Mostafa Button-initialization
        menuButtons.clear(); // Good habit to clear lists in init
        menuButtons.add(new buttons(45, 45, 20, 10, 4));
        menuButtons.add(new buttons(45, 30, 20, 10, 5));
        menuButtons.add(new buttons(45, 15, 20, 10, 6));

        inGamePauseBtn = new buttons(85, 90, 9, 9, 15);

        pauseButtons.clear();
        pauseButtons.add(new buttons(45, 30, 20, 10, 10));
        pauseButtons.add(new buttons(45, 15, 20, 10, 6));

        endButtons.clear();
        endButtons.add(new buttons(65, 15, 20, 10, 12));
        endButtons.add(new buttons(25, 15, 20, 10, 11));

        // Belal All Objects Taking
        allObjects.clear();
        obstaclesList.clear();
        GameController.LightCars.clear(); // Don't forget to clear enemies too!
        GameController.powerUpsList.clear(); // And powerups!

        // Initialize the Bullet Pool (The Optimization we made earlier)
        BulletPool.init(100);

        player = new PlayerCar((int) curX, (int) curY);
        allObjects.add(player);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {

        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        if (GameState == Menu) {
            drawClass.DrawBackground(gl,2 , textures);

            for (buttons btn : menuButtons){
                btn.draw(gl, textures, maxWidth, maxHeight);
            }
            gl.glEnable(GL.GL_TEXTURE_2D);
            gl.glDisable(GL.GL_BLEND);

        } else if (GameState == Game) {
            drawClass.background_loop(gl , textures);
            drawClass.renderAndLogic(gl, textures);
            drawClass.drawSprite(gl, (float) player.getPosX(), (float) player.getPosY(), 1, 1.4f , textures);
            drawClass.drawBullets(gl , player , textures);
            player.updateInvincibility();
            updateMovement();
            //---------------Collision Shehab-----------------------
            updateGameLogic();
            //-------Score---HealthBar  Related
            score(gl, xScore, yScore);
            inGamePauseBtn.draw(gl, textures, maxWidth, maxHeight);
            //drawScoreText(glAutoDrawable);
            drawHealthBar(gl, player.health, 100.0f, healthTextures[0], 3, 85, 40, 20);
            drawClass.drawPowerUps(gl , player);
            checkPlayerDeath();
        }else if(GameState == Pause) {
            drawClass.DrawBackground(gl , 3 , textures);


            for (buttons btn : pauseButtons) {
                btn.draw(gl, textures, maxWidth, maxHeight);
            }
        }else if(GameState == End) {
            drawClass.DrawBackground(gl , 16 ,textures);
            for (buttons btn : endButtons) {
                btn.draw(gl, textures, maxWidth, maxHeight);
            }

        } else if (GameState == Instructions) {

        }
    }

    //---------------------------- KeyBoardHandling ---------------------------

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyBits.set(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyBits.clear(keyCode);
        angle = 0;

    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    public boolean isKeyReleased(final int keyCode) {
        return keyBits.get(keyCode);
    }

    //---------------------------- MouseHandling ---------------------------
    @Override
    public void mouseClicked(MouseEvent e) {
        double mouseX = ((double) e.getX() / windowWidth) * maxWidth;
        double mouseY = ((double) (windowHeight - e.getY()) / windowHeight) * maxHeight;


        if (GameState == Menu) {
            for (int i = 0; i < menuButtons.size(); i++) {
                if (menuButtons.get(i).isClicked(mouseX, mouseY , maxWidth , maxHeight)) {
                    handleButton(i);
                    return;
                }
            }
        }

        else
        if (GameState == Pause) {
            for (int i = 0; i < pauseButtons.size(); i++) {
                if (pauseButtons.get(i).isClicked(mouseX, mouseY , maxWidth , maxHeight)) {
                    handleButton(i + 3);// Resume أو Quit
                    return;
                }
            }
        }

        else
        if (GameState == Game) {
            if (inGamePauseBtn.isClicked(mouseX, mouseY ,  maxWidth , maxHeight)) {
                GameState = Pause;
            }
        }
        if (GameState == End) {
            for (int i = 0; i < endButtons.size(); i++) {
                if (endButtons.get(i).isClicked(mouseX, mouseY , maxWidth , maxHeight)) {
                    handleButton(i + 6);
                }
            }
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
        clicked = true;
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        windowWidth = i2;
        windowHeight = i3;
    }

    // Player Movement
    public void updateMovement() {

        if(isKeyPressed(KeyEvent.VK_Z)){
            player.nitroOn();
        }
        if (isKeyPressed(KeyEvent.VK_SPACE)) {
            player.shoot();
        }

        float currentSpeed = (float) player.getSpeed();
        if (isKeyPressed(KeyEvent.VK_DOWN) && isKeyPressed(KeyEvent.VK_RIGHT) && curY > 0 && curX < maxWidth - 18) {
            curY -= currentSpeed;
            curX += currentSpeed;
            angle = 10;

        }

        else if (isKeyPressed(KeyEvent.VK_DOWN) && isKeyPressed(KeyEvent.VK_LEFT) && curY > 0 && curX > 7) {
            curY -= currentSpeed;
            curX -= currentSpeed;
            angle = -10;

        }
        else if (isKeyPressed(KeyEvent.VK_UP) && isKeyPressed(KeyEvent.VK_RIGHT) && curY < maxHeight - 10 && curX < maxWidth - 18) {
            curY += currentSpeed;
            curX += currentSpeed;
            angle = -10;
        }

        else if (isKeyPressed(KeyEvent.VK_UP) && isKeyPressed(KeyEvent.VK_LEFT) && curY < maxHeight - 18 && curX > 7) {
            curY += currentSpeed;
            curX -= currentSpeed;
            angle = 10;
        }

        else if (isKeyPressed(KeyEvent.VK_UP) && curY < maxHeight - 10){
            curY += currentSpeed;
        }

        else if (isKeyPressed(KeyEvent.VK_DOWN) && curY > 0){
            curY -= (float) (currentSpeed+.2);
        }

        else if (isKeyPressed(KeyEvent.VK_LEFT) && curX > 7){
            curX -= currentSpeed;
            angle = 10;
        }

        else if (isKeyPressed(KeyEvent.VK_RIGHT) && curX < maxWidth - 18) {
            curX += currentSpeed;
            angle = -10;
        }

        player.setPosY(curY);
        player.setPosX(curX);
    }



    // button handling

    private void handleButton(int id) {

        switch (id) {
            case 0:
            case 6:
            case 3: {
                GameState = Game;
                Music.playMusic("MusicAssets/GameBackGround.wav");
            }break;
            case 1: GameState = Instructions; break;
            case 2:
            case 4:
                System.exit(0); break;
            case 5: {
                GameState = Pause;
                Music.stopMusic();
            }break;
            case 7: {
                GameState = Menu;
                Music.playMusic("MusicAssets/MainMenuMusic.wav");
            }break;
            }
    }



    // ----------------------------------Score-----------------------
    public void score(GL gl, int x, int y) {
        // 1. Update logic (Keep your frame counter-logic)
        int score=GameController.score;
        frameCounter++;
        if (frameCounter > 10) {
            if (GameController.doubleScoreActive) {
                GameController.score+= (int) (2*GameController.gameSpeed);
            } else {
                GameController.score += 1;
            }
            System.out.println(GameController.score);
            frameCounter = 0;
        }
        // 2. Convert Score to String to get individual digits
        String scoreString = Integer.toString(GameController.score);
        // 3. Drawing Logic
        gl.glEnable(GL.GL_BLEND);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        // Iterate through every digit in the string
        for (int i = 0; i < scoreString.length(); i++) {
            // Get the character (e.g., '1') and convert to int (1)
            char c = scoreString.charAt(i);
            int digit = Character.getNumericValue(c);
            gl.glBindTexture(GL.GL_TEXTURE_2D, scoreTextures[digit]);
            gl.glPushMatrix();
            // MATH CONVERSION
            // xOffset: multiply index 'i' by a spacing value (e.g., 10 pixels) so digits don't overlap
            int digitWidth = 4; // Adjust this based on how wide your numbers are
            int currentX = x + (i * digitWidth);
            double glX = currentX / 50.0 - 1.0;
            double glY = y / 50.0 - 1.0;
            gl.glTranslated(glX, glY, 0);
            gl.glScaled(0.13, 0.13, 1); // Reduced scale slightly so numbers fit better
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, 1.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();

            gl.glPopMatrix();
        }
        gl.glDisable(GL.GL_BLEND);
    }


    public void saveAndSortScore(String userName, int userScore) {
        ArrayList<ScoreEntry> allScores = new ArrayList<>();
        File file = new File("highscores.txt");

        // 1. READ EXISTING SCORES
        if (file.exists()) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    // We expect format: "Name:Score"
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String name = parts[0];
                        int score = Integer.parseInt(parts[1]);
                        allScores.add(new ScoreEntry(name, score));
                    }
                }
                scanner.close();
            } catch (Exception e) {
                System.out.println("Error reading scores: " + e.getMessage());
            }
        }

        // 2. ADD CURRENT USER
        allScores.add(new ScoreEntry(userName, userScore));

        // 3. SORT (Highest first)
        Collections.sort(allScores);

        // 4. WRITE BACK TO FILE
        try {
            FileWriter writer = new FileWriter(file); // Overwrite file
            for (ScoreEntry entry : allScores) {
                writer.write(entry.toString() + "\n");
            }
            writer.close();
            System.out.println("Score saved successfully!");
        } catch (IOException e) {
            System.out.println("Error writing scores: " + e.getMessage());
        }
    }

    //----------------------------------Health Bar ----------------------------------

    public void drawScoreText(GLAutoDrawable drawable) {
        // 1. Update Score Logic (Keep your existing frame counter)
        frameCounter++;
        if (frameCounter > 10) {
            score++;
            System.out.println(score);
            frameCounter = 0;
        }

        // 2. Prepare the text
        String textToDraw = "Score: " + score;

        // 3. Draw the text
        // We need the width and height of the window to position text correctly
        int width = drawable.getWidth();
        int height = drawable.getHeight();

        renderer.beginRendering(width, height);

        // Set Color (R, G, B, Alpha) - This is Yellow
        renderer.setColor(1.0f, 1.0f, 0.0f, 1.0f);

        // Draw the string
        // x = 10 pixels from left
        // y = height - 50 pixels (Top left corner)
        renderer.draw(textToDraw, 10, height - 50);

        renderer.endRendering();
        GL gl = drawable.getGL();
        gl.glColor3f(1.0f, 1.0f, 1.0f);
    }

    private float[] getHealthColor(float healthPercent) {
        if (healthPercent > 0.75f) return new float[]{0.2f, 1.0f, 0.2f}; // Green
        if (healthPercent > 0.50f) return new float[]{1.0f, 1.0f, 0.2f}; // Yellow
        if (healthPercent > 0.25f) return new float[]{1.0f, 0.6f, 0.0f}; // Orange
        return new float[]{1.0f, 0.2f, 0.2f}; // Red
    }

    private int createBlankTexture(GL gl) { // Changed GL2 to GL
        int[] textureId = new int[1];
        gl.glGenTextures(1, textureId, 0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureId[0]); // Changed GL2.GL_... to GL.GL_...

        ByteBuffer buffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());
        buffer.put((byte) 255).put((byte) 255).put((byte) 255).put((byte) 255); buffer.flip();

        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, 1, 1, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buffer);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);

        return textureId[0];
    }

    public void drawHealthBar(GL gl, float currentHealth, float maxHealth, int frameTextureId, float x, float y, float width, float height) {
        // Calculate health percentage
        float healthPercent = Math.max(0.0f, Math.min(1.0f, currentHealth / maxHealth));
        float[] baseColor = getHealthColor(healthPercent);

        // Update Glow
        glowTimer += 1.0 / 60.0;
        float glowIntensity = (float) (0.5 + 0.5 * Math.sin(glowTimer * Math.PI * 2 / GLOW_PERIOD));

        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        // --- COORDINATE CONVERSION ---
        // Convert Game Coords (0..100) to OpenGL Coords (-1..1)
        float ndcX = (x / 50.0f) - 1.0f;
        float ndcY = (y / 50.0f) - 1.0f;
        float ndcW = width / 50.0f;
        float ndcH = height / 50.0f;

        // =========================================================
        // STEP 1: Draw the Metal Frame FIRST (Background)
        // =========================================================
        gl.glBindTexture(GL.GL_TEXTURE_2D, frameTextureId);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f); // Reset color to white

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 0); gl.glVertex2f(ndcX,        ndcY);
        gl.glTexCoord2f(1, 0); gl.glVertex2f(ndcX + ndcW, ndcY);
        gl.glTexCoord2f(1, 1); gl.glVertex2f(ndcX + ndcW, ndcY + ndcH);
        gl.glTexCoord2f(0, 1); gl.glVertex2f(ndcX,        ndcY + ndcH);
        gl.glEnd();

        // =========================================================
        // STEP 2: Draw the Green Bar SECOND (Overlay)
        // =========================================================
        gl.glBindTexture(GL.GL_TEXTURE_2D, whiteTextureId);

        // We make it slightly see-through (0.7f alpha) so it looks like a glass/hologram
        gl.glColor4f(baseColor[0], baseColor[1], baseColor[2], 0.6f + 0.3f * glowIntensity);

        // --- ALIGNMENT NUMBERS (Tweaked for your image) ---
        // These percentages control where the green bar sits inside the frame
        float offX = ndcW * 0.32f;      // Push right by 22%
        float offY = ndcH * 0.47f;      // Push up by 40%
        float maxFillW = ndcW * 0.55f;  // Width is 73% of the frame
        float fillH = ndcH * 0.08f;     // Height is 25% of the frame

        float currentFillW = maxFillW * healthPercent;

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 0); gl.glVertex2f(ndcX + offX,                ndcY + offY);
        gl.glTexCoord2f(1, 0); gl.glVertex2f(ndcX + offX + currentFillW, ndcY + offY);
        gl.glTexCoord2f(1, 1); gl.glVertex2f(ndcX + offX + currentFillW, ndcY + offY + fillH);
        gl.glTexCoord2f(0, 1); gl.glVertex2f(ndcX + offX,                ndcY + offY + fillH);
        gl.glEnd();

        gl.glDisable(GL.GL_BLEND);
        gl.glColor3f(1.0f, 1.0f, 1.0f);

    }    //------------------------------------Collisions---------------------------------


    //--------------------------For Shehab Collision--------------------------------------

    // Helper method to keep code clean
    private void handlePlayerCollision(PlayerCar player, GameObject other) {

        // SCENARIO A: Player hits Obstacle (Invincible)
        if (other instanceof Obstacles) {
            // Player takes massive damage (or dies instantly)
            player.takeDamage(50);
            System.out.println("Hit Obstacle! Player hurt.");
            // We DO NOT call other.takeDamage(), so Obstacle stays alive (Invincible)
        }

        // SCENARIO B: Player hits Light Car
        else if (other instanceof LightCar) {
            int crashDamage = 20;

            player.takeDamage(crashDamage); // Player takes normal damage
            other.takeDamage(100);          // Light car gets destroyed (or takes damage)
            System.out.println("Crashed into Light Car!");
        }

        // SCENARIO C: Player hits Weight Car (Assuming you have a class named WeightCar)
        // Note: You didn't provide WeightCar class, but here is the logic:
    /* else if (other instanceof WeightCar) {
        int crashDamage = 50; // Player takes MORE damage

        player.takeDamage(crashDamage);
        other.takeDamage(50); // Weight car takes damage too
        System.out.println("Crashed into Heavy Car!");
    }
    */
    }

    public void updateGameLogic() {
        // ------For player hit Game Objects-----
        checkCollision();

        // 2. Bullets vs Obstacles
        for (int i = 0; i < player.bullets.size(); i++) {
            Bullet b = player.bullets.get(i);
            if (b.timer <= 0) continue;

            // Create a hitbox for the bullet (make it slightly larger for easier hitting)
            Rectangle bulletRect = new Rectangle((int)b.posX, (int)b.posY, 4, 4);

            for (GameObject obj : allObjects) {
                if (obj.alive && obj instanceof Obstacles) {
                    if (bulletRect.intersects(obj.getBounds())) {
                        System.out.println("HIT! Bullet destroyed obstacle!");
                        b.timer = -1; // Destroy the bullet
                        break;
                    }
                }
                if (obj.alive && obj instanceof LightCar){
                    if (bulletRect.intersects(obj.getBounds())) {
                        System.out.println("HIT! Bullet destroyed Car");
                        obj.takeDamage(40);
                        b.timer = -1;
                        break;
                    }
                }

            }

        }
        // =========================================================
        // 3. STATE UPDATE: Map Health to Alive
        // =========================================================

        for (LightCar car : GameController.LightCars) {
            // Check if health is 0 BUT it is still marked as alive
            // This ensures we only give points ONCE per car
            if (car.health <= 0 && car.alive) {
                // 1. Change State
                car.alive = false;
                // 2. Add Score
                GameController.score += 100; // Add 100 points (Change this value as needed)
                // 3. Increment Kill Counter
                lightCarsKilled++;
                System.out.println("Enemy Destroyed! Total Kills: " + lightCarsKilled);
            }
        }

        // (Optional: Do the same for general objects if they have health)
        for (GameObject obj : allObjects) {
            if (obj instanceof LightCar && obj.health <= 0) {
                obj.alive = false;
            }
        }



        //====================================================================
        // 4. Remove Dead Objects
        //====================================================================
        for (int i = allObjects.size() - 1; i >= 0; i--) {
            if (!allObjects.get(i).alive) {
                allObjects.remove(i);
            }
        }
        //---------- Sync specific lists----------------
        for (int i = obstaclesList.size() - 1; i >= 0; i--) {
            if (!obstaclesList.get(i).alive) {
                obstaclesList.remove(i);
            }
        }

        // ------------------Light Cars Removing-----------------------
        for (int i = GameController.LightCars.size()-1 ; i>=0 ; i--){
            if (!GameController.LightCars.get(i).alive){
                GameController.LightCars.remove(i);
            }
        }

        // Clean PowerUps (IMPORTANT: This fixes "ghost" powerups)
        for (int i = GameController.powerUpsList.size() - 1; i >= 0; i--) {
            if (!GameController.powerUpsList.get(i).alive) {
                GameController.powerUpsList.remove(i);
            }
        }

    }

    public void checkCollision() {
        //  Player vs GameObjects
        for (GameObject obj : allObjects) {
            if (obj instanceof PlayerCar) continue;

            if (obj.alive && player.getBounds().intersects(obj.getBounds())) {

                // --- CASE A: Obstacle ---
                if (obj instanceof Obstacles) {
                    if (player.invincibilityTimer == 0) {
                        player.takeDamage(20);
                        player.invincibilityTimer = 40;
                        System.out.println("CRASH! Hit Obstacle.");
                    }
                }

                // --- CASE B: PowerUp (THE FIX) ---
                else if (obj instanceof PowerUp) {
                    PowerUp p = (PowerUp) obj;
                    System.out.println("DEBUG: Physical HIT with " + p.getClass().getSimpleName());
                    // Only collect if we haven't already
                    if (!p.isCollected) {
                        System.out.println("DEBUG: >>> ACTIVATING EFFECT for " + p.getClass().getSimpleName() + " <<<");
                        p.apply(player);       // 1. Give Effect
                        p.isCollected = true;  // 2. Mark as collected

                        // 3. Move off screen so we don't hit it again
                        p.setPosY(-5000);

                        System.out.println("COLLECTED POWERUP!");
                    }
                }

                //--- Case C: Player Hit a LightCar ---
                else if(obj instanceof LightCar){
                    if (player.invincibilityTimer == 0) {
                        player.takeDamage(20);  // Less damage
                        player.invincibilityTimer = 40;
                        obj.takeDamage(100);  // Enemy dies
                        System.out.println("Hit LightCar!");
                    }
                }
            }

            // You can add Bullet collision logic here later
        }
    }

    public void checkPlayerDeath(){
        if (player.health <= 0) {
            GameState = End; // Switch to End Screen (State 3)
            Music.playMusic("MusicAssets/GameOverMusic.wav");
            player.health=100;
            score = 0;
        }
    }

    /*public void debugingPowerups(){
        for (GameObject obj : allObjects) {
            if (obj instanceof PlayerCar) continue;

            // --- NEW DEBUG RADAR ---
            // Only print for PowerUps so we don't spam the console too much
            if (obj instanceof PowerUp) {
                PowerUp p = (PowerUp) obj;
                //System.out.println("RADAR: PowerUp Y=" + p.getPosY() + " | Player Y=" + player.getPosY() +
                //        " | PowerUp Size=" + p.getWidth() + "x" + p.getHeight());
            }
            // ----
        }
    }*/
// In CarGLEventListener.java+-`

    public void drawHitboxDebug(GL gl, Rectangle rect) {
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1.0f, 0.0f, 0.0f);

        gl.glPushMatrix();

        // FIXED: Changed -1.0 to -0.9 to match your drawSprite logic
        double x = rect.x / 50.0 - 0.9;
        double y = rect.y / 50.0 - 0.9;

        double w = rect.width / 50.0;
        double h = rect.height / 50.0;

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex2d(x, y);
        gl.glVertex2d(x + w, y);
        gl.glVertex2d(x + w, y + h);
        gl.glVertex2d(x, y + h);
        gl.glEnd();

        gl.glPopMatrix();

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
    }

    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {}
    @Override
    public void actionPerformed(ActionEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

}
