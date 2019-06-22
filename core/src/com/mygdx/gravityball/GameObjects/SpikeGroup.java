package com.mygdx.gravityball.GameObjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import sun.security.provider.ConfigFile;

public class SpikeGroup {
    private Spike[] spikes;

    public SpikeGroup(int numberOfSpikes, Vector2 bottomPos, boolean left, World world){
        spikes = new Spike[numberOfSpikes];

        for (int i = 0; i < spikes.length; i++) {
            float height;
            if (left) height = MathUtils.random(0.5f,3f);
            else height = MathUtils.random(-3f,-0.5f);
            spikes[i] = new Spike(bottomPos.x,bottomPos.y+Spike.WIDTH_Y/2 + i*Spike.WIDTH_Y,height,world);
        }
    }

    public void draw(ShapeRenderer renderer){
        for (Spike s:spikes) {
            s.draw(renderer);
        }
    }
}
