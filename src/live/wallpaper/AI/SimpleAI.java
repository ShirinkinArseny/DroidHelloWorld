package live.wallpaper.AI;

import live.wallpaper.Stone;
import live.wallpaper.Units.ControlledUnit;
import live.wallpaper.Units.NotControlledUnit;

import java.util.LinkedList;

public class SimpleAI implements AI{

    public SimpleAI() {
    }

    public void solve(LinkedList<ControlledUnit> yours,
                      LinkedList<NotControlledUnit> enemies, LinkedList<Stone> stones){


            LinkedList<NotControlledUnit> theirGiants=new LinkedList<>();
            LinkedList<NotControlledUnit> theirTowers=new LinkedList<>();

            for (NotControlledUnit u : enemies) {
                if (u.getType()== NotControlledUnit.Type.Giant)
                    theirGiants.add(u);
                else
                if (u.getType()== NotControlledUnit.Type.Tower)
                    theirTowers.add(u);
            }

        float x=0, y=0;

        if (theirTowers.size()>0) {
            x=theirTowers.get(0).getX();
            y=theirTowers.get(0).getY();
        }
        else
        if (theirGiants.size()>0) {
            x=theirGiants.get(0).getX();
            y=theirGiants.get(0).getY();
        }
        else
        {
            for (NotControlledUnit nc: enemies) {
                x+=nc.getX();
                y+=nc.getY();
            }
            x/=enemies.size();
            y/=enemies.size();
        }

            for (ControlledUnit c: yours) {
                if (c.getType()!= NotControlledUnit.Type.Tower) {
                    c.setWay(x, y);
                }
                else {
                    boolean isFirst=true;
                    float smallest=0;
                    NotControlledUnit nearest=null;

                    for (NotControlledUnit n: enemies) {
                         if (isFirst)
                         {
                             nearest=n;
                             smallest=n.getSquaredLength(c);
                             isFirst=false;
                         }
                        else {
                             float l=n.getSquaredLength(c);
                             if (l<smallest) {
                                 smallest=l;
                                 nearest=n;
                             }
                         }
                    }
                    c.setWay(nearest.getX(), nearest.getY());
                }
            }
    }
}
