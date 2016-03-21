package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.Drawable;

/**
 * A tile that marks a place that player should be able to turn.
 * 
 * @author Jiacong Xu
 * @author Nathaniel Hunter
 */
public class TurnTile extends GameObject implements Drawable{
	
	public PolygonShape shape;
	public Vector2 position;
	
	public TurnTile(){
		super();
		shape = new PolygonShape();
	}
	
	@Override
	public void draw(GameCanvas canvas){
		  canvas.draw(getTexture(), position, getSize());
	}
}
