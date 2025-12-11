import GameObjects.*;
import ScoreRelated.ScoreEntry;
import GameController.*;
import com.sun.opengl.util.j2d.TextRenderer;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
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
import static GameController.GameController.powerUpsList;
import static GameController.TextureHandling.*;


public class CarGLEventListener implements MouseListener, GLEventListener, KeyListener, ActionListener, MouseMotionListener {
    int GameState = 0;
    final int Menu = 0;
    final int Game = 1;
    final int Pause = 2;
    final int End = 3;
    final int Instructions = 4;
    final int HighScoreState = 5;
    int windowWidth = 1;
    int windowHeight = 1;
    ArrayList<buttons> menuButtons = new ArrayList<>();
    ArrayList<buttons> pauseButtons = new ArrayList<>();
    ArrayList<String> highScoreStrings = new ArrayList<>();
    buttons inGamePauseBtn;
    ArrayList<buttons> endButtons = new ArrayList<>();
    buttons instructionsBtn;
    buttons highScoreBtn = new buttons(80, 80, 25, 8, 20);
    buttons backBtn = new buttons(10, 90, 15, 8, 19); // Top Left Back Button

    int mx = 0, my = 0;
    boolean clicked = false;

    // Score Variables
    public static int frameCounter = 0;
    int xScore = 10;
    int yScore = 85;

    boolean scoreSaved = false;

    // Stats Sexy
    int lightCarsKilled = 0;

    // Inside Class Variables
    TextRenderer renderer;

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
        TextureHandling.MainTextures(gld);
        TextureHandling.PowerUpTextures(gld);

        Music.setVolume(70);
        Music.playMusic("MusicAssets/MainMenuMusic.wav");


        renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36));
        whiteTextureId = createBlankTexture(gl); // Keep this, it's specific to the Health Bar logic

        // Mostafa Button-initialization
        menuButtons.clear(); // Good habit to clear lists in init
        menuButtons.add(new buttons(45, 45, 20, 10, 4)); //Play
        menuButtons.add(new buttons(45, 30, 20, 10, 5)); //Instructions
        menuButtons.add(new buttons(45, 15, 20, 10, 6)); // Quit
        menuButtons.add(highScoreBtn);

        inGamePauseBtn = new buttons(85, 90, 9, 9, 15);

        pauseButtons.clear();
        pauseButtons.add(new buttons(45, 30, 20, 10, 10));
        pauseButtons.add(new buttons(45, 15, 20, 10, 6));

        endButtons.clear();
        endButtons.add(new buttons(65, 15, 20, 10, 12));
        endButtons.add(new buttons(25, 15, 20, 10, 11));

        instructionsBtn  = new buttons(85, 85, 7, 7, 19);

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
            if(player.invincibilityTimer % 5 == 0) {
                // This handles the Car, Rotation, AND Nitro all in one go
                drawClass.drawPlayerWithNitro(gl, player, textures, effectsTextures);
            }
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
            drawBlueBarPowerUp( gl);
            drawClass.drawPowerUps(gl , player);
            checkPlayerDeath();
        }else if(GameState == Pause) {
            drawClass.DrawBackground(gl , 3 , textures);


            for (buttons btn : pauseButtons) {
                btn.draw(gl, textures, maxWidth, maxHeight);
            }
        }else if(GameState == End) {
            drawClass.DrawBackground(gl , 16 ,textures);
            drawFrozenScore(gl,50,43);
            saving();
            for (buttons btn : endButtons) {
                btn.draw(gl, textures, maxWidth, maxHeight);
            }
        } else if (GameState == Instructions) {
            drawClass.DrawBackground(gl , 18 ,textures);
            instructionsBtn.draw(gl, textures, maxWidth, maxHeight);
        }
        else if (GameState == HighScoreState) {
            // 1. Draw Background (Reuse Menu or End background)
            drawClass.DrawBackground(gl, 2, textures);

            // 2. Draw Text
            drawHighScoresText(glAutoDrawable);

            // 3. Draw Back Button
            backBtn.draw(gl, textures, maxWidth, maxHeight);
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

    //---------------------------- MouseHandling ---------------------------
    @Override
    public void mouseClicked(MouseEvent e) {
        double mouseX = ((double) e.getX() / windowWidth) * maxWidth;
        double mouseY = ((double) (windowHeight - e.getY()) / windowHeight) * maxHeight;

        if (GameState == Menu) {
            for (int i = 0; i < menuButtons.size(); i++) {
                if (menuButtons.get(i).isClicked(mouseX, mouseY, maxWidth, maxHeight)) {
                    // FIX IS HERE:
                    if (i == 3) {
                        handleButton(8); // Force High Score button to use ID 8
                    } else {
                        handleButton(i); // Use normal IDs (0, 1, 2) for others
                    }
                    return;
                }
            }
        }
        else if (GameState == Pause) {
            for (int i = 0; i < pauseButtons.size(); i++) {
                if (pauseButtons.get(i).isClicked(mouseX, mouseY, maxWidth, maxHeight)) {
                    handleButton(i + 3); // Resume or Quit
                    return;
                }
            }
        }
        else if (GameState == Game) {
            if (inGamePauseBtn.isClicked(mouseX, mouseY, maxWidth, maxHeight)) {
                GameState = Pause;
            }
        }
        else if (GameState == End) {
            for (int i = 0; i < endButtons.size(); i++) {
                if (endButtons.get(i).isClicked(mouseX, mouseY, maxWidth, maxHeight)) {
                    handleButton(i + 6);
                }
            }
        }
        else if (GameState == Instructions) {
            if (instructionsBtn.isClicked(mouseX, mouseY, maxWidth, maxHeight)) {
                GameState = Menu;
            }
        }
        // ADD THIS for the High Score Screen Back Button
        else if (GameState == HighScoreState) {
            if (backBtn.isClicked(mouseX, mouseY, maxWidth, maxHeight)) {
                handleButton(9); // ID 9 = Back to Menu
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
    private void updateMovement() {
        // 1. Actions
        if(isKeyPressed(KeyEvent.VK_Z)){
            player.nitroOn();
        }
        if (isKeyPressed(KeyEvent.VK_SPACE)) {
            player.shoot();
        }

        float currentSpeed = (float) player.getSpeed();

        // 2. Default Rotation (Fixes "stuck" angles)
        // We reset the angle to 0 every frame.
        // If a key is pressed below, it will override this to 10 or -10.
        angle = 0;

        // 3. Horizontal Logic (Left / Right)
        // We use "&& !isKeyPressed..." to prevent jitter if you hold both Left + Right
        if (isKeyPressed(KeyEvent.VK_LEFT) && !isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (curX > 7) {
                curX -= currentSpeed;
            }
            angle = 10; // Tilt Left
        }

        if (isKeyPressed(KeyEvent.VK_RIGHT) && !isKeyPressed(KeyEvent.VK_LEFT)) {
            if (curX < maxWidth - 18) {
                curX += currentSpeed;
            }
            angle = -10; // Tilt Right
        }

        // 4. Vertical Logic (Up / Down)
        // Independent of X, allowing smooth diagonals
        if (isKeyPressed(KeyEvent.VK_UP) && !isKeyPressed(KeyEvent.VK_DOWN)) {
            if (curY < maxHeight - 10) {
                curY += currentSpeed;
            }
        }

        if (isKeyPressed(KeyEvent.VK_DOWN) && !isKeyPressed(KeyEvent.VK_UP)) {
            if (curY > 0) {
                // I kept your logic where reversing is slightly faster (+0.2)
                curY -= (currentSpeed + 0.2f);
            }
        }

        // 5. Update Player Position
        player.setPosY(curY);
        player.setPosX(curX);
    }

    // 1. UPDATE BUTTON HANDLING TO RESET THE SAVE FLAG
    private void handleButton(int id) {
        switch (id) {
            case 3: // Pause -> Resume
            {
                GameState = Game;
                Music.playMusic("MusicAssets/GameBackGround.wav");
            } break;

            case 0: // Menu -> Start
            case 6: // Pause -> Restart
            case 11: // End -> Restart
            {
                // 1. STOP "GHOST" MOVEMENT (Fixes "Controls Suck")
                // We clear the keyboard memory so the car doesn't move automatically
                keyBits.clear();
                angle = 0; // Reset rotation so car faces up

                // 2. RESET POSITION TO CENTER (Fixes "Spawn Place")
                curX = maxWidth / 2.0f;
                curY = maxHeight / 2.0f;

                // 3. CLEAN UP OLD OBJECTS
                allObjects.clear();
                GameController.LightCars.clear();
                GameController.HeavyCars.clear();
                obstaclesList.clear();
                powerUpsList.clear();

                // 4. RESET LOGIC & SPAWNERS
                drawClass.initGameLogic(); // Resets spawn timers
                frameCounter = 0;
                GameController.gameSpeed = 1.0;
                GameController.score = 0;
                GameController.finalScore = 0;
                lightCarsKilled = 0;
                scoreSaved = false;

                // 5. CREATE NEW PLAYER
                player = new PlayerCar((int) curX, (int) curY);
                player.health = 100;
                player.nitro = 200;

                // Add player to collision list
                allObjects.add(player);

                // 6. START GAME
                GameState = Game;
                Music.playMusic("MusicAssets/GameBackGround.wav");
            } break;

            case 1: GameState = Instructions; break;
            case 2:
            case 12: // Exit
                System.exit(0); break;
            case 5: {
                GameState = Pause;
                Music.stopMusic();
            } break;
            case 4:
            case 7: {
                GameState = Menu;
                Music.playMusic("MusicAssets/MainMenuMusic.wav");
            } break;

            case 8: // OPEN HIGH SCORES
            {
                loadHighScores(); // Read file now
                GameState = HighScoreState;
            } break;

            case 9: // BACK TO MENU
            {
                GameState = Menu;
            } break;

        }
    }
    // ----------------------------------Score-----------------------
    public void score(GL gl, int x, int y) {
        // 1. Update logic (Keep your frame counter-logic)
        frameCounter++;
        if (frameCounter > 10) {
            if (GameController.doubleScoreActive) {
                GameController.score+= (int) (2*GameController.gameSpeed);
            } else {
                GameController.score += 1;
            }
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

        try {
            // Create file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
            }

            // READ SCORES
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    allScores.add(new ScoreEntry(parts[0], Integer.parseInt(parts[1])));
                }
            }
            scanner.close();

            // ADD NEW SCORE
            allScores.add(new ScoreEntry(userName, userScore));

            // SORT
            Collections.sort(allScores);

            // WRITE BACK
            FileWriter writer = new FileWriter(file);
            for (ScoreEntry entry : allScores) {
                writer.write(entry.toString() + "\n");
            }
            writer.close();
            System.out.println("Saved: " + userName + " -> " + userScore);

        } catch (IOException e) {
            System.out.println("Error saving score: " + e.getMessage());
            e.printStackTrace();
        }
    }    // 2. UPDATE THE SAVING LOGIC
    public void saving() {
        // Only run this ONCE per death
        if (!scoreSaved) {
            scoreSaved = true; // Lock it so it doesn't pop up 60 times a second

            // Use invokeLater to show the popup smoothly over the GLCanvas
            SwingUtilities.invokeLater(() -> {
                String name = JOptionPane.showInputDialog(null, "Game Over! Score: " + GameController.finalScore + "\nEnter your name:");

                if (name != null && !name.trim().isEmpty()) {
                    // Save the name and score
                    saveAndSortScore(name, GameController.finalScore);
                } else {
                    // Default if they click Cancel
                    saveAndSortScore("Unknown", GameController.finalScore);
                }
            });
        }
    }
    // --- HELPER 1: READ FILE ---
    public void loadHighScores() {
        highScoreStrings.clear();
        File file = new File("highscores.txt");

        if (!file.exists()) {
            highScoreStrings.add("No Scores Yet!");
            return;
        }

        try {
            Scanner scanner = new Scanner(file);
            // Read only top 10 lines
            int count = 0;
            while (scanner.hasNextLine() && count < 10) {
                String line = scanner.nextLine();
                // Format: "Name:Score" -> "1. Name - Score"
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    highScoreStrings.add((count + 1) + ". " + parts[0] + " - " + parts[1]);
                }
                count++;
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading scores");
        }
    }

    // --- HELPER 2: DRAW TEXT ---
    public void drawHighScoresText(GLAutoDrawable drawable) {
        int width = drawable.getWidth();
        int height = drawable.getHeight();

        renderer.beginRendering(width, height);
        renderer.setColor(1.0f, 0.8f, 0.0f, 1.0f); // Gold Color

        // Title
        renderer.draw("TOP 10 PLAYERS", width / 2-100 , height - 100);

        renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f); // White Color

        // Draw each score line
        int y = height - 150;
        for (String s : highScoreStrings) {
            renderer.draw(s, width / 2 - 250, y);
            y -= 40; // Move down for next line
        }

        renderer.endRendering();
    }

    public void drawFrozenScore(GL gl, int x, int y) {
        // 1. Draw Current Score (at x, y)
        drawNumber(gl, GameController.finalScore, x, y);

        // 2. Get the High Score from file
        int bestScore = getHighScoreFromFile();

        // 3. Check if we just beat the high score!
        if (GameController.finalScore > bestScore) {
            bestScore = GameController.finalScore;
        }

        int xOffset = 5;   // Change to 5 to move right, -5 to move left
        int yOffset = 11;  // Change to 20 to move down more, 10 to move up
        // 4. Draw High Score (Shifted down by 12 units)
        // We assume Y increases downwards.
        drawNumber(gl, bestScore, x+xOffset, y +yOffset);
    }
    public void drawNumber(GL gl, int number, int x, int y) {
        String scoreString = Integer.toString(number);

        gl.glEnable(GL.GL_BLEND);
        gl.glColor3f(1.0f, 1.0f, 1.0f);

        for (int i = 0; i < scoreString.length(); i++) {
            int digit = Character.getNumericValue(scoreString.charAt(i));

            gl.glBindTexture(GL.GL_TEXTURE_2D, scoreTextures[digit]);
            gl.glPushMatrix();

            int digitWidth = 6;
            int currentX = x + (i * digitWidth);

            // Coordinate conversion (0..100 to -1..1)
            double glX = (currentX / 50.0) - 1.0;
            double glY = 1.0 - (y / 50.0);

            gl.glTranslated(glX, glY, 0);
            gl.glScaled(0.10, 0.10, 1);

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
    public int getHighScoreFromFile() {
        File file = new File("highscores.txt");
        if (!file.exists()) return 0;

        try {
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Format is "Name:Score". We split by ":"
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    return Integer.parseInt(parts[1]); // Return the score part
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error reading high score: " + e.getMessage());
        }
        return 0; // Default if error
    }
    //----------------------------------Health Bar ----------------------------------

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

    }
    public void drawBlueBar(GL gl, float currentInfo, float maxInfo, int frameTextureId, float x, float y, float width, float height) {

        // 1. Safety Check: If no time left, don't draw anything
        if (currentInfo <= 0) return;

        // 2. Calculate Percentage (0.0 to 1.0)
        float percent = currentInfo / maxInfo;
        if (percent < 0) percent = 0;
        if (percent > 1) percent = 1;

        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        // --- COORDINATE CONVERSION ---
        float ndcX = (x / 50.0f) - 1.0f;
        float ndcY = (y / 50.0f) - 1.0f;
        float ndcW = width / 50.0f;
        float ndcH = height / 50.0f;


        // =========================================================
        // STEP 2: Draw the BLUE FILL
        // =========================================================
        gl.glBindTexture(GL.GL_TEXTURE_2D, whiteTextureId); // Use your blank white texture

        // SET COLOR TO BLUE (R=0, G=0.5, B=1.0)
        gl.glColor4f(0.0f, 0.5f, 1.0f, 0.8f);

        // --- ALIGNMENT (Matches your Health Bar offsets) ---
        float offX = ndcW * 0.32f;
        float offY = ndcH * 0.47f;
        float maxFillW = ndcW * 0.55f;
        float fillH = ndcH * 0.08f;

        float currentFillW = maxFillW * percent; // Shrink width based on time

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 0); gl.glVertex2f(ndcX + offX,                ndcY + offY);
        gl.glTexCoord2f(1, 0); gl.glVertex2f(ndcX + offX + currentFillW, ndcY + offY);
        gl.glTexCoord2f(1, 1); gl.glVertex2f(ndcX + offX + currentFillW, ndcY + offY + fillH);
        gl.glTexCoord2f(0, 1); gl.glVertex2f(ndcX + offX,                ndcY + offY + fillH);
        gl.glEnd();

        gl.glDisable(GL.GL_BLEND);
        gl.glColor3f(1.0f, 1.0f, 1.0f); // Reset color
    }
    public void drawBlueBarPowerUp(GL gl){
        if (player.nitro > 0) {
            drawBlueBar(
                    gl,
                    player.nitro, // Current Fuel (e.g. starts at 100)
                    200,          // Max Fuel (Full bar size)
                    healthTextures[0],
                    4,
                    85,
                    37,
                    15
            );
        }
    }


    //--------------------------For Shehab Collision--------------------------------------

    // Helper method to keep code clean
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
                        b.timer = -1; // Destroy the bullet
                        break;
                    }
                }
                if (obj.alive && obj instanceof LightCar){
                    if (bulletRect.intersects(obj.getBounds())) {
                        obj.takeDamage(40);
                        b.timer = -1;
                        break;
                    }
                }
                if (obj.alive && obj instanceof HeavyCar){
                    if (bulletRect.intersects(obj.getBounds())) {
                        obj.takeDamage(60);
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
            }
        }

        for (HeavyCar car : GameController.HeavyCars) {
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
        for (GameObject obj : allObjects) {
            if (obj instanceof HeavyCar && obj.health <= 0) {
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

        for (int i = GameController.HeavyCars.size()-1 ; i>=0 ; i--){
            if (!GameController.HeavyCars.get(i).alive){
                GameController.HeavyCars.remove(i);
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
                    }
                }

                // --- CASE B: PowerUp (THE FIX) ---
                else if (obj instanceof PowerUp) {
                    PowerUp p = (PowerUp) obj;
                    // Only collect if we haven't already
                    if (!p.isCollected) {
                        p.apply(player);       // 1. Give Effect
                        p.isCollected = true;  // 2. Mark as collected

                        // 3. Move off screen so we don't hit it again
                        p.setPosY(-5000);



                    }
                }

                //--- Case C: Player Hit a LightCar ---
                else if(obj instanceof LightCar){
                    if (player.invincibilityTimer == 0) {
                        player.takeDamage(((LightCar) obj).getDamage());  // Less damage
                        player.invincibilityTimer = 40;
                        obj.takeDamage(100);  // Enemy dies
                    }
                }
                //--- Case D: Player Hit a HeavyCar ---

                else if(obj instanceof HeavyCar){
                    if (player.invincibilityTimer == 0) {
                        player.takeDamage(((HeavyCar) obj).getDamage());  // Less damage
                        player.invincibilityTimer = 40;
                        obj.takeDamage(100);  // Enemy dies
                    }
                }
            }

            // You can add Bullet collision logic here later
        }
    }

    public void checkPlayerDeath(){
        if (player.health <= 0) {
            GameState = End; // Switch to End Screen (State 3)
            GameController.finalScore = GameController.score;
            Music.playMusic("MusicAssets/GameOverMusic.wav");
            //player.health=100;
            //score = 0;
            //GameController.score = 0;
            scoreSaved = false;
        }
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
