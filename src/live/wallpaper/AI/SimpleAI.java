package live.wallpaper.AI;

import live.wallpaper.Configs.Configs;
import live.wallpaper.Units.ControlledUnit;
import live.wallpaper.Units.NotControlledUnit;

import java.util.LinkedList;
import java.util.Random;

public class SimpleAI implements AI{

    private final Random rnd;
    private LinkedList<NotControlledUnit> theirGiants = new LinkedList<>();
    private LinkedList<NotControlledUnit> theirTowers = new LinkedList<>();
    private LinkedList<NotControlledUnit> theirMen = new LinkedList<>();
    private static int aiDeltaTarget;
    private static int aiDeltaTarget2;

    public static void reInit() {
        aiDeltaTarget=Configs.getIntValue(Configs.aiDeltaTarget);
        aiDeltaTarget2=2*aiDeltaTarget;
    }

    public SimpleAI() {
        rnd=new Random();
        theirGiants = new LinkedList<>();
        theirTowers = new LinkedList<>();
        theirMen = new LinkedList<>();
    }

    private float getDeltaPos() {
        return rnd.nextInt(aiDeltaTarget2)-aiDeltaTarget;
    }

    public void solve(LinkedList<ControlledUnit> yours,
                      LinkedList<NotControlledUnit> enemies){

            if (enemies.size()>0) {

                theirGiants.clear();
                theirTowers.clear();
                theirMen.clear();

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

                    for (NotControlledUnit n: theirGiants) {
                        if (n.getHealth()<lastHP) {
                            lastHP=n.getHealth();
                            x = n.getX();
                            y = n.getY();
                        }
                    }

                } else if (theirTowers.size() > 0 && (yours.size()>Configs.getIntValue(Configs.aiOurUnitsCountToAttack)
                        || yours.size()>Configs.getIntValue(Configs.aiOurUnitsWithGiantCountToAttack) && hasOurGiant)
                        && theirMen.size()<Configs.getIntValue(Configs.aiTheirUnitsCountToNotAttack)) {
                    x = theirTowers.get(0).getX();
                    y = theirTowers.get(0).getY();
                    float lastHP=theirTowers.get(0).getHealth();
                    for (NotControlledUnit t: theirTowers) {
                        if (t.getHealth()<lastHP) {
                            lastHP=t.getHealth();
                            x = t.getX();
                            y = t.getY();
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
                        NotControlledUnit nearest=null;
                        for (NotControlledUnit n : enemies) {
                            if (n.getType()!= NotControlledUnit.Type.Tower)
                            {
                                nearest=n;
                                break;
                            }
                        }
                        if (nearest!=null)
                        c.setWay(nearest.getX(), nearest.getY());
                    }
                }
            }
    }
}
