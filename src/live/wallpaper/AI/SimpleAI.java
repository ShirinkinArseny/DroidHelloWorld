package live.wallpaper.AI;

import live.wallpaper.Stone;
import live.wallpaper.Units.ControlledUnit;
import live.wallpaper.Units.NotControlledUnit;

import java.util.LinkedList;
import java.util.Random;

public class SimpleAI implements AI{

    private Random rnd;
    private static int deltaWay=200;
    private static int halfDeltaWay=deltaWay/2;


    public SimpleAI() {
        rnd=new Random();
    }

    private float getDelta(ControlledUnit u) {
         if (u.getType()== NotControlledUnit.Type.Tower) {
             return 0;
         }
        else return rnd.nextInt(deltaWay)-halfDeltaWay;
    }

    public void solve(LinkedList<ControlledUnit> yours,
                      LinkedList<NotControlledUnit> enemies, LinkedList<Stone> stones){


            LinkedList<NotControlledUnit> theirGiants=new LinkedList<>();
            LinkedList<NotControlledUnit> theirTowers=new LinkedList<>();
            LinkedList<NotControlledUnit> theirSmalls=new LinkedList<>();

            for (NotControlledUnit u : enemies) {
                if (u.getType()== NotControlledUnit.Type.Giant)
                    theirGiants.add(u);
                else
                if (u.getType()== NotControlledUnit.Type.Tower)
                    theirTowers.add(u);
                else theirSmalls.add(u);
            }

            for (ControlledUnit c: yours) {
                if (theirTowers.size()>0)
                    c.setWay(theirTowers.get(0).getX()+getDelta(c), theirTowers.get(0).getY()+getDelta(c));
                else
                if (theirGiants.size()>0)
                    c.setWay(theirGiants.get(0).getX()+getDelta(c), theirGiants.get(0).getY()+getDelta(c));
                else
                {
                    float x=0;
                    float y=0;
                    for (NotControlledUnit nc: enemies) {
                        x+=nc.getX();
                        y+=nc.getY();
                    }
                    x/=enemies.size();
                    y/=enemies.size();

                    c.setWay(x+getDelta(c), y+getDelta(c));
                }
            }

    }
}
