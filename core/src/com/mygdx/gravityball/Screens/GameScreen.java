package com.mygdx.gravityball.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.gravityball.GameObjects.Border;
import com.mygdx.gravityball.GameObjects.Player;
import com.mygdx.gravityball.GameObjects.SpikeGroup;

import java.util.ArrayList;

public class GameScreen implements Screen, InputProcessor {

    public static final int WORLD_WIDTH = 100;
    public static final int WORLD_HEIGHT = 100;
    private final float PLAYER_FLOATING_HEIGHT = 50;

    private ShapeRenderer shapeRenderer;
    private ExtendViewport viewport;
    private OrthographicCamera camera;

    private World world;

    private Player player;

    private Border borderLeft1;
    private Border borderLeft2;
    private Border borderRight1;
    private Border borderRight2;

    private ArrayList<SpikeGroup> spikeGroups = new ArrayList<SpikeGroup>();

    private Vector2 gravity = new Vector2(100,0);


    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport=new ExtendViewport(WORLD_WIDTH,WORLD_HEIGHT, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        world = new World(gravity , true);
        world.setVelocityThreshold(0f);

        player = new Player(WORLD_WIDTH/2,PLAYER_FLOATING_HEIGHT,5, world); //TODO: set height dynamicly
        borderLeft1 = new Border(world,new Vector2(0,0),new Vector2(5,WORLD_HEIGHT*3));
        borderRight1 = new Border(world,new Vector2(WORLD_WIDTH-5,0),new Vector2(5,WORLD_HEIGHT*3));
        borderLeft2 = new Border(world,new Vector2(0,borderLeft1.getDim().y),new Vector2(5,WORLD_HEIGHT*3));
        borderRight2 = new Border(world,new Vector2(WORLD_WIDTH-5,borderRight1.getDim().y),new Vector2(5,WORLD_HEIGHT*3));

    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void draw(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setColor(1,1,1,1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        borderLeft1.draw(shapeRenderer);
        borderRight1.draw(shapeRenderer);
        borderLeft2.draw(shapeRenderer);
        borderRight2.draw(shapeRenderer);
        player.draw(shapeRenderer);
        shapeRenderer.end();
    }

    private void update(float delta){
        world.step(delta,6,2);
        player.syncSpriteToBody();
        camera.position.y = player.getPos().y+PLAYER_FLOATING_HEIGHT;
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        setBorderUp();
    }

    private void setBorderUp(){
        if(borderLeft1.getPos().y < viewport.getCamera().position.y-viewport.getMaxWorldHeight()/2)
            borderLeft1.setPosUp();
        if(borderLeft2.getPos().y < viewport.getCamera().position.y-viewport.getMaxWorldHeight()/2)
            borderLeft2.setPosUp();
        if(borderRight1.getPos().y < viewport.getCamera().position.y-viewport.getMaxWorldHeight()/2)
            borderRight1.setPosUp();
        if(borderRight2.getPos().y < viewport.getCamera().position.y-viewport.getMaxWorldHeight()/2)
            borderRight2.setPosUp();
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
        world.setGravity(world.getGravity().scl(-1));
        //player.applyForce(world.getGravity().scl(100));
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
}
