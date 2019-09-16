package com.mygdx.gravityball.GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Spike extends Triangle{
    public final static float WIDTH_Y = 1;

    public Spike(float x, float y, float height, World world) {
        super(new Vector2(x,y-WIDTH_Y/2),
                new Vector2(x,y+WIDTH_Y/2),
                new Vector2(x+height,y),
                world);
    }


}