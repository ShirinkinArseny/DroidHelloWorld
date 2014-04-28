package live.wallpaper.AI;

import live.wallpaper.Stone;
import live.wallpaper.Units.ControlledUnit;
import live.wallpaper.Units.NotControlledUnit;

import java.util.LinkedList;
import java.util.Random;

public class SimpleAI implements AI{

    private boolean was100;
    private Random rnd;

    public SimpleAI() {
        was100=false;
        rnd=new Random();
    }

    public void solve(LinkedList<ControlledUnit> yours,
                      LinkedList<NotControlledUnit> enemies, LinkedList<Stone> stones){


        if ((was100 || enemies.size()>=50) && (enemies.size()>1)) {
            was100=true;
            float midX = 0;
            float midY = 0;

            for (NotControlledUnit u : enemies) {
                midX += u.getX();
                midY += u.getY();
            }
            midX /= enemies.size();
            midY /= enemies.size();

            for (ControlledUnit c : yours) {
                    c.setWay(midX+(rnd.nextFloat()-0.5f)*200f, midY+(rnd.nextFloat()-0.5f)*200f);
            }
        }

    }
}
