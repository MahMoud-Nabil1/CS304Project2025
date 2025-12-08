import GameObjects.*;

import javax.media.opengl.GL;
import GameController.GameController;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static GameController.GameController.obstaclesList;
import static GameController.TextureHandling.powerUpTextures;
import static GameController.TextureHandling.textures;

import GameObjects.PlayerCar;

public class drawClass {
    static int maxWidth = 100;
    static int maxHeight = 100;
    static int[] obstaclesPositions = {13, 29, 45, 62, 79};
    static double roadOffsetY = 0.0f;
    static int PowerUPTimer=0;


    public static void DrawBackground(GL gl, int index, int[] textures){
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

    public static void background_loop(GL gl , int[] textures) {
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

    public static void DrawSpriteWall(GL gl, float x, float y, int index, float scale, int[] textures){
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

    public static void drawAndMoveObstacles(GL gl , int index , int[] textures) {
        if (obstaclesList.size() < 2) {
            obstaclesSpawn(gl);
        }

        List<Obstacles> toRemove = new ArrayList<>();

        for (Obstacles obs : obstaclesList) {
            DrawSpriteWall(gl, (float)obs.getPosX(), (float)obs.getPosY(), 7, 1.4f , textures);
            if (obs.getPosY() < -10) {
                toRemove.add(obs);
            } else {
                obs.posY = (float) (obs.posY - GameController.gameSpeed);
            }
        }
        obstaclesList.removeAll(toRemove);
    }

    private static void obstaclesSpawn(GL gl) {
        int randomizer = (int) (Math.random()*5);
        float spawnX = obstaclesPositions[randomizer];
        float spawnY = 100;
        Obstacles p = new Obstacles(spawnX, spawnY);
        obstaclesList.add(p);
        CarGLEventListener.allObjects.add(p);

    }

    public static void drawBullets(GL gl, PlayerCar player, int[] textures) {
        // 1. Create a "Trash Can" list
        List<Bullet> deadBullets = new ArrayList<>();

        for (Bullet bullet : player.bullets) {
            if (bullet != null) {
                // Check if bullet is alive
                if (bullet.timer >= 0) {
                    // Draw and Move
                    DrawSpriteWall(gl, (float) bullet.posX, (float) (bullet.posY + 10), 8, 1.0f, textures);
                    bullet.posY += 2 + GameController.gameSpeed;
                    bullet.timer--;
                } else {
                    // If it's dead, add to trash can
                    deadBullets.add(bullet);
                }
            }
        }

        // 2. Empty the trash (Remove dead bullets from the player's list)
        player.bullets.removeAll(deadBullets);

        // 3. Handle Fire Rate
        if (player.firerate > 0)
            player.firerate--;
    }
    public static void LightCarSpawn(GL gl) {
        int randomizer = (int) (Math.random()*5);
        float spawnX = obstaclesPositions[randomizer];
        float spawnY = 100;
        LightCar p = new LightCar(spawnX, spawnY);
        GameController.LightCars.add(p);
        CarGLEventListener.allObjects.add(p);

    }

    public static void drawLightCar(GL gl , int[] texture) {
        if (GameController.LightCars.size() < 2) {
            LightCarSpawn(gl);
        }

        List<LightCar> toRemove = new ArrayList<>();

        for (LightCar car : GameController.LightCars) {
            DrawSpriteWall(gl, (float)car.getPosX(), (float)car.getPosY(), 14, 1.4f ,  texture);
            drawHitboxDebug(gl, car.getBounds());
            if (car.getPosY() < -10) {
                toRemove.add(car);
            } else {
                car.posY = (float) (car.posY - GameController.gameSpeed - car.getSpeed());
            }
        }

        GameController.LightCars.removeAll(toRemove);
    }

    public static void drawHitboxDebug(GL gl, Rectangle rect) {
        gl.glDisable(GL.GL_TEXTURE_2D); // Turn off textures to draw lines
        gl.glColor3f(1.0f, 0.0f, 0.0f); // Set color to RED

        gl.glPushMatrix();

        // Convert Game Coords (0..100) to OpenGL Coords (-1..1)
        double x = rect.x / 50.0 - 1.0;
        double y = rect.y / 50.0 - 1.0;
        double w = rect.width / 50.0;
        double h = rect.height / 50.0;

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex2d(x, y);
        gl.glVertex2d(x + w, y);
        gl.glVertex2d(x + w, y + h);
        gl.glVertex2d(x, y + h);
        gl.glEnd();

        gl.glPopMatrix();

        gl.glColor3f(1.0f, 1.0f, 1.0f); // Reset color to White
        gl.glEnable(GL.GL_TEXTURE_2D);  // Turn textures back on
    }

    public static void powerUpsSpawn(GL gl) {

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
            CarGLEventListener.allObjects.add(p);                  // This makes it COLLIDE
        }
    }

    public static void drawPowerUps(GL gl, PlayerCar player) {
        if (GameController.powerUpsList.size() < 6 && PowerUPTimer <= 0) {
            powerUpsSpawn(gl);
            PowerUPTimer = 50;
        }
        player.update();

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

                    int animationOffset = (CarGLEventListener.frameCounter / 3) % 6;

                    int finalTexIndex = baseIndex + animationOffset;

                    //drawSpriteTexture(gl, p.x, p.y, finalTexIndex, 0.7f, powerUpTextures);

                    // NEW (Correct)
                    // We use getPosX() and getPosY() from the parent GameObject
                    drawSpriteTexture(gl, (float) p.getPosX(), (float) p.getPosY(), finalTexIndex, 0.7f, powerUpTextures);
                    if (p.getPosY() <= -50) {
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

    public static void drawSpriteTexture(GL gl, float x, float y, int index, float scale, int[] textures){
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

    public static void drawSprite(GL gl,float x, float y, int index, float scale , int[] textures){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1*scale, 0.1*scale, 1);
        gl.glRotated(CarGLEventListener.angle, 0, 0, 1);
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

}
