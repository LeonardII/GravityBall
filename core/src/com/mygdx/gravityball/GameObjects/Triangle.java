package com.mygdx.gravityball.GameObjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Triangle {
    Vector2 a,b,c;
    private Body body;

    public Triangle(Vector2 a,Vector2 b, Vector2 c, World world) {
        this.a = a;
        this.b = b;
        this.c = c;
        createBox2d(world, a,b,c);
    }

    public void draw(ShapeRenderer renderer){
        renderer.triangle(body.getPosition().x+a.x, body.getPosition().y+a.y,
                body.getPosition().x+b.x,body.getPosition().y+b.y,
                body.getPosition().x+c.x,body.getPosition().y+c.y);
    }
    public void createBox2d(World world, Vector2 a,Vector2 b, Vector2 c){
        BodyDef boxDef = new BodyDef();
        boxDef.type = BodyDef.BodyType.StaticBody;
        boxDef.position.set(0,0);
        body = world.createBody(boxDef);
        body.setUserData(this);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(new Vector2[]{ a,b,c});


        FixtureDef boxFix = new FixtureDef();
        boxFix.shape = polygonShape;
        boxFix.density = 1f;
        boxFix.restitution = 0f;
        boxFix.friction = 0f;

        Fixture fixture = body.createFixture(boxFix);
        polygonShape.dispose();
    }
}
