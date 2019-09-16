package com.mygdx.gravityball.GameObjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.gravityball.Screens.GameScreen;

public class BlackPlague {
    static final float HEIGHT = 0.5f;

    private Triangle[] triangles;


    public BlackPlague(World world, float y){
        int number = (int) (GameScreen.WORLD_WIDTH/Spike.WIDTH_Y);
        triangles = new Triangle[number];

        for (int i = 0; i < triangles.length; i++) {
            float x = i*Spike.WIDTH_Y;
            triangles[i] = new Triangle(
                    new Vector2(x,y),
                    new Vector2(x+Spike.WIDTH_Y/2,y+HEIGHT),
                    new Vector2(x+Spike.WIDTH_Y,y),world);
        }
    }

    public void draw(ShapeRenderer renderer){
        for (Triangle t:triangles) {
            t.draw(renderer);
        }

    }
}
