package live.wallpaper.Geometry;

public class Rectangle extends Point{
    private float w;
    private float h;
    private float w2;
    private float h2;

    public float getH() {
        return h;
    }

    public float getHHalf() {
        return h2;
    }

    public float getW() {
        return w;
    }

    public float getWHalf() {
        return w;
    }

    public Rectangle(float x, float y, float w, float h) {
        super(x, y);
        this.w=w;
        this.h=h;
        w2=w/2;
        h2=h/2;
    }

    public boolean getIntersect(Rectangle m) {
        return (Math.abs(getX()-m.getX())+Math.abs(getY()-m.getY())<=m.w2+m.h2+w2+h2);
    }
}