import GameObjects.*;
import ScoreRelated.ScoreEntry;
import Texture.TextureReader;
import GameController.GameController;
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
import java.sql.SQLOutput;
import java.util.*;
import java.util.List;
import GameObjects.*;

import javax.media.opengl.GLAutoDrawable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class CarGLEventListener extends CarListener implements MouseListener, GLEventListener, KeyListener, ActionListener, MouseMotionListener {
    double roadOffsetY = 0.0f;
    String UserName;
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


    String[] textureNames = {"BackGroundTest.png" , "car.png" , "MenuBackGround.png" , "PauseMenu.png"
            , "StartButton.png" , "InstructionsButton.png" , "QuitButton.png" , "obstacle.png","bullet.png"
            ,"endBackground.png", "continuebBotton.png" , "mainMenuButton.png" ,"playAgainButton.png",
            "pauseButton.png"
    };

    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];

    int[] textures = new int[textureNames.length];
    //--------------------- POWERUP TEXTURE----------------------------------------------------------
    String[] powerUpTextureNames={"blue 1","blue 2","blue 3","blue 4","blue 5","blue 6"
            ,"green 1","green 2","green 3","green 4","green 5","green 6"
            ,"red 1","red 2","red 3","red 4","red 5","red 6"
            ,"yellow 1","yellow 2","yellow 3","yellow 4","yellow 5","yellow 6"};
    TextureReader.Texture[] powerUpTexture = new TextureReader.Texture[powerUpTextureNames.length];
    int[] powerUpTextures = new int[powerUpTextureNames.length];


    //---------------------- For Shehab Score 0 1 2 3 4 5 6 7 8 9 ----------------------------
    String[] scoreTextureNames = {"0.png" , "1.png","2.png","3.png","4.png","5.png"
            ,"6.png","7.png","8.png","9.png"};

    TextureReader.Texture[] scoreTexture = new TextureReader.Texture[scoreTextureNames.length];

    int[] scoreTextures = new int[scoreTextureNames.length];

    // Score Variables
    int frameCounter = 0;
    int score = 0;
    int xScore = 10;
    int yScore = 80;

    // Inside Class Variables
    int healthAnimCounter = 0; // Counts frames for the health bar
    TextRenderer renderer;

    //---------------------- For Shehab HealthBar ----------------------------------------

    String[] healthTextureNames = {
             "HealthBar.png"
            ,"FullState1.png" ,"HealthReceviedFull.png" //100
            ,"3_4State1.png","3_4State2.png"            //75
            ,"HalfState1.png","HalfState2.png"
            ,"LowState1.png","LowState2.png"
    };

    TextureReader.Texture[] healthTexture = new TextureReader.Texture[healthTextureNames.length];
    int[] healthTextures = new int[healthTextureNames.length];

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
    int angle = 0;
    PlayerCar player;
    float curX = maxWidth / 2.0f;
    float curY = maxHeight / 2.0f;


    //---------Obstacles---------
    ArrayList<Obstacles> obstaclesList = new ArrayList<>();
    int numberOfObstacles = 3;
    int obstacleTextureIndex = 7;
    int[] obstaclesPositions = {13, 29, 45, 62, 79};


    int PowerUPTimer=0;



    //---------init-func---------
    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        //---------------------------- MainGame TextureHandling ---------------------------
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);
        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        //--------------------For PowerUps Textures---------------------------------------
        gl.glGenTextures(powerUpTextureNames.length, powerUpTextures, 0);

        for (int i = 0; i < powerUpTextureNames.length; i++) {
            try {
                if (i>=18 && i<24){
                    powerUpTexture[i] = TextureReader.readTexture(assetsFolderName + "//PowerUps"+"//Yellow" + "//" + powerUpTextureNames[i]+".png", true);
                    gl.glBindTexture(GL.GL_TEXTURE_2D, powerUpTextures[i]);
                }
                if (i >= 12 && i<18) {
                    powerUpTexture[i] = TextureReader.readTexture(assetsFolderName + "//PowerUps" + "//Red" + "//" + powerUpTextureNames[i] + ".png", true);
                    gl.glBindTexture(GL.GL_TEXTURE_2D, powerUpTextures[i]);
                }
                if (i >= 6 && i < 12){
                    powerUpTexture[i] = TextureReader.readTexture(assetsFolderName + "//PowerUps" + "//Green" + "//" + powerUpTextureNames[i] + ".png", true);
                    gl.glBindTexture(GL.GL_TEXTURE_2D, powerUpTextures[i]);
                }
                if (i < 6) {
                    powerUpTexture[i] = TextureReader.readTexture(assetsFolderName + "//PowerUps" + "//Blue" + "//" + powerUpTextureNames[i] + ".png", true);
                    gl.glBindTexture(GL.GL_TEXTURE_2D, powerUpTextures[i]);
                }



                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        powerUpTexture[i].getWidth(), powerUpTexture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        powerUpTexture[i].getPixels()
                );
            } catch (Exception e){
                System.out.println(e);
            }
        }

        //---------------------------- Mostafa Button-initialization ----------------------------------

        menuButtons.add(new buttons(45, 45, 20, 10, 4));
        menuButtons.add(new buttons(45, 30, 20, 10, 5));
        menuButtons.add(new buttons(45, 15, 20, 10, 6));
        inGamePauseBtn = new buttons(85, 85, 15, 10, 13);
        pauseButtons.add(new buttons(45, 30, 20, 10, 10));
        pauseButtons.add(new buttons(45, 15, 20, 10, 6));
        endButtons.add(new buttons(65, 15, 20, 10, 12));
        endButtons.add(new buttons(25, 15, 20, 10, 11));


        // --------------------------- Shehab Score Texture  ------------------------------------
        gl.glGenTextures(scoreTextureNames.length, scoreTextures, 0);
        for (int i = 0; i < scoreTextureNames.length; i++) {
            try {
                scoreTexture[i] = TextureReader.readTexture(assetsFolderName + "//Score" + "//" + scoreTextureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, scoreTextures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        scoreTexture[i].getWidth(), scoreTexture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        scoreTexture[i].getPixels()
                );
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36));


        // --------------------------- Mahmoud UserName Taking ---------------------------------

//        TakeUserName();


        //---------------------------- For Shehab Health Bar   ---------------------------------

        gl.glGenTextures(healthTextureNames.length, healthTextures, 0);
        for(int i = 0; i < healthTextureNames.length; i++){
            try {
                healthTexture[i] = TextureReader.readTexture(assetsFolderName + "//HealthBar" + "//" + healthTextureNames[i] , true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, healthTextures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        healthTexture[i].getWidth(), healthTexture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        healthTexture[i].getPixels()
                );
            } catch( IOException e ) {
                System.out.println(e);
            }
        }

        whiteTextureId = createBlankTexture(gl);

        //-----------------------------Belal All Objects Taking-------------------------------------------
        allObjects.clear();
        player = new PlayerCar((int) curX, (int) curY);
        obstaclesList.clear();
        allObjects.add(player);

        for (int i = 0; i < numberOfObstacles; i++) {
            int randomX = obstaclesPositions[(int) (Math.random() * 5)];
            int startY = maxHeight + (i * 30);
            Obstacles obs = new Obstacles(randomX, startY);
            obstaclesList.add(obs);

            allObjects.add(obs);
        }

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        if (GameState == Menu) {
            DrawBackground(gl,2);

            for (buttons btn : menuButtons){
                btn.draw(gl, textures, maxWidth, maxHeight);
            }
            gl.glEnable(GL.GL_TEXTURE_2D);
            gl.glDisable(GL.GL_BLEND);

        } else if (GameState == Game) {
            background_loop(gl);
            drawAndMoveObstacles(gl);
            drawSprite(gl, (float) player.getPosX(), (float) player.getPosY(), 1, 1.4f);
            drawBullets(gl);
            player.updateInvincibility();
            updateMovement();



            //---------------Colligion Shehab-----------------------
            updateGameLogic();

            //-------Score---HealthBar  Related
            score(gl, xScore, yScore);
            inGamePauseBtn.draw(gl, textures, maxWidth, maxHeight);
            //drawScoreText(glAutoDrawable);
            drawHealthBar(gl, player.health, 100.0f, healthTextures[0], 3, 85, 40, 20);
            drawPowerUps(gl);


            checkPlayerDeath();

        }else if(GameState == Pause) {
            DrawBackground(gl , 3);


            for (buttons btn : pauseButtons) {
                btn.draw(gl, textures, maxWidth, maxHeight);
            }
        }else if(GameState == End) {
            DrawBackground(gl , 9);
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

        else if (isKeyPressed(KeyEvent.VK_DOWN) && isKeyPressed(KeyEvent.VK_LEFT) && curY > 0 && curX > 0) {
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
    public void drawBullets(GL gl) {
            for (Bullet bullet : player.bullets) {
                if (bullet != null) {
                    if (bullet.timer>=0) {
                        DrawSpriteWall(gl, (float) bullet.posX, (float) (bullet.posY+10), 8, 1.0f);
                        bullet.posY += 2+GameController.gameSpeed;
                        bullet.timer--;
                    }
                }
            }
            if (player.firerate>0)
                player.firerate--;
    }

    public void powerUpsSpawn(GL gl) {

        float minX = 15;
        float maxX = 85;
        float spawnX = minX + (float)(Math.random() * ((maxX - minX) + 1));
        float spawnY = 100;
        int randomizer = (int) (Math.random()*4);

        int duration = 300;

        // 1. Declare a variable to hold the ONE object
        PowerUp p = null;

        switch (randomizer) {
            case 0:
                p = new Nitro(spawnX, spawnY);
                break;
            case 1:
                p = new Repair(spawnX, spawnY);
                break;
            case 2:
                p = new DoubleBullets(spawnX, spawnY, duration);
                break;
            case 3:
                p = new DoubleScore((int)spawnX, (int)spawnY, duration);
                break;
        }

        // 3. Add the SAME object to BOTH lists
        if (p != null) {
            GameController.powerUpsList.add(p); // This makes it DRAW and UPDATE
            allObjects.add(p);                  // This makes it COLLIDE
        }
    }
    public void drawPowerUps(GL gl) {
        if (GameController.powerUpsList.size() < 6 && PowerUPTimer <= 0) {
            powerUpsSpawn(gl);
            PowerUPTimer = 500;
        }

        try {
            for (int i = 0; i < GameController.powerUpsList.size(); i++) {
                PowerUp p = GameController.powerUpsList.get(i);

                p.update(player);
                // A Shehab Collision If statement for delteing the powerup
                if (!p.isCollected) {

                    int baseIndex = 0;
                    if (p instanceof Nitro) {
                        baseIndex = 0;
                    } else if (p instanceof Repair) {
                        baseIndex = 6;
                    } else if (p instanceof DoubleBullets) {
                        baseIndex = 12;
                    } else if (p instanceof DoubleScore) {
                        baseIndex = 18;
                    }

                    int animationOffset = (frameCounter / 3) % 6;

                    int finalTexIndex = baseIndex + animationOffset;

                    //drawSpriteTexture(gl, p.x, p.y, finalTexIndex, 0.7f, powerUpTextures);

                    // NEW (Correct)
                    // We use getPosX() and getPosY() from the parent GameObject
                    drawSpriteTexture(gl, (float) p.getPosX(), (float) p.getPosY(), finalTexIndex, 0.7f, powerUpTextures);
                    if (p.getPosY() <= -6) {
                        GameController.powerUpsList.remove(i);
                    }
                }
                if (p.getPosY() <= -100 || !p.alive) {
                    // Cleanup handled in updateGameLogic mostly, but safe to keep checks
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        PowerUPTimer--;
    }

    public void drawAndMoveObstacles(GL gl) {
        for (Obstacles obs : obstaclesList) {
            DrawSpriteWall(gl, (float) obs.getPosX(), (float) obs.getPosY(), obstacleTextureIndex, 1.3f);

            obs.setPosY(((int) ((obs.getPosY() - GameController.gameSpeed))));

            if (obs.getPosY() < -2) {
                obs.setPosY(maxHeight + 10);

                int newXPos = (int)(Math.random() * 5);
                obs.setPosX(obstaclesPositions[newXPos]);
            }
        }
    }

    public void DrawBackground(GL gl , int index){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void background_loop(GL gl) {
        roadOffsetY -= 0.02f * GameController.gameSpeed;
        if (roadOffsetY <= -2.0f) {
            roadOffsetY = 0.0f;
        }
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);

        gl.glPushMatrix();
        gl.glTranslated(0.0f, roadOffsetY, 0.0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslated(0.0f, roadOffsetY + 2.0f, 0.0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }




    public void drawSpriteTexture(GL gl,float x, float y, int index, float scale,int[] textures){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1*scale, 0.1*scale, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }
    public void DrawSpriteWall(GL gl,float x, float y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1*scale, 0.1*scale, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public void drawSprite(GL gl,float x, float y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1*scale, 0.1*scale, 1);
        gl.glRotated(angle, 0, 0, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    // button handling

    private void handleButton(int id) {

        switch (id) {
            case 0: GameState = Game; break;
            case 1: GameState = Instructions; break;
            case 2: System.exit(0); break;
            case 3: GameState = Game; break;
            case 4: System.exit(0); break;
            case 5: GameState = Pause; break;
            case 6: GameState = Game; break;
            case 7: GameState = Menu; break;
        }
    }


    // ----------------------------------Score-----------------------
    public void score(GL gl, int x, int y) {
        // 1. Update logic (Keep your frame counter-logic)
        //int score=GameController.score;
        frameCounter++;
        if (frameCounter > 10) {
            score++;
            System.out.println(score);
            frameCounter = 0;
        }
        // 2. Convert Score to String to get individual digits
        String scoreString = Integer.toString(score);
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


    public void TakeUserName() {
        // Keep asking as long as UserName is null (Cancel) or Empty
        while (UserName == null || UserName.trim().isEmpty()) {

            UserName = JOptionPane.showInputDialog(null, "Please enter your name (Required):");

            // If they try to be sneaky and click Cancel or leave it empty...
            if (UserName == null || UserName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must enter a name to play!");
            }
        }

        System.out.println("User entered: " + UserName);
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
    public void checkCollision(GameObject obj1, GameObject obj2) {
        if (obj1.getBounds().intersects(obj2.getBounds())) {

            // Check: Is one of them the Player?
            if (obj1 instanceof PlayerCar || obj2 instanceof PlayerCar) {

                PlayerCar player = (obj1 instanceof PlayerCar) ? (PlayerCar)obj1 : (PlayerCar)obj2;
                GameObject other = (obj1 instanceof PlayerCar) ? obj2 : obj1;

                // 1. Player hits Obstacle
                if (other instanceof Obstacles) {
                    Obstacles obs = (Obstacles) other;
                    player.takeDamage(obs.damage);
                    // Obstacle does NOT die (invincible)
                    System.out.println("Hit Obstacle!");
                }
                // 2. Player hits LightCar
                else if (other instanceof LightCar) {
                    player.takeDamage(20);  // Less damage
                    other.takeDamage(100);  // Enemy dies
                    System.out.println("Hit LightCar!");
                }
                // 3. Player hits HeavyCar
                else if (other instanceof HeavyCar) {
                    player.takeDamage(50);  // MORE damage
                    other.takeDamage(50);   // Heavy car takes damage (might not die immediately)
                    System.out.println("Hit HeavyCar!");
                }
            }

            // You can add Bullet collision logic here later
        }
    }

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
        // 1. Player vs Obstacles AND PowerUps
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
            }


        }

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

                        obj.takeDamage(100); // Kill the rock
                        b.timer = -1; // Destroy the bullet
                        break;
                    }
                }
            }
        }

        // 3. Remove Dead Objects
        for (int i = allObjects.size() - 1; i >= 0; i--) {
            if (!allObjects.get(i).alive) {
                allObjects.remove(i);
            }
        }
        // Sync specific lists
        for (int i = obstaclesList.size() - 1; i >= 0; i--) {
            if (!obstaclesList.get(i).alive) {
                obstaclesList.remove(i);
            }
        }

        // Clean PowerUps (IMPORTANT: This fixes "ghost" powerups)
        for (int i = GameController.powerUpsList.size() - 1; i >= 0; i--) {
            if (!GameController.powerUpsList.get(i).alive) {
                GameController.powerUpsList.remove(i);
            }
        }

    }
    public void checkPlayerDeath(){
        if (player.health <= 0) {
            GameState = End; // Switch to End Screen (State 3)
            player.health=100;
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
