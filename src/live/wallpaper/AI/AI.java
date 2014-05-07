package live.wallpaper.AI;

import live.wallpaper.Units.ControlledUnit;
import live.wallpaper.Units.NotControlledUnit;

import java.util.HashSet;
import java.util.LinkedList;

public interface AI {

    public void solve(LinkedList<ControlledUnit> yours, LinkedList<NotControlledUnit> enemies);

}
