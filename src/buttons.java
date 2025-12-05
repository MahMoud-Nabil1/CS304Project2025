import javax.media.opengl.GL;

public class buttons {
    float x, y;
    float w, h;
    int textureIndex;

    public buttons(float x, float y, float w, float h, int textureIndex) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.textureIndex = textureIndex;
    }

    public void draw(GL gl, int[] textures, int maxWidth, int maxHeight) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textureIndex]);

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * (w / 10.0f), 0.1 * (h / 10.0f), 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 0); gl.glVertex3f(-1, -1, -1);
        gl.glTexCoord2f(1, 0); gl.glVertex3f( 1, -1, -1);
        gl.glTexCoord2f(1, 1); gl.glVertex3f( 1,  1, -1);
        gl.glTexCoord2f(0, 1); gl.glVertex3f(-1,  1, -1);
        gl.glEnd();

        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public boolean isClicked(double mx, double my, int maxWidth, int maxHeight) {

        // Convert mouse to same coordinate system used in draw()
        double nx = mx / (maxWidth / 2.0) - 1;  // normalized x
        double ny = my / (maxHeight / 2.0) - 1; // normalized y

        // Get button center position in the same coordinate system
        double bx = x / (maxWidth / 2.0) - 0.9;
        double by = y / (maxHeight / 2.0) - 0.9;

        // Button half-size in same scale as draw()
        double bw = 0.1 * (w / 10.0f);
        double bh = 0.1 * (h / 10.0f);

        // Check collision
        return nx >= (bx - bw) && nx <= (bx + bw) &&
                ny >= (by - bh) && ny <= (by + bh);
    }
}

