package live.wallpaper;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import live.wallpaper.AI.AI;
import live.wallpaper.AI.SimpleAI;
import live.wallpaper.Units.*;

import java.util.*;

import static live.wallpaper.Geometry.Point.cw;

public class LiveWallpaperPainting {

    private SurfaceHolder surfaceHolder;

    private Bitmap bg;
    private Bitmap dustTextute;
    private Bitmap spawnTexture;
    private LinkedList<Unit> units;
    private LinkedList<Unit> unitsAddBuffer;
    private LinkedList<float[]> dust;
    private LinkedList<float[]> deathes;
    private LinkedList<float[]> spawns;
    private LinkedList<live.wallpaper.Geometry.Point> intersections;
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
    private LinkedList<live.wallpaper.Geometry.Point>[] pts=new LinkedList[]{new LinkedList(), new LinkedList()};
    private AI ai=new SimpleAI();
    private LinkedList<ControlledUnit>[] controlledUnits= new LinkedList[]{new LinkedList(), new LinkedList()};
    private LinkedList<NotControlledUnit>[] uncontrolledUnits= new LinkedList[]{new LinkedList(), new LinkedList()};


    public LiveWallpaperPainting(SurfaceHolder surfaceHolder, Context context) {
        this.surfaceHolder = surfaceHolder;

        Bitmap[][] menTexture = new Bitmap[2][3];
        menTexture[0][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.red);
        menTexture[0][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bigred);
        menTexture[0][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.redtower);
        menTexture[1][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue);
        menTexture[1][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bigblue);
        menTexture[1][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bluetower);
        Unit.init(menTexture);

        bg =               BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
        dustTextute  =     BitmapFactory.decodeResource(context.getResources(), R.drawable.dust);
        spawnTexture=      BitmapFactory.decodeResource(context.getResources(), R.drawable.spawn);

        deaths=new int[]{0, 0};
        units = new LinkedList<>();
        unitsAddBuffer =new LinkedList<>();
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

    private void spawn(Unit m) {
        spawns.add(new float[]{m.getX()-spawnTexture.getWidth()/2, m.getY()-spawnTexture.getHeight()/2, 1f});
        units.add(m);
    }

    private void add() {
        for (int team=0; team<2; team++) {
            float x=((team==0)?0:width*2/3)+rnd.nextInt(width/3);
            float y=rnd.nextInt(height);
            boolean gigant=0==rnd.nextInt(100);
            if (gigant){
                spawn(new Giant(x, y, team));
                return;
            }
            /*boolean tower=0==rnd.nextInt(1000);
            if (tower){
                spawn(new Tower(x, y, team));
                return;
            }*/
            spawn(new Man(x, y, team));
        }
    }

    private void update() {

        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).getHealth() <= 0) {
                deaths[units.get(i).getTeam()]++;
                deathes.add(new float[]{units.get(i).getX(), units.get(i).getY(), 1f, units.get(i).getTeam()});
                units.remove(i);
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

            controlledUnits[0].clear();
            controlledUnits[1].clear();
            uncontrolledUnits[0].clear();
            uncontrolledUnits[1].clear();

            for (Unit u: units) {
                controlledUnits[u.getTeam()].add(u);
                uncontrolledUnits[u.getTeam()].add(u);
            }

            ai.solve(controlledUnits[0], uncontrolledUnits[1], new LinkedList<Stone>());
            ai.solve(controlledUnits[1], uncontrolledUnits[0], new LinkedList<Stone>());

            intersections.clear();
            for (int i = 0; i < units.size() - 1; i++)
                for (int j = i + 1; j < units.size(); j++) {
                    if ((units.get(i).getTeam() != units.get(j).getTeam()))
                        if (units.get(i).getIntersect(units.get(j))) {
                            units.get(i).changeHealth(-0.1f / units.get(j).getPower());
                            units.get(j).changeHealth(-0.1f / units.get(i).getPower());
                            dust.add(new float[]{units.get(i).getX() - 28 + rnd.nextInt(20), units.get(i).getY() - 28 + rnd.nextInt(20), 1f});
                            intersections.add(units.get(i).clone());
                        }
                }
            reWayAndReIntersectTimer=10;
        }
        else {
            for (live.wallpaper.Geometry.Point p: intersections)
            dust.add(new float[]{p.getX() - 28 + rnd.nextInt(20), p.getY() - 28 + rnd.nextInt(20), 1f});
        }

        for (Unit aMen : units) {
            aMen.move();
        }

        while (!unitsAddBuffer.isEmpty())  {
            units.add(unitsAddBuffer.get(0));
            unitsAddBuffer.remove(0);
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

    private void drawUnits(Canvas canvas) {
        for (Unit m : units)
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

    public static live.wallpaper.Geometry.Point[] convexHull(LinkedList<live.wallpaper.Geometry.Point> p) {
        int n = p.size();
        if (n <= 1)
            return null;
        Collections.sort(p, live.wallpaper.Geometry.Point.comparer);
        live.wallpaper.Geometry.Point[] q = new live.wallpaper.Geometry.Point[n * 2];
        int cnt = 0;
        for (int i = 0; i < n; q[cnt++] = p.get(i++))
            for (; cnt > 1 && !cw(q[cnt - 2], q[cnt - 1], p.get(i)); --cnt)
                ;
        for (int i = n - 2, t = cnt; i >= 0; q[cnt++] = p.get(i--))
            for (; cnt > t && !cw(q[cnt - 2], q[cnt - 1], p.get(i)); --cnt)
                ;
        live.wallpaper.Geometry.Point[] res = new live.wallpaper.Geometry.Point[cnt - 1 - (q[0].compareTo(q[1]) == 0 ? 1 : 0)];
        System.arraycopy(q, 0, res, 0, res.length);
        return res;
    }

    private void drawContures(Canvas canvas) {
        for (int team=0; team<2; team++)
            pts[team].clear();
        for (Unit m : units)
            if (m.getTeam() == 0)
                pts[0].add(m);
            else pts[1].add(m);

        for (int team=0; team<2; team++) {
            if (pts[team].size()>1) {

                live.wallpaper.Geometry.Point[] cont=convexHull(pts[team]);

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
        drawUnits(canvas);
        drawDeathes(canvas);
    }
}
        