package com.mygdx.gravityball.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Spike {
    Vector2 pos;
    float height; //pos if left, and negative if right
    final static float WIDTH_Y = 50;

    private Body body;

    public Spike(float x, float y, float height, World world) {
        this.pos = new Vector2(x,y);
        this.height = height;
        createBox2d(world);
    }

    public void draw(ShapeRenderer renderer){
        renderer.setColor(Color.WHITE);
        renderer.triangle(pos.x, pos.y-WIDTH_Y/2, pos.x,pos.y+WIDTH_Y/2, pos.x+height, pos.y);
    }
    public void createBox2d(World world){
        BodyDef boxDef = new BodyDef();
        boxDef.type = BodyDef.BodyType.StaticBody;
        boxDef.position.set(pos.x,pos.y);
        body = world.createBody(boxDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(new Vector2[]{ new Vector2(pos.x, pos.y-WIDTH_Y/2), new Vector2(pos.x,pos.y+WIDTH_Y/2), new Vector2(pos.x+height, pos.y)});

        FixtureDef boxFix = new FixtureDef();
        boxFix.shape = polygonShape;
        boxFix.density = 1f;
        boxFix.restitution = 0f;
        boxFix.friction = 0f;

        Fixture fixture = body.createFixture(boxFix);
        polygonShape.dispose();
    }
}