package com.mygdx.gravityball.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {
    public Stage stage;
    private Viewport viewport;

    private float score;
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

        BitmapFont font = new BitmapFont();

        scoreLabel = new Label(String.format("%.1f",score),new Label.LabelStyle(font, Color.WHITE));
        scoreLabel.setPosition(viewport.getWorldWidth()/2-scoreLabel.getWidth()/2,viewport.getWorldHeight()-scoreLabel.getHeight());
        stage.addActor(scoreLabel);
        stage.getViewport().apply();
    }

    public void update() {

        stage.act();
        scoreLabel.setText(String.format("%.2f",score));
        stage.draw();
    }

    public void setScore(int score) {
        this.score = score;
    }
}
