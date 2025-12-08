package GameController;

import Texture.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.io.IOException;

public class TextureHandling {

    static String assetsFolderName = "Assets/";

    public static String[] textureNames = {"BackGroundTest.png" , "car.png" , "MenuBackGround.png" , "PauseMenu.png"
            , "StartButton.png" , "InstructionsButton.png" , "QuitButton.png" , "obstacle.png","bullet.png"
            ,"endBackground.png", "continuebBotton.png" , "mainMenuButton.png" ,"playAgainButton.png",
            "pauseButton.png" , "Lightcar.png","pauseButton2.png" ,"loseMenu2.png"
    };

    public static TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];

    public static int[] textures = new int[textureNames.length];
    //--------------------- POWER-UP TEXTURE----------------------------------------------------------
    public static String[] powerUpTextureNames={"blue 1","blue 2","blue 3","blue 4","blue 5","blue 6"
            ,"green 1","green 2","green 3","green 4","green 5","green 6"
            ,"red 1","red 2","red 3","red 4","red 5","red 6"
            ,"yellow 1","yellow 2","yellow 3","yellow 4","yellow 5","yellow 6"};
    public static TextureReader.Texture[] powerUpTexture = new TextureReader.Texture[powerUpTextureNames.length];
    public static int[] powerUpTextures = new int[powerUpTextureNames.length];


    //---------------------- For Shehab Score 0 1 2 3 4 5 6 7 8 9 ----------------------------
    public static String[] scoreTextureNames = {"0.png" , "1.png","2.png","3.png","4.png","5.png"
            ,"6.png","7.png","8.png","9.png"};

    public static TextureReader.Texture[] scoreTexture = new TextureReader.Texture[scoreTextureNames.length];
    public static int[] scoreTextures = new int[scoreTextureNames.length];

    public static String[] healthTextureNames = {
            "HealthBar.png"
            ,"FullState1.png" ,"HealthReceviedFull.png" //100
            ,"3_4State1.png","3_4State2.png"            //75
            ,"HalfState1.png","HalfState2.png"
            ,"LowState1.png","LowState2.png"
    };
    public static TextureReader.Texture[] healthTexture = new TextureReader.Texture[healthTextureNames.length];
    public static int[] healthTextures = new int[healthTextureNames.length];



    public static void MainTextures(GLAutoDrawable gld){
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
    }

    public static void PowerUpTextures(GLAutoDrawable gld){
        GL gl = gld.getGL();
        gl.glClearColor(1,1,1,1);
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
    }




}
