package live.wallpaper.Geometry;

public class Rectangle extends Point{
    private float w;
    private float w2;
    private float h2;

    public float getHalfHeight() {
        return h2;
    }

    public float getWidth() {
        return w;
    }

    public float getHalfWidth() {
        return w2;
    }

    public Rectangle(float x, float y, float w, float h) {
        super(x, y);
        this.w=w;
        w2=w/2;
        h2=h/2;
    }

    public Rectangle(Point p, float w, float h) {
        this(p.getX(), p.getY(), w, h);
    }

    public boolean getIntersect(Rectangle m) {
        return (Math.abs(getX()-m.getX()))<=(m.w2+w2) &&
               (Math.abs(getY()-m.getY()))<=(m.h2+h2);
    }
}