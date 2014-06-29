package com.acidspacecompany.epicwallpaperfight.DrawLayers;

import android.util.Log;
import com.acidspacecompany.epicwallpaperfight.AI.AI;
import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.DrawLayers.BloodLayer.BloodLayer;
import com.acidspacecompany.epicwallpaperfight.DrawLayers.MessagesLayer.MessagesLayer;
import com.acidspacecompany.epicwallpaperfight.DrawLayers.SpawnLayer.SpawnsLayer;
import com.acidspacecompany.epicwallpaperfight.Geometry.Point;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;
import com.acidspacecompany.epicwallpaperfight.TimeFunctions.LoopedTicker;
import com.acidspacecompany.epicwallpaperfight.Units.*;

import java.util.*;

public class UnitLayer {

    private static final Random rnd = new Random();
    private static AI ai;
    private static final LinkedList<Unit>[] sortedByTextureUnits = new LinkedList[]{
            new LinkedList<ControlledUnit>(), new LinkedList<ControlledUnit>(),
            new LinkedList<ControlledUnit>(), new LinkedList<ControlledUnit>(),
            new LinkedList<ControlledUnit>(), new LinkedList<ControlledUnit>(),
            new LinkedList<ControlledUnit>(), new LinkedList<ControlledUnit>()
    };
    private static final LinkedList<ControlledUnit>[] controlledUnits =
            new LinkedList[]{new LinkedList<ControlledUnit>(), new LinkedList<ControlledUnit>()}; //Lists of units to send in AI
    private static final LinkedList<NotControlledUnit>[] uncontrolledUnits =
            new LinkedList[]{new LinkedList(), new LinkedList()}; //Lists of units to send in AI
    private static final LinkedList<Unit>[] dividedUnits =
            new LinkedList[]{new LinkedList(), new LinkedList()}; //Lists of units to send in AI
    private static int[] kills = new int[]{0, 0};
    private static Synchroniser synchroniser;
    private static LoopedTicker updateSpawnAndIntersection;
    private static int topBoard;
    private static int bottomBoard;
    private static int sideBoard;

    public static int[] getTeamSizes() {
        return kills;
    }

    public static void init() {
        synchroniser = new Synchroniser("UnitLayerSync");
        ai = new AI();
        updateSpawnAndIntersection = new LoopedTicker(0.2f, new Runnable() {
            @Override
            public void run() {
                synchroniser.waitForUnlockAndLock();
                sortPeople();
                updateIntersections();
                updateAI();
                autoSpawn();
                synchroniser.unlock();
            }
        });
    }

    public static void resize(int width, int height) {

        topBoard = LocalConfigs.getIntValue(LocalConfigs.worldVerticalTopBorders);
        bottomBoard = LocalConfigs.getIntValue(LocalConfigs.worldVerticalBottomBorders);
        sideBoard = LocalConfigs.getIntValue(LocalConfigs.worldHorizontalBorders);

        float wOld = LocalConfigs.getDisplayWidth() - 2 * sideBoard;
        float hOld = LocalConfigs.getDisplayHeight()
                - topBoard - bottomBoard;

        float wNew = width - 2 * sideBoard;
        float hNew = height - topBoard - bottomBoard;

        synchroniser.waitForUnlockAndLock();
        for (int i = 0; i < 2; i++)
            for (Unit u : dividedUnits[i]) {

                float posOtnX = (u.getX() - sideBoard) / wOld;

                float posOtnY = (u.getY() - topBoard) / hOld;

                float posX = posOtnY * wNew + sideBoard;

                float posY = posOtnX * hNew + topBoard;
                u.setPosition(posX, posY);
                //u.reMatrix();
            }
        synchroniser.unlock();
    }

    private static void updateAI() {
        controlledUnits[0].clear();
        controlledUnits[1].clear();
        uncontrolledUnits[0].clear();
        uncontrolledUnits[1].clear();


        for (int i = 0; i < 2; i++) {
            for (Unit u : dividedUnits[i]) {
                if (u.getType() != NotControlledUnit.Type.Bullet) {
                    controlledUnits[i].add(u);
                    uncontrolledUnits[i].add(u);
                }
            }
        }

        ai.solve(controlledUnits[0], uncontrolledUnits[1]);
        ai.solve(controlledUnits[1], uncontrolledUnits[0]);
    }

    private static void applyDamages(Unit unit, Unit enemy) {
        float attack1 = -0.2f * unit.getPower() * rnd.nextFloat();
        int att = (int) (attack1 * 50000 / unit.getPower());

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

    private static final ArrayList<Unit> additionals = new ArrayList<>();

    private static void moveUnits(float dt) {
        for (LinkedList<Unit> u2 : dividedUnits)
            for (Unit aMen : u2) {
                aMen.move(dt);
                if (aMen.getType() == NotControlledUnit.Type.Tower) {
                    Unit u = ((Tower) aMen).getAddition();
                    if (u != null) {
                        additionals.add(u);
                    }
                    ((Tower) aMen).clearAddition();
                }
            }
        for (Unit u : additionals)
            spawn(u);
        additionals.clear();
    }

    private static int getTextureUnitID(Unit u) {
        int delta = 0;
        if (u.getTeam() == 1)
            delta = 1;
        return u.getTypeNumber() * 2 + delta;
    }

    private static void spawn(Unit m) {
        SpawnsLayer.addSpawn(m.getX(), m.getY());
        dividedUnits[m.getTeam()].add(m);
        sortedByTextureUnits[getTextureUnitID(m)].add(m);
    }

    private static void updateIntersections() {

        for (int i = 0; i < 2; i++) {
            int enemyTeam = i == 0 ? 1 : 0;
            for (Unit unit : dividedUnits[i]) {

                int startIndex = getNearestMenIndex(unit.getX0(), enemyTeam);
                int endIndex = getNearestMenIndex(unit.getX1(), enemyTeam) + 1;
                if (endIndex >= dividedUnits[enemyTeam].size())
                    endIndex = dividedUnits[enemyTeam].size();

                for (int j = startIndex; j < endIndex; j++) {
                    fightUnits(dividedUnits[enemyTeam].get(j), unit);
                }
            }
        }
    }

    public static void killEverybody() {
        synchroniser.waitForUnlockAndLock();
        for (LinkedList<Unit> llu : dividedUnits) {
            for (Unit u : llu) {
                u.kill();
                startBlood(u);
            }
            llu.clear();
        }
        for (LinkedList<Unit> llu : sortedByTextureUnits)
            llu.clear();
        synchroniser.unlock();
        kills[0] = 0;
        kills[1] = 0;
    }

    private static void startBlood(Unit u) {
        int tx;
        if (u.getType() != NotControlledUnit.Type.Tower && u.getType() != NotControlledUnit.Type.Bullet)
            tx = 0;
        else tx = 1;
        for (int j = 0; j < LocalConfigs.getIntValue(LocalConfigs.bloodCount); j++)
            BloodLayer.add(u.getX(), u.getY(),
                    j * 0.5f, tx);
    }

    private static void updateDeath() {
        for (LinkedList<Unit> units : dividedUnits)
            for (int i = 0; i < units.size(); i++) {
                Unit c = units.get(i);
                if (c.getHealth() <= 0) {

                    if (c.getType() == NotControlledUnit.Type.Giant)
                        MessagesLayer.showMessage(c.getX(), c.getY(), "GIANT DEATH!", c.getTeam());
                    else if (c.getType() == NotControlledUnit.Type.Tower)
                        MessagesLayer.showMessage(c.getX(), c.getY(), "TOWER DESTROYED!", c.getTeam());
                    else if (c.getType() == NotControlledUnit.Type.Man)
                        MessagesLayer.showMessage(c.getX(), c.getY(), "-1", c.getTeam());

                    startBlood(c);

                    if (c.getType() != NotControlledUnit.Type.Bullet)
                        kills[c.getTeam()]++;
                    units.remove(c);
                }
            }

        for (LinkedList<Unit> units : sortedByTextureUnits)
            for (int i = 0; i < units.size(); i++) {
                if (units.get(i).getHealth() <= 0) {
                    units.remove(i);
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
                            sideBoard :
                            LocalConfigs.getDisplayWidth() * 2 / 3 - sideBoard)
                            + rnd.nextInt(LocalConfigs.getDisplayWidth() / 3),

                    rnd.nextInt(LocalConfigs.getDisplayHeight() - bottomBoard - topBoard)
                            + topBoard
            );
        } else {
            return new Point(

                    rnd.nextInt(LocalConfigs.getDisplayWidth()
                            - 2 * sideBoard)
                            + sideBoard,

                    ((team == 0) ?
                            topBoard :
                            LocalConfigs.getDisplayHeight() * 2 / 3 - bottomBoard)
                            + rnd.nextInt(LocalConfigs.getDisplayWidth() / 3)
            );
        }
    }

    public static float getMiddleXUnits() {
        float x = LocalConfigs.getDisplayWidth() / 2;
        int size = 0;
        for (LinkedList<Unit> u : dividedUnits)
            for (Unit u2 : u) {
                x += u2.getX();
                size++;
            }
        if (size==0) return 0;
        return x / size;
    }

    private static int getNearestMenIndex(float x, int arrayIndex) {
        int leftIndex = 0;
        int rightIndex = dividedUnits[arrayIndex].size() - 1;
        while (rightIndex - leftIndex > 1) {
            int midIndex = (leftIndex + rightIndex) / 2;
            if (dividedUnits[arrayIndex].get(midIndex).getX() < x)
                leftIndex = midIndex;
            else rightIndex = midIndex;
        }
        return leftIndex;
    }

    private static final Comparator<Unit> xAxisComparator = new Comparator<Unit>() {
        @Override
        public int compare(Unit unit, Unit unit2) {
            return Float.compare(unit.getX(), unit2.getX());
        }
    };

    private static void sortPeople() {
        for (int k = 0; k < 2; k++) {
            Collections.sort(dividedUnits[k], xAxisComparator);
        }
    }

    public static void update(float dt) {
        synchroniser.waitForUnlockAndLock();
        updateDeath();
        moveUnits(dt);
        synchroniser.unlock();
        updateSpawnAndIntersection.tick(dt);
    }

    public static void drawRectangles() {
        synchroniser.waitForUnlockAndLock();
        for (LinkedList<Unit> u : sortedByTextureUnits) {
            if (!u.isEmpty()) {
                if (u.get(0).getType()!= NotControlledUnit.Type.Bullet && u.get(0).getType()!= NotControlledUnit.Type.Tower)
                for (Unit m : u)
                    m.drawHealth();
            }
        }
        synchroniser.unlock();
    }

    public static void draw() {
        synchroniser.waitForUnlockAndLock();
        Graphic.bindColor(1, 1, 1, 1);

        for (LinkedList<Unit> u : sortedByTextureUnits) {
            if (!u.isEmpty()) {
                if (u.get(0).getType()!= NotControlledUnit.Type.Bullet) {
                    u.get(0).prepareDrawShadow();
                    for (Unit m : u)
                        m.drawShadow();
                }
            }
            Graphic.unBindMatrices();
        }

        for (LinkedList<Unit> u : sortedByTextureUnits) {
            if (!u.isEmpty()) {
                u.get(0).prepareDrawBase();
                for (Unit m : u)
                    m.drawBase();
            }
            Graphic.unBindMatrices();
        }

        synchroniser.unlock();
    }
}
