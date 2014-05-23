package live.wallpaper.Geometry;

public class Point {

    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSquaredLength(float x2, float y2) {
        return (x2-x)*(x2-x)+(y2-y)*(y2-y);
    }

    public void setPosition(float dx, float dy) {
        x=dx;
        y=dy;
    }

    public void changePosition(float dx, float dy) {
        x+=dx;
        y+=dy;
    }

    public Point(float x, float y) {
        this.x=x;
        this.y=y;
    }
}
