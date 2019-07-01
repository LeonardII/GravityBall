package com.mygdx.gravityball.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.gravityball.GameObjects.Border;
import com.mygdx.gravityball.GameObjects.Level;
import com.mygdx.gravityball.GameObjects.Line;
import com.mygdx.gravityball.GameObjects.Player;
import com.mygdx.gravityball.GameObjects.Spike;
import com.mygdx.gravityball.GameObjects.SpikeGroup;
import com.mygdx.gravityball.Scenes.Hud;

import java.util.ArrayList;
import java.util.LinkedList;

import sun.management.Sensor;

public class GameScreen implements Screen, InputProcessor, ContactListener {

    public static final int WORLD_WIDTH = 10;
    public static final int WORLD_HEIGHT = 10;
    public static final int WORLD_BOTTOM = -2; //With curved edges screen goes beyond zero
    public static final float PLAYER_FLOATING_HEIGHT = 5;
    public static final float PLAYER_SIZE = 0.4f;
    private final float BORDER_WIDTH = 0.5f;


    private ShapeRenderer shapeRenderer;
    private ExtendViewport viewport;
    private OrthographicCamera camera;

    private Hud hud;
    private SpriteBatch batch;

    private World world;

    private Player player;
    private boolean bordering = false;
    private Line line;


    //Level
    private Level[] levels = {
            new Level(100,Color.CYAN,Color.PINK,20,5,15,3,5,50, 10),
            new Level(100,Color.BLUE,Color.GOLD,20,1,1,1,1,5,15),
            new Level(100,Color.FOREST,Color.WHITE,50,5,15,1,3,50,20),
            new Level(100,Color.LIGHT_GRAY,Color.DARK_GRAY,30,1,5,1,3,10,15),
            new Level(100,Color.WHITE,Color.BROWN,30,1,6,1,4,10,15)
    };
    private int curl = 0;





    private float meters = 0;
    private final float METERS_TO_SCORE = 1f; //TODO: 0.1f

    private Border borderLeft;
    private Border borderRight;

    private LinkedList<SpikeGroup> spikeGroups = new LinkedList<SpikeGroup>();

    private Vector2 gravity = new Vector2(0,0);

    private long startTime;
    private long screenShakeX = 0;
    private float shakeAmp = 0;
    private final float ANIMATION_LENGTH = 1f;


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

        player = new Player(WORLD_WIDTH/2,PLAYER_FLOATING_HEIGHT, MenuScreen.PLAYER_INIT_SIZE/2, PLAYER_SIZE, world); //TODO: set height dynamicaly
        borderLeft = new Border(world,new Vector2(-0.5f,WORLD_BOTTOM-WORLD_HEIGHT),new Vector2(BORDER_WIDTH+0.5f,WORLD_HEIGHT*4));
        borderRight = new Border(world,new Vector2(WORLD_WIDTH-BORDER_WIDTH,WORLD_BOTTOM-WORLD_HEIGHT),new Vector2(BORDER_WIDTH+0.5f,WORLD_HEIGHT*4));
        line = new Line(0.1f);
        line.setNew(new Vector2(0,(PLAYER_FLOATING_HEIGHT+100)/METERS_TO_SCORE),new Vector2(WORLD_WIDTH,(PLAYER_FLOATING_HEIGHT+100)/METERS_TO_SCORE), levels[1].levelColor);

        startTime = TimeUtils.millis();

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

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        line.draw(shapeRenderer);
        shapeRenderer.setColor(levels[curl].levelColor);
        borderLeft.draw(shapeRenderer);
            borderRight.draw(shapeRenderer);
            for(SpikeGroup group : spikeGroups){
                group.draw(shapeRenderer);
            }

        shapeRenderer.setColor(levels[curl].playerColor);
        player.draw(shapeRenderer);
        shapeRenderer.end();
        hud.stage.draw();
    }

    private void update(float delta){
        Gdx.app.log("fps",Gdx.graphics.getFramesPerSecond()+"");

        //PHYSICS
        meters = (player.getPos().y - PLAYER_FLOATING_HEIGHT);
        //maxSpeed = 40-5000/(meters +200); //TODO
        Vector2 dir = player.getVelocity();
        dir.scl(-1);
        Vector2 vel = new Vector2(dir);
        dir.setLength(0.4f);
        vel.scl(0.035f);
        vel.add(dir);

        vel.scl(1,0.6f);
        if(bordering && !player.isDead()) player.applyForce(new Vector2(0,Math.abs(levels[curl].maxSpeed/(player.getVelocity().y +1))));
        //if(player.getVelocity().len() < 0.5f) lost();


        //STARTANIMATION
        float x = (TimeUtils.timeSinceMillis(startTime) / 1000f);
        float startAnimationOffset = 0f;
        if(x< ANIMATION_LENGTH) {
            startAnimationOffset = coolAnimation(x, ANIMATION_LENGTH);
        }else{
            //Drag Player
            player.applyForce(vel);
            //Gravity
            if(player.isGoLeft()){
                player.applyForce( new Vector2(-5f,0).scl(vel.len()));
            }else {
                player.applyForce(new Vector2(5f, 0).scl(vel.len()));
            }
        }


        //SCORE
        hud.setScore(METERS_TO_SCORE*meters);
        hud.setOrientation(0); //TODO: set ORIENTATION
        hud.update();

        //UPDATE WORLD
        world.step(delta,6,2);
        setBorderUp();
        player.syncSpriteToBody();
        borderRight.syncSpriteToBody();
        borderLeft.syncSpriteToBody();

        //LEVEL STUFF
        if(line.getY()+PLAYER_FLOATING_HEIGHT-player.getPos().y<WORLD_BOTTOM-1 && curl < levels.length-1){
            line.setNew(new Vector2(0,(line.getY()+100)/METERS_TO_SCORE),new Vector2(WORLD_WIDTH,(line.getY()+100)/METERS_TO_SCORE),levels[curl+1].levelColor);
            line.setLevelIndex(curl);
            createSendOff();
        }
        if(player.getPos().y > line.getY() && line.getLevelIndex() == curl && curl<levels.length-1){
            curl++;
        }




        //SYNC CAMERA TO PLAYER
        camera.position.y = player.getPos().y+PLAYER_FLOATING_HEIGHT - startAnimationOffset;
        float shake = 0;//shakeAmp / (float) Math.pow((0.002f*(TimeUtils.millis()-screenShakeX)+0.5),2);
        camera.position.x = camera.viewportWidth / 2 + shake;
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
    }

    private void createSendOff() {
        float bottomPos = line.getY()-levels[curl].sendoffLength*Spike.WIDTH_Y;
        spikeGroups.add(new SpikeGroup(levels[curl].sendoffLength, new Vector2(BORDER_WIDTH,bottomPos),1,1, true,world));
        spikeGroups.add(new SpikeGroup(levels[curl].sendoffLength, new Vector2(WORLD_WIDTH-BORDER_WIDTH,bottomPos),1,1, false,world));
    }

    private void setBorderUp(){
        Vector2 vel = player.getVelocity();
        vel.x = 0;
        borderRight.setVel(vel);
        borderLeft.setVel(vel);
    }

    private float lastSpikes = 0;
    private void spikeSpawn(){
        if(meters -lastSpikes > levels[curl].spikeGroupDistance
                && (WORLD_HEIGHT*3+player.getPos().y < line.getY() - 2*(levels[curl].sendoffLength * Spike.WIDTH_Y))
                || curl != line.getLevelIndex()){

            int number = MathUtils.random(levels[curl].minSpikes,levels[curl].maxSpikes);
            Vector2 bottomPos;
            boolean left;
            if(MathUtils.random(1)==1){
                bottomPos = new Vector2(BORDER_WIDTH,WORLD_HEIGHT*3+player.getPos().y);
                left = true;
            }else{
                bottomPos = new Vector2(WORLD_WIDTH-BORDER_WIDTH,WORLD_HEIGHT*3+player.getPos().y);
                left = false;
            }
            if(!areSpikesBetween(bottomPos.y,bottomPos.y+number*Spike.WIDTH_Y,left)){
                spikeGroups.add(new SpikeGroup(number,bottomPos,levels[curl].minSpikeHeight,levels[curl].maxSpikeHeight,left,world));
                lastSpikes = meters;
            }
        }
    }

    private boolean areSpikesBetween(float y1, float y2, boolean left){
        if(y1 < y2){
            y1 -= player.getRadius()*3;
            y2 += player.getRadius()*3;
        }else
            Gdx.app.exit();
        
        for(SpikeGroup g: spikeGroups){
            float x1 = g.getY1();
            float x2 = g.getY2();
            if( //g.isLeft() == left &&
                ((x1 >= y1 && x1 <= y2) ||
                (x2 >= y1 && x2 <= y2) ||
                (y1 >= x1 && y1 <= x2) ||
                (y2 >= x1 && y2 <= x2))){
                return true;
            }
         }
         return false;
    }

    public float coolAnimation(float x, float animationLength){
        player.setRadius(((PLAYER_SIZE-MenuScreen.PLAYER_INIT_SIZE/2)/animationLength)*x+MenuScreen.PLAYER_INIT_SIZE/2);

        float SH = GameScreen.WORLD_HEIGHT - PLAYER_FLOATING_HEIGHT;

        x/=animationLength;

        return  SH - 48.3584f*x + 83.45865f*x*x - 40.10025f*x*x*x;
    }


    private void lost(){
        player.die(world);
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
        v.y = 0;
        if(!player.isDead()){
            player.applyForce(v);
            player.setGoLeft(!player.isGoLeft());
        }else{
            ((Game)Gdx.app.getApplicationListener()).setScreen(new MenuScreen((int) meters));
        }
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
            screenShakeX = TimeUtils.millis();
            shakeAmp = (float) Math.pow(player.getVelocity().x,2)* (Math.signum(player.getVelocity().x)) * -0.01f / 900f;

            Gdx.app.log("beginContact","now");
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
