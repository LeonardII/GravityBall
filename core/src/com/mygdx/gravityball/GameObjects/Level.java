package com.mygdx.gravityball.GameObjects;

import com.badlogic.gdx.graphics.Color;

public class Level {
    public float length;
    public Color levelColor;
    public Color playerColor; //Test
    public float maxSpeed;
    public int minSpikes;
    public int maxSpikes;
    public int maxSpikeHeight;
    public int minSpikeHeight;
    public float spikeGroupDistance;

    public Level(float length, Color levelColor, Color playerColor, float maxSpeed, int minSpikes, int maxSpikes,
                 int minSpikeHeight, int maxSpikeHeight, float spikeGroupDistance) {
        this.length = length;
        this.levelColor = levelColor;
        this.playerColor = playerColor;
        this.maxSpeed = maxSpeed;
        this.minSpikes = minSpikes;
        this.maxSpikes = maxSpikes;
        this.minSpikeHeight = minSpikeHeight;
        this.maxSpikeHeight = maxSpikeHeight;
        this.spikeGroupDistance = spikeGroupDistance;
    }
}
