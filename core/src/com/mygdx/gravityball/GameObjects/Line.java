package com.mygdx.gravityball.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Line {
    private Vector2 pointLeft = new Vector2(0,0);
    private Vector2 pointRight = new Vector2(0,0);
    private float width;
    private Color color = Color.BLACK;
    private int levelIndex = 0;

    public Line(float width){
        this.width = width;
        levelIndex = 0;
    }

    public void setNew(Vector2 left, Vector2 right, Color c){
        pointLeft = left;
        pointRight = right;
        color = c;
    }

    public void draw(ShapeRenderer renderer){
        renderer.setColor(color);
        renderer.rectLine(pointLeft,pointRight,width);
        Gdx.app.log("line",pointLeft+""+pointRight+""+width);
    }

    public float getY(){
        return pointLeft.y;
    }
    public int getLevelIndex() { return levelIndex; }
    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }

}
