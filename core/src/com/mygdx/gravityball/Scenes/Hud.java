package com.mygdx.gravityball.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.gravityball.Screens.GameScreen;


public class Hud {
    public Stage stage;
    private Viewport viewport;

    private float score;
    Label scoreLabel;

    public Hud(SpriteBatch batch){
        score = 0;
        viewport = new FitViewport(GameScreen.WORLD_WIDTH,GameScreen.WORLD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport,batch);

        BitmapFont font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(0.1f);
        scoreLabel = new Label(String.valueOf(score),new Label.LabelStyle(font, Color.WHITE));
        scoreLabel.setPosition(GameScreen.WORLD_WIDTH/2-scoreLabel.getWidth()/2,viewport.getWorldHeight()-1.5f);
        stage.addActor(scoreLabel);
    }

    public void setScore(float score) {
        this.score = score;
        Gdx.app.log("lol",""+score);
    }
}
