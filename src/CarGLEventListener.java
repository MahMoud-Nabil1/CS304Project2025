import GameObjects.LightCar;
import GameObjects.Obstacles;
import GameObjects.PlayerCar;
import Texture.TextureReader;
import GameController.GameController;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

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
    float[] btnYPositions = {45, 30, 15};
    float[] pauseBtnY = {30, 15};
    float btnW = 20;
    float btnH = 10;
    int windowWidth = 1;
    int windowHeight = 1;

    int xM = 700;
    int yM = 700;

    String[] textureNames = {"BackGroundTest.png" , "Man1.png" , "MenuBackGround.png" , "PauseMenu.png"
            , "StartButton.png" , "InstructionsButton.png" , "QuitButton.png" , "obstacle.png"
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
    int numberOfObstacles = 5;
    int obstacleTextureIndex = 7;


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

        player = new PlayerCar((int) curX, (int) curY);

        obstaclesList.clear();
        for (int i = 0; i < numberOfObstacles; i++) {
            int randomX = 10 + (int)(Math.random() * 80);
            int startY = maxHeight + (i * 30);
            Obstacles obs = new Obstacles(randomX, startY);
            obstaclesList.add(obs);
        }
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        double gameSpeed = GameController.gameSpeed;

//        if(!UserNameEntered) {
//            TakeUserName();
//            UserNameEntered = true;
//        }
        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        if (GameState == Menu) {
            DrawBackground(gl,2);
            for (int i = 0; i < btnYPositions.length; i++) {
                MakeButton(gl, i);
            }
            gl.glEnable(GL.GL_TEXTURE_2D);
            gl.glDisable(GL.GL_BLEND);

        } else if (GameState == Game) {
            background_loop(gl, gameSpeed);
            drawAndMoveObstacles(gl);
            DrawSprite(gl, (float) player.getPosX(), (float) player.getPosY(), 1, 1f);
            updateMovement();

            score(gl , xScore , yScore);


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

    public void drawAndMoveObstacles(GL gl) {
        for (Obstacles obs : obstaclesList) {
            DrawSpriteWall(gl, (float) obs.getPosX(), (float) obs.getPosY(), obstacleTextureIndex, 1.0f);

            obs.setPosY((int) ((int) (obs.getPosY() - 1) * GameController.gameSpeed));

            if (obs.getPosY() < -10) {
                obs.setPosY((int) ((maxHeight + 10)*GameController.gameSpeed));

                int newX = (int)(10 + (int)(Math.random() * 80) * GameController.gameSpeed);
                obs.setPosX(newX);
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

    public void background_loop(GL gl, double gameSpeed) {

        roadOffsetY -= 0.02f * gameSpeed;

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
        float btnY = btnYPositions[index];
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

    public void TakeUserName(){
        UserName = JOptionPane.showInputDialog(null, "Please enter your name:");

        if (UserName != null && !UserName.trim().isEmpty()) {
            System.out.println("User entered: " + UserName);
        } else {
            System.out.println("User cancelled or entered nothing.");
        }
    }

    public void updateMovement() {

        float currentSpeed = (float) player.getSpeed();

        if (isKeyPressed(KeyEvent.VK_UP) && isKeyPressed(KeyEvent.VK_RIGHT) && curY < maxHeight - 10 && curX < maxWidth - 18) {
            curY += currentSpeed;
            curX += currentSpeed;
            angle = -45;
        } else if (isKeyPressed(KeyEvent.VK_UP) && isKeyPressed(KeyEvent.VK_LEFT) && curY < maxHeight - 18 && curX > 7) {
            curY += currentSpeed;
            curX -= currentSpeed;
            angle = 45;
        } else if (isKeyPressed(KeyEvent.VK_UP) && curY < maxHeight - 10)
            curY += currentSpeed;

        else if (isKeyPressed(KeyEvent.VK_DOWN) && curY > 0)
            curY -= currentSpeed;

            else if (isKeyPressed(KeyEvent.VK_LEFT) && curX > 7)
                curX -= currentSpeed;

            else if (isKeyPressed(KeyEvent.VK_RIGHT) && curX < maxWidth - 18)
                curX += currentSpeed;
            player.setPosX((int) curX);
            player.setPosY((int) curY);
            if(isKeyPressed(KeyEvent.VK_Z)){
                player.nitroOn();
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
    public void DrawSprite(GL gl,float x, float y, int index, float scale){
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

            for (int i = 0; i < btnYPositions.length; i++) {

                float centerX = btnX;
                float centerY = btnYPositions[i];
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

}
