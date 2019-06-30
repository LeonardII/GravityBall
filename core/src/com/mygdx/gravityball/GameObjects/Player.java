package com.mygdx.gravityball.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import javax.swing.SpringLayout;

public class Player {

    private float radius;
    private float finalRadius;
    private Vector2 pos;

    private Body body;
    private Fixture fixture;


    private boolean goLeft = false;
    private boolean dead = false;

    public Player(float x, float y,float initRadius, float radius, World world){
        pos = new Vector2(x, y);
        this.radius = initRadius;
        this.finalRadius = radius;
        createBoc2d(world);
    }

    public Body createBoc2d(World world){
        BodyDef playerDef = new BodyDef();
        playerDef.type = BodyDef.BodyType.DynamicBody;
        playerDef.position.set(pos);
        body = world.createBody(playerDef);
        body.setUserData(this);

        CircleShape playerShape = new CircleShape();
        playerShape.setRadius(finalRadius);

        FixtureDef playerFix = new FixtureDef();
        playerFix.shape = playerShape;
        playerFix.density = 0.15f;
        playerFix.restitution = 0f;//0.2f;
        playerFix.friction = 0f;

        fixture = body.createFixture(playerFix);
        playerShape.dispose();

        body.setLinearVelocity(new Vector2(0,15));

        return body;
    }

    public void draw(ShapeRenderer renderer){
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

    public Vector2 getVelocity() {
        return body.getLinearVelocity();
    }

    public boolean isGoLeft() {
        return goLeft;
    }

    public void setGoLeft(boolean goLeft) {
        this.goLeft = goLeft;
    }

    public void setRadius(float r) {
        radius = r;
    }

    public boolean isDead() {
        return dead;
    }

    public void die(World world){
        dead = true;

        /*BodyDef playerDef = new BodyDef();
        playerDef.type = BodyDef.BodyType.DynamicBody;
        playerDef.position.set(pos);
        Body b = world.createBody(playerDef);
        b.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.set(new Vector2[]{new Vector2(1,0),new Vector2(0,0),new Vector2(0,1)});

        FixtureDef playerFix = new FixtureDef();
        playerFix.shape = shape;
        playerFix.density = 0.15f;
        playerFix.restitution = 0f;//0.2f;
        playerFix.friction = 0f;

        Fixture fixture = b.createFixture(playerFix);
        shape.dispose();

        deadBodies.add(b);*/

    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
