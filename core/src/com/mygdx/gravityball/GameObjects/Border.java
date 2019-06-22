package com.mygdx.gravityball.GameObjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Border {
    //idee: sehr leichte hügel und täler um es dynamischer zu machen

    private Body body;
    private Vector2 pos;
    private Vector2 dim;

    public Border(World world,Vector2 pos, Vector2 dim){
        this.pos = pos;
        this.dim = dim;
        //this.dim.y *= -1;
        createBox2d(world);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1,1,1,1);
        shapeRenderer.rect(pos.x, pos.y, dim.x, dim.y);
    }


    public void createBox2d(World world){
        BodyDef boxDef = new BodyDef();
        boxDef.type = BodyDef.BodyType.KinematicBody;
        boxDef.position.set(pos.x+dim.x/2,pos.y+dim.y/2);
        body = world.createBody(boxDef);
        body.setUserData(this);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(dim.x/2, dim.y/2);

        FixtureDef boxFix = new FixtureDef();
        boxFix.shape = groundBox;
        boxFix.density = 1f;
        boxFix.restitution = 0f;
        boxFix.friction = 0f;

        Fixture fixture = body.createFixture(boxFix);
        groundBox.dispose();
    }

    public void setPosUp(){
        pos.y += dim.y;
    }

    public Vector2 getDim() {
        return dim;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void syncSpriteToBody(){
        Vector2 b = body.getPosition();
        b.add(-dim.x/2,-dim.y/2);
        pos.set(b);
    }

    public void setVel(Vector2 vel) {
        body.setLinearVelocity(vel);

    }
}
