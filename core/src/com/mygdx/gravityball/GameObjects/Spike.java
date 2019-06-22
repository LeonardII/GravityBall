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
    float height; //pos if left, and negative if right
    final static float WIDTH_Y = 1;

    private Body body;

    public Spike(float x, float y, float height, World world) {
        this.height = height;
        createBox2d(world, x, y);
    }

    public void draw(ShapeRenderer renderer){
        renderer.setColor(Color.WHITE);
        renderer.triangle(body.getPosition().x, body.getPosition().y-WIDTH_Y/2,
                body.getPosition().x,body.getPosition().y+WIDTH_Y/2,
                body.getPosition().x+height, body.getPosition().y);
    }
    public void createBox2d(World world, float x, float y){
        BodyDef boxDef = new BodyDef();
        boxDef.type = BodyDef.BodyType.StaticBody;
        boxDef.position.set(0,0);
        body = world.createBody(boxDef);
        body.setUserData(this);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(new Vector2[]{ new Vector2(body.getPosition().x, body.getPosition().y-WIDTH_Y/2),
                new Vector2(body.getPosition().x,body.getPosition().y+WIDTH_Y/2),
                new Vector2(body.getPosition().x+height, body.getPosition().y)});

        FixtureDef boxFix = new FixtureDef();
        boxFix.shape = polygonShape;
        boxFix.density = 1f;
        boxFix.restitution = 0f;
        boxFix.friction = 0f;

        Fixture fixture = body.createFixture(boxFix);
        body.setTransform(x,y,0);
        polygonShape.dispose();
    }
}