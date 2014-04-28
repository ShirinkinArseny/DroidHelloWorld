package live.wallpaper;

import android.graphics.*;

import java.util.Random;

public class Man extends Point{
    private float length;
    private float xWay;
    private float yWay;
    private int w;
    private int h;
    private int w2;
    private int h2;
    private int team;
    private float health;
    private float healthCoef;
    private Paint p;
    private Bitmap bmp;
    private Random rnd=new Random();
    private int reWayTimer=0;

    public Man(Bitmap bmp, float x, float y, int team, float health) {
        super(x, y);
        this.bmp = bmp;
        xWay=x;
        yWay=y;
        length=0;
        w = bmp.getWidth();
        h = bmp.getHeight();
        w2=w/2;
        h2=h/2;
        this.team=team;
        this.health=1f;
        healthCoef=health;
        p = new Paint();
    }

    public boolean getIntersect(float [] pos) {
        return (Math.abs(getX()-pos[0])+Math.abs(getY()-pos[1]))<=pos[2]+pos[3]+w2+h2;
    }

    public boolean getIntersect(Man m) {
        return  getIntersect(new float[]{
                m.getX(), m.getY(), m.w2, m.h2});
    }


    public int getTeam() {
        return team;
    }

    public float getHealth() {
         return health;
    }

    public void dropWay() {
        length=100000;
    }

    public boolean getNeedReway() {
        reWayTimer--;
        return reWayTimer<=0;
    }

    public void setWay(float x, float y) {
        float newLength=Math.abs(x-this.getX())+Math.abs(y-this.getY());
        if (newLength<length) {
            length=newLength;
            xWay=x;
            yWay=y;
        }
        reWayTimer=20;
    }

    public void changeHealth(float h) {
         health+=healthCoef*h;
         if (health>1f) health=1f;
    }

    public float getPower() {
        return healthCoef;
    }

    public void move() {
        double dl=Math.sqrt((getX()-xWay)*(getX()-xWay)+(getY()-yWay)*(getY()-yWay));
        float dx= (float) ((xWay-getX())/dl);
        float dy= (float) ((yWay-getY())/dl);
        dx*=2+2*rnd.nextFloat();
        dy*=2+2*rnd.nextFloat();
        changePosition(dx, dy);
    }

    public void draw(Canvas c) {
        c.drawBitmap(bmp, getX()-w2, getY()-h2, null);
        p.setColor(Color.rgb((int)(255*(1-health)), (int)(255*health), 0));
        p.setAlpha(128);
        c.drawRect(getX()-w2, getY()-2-h2, getX()+w*health-w2, getY()-h2, p);
    }
}