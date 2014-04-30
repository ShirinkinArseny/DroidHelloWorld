package live.wallpaper.Geometry;

import java.util.Comparator;

public class Point {


    private float x;
    private float y;

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public float getSquaredLength(Point p) {
        return getSquaredLength(p.x, p.y);
    }

    public float getSquaredLength(float x2, float y2) {
        return (x2-x)*(x2-x)+(y2-y)*(y2-y);
    }

    public void changePosition(float dx, float dy) {
        x+=dx;
        y+=dy;
    }

    public Point(float x, float y) {
        this.x=x;
        this.y=y;
    }

    public Point clone() {
        return new Point(x, y);
    }

    public int compareTo(Point o) {
        int sx = x>o.x?1:x==0?0:-1;
        return sx != 0 ? sx : y>o.y?1:-1;
    }

    public static Comparator<Point> comparer = new Comparator<Point>() {
        @Override
        public int compare(Point o1, Point o2) {
            return o1.compareTo(o2);
        }
    };

    public static boolean cw(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x) < 0;
    }

    public String toString() {
        return "{"+(int)x+" "+(int)y+"}";
    }
}
