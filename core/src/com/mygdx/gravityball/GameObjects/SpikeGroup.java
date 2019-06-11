package com.mygdx.gravityball.GameObjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import sun.security.provider.ConfigFile;

public class SpikeGroup {
    private Spike[] spikes;

    public SpikeGroup(int numberOfSpikes, Vector2 bottomPos, int left, World world){
        spikes = new Spike[numberOfSpikes];

        for (int i = 0; i < spikes.length; i++) {
            spikes[i] = new Spike(bottomPos.y,bottomPos.y+Spike.WIDTH_Y/2 + i*Spike.WIDTH_Y,MathUtils.random(50*left,300*left),world);
        }
    }

    public void draw(ShapeRenderer renderer){
        for (Spike s:spikes) {
            s.draw(renderer);
        }
    }


}
