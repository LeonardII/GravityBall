package com.mygdx.gravityball.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuScreen implements Screen {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    protected Stage stage;
    private ExtendViewport viewport;
    private OrthographicCamera camera;

    Image title;
    public final static float PLAYER_INIT_SIZE = 3f;
    Image menuPlaer;
    Label highScoreLabel;
    TextButton playButton;

    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;
    Preferences prefs;


    int highscore;

    public MenuScreen()
    {
        init(0);
    }
    public MenuScreen(int score){
        init(score);
    }
    private void init(int score){
        prefs = Gdx.app.getPreferences("game preferences");
        if(prefs.contains("highscore")) {
            if (prefs.getInteger("highscore") < score) {
                prefs.putInteger("highscore", score);
                prefs.flush();
                this.highscore = score;
            } else {
                this.highscore = prefs.getInteger("highscore");
            }
        }else{
            prefs.putInteger("highscore", score);
        }

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        viewport=new ExtendViewport(GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {
        //Stage should controll input:
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.top();

        title = new Image(new Texture(Gdx.files.internal("title.png")));
        title.setScaling(Scaling.fillX);
        title.setAlign(Align.top);

        BitmapFont font;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ostrich-regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 200;
        font = generator.generateFont(parameter);
        generator.dispose();
        highScoreLabel = new Label("highscore: "+highscore, new Label.LabelStyle(font, Color.WHITE));
        Group group = new Group();
        group.addActor(highScoreLabel);
        group.setScale(0.004f);




        menuPlaer = new Image(new Texture(Gdx.files.internal("menuPlayer.png")));
        menuPlaer.setSize(PLAYER_INIT_SIZE,PLAYER_INIT_SIZE);
        menuPlaer.setPosition(GameScreen.WORLD_WIDTH/2-PLAYER_INIT_SIZE/2,
                GameScreen.WORLD_HEIGHT-PLAYER_INIT_SIZE/2);


        font = new BitmapFont();
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons.pack"));
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("playButton");
        textButtonStyle.down = skin.getDrawable("plusButton");
        //textButtonStyle.checked = skin.getDrawable("checked-button");
        playButton = new TextButton("", textButtonStyle);
        //Create buttons
        //Button playButton = new Button(new Button.ButtonStyle());

        //Add listeners to buttons
        menuPlaer.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            }
        });

        mainTable.padTop(2);
        mainTable.add(title).height(4f).padBottom(1);
        mainTable.row();
        mainTable.add(group).width(4).padRight(3).padLeft(3);

        //Add table to stage
        stage.addActor(mainTable);
        stage.addActor(menuPlaer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(viewport.getWorldWidth()/2,viewport.getWorldHeight()*0.3f,4f);
        shapeRenderer.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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

    }

}
