package com.mygdx.gravityball.GameObjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import sun.security.provider.ConfigFile;

public class SpikeGroup {
    private Spike[] spikes;
    private float y1,y2;
    private boolean left;

    public SpikeGroup(int numberOfSpikes, Vector2 bottomPos,int minHeight, int maxHeight, boolean left, World world){
        spikes = new Spike[numberOfSpikes];
        this.left = left;
        y1 = bottomPos.y;

        for (int i = 0; i < spikes.length; i++) {
            int max = Math.min(Math.min(i+minHeight,spikes.length-i+minHeight-1),maxHeight);
            float height = MathUtils.random(minHeight,max);
            height*=0.6f;
            if (!left) height*=-1;
            spikes[i] = new Spike(bottomPos.x,bottomPos.y+Spike.WIDTH_Y/2 + i*Spike.WIDTH_Y,height,world);
        }
        y2 = bottomPos.y+Spike.WIDTH_Y/2 + (spikes.length-1)*Spike.WIDTH_Y;
    }

    public void draw(ShapeRenderer renderer){
        for (Spike s:spikes) {
            s.draw(renderer);
        }
    }

    public float getY1() {
        return y1;
    }

    public float getY2() {
        return y2;
    }

    public boolean isLeft() {
        return left;
    }

}
