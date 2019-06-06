package com.mygdx.gravityball.GameObjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import javax.swing.SpringLayout;

public class Player {

    private float radius;
    private Vector2 pos;

    private Body body;

    public Player(float x, float y, float radius, World world){
        pos = new Vector2(x, y);
        this.radius = radius;
        createBoc2d(world);
    }

    public Body createBoc2d(World world){
        BodyDef playerDef = new BodyDef();
        playerDef.type = BodyDef.BodyType.DynamicBody;
        playerDef.position.set(pos);
        body = world.createBody(playerDef);

        CircleShape playerShape = new CircleShape();
        playerShape.setRadius(radius);

        FixtureDef playerFix = new FixtureDef();
        playerFix.shape = playerShape;
        playerFix.density = 5f;
        playerFix.restitution = 0.2f;
        playerFix.friction = 0f;

        Fixture fixture = body.createFixture(playerFix);
        playerShape.dispose();

        body.setLinearVelocity(new Vector2(0,10));
        return body;
    }

    public void draw(ShapeRenderer renderer){

        renderer.setColor(1,1,1,1);
        renderer.circle(pos.x,pos.y,radius,100);
    }

    public void syncSpriteToBody(){
        pos.set(body.getPosition());
    }

    public Vector2 getPos(){
        return pos;
    }

    public void setPos(Vector2 pos){
        this.pos = pos;
    }

    public float getRadius() {
        return radius;
    }
    public void applyForce(Vector2 f){
        body.applyForceToCenter(f,true);
    }
}
