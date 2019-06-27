package com.mygdx.gravityball.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.gravityball.GameObjects.Border;
import com.mygdx.gravityball.GameObjects.Player;
import com.mygdx.gravityball.GameObjects.Spike;
import com.mygdx.gravityball.GameObjects.SpikeGroup;
import com.mygdx.gravityball.Scenes.Hud;

import java.util.ArrayList;

public class GameScreen implements Screen, InputProcessor, ContactListener {

    public static final int WORLD_WIDTH = 10;
    public static final int WORLD_HEIGHT = 10;
    public static final int WORLD_BOTTOM = -2; //With curved edges screen goes beyond zero
    private final float PLAYER_FLOATING_HEIGHT = 5;
    private final float BORDER_WIDTH = 0.5f;

    private ShapeRenderer shapeRenderer;
    private ExtendViewport viewport;
    private OrthographicCamera camera;

    private Hud hud;
    private SpriteBatch batch;

    private World world;

    private Player player;
    private boolean bordering = false;

    private float score = 0;
    private float maxSpeed = 20;

    private Border borderLeft;
    private Border borderRight;

    private ArrayList<SpikeGroup> spikeGroups = new ArrayList<SpikeGroup>();

    private Vector2 gravity = new Vector2(0,0);


    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        hud = new Hud(batch);
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport=new ExtendViewport(WORLD_WIDTH,WORLD_HEIGHT, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        world = new World(gravity , true);
        world.setVelocityThreshold(0f);
        world.setContactListener(this);

        player = new Player(WORLD_WIDTH/2,PLAYER_FLOATING_HEIGHT,0.4f, world); //TODO: set height dynamicaly
        borderLeft = new Border(world,new Vector2(0,WORLD_BOTTOM),new Vector2(BORDER_WIDTH,WORLD_HEIGHT*3));
        borderRight = new Border(world,new Vector2(WORLD_WIDTH-BORDER_WIDTH,WORLD_BOTTOM),new Vector2(BORDER_WIDTH,WORLD_HEIGHT*3));

        spikeGroups.add(new SpikeGroup(5,new Vector2(BORDER_WIDTH,50),true,world));
    }

    @Override
    public void render(float delta) {
        spikeSpawn();
        update(delta);
        draw();
    }

    private void draw(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setColor(1,1,1,1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        borderLeft.draw(shapeRenderer);
        borderRight.draw(shapeRenderer);
        for(SpikeGroup group : spikeGroups){
            group.draw(shapeRenderer);
        }
        player.draw(shapeRenderer);
        shapeRenderer.end();
        hud.stage.draw();
    }

    private void update(float delta){
        score = (player.getPos().y - PLAYER_FLOATING_HEIGHT);
        maxSpeed = 40-5000/(score+200);
        Vector2 dir = player.getVelocity();
        dir.scl(-1);
        Vector2 vel = new Vector2(dir);
        dir.setLength(0.4f);
        vel.scl(0.035f);
        vel.add(dir);

        vel.scl(1,0.6f);

        //Drag Player
        player.applyForce(vel);
        //Gravity
        if(player.isGoLeft()){
            player.applyForce( new Vector2(-5f,0).scl(vel.len()));
        }else {
            player.applyForce(new Vector2(5f, 0).scl(vel.len()));
        }

        hud.setScore((int)score);
        hud.update();

        if(bordering) player.applyForce(new Vector2(0,Math.abs(maxSpeed/(player.getVelocity().y +1))));
        //if(player.getVelocity().len() < 0.5f) lost();

        world.step(delta,6,2);
        setBorderUp();

        player.syncSpriteToBody();
        borderRight.syncSpriteToBody();
        borderLeft.syncSpriteToBody();

        camera.position.y = player.getPos().y+PLAYER_FLOATING_HEIGHT;
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
    }

    private void setBorderUp(){
        Vector2 vel = player.getVelocity();
        vel.x = 0;
        borderRight.setVel(vel);
        borderLeft.setVel(vel);
    }

    //TODO: must not depend on time, instead on length
    private float spacingMeters = 10f;
    private float lastSpikes = 0;
    private void spikeSpawn(){
        if(score-lastSpikes > spacingMeters){
            int number = MathUtils.random(1,5);
            Vector2 bottomPos;
            boolean left;
            if(MathUtils.random(1)==1){
                bottomPos = new Vector2(BORDER_WIDTH,WORLD_HEIGHT*3+player.getPos().y);
                left = true;
            }else{
                bottomPos = new Vector2(WORLD_WIDTH-BORDER_WIDTH,WORLD_HEIGHT*3+player.getPos().y);
                left = false;
            }
            spikeGroups.add(new SpikeGroup(number,bottomPos,left,world));
            spacingMeters *=0.99f;
            lastSpikes = score;
        }
    }

    private void lost(){

        ((Game)Gdx.app.getApplicationListener()).setScreen(new MenuScreen((int) score));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        world.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //world.setGravity(world.getGravity().scl(-1));
        Vector2 v = new Vector2(player.getVelocity());
        v.scl(-1);
        player.applyForce(v);
        player.setGoLeft(!player.isGoLeft());
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();
        if((a instanceof Player && b instanceof Border) || (b instanceof Player && a instanceof Border)){
            bordering = true;
        }

        if((a instanceof Player && b instanceof Spike) || (b instanceof Player && a instanceof Spike)){
            lost();
        }
    }

    @Override
    public void endContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();
        if((a instanceof Player && b instanceof Border) || (b instanceof Player && a instanceof Border)){
            bordering = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
