package live.wallpaper.AI;

import live.wallpaper.Stone;
import live.wallpaper.Units.ControlledUnit;
import live.wallpaper.Units.NotControlledUnit;

import java.util.LinkedList;

public class SimpleAI implements AI{


    public void solve(LinkedList<ControlledUnit> yours,
                      LinkedList<NotControlledUnit> enemies, LinkedList<Stone> stones){


        if (enemies.size()>=1) {
            float midX = 0;
            float midY = 0;

            for (NotControlledUnit u : enemies) {
                midX += u.getX();
                midY += u.getY();
            }
            midX /= enemies.size();
            midY /= enemies.size();

            for (ControlledUnit c : yours) {
                c.setWay(midX, midY);
            }
        }

    }
}
