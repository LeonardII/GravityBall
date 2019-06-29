package com.mygdx.gravityball.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import sun.font.TrueTypeFont;

public class Hud {
    public Stage stage;
    private Viewport viewport;

    private int score;
    Label scoreLabel;

    public Hud(SpriteBatch batch){
        score = 0;
        viewport = new ScreenViewport(new OrthographicCamera());
        stage = new Stage(viewport,batch);

        /*FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("ostrich-regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 100;
        BitmapFont font = gen.generateFont(params);
        gen.dispose();*/

        BitmapFont font;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ostrich-regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 200;
        font = generator.generateFont(parameter);
        generator.dispose();

        scoreLabel = new Label(String.valueOf(score),new Label.LabelStyle(font, Color.WHITE));

        scoreLabel.setBounds( 0, 0, viewport.getWorldWidth(),viewport.getWorldHeight() );
        scoreLabel.setAlignment( Align.top );

        stage.addActor(scoreLabel);
        stage.getViewport().apply();
    }

    public void update() {

        stage.act();
        scoreLabel.setText(String.valueOf(score));
        stage.draw();
    }

    public void setScore(float score) {
        this.score = (int) score;
    }

    public void setOrientation(int orientation) {
        Gdx.app.log("orientation",orientation+"");
        scoreLabel.setRotation(orientation);
    }
}
