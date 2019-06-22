package com.mygdx.gravityball;

import com.badlogic.gdx.Game;
import com.mygdx.gravityball.Screens.GameScreen;
import com.mygdx.gravityball.Screens.MenuScreen;

public class GravityBall extends Game {
	
	@Override
	public void create () {
		setScreen(new MenuScreen());
	}
}
