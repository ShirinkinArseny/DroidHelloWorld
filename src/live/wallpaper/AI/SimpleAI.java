package live.wallpaper.AI;

import android.util.Log;
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
                if (c.getHealth()<0.4f) {
                    c.setWay(c.getX()*2-midX, c.getY()*2-midY);
                }
                else
                    c.setWay(midX+rnd.nextInt(200)-100, midY+rnd.nextInt(200)-100);
            }
        }

    }
}
