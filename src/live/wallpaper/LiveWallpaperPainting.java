package live.wallpaper;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.*;

import static live.wallpaper.Point.cw;

public class LiveWallpaperPainting {

    private SurfaceHolder surfaceHolder;

    private Bitmap bg;
    private Bitmap dustTextute;
    private Bitmap spawnTexture;
    private Bitmap[][] menTexture;
    private LinkedList<Man> men;
    private LinkedList<Man> menAddBuffer;
    private LinkedList<float[]> dust;
    private LinkedList<float[]> deathes;
    private LinkedList<float[]> spawns;
    private LinkedList<Point> intersections;
    private Random rnd = new Random();
    private int width, height;
    private boolean active=true;
    private int[] deaths;
    private Paint p;
    private Paint bluredP;
    private Path polygon = new Path();
    private int allowAdd=0;
    private int gcTime=0;
    private int reWayAndReIntersectTimer;
    private boolean drawContur=false;
    private LinkedList<Point>[] pts=new LinkedList[]{new LinkedList(), new LinkedList()};

    public LiveWallpaperPainting(SurfaceHolder surfaceHolder, Context context) {
        this.surfaceHolder = surfaceHolder;

        menTexture = new Bitmap[2][2];
        menTexture[0][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.red);
        menTexture[0][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bigred);
        menTexture[1][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue);
        menTexture[1][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bigblue);
        bg =               BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
        dustTextute  =     BitmapFactory.decodeResource(context.getResources(), R.drawable.dust);
        spawnTexture=      BitmapFactory.decodeResource(context.getResources(), R.drawable.spawn);

        deaths=new int[]{0, 0};
        men = new LinkedList<>();
        menAddBuffer=new LinkedList<>();
        dust=new LinkedList<>();
        deathes=new LinkedList<>();
        spawns=new LinkedList<>();
        intersections=new LinkedList<>();

        p = new Paint();
        p.setTextSize(20f);

        bluredP=new Paint();
    }

    public void pausePainting() {
        active=false;
    }

    public void resumePainting() {
        active=true;
    }

    public void stopPainting() {
        active=false;
    }

    public void run() {
        final int[] timer = {0};
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                    if (active) {
                        update();
                        try {
                        Canvas c = surfaceHolder.lockCanvas();
                        draw(c);
                        surfaceHolder.unlockCanvasAndPost(c);
                        }
                        catch (Exception e) {}
                        if (timer[0] <= 0) {
                            timer[0] = 10;
                            //dublicate();
                            add();
                        }
                        timer[0]--;
                        gcTime--;
                        if (gcTime<=0) {
                            gcTime=1000;
                            System.gc();
                        }
                    }
            }
        }, 0, 20);
    }

    public void setSurfaceSize(int width, int height) {
        synchronized (this) {
            this.width = width;
            this.height = height;
            this.notify();
            bg = Bitmap.createScaledBitmap(bg, width, height, true);
        }
    }

    public boolean doTouchEvent(MotionEvent event) {
        if (allowAdd<=0) {
            /*float posX = event.getX();
            float posY = event.getY();

                int team=(posX<width/2)?0:1;
                boolean gigant=0==rnd.nextInt(100);
                spawn(new Man(
                        menTexture[team][gigant?1:0],
                        posX,
                        posY,
                        team, gigant?0.2f:1f
                ));*/
            drawContur=!drawContur;
            allowAdd = 10;
        }
        return true;
    }

    private void spawn(Man m) {
        spawns.add(new float[]{m.getX()-spawnTexture.getWidth()/2, m.getY()-spawnTexture.getHeight()/2, 1f});
        men.add(m);
    }

    private void add() {
        for (int team=0; team<2; team++) {
            boolean gigant=0==rnd.nextInt(100);
            spawn(new Man(
                    menTexture[team][gigant?1:0],
                    ((team==0)?0:width*2/3)+rnd.nextInt(width/3),
                    rnd.nextInt(height),
                    team, gigant?0.2f:1f
            ));
        }
    }

    private void update() {

        for (int i = 0; i < men.size(); i++) {
            if (men.get(i).getHealth() <= 0) {
                deaths[men.get(i).getTeam()]++;
                deathes.add(new float[]{men.get(i).getX(), men.get(i).getY(), 1f, men.get(i).getTeam()});
                men.remove(i);
            }
        }

        for (int i=0; i<spawns.size(); i++) {
            spawns.get(i)[2]-=0.02f;
            if (spawns.get(i)[2]<=0)
                spawns.remove(i);
        }

        for (int i=0; i<dust.size(); i++) {
            dust.get(i)[2]-=0.1f;
            if (dust.get(i)[2]<=0)
                dust.remove(i);
        }

        for (int i=0; i<deathes.size(); i++) {
            deathes.get(i)[2]-=0.1f;
            if (deathes.get(i)[2]<=0)
                deathes.remove(i);
        }

        reWayAndReIntersectTimer--;
        if (reWayAndReIntersectTimer<=0) {
            intersections.clear();
            for (int i = 0; i < men.size() - 1; i++)
                for (int j = i + 1; j < men.size(); j++) {
                    if ((men.get(i).getTeam() != men.get(j).getTeam()))
                        if (men.get(i).getIntersect(men.get(j))) {
                            men.get(i).changeHealth(-0.1f / men.get(j).getPower());
                            men.get(j).changeHealth(-0.1f / men.get(i).getPower());
                            dust.add(new float[]{men.get(i).getX() - 28 + rnd.nextInt(20), men.get(i).getY() - 28 + rnd.nextInt(20), 1f});
                            intersections.add(men.get(i).clone());
                        }
                }
            reWayAndReIntersectTimer=10;
        }
        else {
            for (Point p: intersections)
            dust.add(new float[]{p.getX() - 28 + rnd.nextInt(20), p.getY() - 28 + rnd.nextInt(20), 1f});
        }

        for (Man aMen1 : men) aMen1.dropWay();

        for (int i = 0; i < men.size(); i++) {
            if (men.get(i).getNeedReway())
                for (Man aMen : men) {
                    if (men.get(i).getTeam() != aMen.getTeam()) {
                        men.get(i).setWay(aMen.getX(), aMen.getY());
                    }
                }
        }

        for (Man aMen : men) {
            aMen.move();
        }

        while (!menAddBuffer.isEmpty())  {
            men.add(menAddBuffer.get(0));
            menAddBuffer.remove(0);
        }

        if (allowAdd>0)
        allowAdd--;
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bg, 0, 0, null);
    }

    private void drawBlood(Canvas canvas) {
        p.setAlpha(255);
        for (float[] f: dust) {
            // p.setAlpha((int) (f[2]*128));
            canvas.drawBitmap(dustTextute, f[0], f[1], p);
        }
    }

    private void drawMen(Canvas canvas) {
        for (Man m : men)
            m.draw(canvas);
    }

    private void drawDeathes(Canvas canvas) {
        for (float[] f: deathes) {
            if (f[3]==0)
                p.setColor(Color.rgb(255, 128, 128));
            else
                p.setColor(Color.rgb(128, 128, 255));
            p.setAlpha((int) (f[2]*256));

            canvas.drawText("-1", f[0], f[1]-(1-f[2])*50, p);
        }
    }

    private void drawSpawns(Canvas canvas) {
        for (float[] f: spawns) {
            p.setAlpha((int) (f[2]*256));
            canvas.drawBitmap(spawnTexture, f[0], f[1], p);
        }
    }

    public static Point[] convexHull(LinkedList<Point> p) {
        int n = p.size();
        if (n <= 1)
            return null;
        Collections.sort(p, Point.comparer);
        Point[] q = new Point[n * 2];
        int cnt = 0;
        for (int i = 0; i < n; q[cnt++] = p.get(i++))
            for (; cnt > 1 && !cw(q[cnt - 2], q[cnt - 1], p.get(i)); --cnt)
                ;
        for (int i = n - 2, t = cnt; i >= 0; q[cnt++] = p.get(i--))
            for (; cnt > t && !cw(q[cnt - 2], q[cnt - 1], p.get(i)); --cnt)
                ;
        Point[] res = new Point[cnt - 1 - (q[0].compareTo(q[1]) == 0 ? 1 : 0)];
        System.arraycopy(q, 0, res, 0, res.length);
        return res;
    }

    private void drawContures(Canvas canvas) {
        for (int team=0; team<2; team++)
            pts[team].clear();
        for (Man m : men)
            if (m.getTeam() == 0)
                pts[0].add(m);
            else pts[1].add(m);

        for (int team=0; team<2; team++) {
            if (pts[team].size()>1) {

                Point[] cont=convexHull(pts[team]);

                polygon.reset();
                polygon.moveTo(cont[0].getX(), cont[0].getY());
                for (int i=1; i<cont.length; i++)
                    polygon.lineTo(cont[i].getX(), cont[i].getY());
                polygon.close();
                if (team == 0)
                    bluredP.setColor(Color.rgb(255, 0, 0));
                else
                    bluredP.setColor(Color.rgb(0, 0, 255));
                bluredP.setAlpha(50);
                canvas.drawPath(polygon, bluredP);
            }
        }
    }

    private void draw(Canvas canvas) {
        drawBackground(canvas);
        drawBlood(canvas);
        if (drawContur) drawContures(canvas);
        drawSpawns(canvas);
        drawMen(canvas);
        drawDeathes(canvas);
    }
}
        