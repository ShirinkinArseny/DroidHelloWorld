package live.wallpaper.Geometry;

import android.util.Log;

import java.util.Comparator;

public class Point {


    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
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
