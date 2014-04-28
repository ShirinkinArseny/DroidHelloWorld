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

public class World {

    private SurfaceHolder surfaceHolder;

    private Bitmap bg;//background picture
    private Bitmap dustTextute;//blood texture
    private Bitmap spawnTexture;//spawn texture
    private LinkedList<Unit> units;//all units
    private LinkedList<Unit> unitsAddBuffer;//units, which'll be added in next update
    private LinkedList<float[]> dust;//blood doordinates
    private LinkedList<float[]> spawns;//spawn coordinates
    private LinkedList<Message> messages;//coordinates of messages
    private Random rnd = new Random();
    private int width, height;//size of screen
    private boolean active=true;//is working
    private int[] deaths;//count of death of reds and blues
    private Paint p; //Main paint for everything
    private Paint pBlue; //Blue paint
    private Paint pRed; //Red paint
    private Path polygon = new Path(); //Path for drawing strategy situation
    private Ticker allowTouchScreen; //timer for touching
    private Ticker gcTime;//timer for hand calling for gc()
    private Ticker reWayAndReIntersectTime; //timer for recalculating intersections and calling AI
    private boolean drawContur=false;//draw or not draw strategic situation
    private AI ai=new SimpleAI();//AI
    private LinkedList<live.wallpaper.Geometry.Point>[] pts=
            new LinkedList[]{new LinkedList(), new LinkedList()}; //Points for calculating strategic situation
    private LinkedList<ControlledUnit>[] controlledUnits=
            new LinkedList[]{new LinkedList(), new LinkedList()}; //Lists of units to send in AI
    private LinkedList<NotControlledUnit>[] uncontrolledUnits=
            new LinkedList[]{new LinkedList(), new LinkedList()}; //Lists of units to send in AI


    public World(SurfaceHolder surfaceHolder, Context context) {
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
        messages =new LinkedList<>();
        spawns=new LinkedList<>();

        allowTouchScreen =new Ticker(10);
        gcTime=new Ticker(1000);
        reWayAndReIntersectTime=new Ticker(10);


        p = new Paint();
        p.setTextSize(20f);

        pBlue=new Paint();
        pBlue.setColor(Color.rgb(128, 128, 255));
        pBlue.setAlpha(128);
        pBlue.setTextSize(20f);

        pRed=new Paint();
        pRed.setColor(Color.rgb(255, 128, 128));
        pRed.setAlpha(128);
        pRed.setTextSize(20f);
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
        final Ticker spawnTimer=new Ticker(20);
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
                        spawnTimer.tick();
                        if(spawnTimer.getIsNextRound())
                            autoSpawn();
                        gcTime.tick();
                        if (gcTime.getIsNextRound())
                            System.gc();
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
        if (allowTouchScreen.getIsNextRound()) {
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
        }
        return true;
    }

    private void spawn(Unit m) {
        spawns.add(new float[]{m.getX()-spawnTexture.getWidth()/2, m.getY()-spawnTexture.getHeight()/2, 1f});
        units.add(m);
    }

    private void autoSpawn() {
        for (int team=0; team<2; team++) {
            float x=((team==0)?0:width*2/3)+rnd.nextInt(width/3);
            float y=rnd.nextInt(height);
            boolean gigant=0==rnd.nextInt(100);
            if (gigant){
                spawn(new Giant(x, y, team));
                showMessage(x, y, "GIANT SPAWNED!", team);
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

    private void showMessage(float x, float y, String text, int color) {
        messages.add(
                new Message(text, x, y,
                        0.03f, color == 0 ? pRed : pBlue)
        );
    }

    private void updateDeath() {
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).getHealth() <= 0) {
                deaths[units.get(i).getTeam()]++;

                if (units.get(i).getType()== NotControlledUnit.Type.Giant)
                showMessage(units.get(i).getX(), units.get(i).getY(), "GIANT DEATH!", units.get(i).getTeam());

                for (int j=0; j<10; j++)
                dust.add(new float[]{units.get(i).getX() - 28 + rnd.nextInt(20), units.get(i).getY() - 28 + rnd.nextInt(20), 1f+j/5f});
                units.remove(i);
            }
        }
    }

    private void updateGraphicStuff() {
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

        for (int i=0; i< messages.size(); i++) {
            messages.get(i).update();
            if (messages.get(i).getNeedToRemove())
                messages.remove(i);
        }
    }

    private void updateAI() {
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
    }

    private void updateIntersections() {
        for (int i = 0; i < units.size() - 1; i++)
            for (int j = i + 1; j < units.size(); j++) {
                if ((units.get(i).getTeam() != units.get(j).getTeam()))
                    if (units.get(i).getIntersect(units.get(j))) {
                        float attack1=-0.1f / units.get(j).getPower()* rnd.nextFloat();
                        float attack2=-0.1f / units.get(i).getPower() * rnd.nextFloat();
                        int att[] =new int[]{(int)(attack1*100000*units.get(j).getPower()),
                                             (int)(attack2*100000*units.get(i).getPower())};

                        if (att[0]<-9991) {
                            attack1*=att[0]+10000;
                            showMessage(units.get(i).getX(), units.get(i).getY(), "CRITICAL x"+(att[0]+10000)+"!", units.get(i).getTeam());

                        }
                        if (att[1]<-9991) {
                            attack2*=att[1]+10000;
                            showMessage(units.get(j).getX(), units.get(j).getY(), "CRITICAL x"+(att[1]+10000)+"!", units.get(j).getTeam());
                        }


                        units.get(i).changeHealth(attack1);
                        units.get(j).changeHealth(attack2);
                    }
            }
    }

    private void updateIntersectionAndAI() {
        reWayAndReIntersectTime.tick();
        if (reWayAndReIntersectTime.getIsNextRound()) {
            updateAI();
            updateIntersections();
        }
    }

    private void moveUnits() {
        for (Unit aMen : units) {
            aMen.move();
        }
    }

    private void addUnitsFromBuffer() {
        while (!unitsAddBuffer.isEmpty())  {
            units.add(unitsAddBuffer.get(0));
            unitsAddBuffer.remove(0);
        }
    }

    private void doRegeneration() {
        for (Unit unit : units) {
            unit.changeHealth(0.001f);
        }
    }

    private void update() {

        doRegeneration();
        updateDeath();
        updateGraphicStuff();
        updateIntersectionAndAI();
        moveUnits();
        addUnitsFromBuffer();

        allowTouchScreen.tick();
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

    private void drawMessages(Canvas canvas) {
        for (Message f: messages)
            f.draw(canvas);
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
                    p.setColor(Color.rgb(255, 0, 0));
                else
                    p.setColor(Color.rgb(0, 0, 255));
                p.setAlpha(50);
                canvas.drawPath(polygon, p);
            }
        }
    }

    private void draw(Canvas canvas) {
        drawBackground(canvas);
        drawBlood(canvas);
        if (drawContur) drawContures(canvas);
        drawSpawns(canvas);
        drawUnits(canvas);
        drawMessages(canvas);
    }
}
        