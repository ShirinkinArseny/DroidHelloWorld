package live.wallpaper;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import live.wallpaper.AI.AI;
import live.wallpaper.AI.SimpleAI;
import live.wallpaper.Units.*;

import java.util.*;

public class World {

    private SurfaceHolder surfaceHolder;

    private Bitmap bg;//background picture
    private Bitmap dustTextute[];//blood texture
    private Bitmap spawnTexture;//spawn texture
    private LinkedList<Unit> unitsAddBuffer;//units, which'll be added in next update
    private LinkedList<float[]> dust;//blood doordinates
    private LinkedList<float[]> spawns;//spawn coordinates
    private LinkedList<float[]> spawnsAddBuffer;//spawn coordinates
    private final LinkedList<Message> messages;//coordinates of messages
    private Random rnd = new Random();
    private int width, height;//size of screen
    private boolean active=true;//is working
    private int[] deaths;//count of death of reds and blues
    private Paint p; //Main paint for everything
    private Paint pBlue; //Blue paint
    private Paint pRed; //Red paint
    private Ticker allowTouchScreen; //timer for touching
    private Ticker gcTime;//timer for hand calling for gc()
    private Ticker reWayAndReIntersectTime; //timer for recalculating intersections and calling AI
    private AI ai=new SimpleAI();//AI
    private LinkedList<ControlledUnit>[] controlledUnits=
            new LinkedList[]{new LinkedList(), new LinkedList()}; //Lists of units to send in AI
    private LinkedList<NotControlledUnit>[] uncontrolledUnits=
            new LinkedList[]{new LinkedList(), new LinkedList()}; //Lists of units to send in AI
    private LinkedList<Unit>[] dividedUnits=
            new LinkedList[]{new LinkedList(), new LinkedList(), new LinkedList(), new LinkedList()}; //Lists of units to send in AI

    public World(SurfaceHolder surfaceHolder, Context context) {
        this.surfaceHolder = surfaceHolder;

        Bitmap[][] menTexture = new Bitmap[2][4];
        menTexture[0][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.red);
        menTexture[0][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bigred);
        menTexture[0][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.redtower);
        menTexture[1][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue);
        menTexture[1][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bigblue);
        menTexture[1][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bluetower);
        menTexture[0][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        menTexture[1][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        Unit.init(menTexture);

        dustTextute=new Bitmap[2];
        bg =               BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        dustTextute[0]  =  BitmapFactory.decodeResource(context.getResources(), R.drawable.blood);
        dustTextute[1]  =  BitmapFactory.decodeResource(context.getResources(), R.drawable.coal);
        spawnTexture=      BitmapFactory.decodeResource(context.getResources(), R.drawable.spawn);

        deaths=new int[]{0, 0};
        unitsAddBuffer =new LinkedList<>();
        dust=new LinkedList<>();
        messages =new LinkedList<>();
        spawns=new LinkedList<>();
        spawnsAddBuffer=new LinkedList<>();

        allowTouchScreen =new Ticker(1);
        gcTime=new Ticker(1000);
        reWayAndReIntersectTime=new Ticker(5);

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
        final Ticker spawnTimer=new Ticker(10);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                    if (active) {
                        try {
                        update();
                        Canvas c = surfaceHolder.lockCanvas();
                        draw(c);
                        surfaceHolder.unlockCanvasAndPost(c);
                        }
                        catch (Exception e) {
                            Log.i("Cycle", String.valueOf(e));
                        }
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
            for (int i=0; i<4; i++)
            for (Unit u: dividedUnits[i]) {
                u.changePosition(u.getX()*width/this.width-u.getX(), u.getY()*height/this.height-u.getY());
            }
            this.width = width;
            this.height = height;
            this.notify();
            Unit.setScreenSize(width, height);
            bg = Bitmap.createScaledBitmap(bg, width, height, true);
        }
    }

    public boolean doTouchEvent(MotionEvent event) {
        if (allowTouchScreen.getIsNextRound()) {
            float posX = event.getX();
            float posY = event.getY();
            int team=(posX<width/2)?0:1;

            boolean gigant=0==rnd.nextInt(100);
            if (gigant){
                spawn(new Giant(posX, posY, team));
                showMessage(posX, posY, "GIANT SPAWNED!", team);
            }
            else
            spawn(new Man(posX, posY, team));
        }
        return true;
    }

    private void spawn(Unit m) {
        spawnsAddBuffer.add(new float[]{m.getX()-spawnTexture.getWidth()/2, m.getY()-spawnTexture.getHeight()/2, 1f});
        unitsAddBuffer.add(m);
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
            boolean tower=0==rnd.nextInt(100);
            if (tower){
                spawn(new Tower(x, y, team));
                showMessage(x, y, "TOWER BUILT!", team);
                return;
            }
            spawn(new Man(x, y, team));
        }
    }

    private void showMessage(float x, float y, String text, int color) {
        messages.add(
                new Message(text, x, y,
                        0.03f, color == 0 ? pRed : pBlue)
        );
    }

    private void updateDeath(LinkedList<Unit> units) {
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).getHealth() <= 0) {
                deaths[units.get(i).getTeam()]++;

                Unit c=units.get(i);
                if (c.getType()== NotControlledUnit.Type.Giant)
                showMessage(units.get(i).getX(), units.get(i).getY(), "GIANT DEATH!", units.get(i).getTeam());
                else
                if (c.getType()== NotControlledUnit.Type.Tower)
                    showMessage(units.get(i).getX(), units.get(i).getY(), "TOWER DESTROYED!", units.get(i).getTeam());
                else
                if (c.getType()== NotControlledUnit.Type.Man)
                    showMessage(units.get(i).getX(), units.get(i).getY(), "-1", units.get(i).getTeam());

                int tx;
                if (c.getType()!= NotControlledUnit.Type.Tower && c.getType()!= NotControlledUnit.Type.Bullet)
                    tx=0;
                else tx=1;
                for (int j=0; j<10; j++)
                dust.add(new float[]{units.get(i).getX() - 28 + rnd.nextInt(20), units.get(i).getY() - 28 + rnd.nextInt(20), 1f+j/5f, tx});

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


        for (int i=0; i<2; i++)
        for (Unit u: dividedUnits[i]) {
            controlledUnits[i].add(u);
            uncontrolledUnits[i].add(u);
        }

        ai.solve(controlledUnits[0], uncontrolledUnits[1], new LinkedList<Stone>());
        ai.solve(controlledUnits[1], uncontrolledUnits[0], new LinkedList<Stone>());
    }

    private void fightUnits(Unit unit, Unit enemy) {
        if (unit.getIntersect(enemy)) {
            float attack1 = -0.1f * unit.getPower() * rnd.nextFloat();
            int att = (int) (attack1 * 100000/unit.getPower());

            if (att < -9991) {
                attack1 *= att + 10000;
                showMessage(enemy.getX(), enemy.getY(),
                        "CRITICAL x" + (att + 10000) + "!", enemy.getTeam());
            }

            enemy.changeHealth(attack1);
        }
    }

    private void updateIntersections() {
                for (int i=0; i<2; i++) {
                    int enemyTeam=Math.abs(i-1);
                    for (Unit unit: dividedUnits[i]) {
                        for (Unit enemy : dividedUnits[enemyTeam]) {
                            fightUnits(unit, enemy);
                        }
                        for (Unit enemy : dividedUnits[enemyTeam + 2]) {
                            fightUnits(enemy, unit);
                        }
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

    private void moveUnit(Unit u) {
        Unit[] add=new Unit[1];
        u.move(add);
        if (add[0]!=null) {
            spawn(add[0]);
        }
    }

    private void moveUnits() {
        for (int i=0; i<4; i++)
        for (Unit aMen : dividedUnits[i]) {
            moveUnit(aMen);
        }
    }

    private void addUnitsFromBuffer() {
        while (!unitsAddBuffer.isEmpty())  {
            Unit add=unitsAddBuffer.get(0);
            if (add.getType()== NotControlledUnit.Type.Bullet)
                dividedUnits[add.getTeam()+2].add(add);
            else
                dividedUnits[add.getTeam()].add(add);
            unitsAddBuffer.remove(0);
            spawns.add(spawnsAddBuffer.get(0));
            spawnsAddBuffer.remove(0);
        }
    }

    private void doRegeneration() {
        for (int i=0; i<2; i++)
        for (Unit unit : dividedUnits[i]) {
            unit.changeHealth(0.001f);
        }
    }

    private void update() {

        doRegeneration();
        for (int i=0; i<4; i++)
        updateDeath(dividedUnits[i]);
        updateGraphicStuff();
        updateIntersectionAndAI();
        moveUnits();
        addUnitsFromBuffer();

        allowTouchScreen.tick();
    }

    public void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bg, 0, 0, null);
    }

    private void drawBlood(Canvas canvas) {
        p.setAlpha(255);
        for (float[] f: dust) {
            canvas.drawBitmap(dustTextute[((int) f[3])], f[0], f[1], p);
        }
    }

    private void drawUnits(Canvas canvas) {
        for (int i=0; i<4; i++)
        for (Unit m : dividedUnits[i])
            m.draw(canvas);
    }

    private void drawMessages(Canvas canvas) {
            for (Message f : messages)
                f.draw(canvas);
    }

    private void drawSpawns(Canvas canvas) {
        for (float[] f: spawns) {
            p.setAlpha((int) (f[2]*256));
            canvas.drawBitmap(spawnTexture, f[0], f[1], p);
        }
    }

    private void draw(Canvas canvas) {
        drawBackground(canvas);
        drawBlood(canvas);
        drawSpawns(canvas);
        drawUnits(canvas);
        drawMessages(canvas);
    }
}
        