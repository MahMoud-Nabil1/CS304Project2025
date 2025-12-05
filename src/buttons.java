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
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9,
                y / (maxHeight / 2.0) - 0.9,
                0);
        gl.glScaled(0.1 * (w / 10.0f),
                0.1 * (h / 10.0f),
                1);

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 0); gl.glVertex3f(-1, -1, -1);
        gl.glTexCoord2f(1, 0); gl.glVertex3f( 1, -1, -1);
        gl.glTexCoord2f(1, 1); gl.glVertex3f( 1,  1, -1);
        gl.glTexCoord2f(0, 1); gl.glVertex3f(-1,  1, -1);
        gl.glEnd();

        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public boolean isClicked(double mx, double my) {

        float left   = x - w / 2f;
        float right  = x + w / 2f;
        float bottom = y - h / 2f;
        float top    = y + h / 2f;

        return mx >= left && mx <= right &&
                my >= bottom && my <= top;
    }
}

