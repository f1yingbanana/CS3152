package com.ramenstudio.sandglass.game.model;

import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.Drawable;

public interface SandglassTile extends Drawable{
	public boolean isFlippable();
	public boolean isGround();
	public boolean isGoal();
	public void draw(GameCanvas canvas);
	public float getWidth();
	public float getHeight();
}
