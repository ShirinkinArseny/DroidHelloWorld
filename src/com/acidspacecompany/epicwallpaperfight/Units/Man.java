package com.acidspacecompany.epicwallpaperfight.Units;

import com.acidspacecompany.epicwallpaperfight.Geometry.Point;

public class Man extends Unit{
    private static final float speed=160f;

    public Man(Point p, int team) {
        super(p, team, 1f, speed, Type.Man);
    }
}