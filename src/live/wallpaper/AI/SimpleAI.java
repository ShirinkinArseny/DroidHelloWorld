package live.wallpaper.AI;

import live.wallpaper.Configs;
import live.wallpaper.Units.ControlledUnit;
import live.wallpaper.Units.NotControlledUnit;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class SimpleAI implements AI{

    private Random rnd;

    public SimpleAI() {
        rnd=new Random();
    }

    private float getDeltaPos() {
        return rnd.nextInt(2* Configs.aiDeltaTarget)-Configs.aiDeltaTarget;
    }

    public void solve(LinkedList<ControlledUnit> yours,
                      LinkedList<NotControlledUnit> enemies){

            if (enemies.size()>0) {
                LinkedList<NotControlledUnit> theirGiants = new LinkedList<>();
                LinkedList<NotControlledUnit> theirTowers = new LinkedList<>();
                LinkedList<NotControlledUnit> theirMen = new LinkedList<>();

                for (NotControlledUnit u : enemies) {
                    if (u.getType() == NotControlledUnit.Type.Giant)
                        theirGiants.add(u);
                    else if (u.getType() == NotControlledUnit.Type.Tower)
                        theirTowers.add(u);
                    else theirMen.add(u);
                }

                boolean hasOurGiant=false;
                for (ControlledUnit u : yours) {
                    if (u.getType() == NotControlledUnit.Type.Giant)
                    {
                        hasOurGiant=true;
                        break;
                    }
                }

                float x, y;

                if (theirGiants.size() > 0) {
                    float lastHP=theirGiants.get(0).getHealth();
                    x = theirGiants.get(0).getX();
                    y = theirGiants.get(0).getY();
                    for (int i=1; i<theirGiants.size(); i++) {
                        if (theirGiants.get(i).getHealth()<lastHP) {
                            lastHP=theirGiants.get(i).getHealth();
                            x = theirGiants.get(i).getX();
                            y = theirGiants.get(i).getY();
                        }
                    }
                } else if (theirTowers.size() > 0 && (yours.size()>Configs.aiOurUnitsCountToAttack
                        || yours.size()>Configs.aiOurUnitsWithGiantCountToAttack && hasOurGiant)
                        && theirMen.size()<Configs.aiTheirUnitsCountToNotAttack) {
                    x = theirTowers.get(0).getX();
                    y = theirTowers.get(0).getY();
                    float lastHP=theirTowers.get(0).getHealth();
                    for (int i=1; i<theirTowers.size(); i++) {
                        if (theirTowers.get(i).getHealth()<lastHP) {
                            lastHP=theirTowers.get(i).getHealth();
                            x = theirTowers.get(i).getX();
                            y = theirTowers.get(i).getY();
                        }
                    }
                } else if (theirMen.size()>0) {
                    x=theirMen.get(0).getX();
                    y=theirMen.get(0).getY();
                }
                else {
                    x=enemies.get(0).getX();
                    y=enemies.get(0).getY();
                }

                for (ControlledUnit c : yours) {
                    if (c.getType() != NotControlledUnit.Type.Tower) {
                        if (theirGiants.isEmpty() && theirTowers.isEmpty())
                            c.setWay(x+getDeltaPos(), y+getDeltaPos());
                        else
                        c.setWay(x, y);
                    } else {
                        boolean isFirst = true;
                        float smallest = 0;
                        NotControlledUnit nearest = null;

                        for (NotControlledUnit n : enemies) {
                            if (isFirst) {
                                nearest = n;
                                smallest = n.getSquaredLength(c);
                                isFirst = false;
                            } else {
                                float l = n.getSquaredLength(c);
                                if (l < smallest) {
                                    smallest = l;
                                    nearest = n;
                                }
                            }
                        }
                        //nearest CAN'T be null, cuz enemies.size>0 and nearest enemy become nearest
                        c.setWay(nearest.getX(), nearest.getY());
                    }
                }
            }
    }
}
