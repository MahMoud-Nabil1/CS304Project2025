import GameObjects.LightCar;
import Texture.TextureReader;
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

public class CarGLEventListener extends CarListener implements MouseListener , GLEventListener, KeyListener , ActionListener , MouseMotionListener {
    double roadOffsetY = 0.0f;
    String UserName;
    boolean UserNameEntered = false;
    int GameState   = 0;
    final int Menu  = 0;
    final int Game  = 1;
    final int Pause = 2;
    final int End   = 3;
    final int Instructions = 4;
    float btnX = 45;
    float[] btnYPositions = {45, 30, 15};
    float btnW = 20;
    float btnH = 10;

    int xM = 700;
    int yM = 700;

    String[] textureNames = {"BackGroundTest.png" , "Man1.png" , "MenuBackGround.png"
            , "StartButton.png" , "InstructionsButton.png" , "QuitButton.png"
    };
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];
    public BitSet keyBits = new BitSet(256);
    int maxWidth =  100;
    int maxHeight = 100;
    float x = maxWidth/2.0f ;
    float y =maxHeight/2.0f ;
    float playerSpeed = 0.5f;
    int angle = 0;
    buttons[] menu;
    buttons[] pause;
    buttons[] endgame;
    buttons[] game;



    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);
        for(int i = 0; i < textureNames.length; i++){
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i] , true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch( IOException e ) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
//        if(!UserNameEntered) {
//            TakeUserName();
//            UserNameEntered = true;
//        }
        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        if(GameState == Menu) {
            DrawBackground(gl);
            for(int i = 0; i < btnYPositions.length; i++) {
                MakeButton(gl, i);
            }
            gl.glEnable(GL.GL_TEXTURE_2D);
            gl.glDisable(GL.GL_BLEND);

        }else if(GameState == Game) {
            background_loop(gl);
            updateMovement();
            DrawSprite(gl,x,y,1,1f);
        }else if(GameState == Pause) {

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
    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public void DrawBackground(GL gl){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);

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
    public void background_loop(GL gl){

        roadOffsetY -= 0.02f;

        if(roadOffsetY <= -2.0f){
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
        int TexturePosition = index + 3;
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

    public void TakeUserName(){
        UserName = JOptionPane.showInputDialog(null, "Please enter your name:");

        if (UserName != null && !UserName.trim().isEmpty()) {
            System.out.println("User entered: " + UserName);
        } else {
            System.out.println("User cancelled or entered nothing.");
        }
    }
    public void updateMovement() {
        if (isKeyPressed(KeyEvent.VK_UP) && isKeyPressed(KeyEvent.VK_RIGHT) && y < maxHeight - 10  && x < maxWidth - 18){
            y += playerSpeed;
            x += playerSpeed;
            angle = -45;
        }
        else if (isKeyPressed(KeyEvent.VK_UP) && isKeyPressed(KeyEvent.VK_LEFT ) && y < maxHeight - 18  && x > 7) {
            y += playerSpeed;
            x -= playerSpeed;
            angle = 45;
        }
        else if (isKeyPressed(KeyEvent.VK_UP) && y < maxHeight - 10)
            y += playerSpeed;

        else if (isKeyPressed(KeyEvent.VK_DOWN) && y > 0)
            y -= playerSpeed;

        else if (isKeyPressed(KeyEvent.VK_LEFT) && x > 7)
            x -= playerSpeed;

        else if (isKeyPressed(KeyEvent.VK_RIGHT) && x < maxWidth - 18)
            x += playerSpeed;
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
        double convertedX = ((double)e.getX() / xM) * maxWidth;
        double convertedY = ((double)(yM - e.getY()) / yM) * maxHeight;
        for (int i = 0; i < btnYPositions.length; i++) {
            float btnY = btnYPositions[i];

            float btnXStart = btnX - btnW/2.0f;
            float btnXEnd = btnX + btnW/2.0f;
            float btnYStart = btnY - btnH/2.0f;
            float btnYEnd = btnY + btnH/2.0f;

            if (convertedX >= btnXStart && convertedX <= btnXEnd &&
                    convertedY >= btnYStart && convertedY <= btnYEnd) {
                switch(i) {
                    case 0: GameState = Game; break;
                    case 1: GameState = Instructions; break;
                    case 2: System.exit(0); break;
                }
            }
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
}
