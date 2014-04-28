package live.wallpaper.AI;

import live.wallpaper.Stone;
import live.wallpaper.Units.ControlledUnit;
import live.wallpaper.Units.NotControlledUnit;

import java.util.LinkedList;

public interface AI {

    public void solve(LinkedList<ControlledUnit> yours, LinkedList<NotControlledUnit> enemies, LinkedList<Stone> stones);

}
