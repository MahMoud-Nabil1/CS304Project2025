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
import java.util.*;
import java.util.List;
import GameObjects.*;

public class CarGLEventListener extends CarListener implements MouseListener, GLEventListener, KeyListener, ActionListener, MouseMotionListener {
    double roadOffsetY = 0.0f;
    String UserName;
    boolean UserNameEntered = false;
    int GameState = 0;
    final int Menu = 0;
    final int Game = 1;
    final int Pause = 2;
    final int End = 3;
    final int Instructions = 4;
    float btnX = 45;
    float[] menuBtnsY = {45, 30, 15};
    float[] pauseBtnY = {30, 15};
    float btnW = 20;
    float btnH = 10;
    int windowWidth = 1;
    int windowHeight = 1;
    float inGamePauseBtnX = 60;
    float inGamePauseBtnY = 90;
    float inGamePauseBtnW = 80;
    float inGamePauseBtnH = 80;

    int inGamePauseTextureIndex = 7;

    int xM = 700;
    int yM = 700;

    String[] textureNames = {"BackGroundTest.png" , "Man1.png" , "MenuBackGround.png" , "PauseMenu.png"
            , "StartButton.png" , "InstructionsButton.png" , "QuitButton.png" , "obstacle.png","bullet.png"
    };
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];

    //---------------------- For Shehab Score 0 1 2 3 4 5 6 7 8 9 ----------------------------
    String[] scoreTextureNames = {"0.png" , "1.png","2.png","3.png","4.png","5.png","6.png","7.png","8.png","9.png"};
    TextureReader.Texture[] scoreTexture = new TextureReader.Texture[scoreTextureNames.length];
    int[] scoreTextures = new int[scoreTextureNames.length];

    // Score Variables
    int frameCounter = 0;
    int score = 0;
    int xScore = 10;
    int yScore = 90;

    // Inside Class Variables
    int healthAnimCounter = 0; // Counts frames for the health bar
    TextRenderer renderer;

    //---------------------- For Shehab HealthBar ----------------------------------------

    String[] healthTextureNames = {
            "FullState1.png" ,"HealthReceviedFull.png" //100
            ,"3_4State1.png","3_4State2.png"            //75
            ,"HalfState1.png","HalfState2.png"
            ,"LowState1.png","LowState2.png"
    };

    TextureReader.Texture[] healthTexture = new TextureReader.Texture[healthTextureNames.length];
    int[] healthTextures = new int[healthTextureNames.length];



    int xHealthBar=10;
    int yHealthBar=90;

    //--------------------------For Shehab Collegians-----------------------------------------------------------
    public static ArrayList<GameObject> allObjects = new ArrayList<>();


    //-------------------------------------------------------------------------------------


    public BitSet keyBits = new BitSet(256);
    int maxWidth = 100;
    int maxHeight = 100;
    float x = maxWidth / 2.0f;
    float y = maxHeight / 2.0f;
    int angle = 0;

    PlayerCar player;
    float curX = maxWidth / 2.0f;
    float curY = maxHeight / 2.0f;
    float playerSpeed = 0.5f;
    float movementScale = 2.0f;


    ArrayList<Obstacles> obstaclesList = new ArrayList<>();
    int numberOfObstacles = 3;
    int obstacleTextureIndex = 7;
    int[] obstaclesPositions = {13, 29, 45, 62, 79};




    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
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


        // -------------------For Shehab ScoreTexture------------------------------------
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
        // ---------------------------Mahmoud UserName Taking---------------------------------

        TakeUserName();


        //---------------------------- For Shehab Health Bar ---------------------------------

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

        //-----------------------------Belal All Objects Taking-------------------------------------------
        allObjects.clear();







        player = new PlayerCar((int) curX, (int) curY);

        obstaclesList.clear();
        for (int i = 0; i < numberOfObstacles; i++) {
            int randomX = obstaclesPositions[(int) (Math.random() * 5)];
            int startY = maxHeight + (i * 30);
            Obstacles obs = new Obstacles(randomX, startY);
            obstaclesList.add(obs);
        }





    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        double gameSpeed = GameController.gameSpeed;


        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        if (GameState == Menu) {
            DrawBackground(gl,2);
            for (int i = 0; i < menuBtnsY.length; i++) {
                MakeButton(gl, i);
            }
            gl.glEnable(GL.GL_TEXTURE_2D);
            gl.glDisable(GL.GL_BLEND);

        } else if (GameState == Game) {
            background_loop(gl);
            drawAndMoveObstacles(gl);
            drawSprite(gl, (float) player.getPosX(), (float) player.getPosY(), 1, 1f);
            updateMovement();
            drawBullets(gl);

            //-------Score---HealthBar  Related
            score(gl , xScore , yScore);
            healthBarPlayer(gl, 75,xHealthBar,yHealthBar );
            DrawInGamePauseButton(gl);
            drawScoreText(glAutoDrawable);


        }else if(GameState == Pause) {
            DrawBackground(gl , 3);

            for(int i = 0; i < pauseBtnY.length; i++){
                DrawPauseButton(gl, i);
            }
        }else if(GameState == End) {

        } else if (GameState == Instructions) {

        }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }
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
    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        windowWidth = i2;
        windowHeight = i3;
    }
    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public void drawBullets(GL gl) {
            for (Bullet bullet : player.bullets) {
                if (bullet != null) {
                    if (bullet.timer>=0) {
                        DrawSpriteWall(gl, (float) bullet.posX, (float) bullet.posY, 8, 0.3f);
                        bullet.posY += 0.5;
                        bullet.timer--;
                    }
                }
            }
            if (player.firerate>0)
                player.firerate--;
    }
    public void drawAndMoveObstacles(GL gl) {
        for (Obstacles obs : obstaclesList) {
            DrawSpriteWall(gl, (float) obs.getPosX(), (float) obs.getPosY(), obstacleTextureIndex, 1.0f);

            obs.setPosY(((int) (obs.getPosY() - 1 - GameController.gameSpeed)));

            if (obs.getPosY() < -10) {
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

    public void MakeButton(GL gl, int index){
        float btnY = menuBtnsY[index];
        int TexturePosition = index + 4;
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[TexturePosition]);
        gl.glPushMatrix();
        gl.glTranslated(btnX/(maxWidth/2.0) - 0.9, btnY/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1 * (btnW/10.0), 0.1 * (btnH/10.0), 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
    }
    public void DrawPauseButton(GL gl, int index){
        float centerX = btnX;
        float centerY = pauseBtnY[index];
        int textureIndex = (index == 0) ? 4 : 6;
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textureIndex]);
        gl.glPushMatrix();
        gl.glTranslated(centerX/(maxWidth/2.0) - 0.9, centerY/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1 * (btnW/10.0), 0.1 * (btnH/10.0), 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0,0); gl.glVertex3f(-1,-1,-1);
        gl.glTexCoord2f(1,0); gl.glVertex3f( 1,-1,-1);
        gl.glTexCoord2f(1,1); gl.glVertex3f( 1, 1,-1);
        gl.glTexCoord2f(0,1); gl.glVertex3f(-1, 1,-1);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }
    public void DrawInGamePauseButton(GL gl) {



        int xPos = 90;
        int yPos = 90;
        int textureIndex =4 ;

        double glX = xPos / 50.0 - 1.0;
        double glY = yPos / 50.0 - 1.0;


        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textureIndex]);


        gl.glPushMatrix();
        gl.glTranslated(glX, glY, 0);
        gl.glScaled(0.20, 0.15, 1);

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();

        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }


    public void updateMovement() {

       if(isKeyPressed(KeyEvent.VK_Z)){
                player.nitroOn();
       }
       if (isKeyPressed(KeyEvent.VK_SPACE)) {
                player.shoot();
       }

        float currentSpeed = (float) player.getSpeed();

        if (isKeyPressed(KeyEvent.VK_UP) && isKeyPressed(KeyEvent.VK_RIGHT) && curY < maxHeight - 10 && curX < maxWidth - 18) {
            curY += currentSpeed;
            curX += currentSpeed;
            angle = -45;
            player.setPosY(curY);
            player.setPosX(curX);
        } else if (isKeyPressed(KeyEvent.VK_UP) && isKeyPressed(KeyEvent.VK_LEFT) && curY < maxHeight - 18 && curX > 7) {
            curY += currentSpeed;
            curX -= currentSpeed;
            angle = 45;
            player.setPosY(curY);
            player.setPosX(curX);
        } else if (isKeyPressed(KeyEvent.VK_UP) && curY < maxHeight - 10){
            curY += currentSpeed;
            player.setPosY(curY);
        }
        else if (isKeyPressed(KeyEvent.VK_DOWN) && curY > 0){
            curY -= currentSpeed;
            player.setPosY(curY);
        }
        else if (isKeyPressed(KeyEvent.VK_LEFT) && curX > 7){
            curX -= currentSpeed;
            player.setPosX(curX);
        }
        else if (isKeyPressed(KeyEvent.VK_RIGHT) && curX < maxWidth - 18) {
            curX += currentSpeed;
            player.setPosX(curX);
        }




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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (GameState == Menu) {
            double convertedX = ((double) e.getX() / windowWidth) * maxWidth;
            double convertedY = ((double) (windowHeight - e.getY()) / windowHeight) * maxHeight;

            for (int i = 0; i < menuBtnsY.length; i++) {

                float centerX = btnX;
                float centerY = menuBtnsY[i];
                float left = centerX - (btnW / 2.0f);
                float right = centerX + (btnW / 2.0f);
                float bottom = centerY - (btnH / 2.0f);
                float top = centerY + (btnH / 2.0f);
                if (convertedX >= left && convertedX <= right &&
                        convertedY >= bottom && convertedY <= top) {
                    switch (i) {
                        case 0:
                            GameState = Game;
                            break;
                        case 1:
                            GameState = Instructions;
                            break;
                        case 2:
                            System.exit(0);
                            break;
                    }
                }
            }
        }else if(GameState == Game){
            double convertedX = ((double) e.getX() / windowWidth) * maxWidth;
            double convertedY = ((double) (windowHeight - e.getY()) / windowHeight) * maxHeight;


            float centerX = 90;
            float centerY = 90;

            float halfWidth = 0.20f * 50 / 2.0f;
            float halfHeight = 0.15f * 50 / 2.0f;

            float left   = centerX - halfWidth;
            float right  = centerX + halfWidth;
            float bottom = centerY - halfHeight;
            float top    = centerY + halfHeight;

            if(convertedX >= left && convertedX <= right &&
                    convertedY >= bottom && convertedY <= top){
                GameState = Pause;
                System.out.println("Game Paused!");
            }

        }else if(GameState == Pause){
            double convertedX = ((double) e.getX() / windowWidth) * maxWidth;
            double convertedY = ((double) (windowHeight - e.getY()) / windowHeight) * maxHeight;

            for(int i = 0; i < pauseBtnY.length; i++){

                float centerX = btnX;
                float centerY = pauseBtnY[i];

                float left   = centerX - (btnW / 2.0f);
                float right  = centerX + (btnW / 2.0f);
                float bottom = centerY - (btnH / 2.0f);
                float top    = centerY + (btnH / 2.0f);

                if(convertedX >= left && convertedX <= right &&
                        convertedY >= bottom && convertedY <= top){

                    if(i == 0){
                        GameState = Game;   // Resume
                    }
                    else if(i == 1){
                        System.exit(0);     // Exit
                    }
                }
            }
        }else if(GameState == End){

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    // ----------------------------------Score-----------------------
    public void score(GL gl, int x, int y) {
        // 1. Update logic (Keep your frame counter-logic)
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

    public void healthBarPlayer(GL gl, double currentHealth, int x, int y) {
        // 1. Update Animation Counter
        healthAnimCounter++;
        if (healthAnimCounter > 20) {
            healthAnimCounter = 0; // Reset every 20 frames
        }

        // 2. Determine Base Index (The starting image for this health level)
        int baseIndex = -1;

        if (currentHealth >= 100) {
            baseIndex = 0; // 100% starts at index 0
        } else if (currentHealth >= 75) {
            baseIndex = 2; // 75% starts at index 2
        } else if (currentHealth >= 50) {
            baseIndex = 4; // 50% starts at index 4
        } else if (currentHealth > 0) {
            baseIndex = 6; // 25% starts at index 6
        } else {
            return; // Dead
        }

        // 3. Determine Animation Frame (0 or 1)
        // If counter is 0-10: use Offset 0 (Normal)
        // If counter is 11-20: use Offset 1 (Glow)
        int animationOffset = 0;
        if (healthAnimCounter > 10) {
            animationOffset = 1;
        }

        // Final Texture Index = Base + Offset
        int finalTextureIndex = baseIndex + animationOffset;

        // 4. Draw
        gl.glEnable(GL.GL_BLEND);
        gl.glColor3f(1.0f, 1.0f, 1.0f);

        gl.glBindTexture(GL.GL_TEXTURE_2D, healthTextures[finalTextureIndex]);

        gl.glPushMatrix();

        double glX = x / 50.0 - 1.0;
        double glY = y / 50.0 - 1.0;

        gl.glTranslated(glX, glY, 0);
        gl.glScaled(0.2, 0.2, 1);

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);
        gl.glEnd();

        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }


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
    }


    //------------------------------------Collisons---------------------------------

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
        // --- PART 1: CHECK COLLISIONS ---
        // Loop through every pair of objects
        for (int i = 0; i < allObjects.size(); i++) {
            GameObject obj1 = allObjects.get(i);

            for (int j = i + 1; j < allObjects.size(); j++) {
                GameObject obj2 = allObjects.get(j);

                // Only check if both exist (are alive)
                if (obj1.alive && obj2.alive) {
                    checkCollision(obj1, obj2);
                }
            }
        }

        // --- PART 2: REMOVE DEAD OBJECTS ---
        // Iterate backwards to safely remove items from the list
        for (int i = allObjects.size() - 1; i >= 0; i--) {
            if (!allObjects.get(i).alive) {
                allObjects.remove(i);
                // Optional: System.out.println("Object removed!");
            }
        }
    }


}
