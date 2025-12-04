public class buttons {
    public float x, y, width, height;
    public String label;
    public int targetState;
    public int textureIndex;

    public buttons(float x, float y, float width, float height, String label, int targetState, int textureIndex) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.targetState = targetState;
        this.textureIndex = textureIndex;
    }



    public boolean isClicked(double mx, double my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }
}

