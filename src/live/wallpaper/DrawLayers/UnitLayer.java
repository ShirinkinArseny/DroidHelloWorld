package live.wallpaper.DrawLayers;

import android.graphics.Canvas;
import live.wallpaper.AI.AI;
import live.wallpaper.AI.SimpleAI;
import live.wallpaper.Configs;
import live.wallpaper.DrawLayers.BloodLayer.BloodLayer;
import live.wallpaper.DrawLayers.MessagesLayer.MessagesLayer;
import live.wallpaper.DrawLayers.SpawnLayer.SpawnsLayer;
import live.wallpaper.TimeFunctions.LoopedTicker;
import live.wallpaper.Units.ControlledUnit;
import live.wallpaper.Units.NotControlledUnit;
import live.wallpaper.Units.Tower;
import live.wallpaper.Units.Unit;

import java.util.LinkedList;
import java.util.Random;

public class UnitLayer{

    private static Random rnd = new Random();
    private static AI ai=new SimpleAI();
    private static LinkedList<ControlledUnit>[] controlledUnits=
            new LinkedList[]{new LinkedList(), new LinkedList()}; //Lists of units to send in AI
    private static LinkedList<NotControlledUnit>[] uncontrolledUnits=
            new LinkedList[]{new LinkedList(), new LinkedList()}; //Lists of units to send in AI
    private static LinkedList<Unit>[] dividedUnits=
            new LinkedList[]{new LinkedList(), new LinkedList(), new LinkedList(), new LinkedList()}; //Lists of units to send in AI
    private static LoopedTicker reWayAndReIntersectTime;
    private static int[] kills=new int[]{0, 0};
    private static Synchroniser syncer;

    public static int[] getTeamSizes() {
        return kills;
    }

    public static void init() {
        syncer=new Synchroniser();
        reWayAndReIntersectTime=new LoopedTicker(0.2f, new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        syncer.waitForUnlock();
                        syncer.lock();
                        updateAI();
                        updateIntersections();
                        syncer.unlock();
                    }
                }).start();
            }
        });
    }

    public static void resize(int width, int height) {
            float wOld = Configs.getDisplayWidth() - 2 * Configs.getWorldHorizontalBorders();
            float hOld = Configs.getDisplayHeight()
                    - Configs.getWorldVerticalTopBorders() - Configs.getWorldVerticalBottomBorders();

            float wNew = width - 2 * Configs.getWorldHorizontalBorders();
            float hNew = height - Configs.getWorldVerticalTopBorders() - Configs.getWorldVerticalBottomBorders();

        syncer.waitForUnlock();
        syncer.lock();
            for (int i = 0; i < 4; i++)
                for (Unit u : dividedUnits[i]) {

                    float posX = (u.getX() - Configs.getWorldHorizontalBorders()) / wOld * wNew + Configs.getWorldHorizontalBorders();
                    float posY = (u.getY() - Configs.getWorldVerticalTopBorders()) / hOld * hNew + Configs.getWorldVerticalTopBorders();
                    u.setPosition(posX, posY);
                }
        syncer.unlock();
    }

    private static void updateAI() {
        controlledUnits[0].clear();
        controlledUnits[1].clear();
        uncontrolledUnits[0].clear();
        uncontrolledUnits[1].clear();


        for (int i=0; i<2; i++)
            for (Unit u: dividedUnits[i]) {
                controlledUnits[i].add(u);
                uncontrolledUnits[i].add(u);
            }

        ai.solve(controlledUnits[0], uncontrolledUnits[1]);
        ai.solve(controlledUnits[1], uncontrolledUnits[0]);
    }

    private static void applyDamages(Unit unit, Unit enemy) {
        float attack1 = -0.2f * unit.getPower() * rnd.nextFloat();
        int att = (int) (attack1 * 50000/unit.getPower());

        if (att < -9991) {
            attack1 *= att + 10000;
            MessagesLayer.showMessage(enemy.getX(), enemy.getY(),
                    "CRITICAL x" + (att + 10000) + "!", enemy.getTeam());
        }
        enemy.changeHealth(attack1);
        if (enemy.getHealth()<=0) unit.bumpKills();
    }

    private static void fightUnits(Unit unit, Unit enemy) {
        if (unit.getIntersect(enemy)) {
            applyDamages(unit, enemy);
            applyDamages(enemy, unit);
        }
    }

    private static void moveUnits(float dt) {
        for (int i=0; i<4; i++)
            for (Unit aMen : dividedUnits[i]) {
                aMen.move(dt);
                if (aMen.getType()== NotControlledUnit.Type.Tower) {
                    Unit u = ((Tower) aMen).getAddition();
                    if (u != null) {
                        unlockedSpawn(u);
                    }
                    ((Tower) aMen).clearAddition();
                }
            }
    }

    private static void unlockedSpawn(Unit m) {
        SpawnsLayer.addSpawn(m.getX(), m.getY());
        if (m.getType()== NotControlledUnit.Type.Bullet)
            dividedUnits[m.getTeam()+2].add(m);
        else
            dividedUnits[m.getTeam()].add(m);
    }

    public static void spawn(Unit m) {
        syncer.waitForUnlock();
        syncer.lock();
        SpawnsLayer.addSpawn(m.getX(), m.getY());
        if (m.getType()== NotControlledUnit.Type.Bullet)
            dividedUnits[m.getTeam()+2].add(m);
        else
            dividedUnits[m.getTeam()].add(m);
        syncer.unlock();
    }


    private static void doRegeneration(float dt) {
        for (int i=0; i<2; i++)
            for (Unit unit : dividedUnits[i]) {
                unit.changeHealth(0.1f*dt);
            }
    }

    private static void updateIntersections() {
        for (Unit u1: dividedUnits[0])
        for (Unit u2 : dividedUnits[1])
            fightUnits(u1, u2);

        for (int i=0; i<2; i++) {
            int enemyTeam=Math.abs(i-1);
            for (Unit unit: dividedUnits[i]) {
                for (Unit enemy : dividedUnits[enemyTeam + 2]) {
                    fightUnits(enemy, unit);
                }
            }
        }
    }

    public static void killEverybody() {
        syncer.waitForUnlock();
        syncer.lock();
        for (int i=0; i<2; i++) {
            for (Unit u: dividedUnits[i])
                u.changeHealth(-10f);
        }
        for (int i=0; i<2; i++)
        updateDeath(dividedUnits[i]);
        kills[0]=0;
        kills[1]=0;
        syncer.unlock();
    }

    private static void updateDeath(LinkedList<Unit> units) {
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).getHealth() <= 0) {

                Unit c=units.get(i);
                if (c.getType()== NotControlledUnit.Type.Giant)
                    MessagesLayer.showMessage(units.get(i).getX(), units.get(i).getY(), "GIANT DEATH!", units.get(i).getTeam());
                else
                if (c.getType()== NotControlledUnit.Type.Tower)
                    MessagesLayer.showMessage(units.get(i).getX(), units.get(i).getY(), "TOWER DESTROYED!", units.get(i).getTeam());
                else
                if (c.getType()== NotControlledUnit.Type.Man)
                    MessagesLayer.showMessage(units.get(i).getX(), units.get(i).getY(), "-1", units.get(i).getTeam());

                int tx;
                if (c.getType()!= NotControlledUnit.Type.Tower && c.getType()!= NotControlledUnit.Type.Bullet)
                    tx=0;
                else tx=1;
                for (int j=0; j< Configs.getBloodCount(); j++)
                    BloodLayer.add(units.get(i).getX() - 28 + rnd.nextInt(20),
                            units.get(i).getY() - 28 + rnd.nextInt(20),j*Configs.getBloodInterval(), tx);

                if (tx==0)
                kills[c.getTeam()]++;
                units.remove(i);
            }
        }
    }

    public static void update(float dt) {
        syncer.waitForUnlock();
        syncer.lock();
        doRegeneration(dt);
        for (int i=0; i<4; i++)
            updateDeath(dividedUnits[i]);
        moveUnits(dt);
        syncer.unlock();
        reWayAndReIntersectTime.tick(dt);
    }


    public static void draw(Canvas canvas) {
        syncer.waitForUnlock();
        syncer.lock();
        for (int i=0; i<4; i++)
            for (Unit m : dividedUnits[i])
                m.draw(canvas);
        syncer.unlock();
    }
}
