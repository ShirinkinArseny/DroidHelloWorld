package live.wallpaper.DrawLayers;

import live.wallpaper.AI.AI;
import live.wallpaper.Configs.LocalConfigs;
import live.wallpaper.DrawLayers.BloodLayer.BloodLayer;
import live.wallpaper.DrawLayers.MessagesLayer.MessagesLayer;
import live.wallpaper.DrawLayers.SpawnLayer.SpawnsLayer;
import live.wallpaper.Geometry.Point;
import live.wallpaper.TimeFunctions.LoopedTicker;
import live.wallpaper.Units.*;

import java.util.LinkedList;
import java.util.Random;

public class UnitLayer{

    private static final Random rnd = new Random();
    private static AI ai;
    //TODO: нужно с этим что=то решить
    private static final LinkedList<ControlledUnit>[] controlledUnits=
            new LinkedList[]{new LinkedList<ControlledUnit>(), new LinkedList<ControlledUnit>()}; //Lists of units to send in AI
    private static final LinkedList<NotControlledUnit>[] uncontrolledUnits=
            new LinkedList[]{new LinkedList(), new LinkedList()}; //Lists of units to send in AI
    private static final LinkedList<Unit>[] dividedUnits=
            new LinkedList[]{new LinkedList(), new LinkedList(), new LinkedList(), new LinkedList(), new LinkedList(), new LinkedList()}; //Lists of units to send in AI
    private static int[] kills=new int[]{0, 0};
    private static Synchroniser synchroniser;
    private static LoopedTicker updateSpawnAndIntersection;

    public static int[] getTeamSizes() {
        return kills;
    }

    public static void init(float coef) {
        synchroniser =new Synchroniser("UnitLayerSync");
        ai=new AI(coef);
        updateSpawnAndIntersection=new LoopedTicker(0.2f, new Runnable() {
            @Override
            public void run() {
                synchroniser.waitForUnlockAndLock();
                updateIntersections();
                updateAI();
                autoSpawn();
                synchroniser.unlock();
            }
        });
    }

    public static void resize(int width, int height) {
            float wOld = LocalConfigs.getDisplayWidth() - 2 * LocalConfigs.getIntValue(LocalConfigs.worldHorizontalBorders);
            float hOld = LocalConfigs.getDisplayHeight()
                    - LocalConfigs.getIntValue(LocalConfigs.worldVerticalTopBorders)
                    - LocalConfigs.getIntValue(LocalConfigs.worldVerticalBottomBorders);

            float wNew = width - 2 * LocalConfigs.getIntValue(LocalConfigs.worldHorizontalBorders);
            float hNew = height - LocalConfigs.getIntValue(LocalConfigs.worldVerticalTopBorders)
                    - LocalConfigs.getIntValue(LocalConfigs.worldVerticalBottomBorders);

        synchroniser.waitForUnlockAndLock();
            for (int i = 0; i < 6; i++)
                for (Unit u : dividedUnits[i]) {

                    float posX = (u.getX() - LocalConfigs.getIntValue(LocalConfigs.worldHorizontalBorders))
                            / wOld * wNew + LocalConfigs.getIntValue(LocalConfigs.worldHorizontalBorders);

                    float posY = (u.getY() - LocalConfigs.getIntValue(LocalConfigs.worldVerticalTopBorders))
                            / hOld * hNew + LocalConfigs.getIntValue(LocalConfigs.worldVerticalTopBorders);
                    u.setPosition(posX, posY);
                }
        synchroniser.unlock();
    }

    private static void updateAI() {
        controlledUnits[0].clear();
        controlledUnits[1].clear();
        uncontrolledUnits[0].clear();
        uncontrolledUnits[1].clear();


        for (int i=0; i<2; i++) {
            for (Unit u : dividedUnits[i]) {
                controlledUnits[i].add(u);
                uncontrolledUnits[i].add(u);
            }
            for (Unit u : dividedUnits[i+2]) {
                controlledUnits[i].add(u);
                uncontrolledUnits[i].add(u);
            }
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
                    "CRITICAL X" + (att + 10000) + "!", enemy.getTeam());
        }
        enemy.changeHealth(attack1);
        //if (enemy.getHealth()<=0) unit.bumpKills();
    }

    private static void fightUnits(Unit unit, Unit enemy) {
        if (unit.getIntersect(enemy)) {
            applyDamages(unit, enemy);
            applyDamages(enemy, unit);
        }
    }

    private static void moveUnits(float dt) {
        for (int i=0; i<6; i++)
            for (Unit aMen : dividedUnits[i]) {
                aMen.move(dt);
                if (aMen.getType()== NotControlledUnit.Type.Tower) {
                    Unit u = ((Tower) aMen).getAddition();
                    if (u != null) {
                        spawn(u);
                    }
                    ((Tower) aMen).clearAddition();
                }
            }
    }

    private static void spawn(Unit m) {
        SpawnsLayer.addSpawn(m.getX(), m.getY());
        if (m.getType()== NotControlledUnit.Type.Bullet)
            dividedUnits[m.getTeam()+4].add(m);
        else
        if (m.getType()== NotControlledUnit.Type.Tower)
            dividedUnits[m.getTeam()+2].add(m);
        else
            dividedUnits[m.getTeam()].add(m);
    }


    private static void doRegeneration(float dt) {
        for (int i=0; i<4; i++)
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
                //check for intersection with towers
                for (Unit enemy : dividedUnits[enemyTeam + 2]) {
                    fightUnits(enemy, unit);
                }
                //check for intersection with bullets
                for (Unit enemy : dividedUnits[enemyTeam + 4]) {
                    fightUnits(enemy, unit);
                }
            }
        }
    }

    public static void killEverybody() {
        synchroniser.waitForUnlockAndLock();
        for (int i=0; i<4; i++) {
            for (Unit u: dividedUnits[i])
                u.kill();
        }
        for (LinkedList<Unit> llu: dividedUnits)  {
            for (Unit u: llu)
            {
                u.kill();
                startBlood(u);
            }
            llu.clear();
        }
        synchroniser.unlock();
        kills[0]=0;
        kills[1]=0;
    }

    private static void startBlood(Unit u) {
        int tx;
        if (u.getType()!= NotControlledUnit.Type.Tower && u.getType()!= NotControlledUnit.Type.Bullet)
            tx=0;
        else tx=1;
        for (int j=0; j< LocalConfigs.getIntValue(LocalConfigs.bloodCount); j++)
            BloodLayer.add(u.getX(), u.getY(),
                    j* 0.5f, tx);
    }

    private static void updateDeath() {
        for (LinkedList<Unit> units: dividedUnits )
        for (int i=0; i<units.size(); i++) {
            Unit c=units.get(i);
            if (c.getHealth() <= 0) {

                if (c.getType()== NotControlledUnit.Type.Giant)
                    MessagesLayer.showMessage(c.getX(),c.getY(), "GIANT DEATH!", c.getTeam());
                else
                if (c.getType()== NotControlledUnit.Type.Tower)
                    MessagesLayer.showMessage(c.getX(), c.getY(), "TOWER DESTROYED!", c.getTeam());
                else
                if (c.getType()== NotControlledUnit.Type.Man)
                    MessagesLayer.showMessage(c.getX(), c.getY(), "-1", c.getTeam());

                startBlood(c);

                if (c.getType()!= NotControlledUnit.Type.Bullet)
                kills[c.getTeam()]++;
                units.remove(c);
            }
        }
    }


    private static void showMessage(Point p, String text, int color) {
        MessagesLayer.showMessage(p, text, color);
    }

    private static void autoSpawn() {
        for (int team = 0; team < 2; team++) {
            Point p = getSpawnPoint(team);
            boolean gigant = 0 == rnd.nextInt(LocalConfigs.getIntValue(LocalConfigs.worldGianSpawnProbability));
            if (gigant) {
                UnitLayer.spawn(new Giant(p, team));
                showMessage(p, "GIANT SPAWNED!", team);
                return;
            }
            boolean tower = 0 == rnd.nextInt(LocalConfigs.getIntValue(LocalConfigs.worldTowerSpawnProbability));
            if (tower) {
                UnitLayer.spawn(new Tower(p, team));
                showMessage(p, "TOWER BUILT!", team);
                return;
            }
            UnitLayer.spawn(new Man(p, team));
        }
    }

    private static Point getSpawnPoint(int team) {
        if (LocalConfigs.getDisplayWidth() > LocalConfigs.getDisplayHeight()) {
            return new Point(

                    ((team == 0) ?
                            LocalConfigs.getIntValue(LocalConfigs.worldHorizontalBorders) :
                            LocalConfigs.getDisplayWidth() * 2 / 3 - LocalConfigs.getIntValue(LocalConfigs.worldHorizontalBorders))
                            + rnd.nextInt(LocalConfigs.getDisplayWidth() / 3),

                    rnd.nextInt(LocalConfigs.getDisplayHeight() - LocalConfigs.getIntValue(LocalConfigs.worldVerticalBottomBorders)
                            - LocalConfigs.getIntValue(LocalConfigs.worldVerticalTopBorders))
                            +  LocalConfigs.getIntValue(LocalConfigs.worldVerticalTopBorders)
            );
        } else {
            return new Point(

                    rnd.nextInt(LocalConfigs.getDisplayWidth()
                            - 2 * LocalConfigs.getIntValue(LocalConfigs.worldHorizontalBorders))
                            + LocalConfigs.getIntValue(LocalConfigs.worldHorizontalBorders),

                    ((team == 0) ?
                            LocalConfigs.getIntValue(LocalConfigs.worldVerticalTopBorders) :
                            LocalConfigs.getDisplayHeight() * 2 / 3 - LocalConfigs.getIntValue(LocalConfigs.worldVerticalBottomBorders))
                            + rnd.nextInt(LocalConfigs.getDisplayWidth() / 3)
            );
        }
    }

    public static void update(float dt) {
        synchroniser.waitForUnlockAndLock();
        doRegeneration(dt);
        updateDeath();
        moveUnits(dt);
        synchroniser.unlock();
        updateSpawnAndIntersection.tick(dt);
    }

    public static void drawRectangles() {
        synchroniser.waitForUnlockAndLock();
        for (int i=0; i<4; i++)
            for (Unit m : dividedUnits[i])
                m.drawHealth();
        synchroniser.unlock();
    }

    public static void draw() {
        synchroniser.waitForUnlockAndLock();
        for (int i=0; i<4; i++)
            for (Unit m : dividedUnits[i])
                m.drawShadow();
        for (int i=0; i<6; i++)
            for (Unit m : dividedUnits[i])
                m.drawBase();
        synchroniser.unlock();
    }
}
