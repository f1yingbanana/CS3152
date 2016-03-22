package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.Drawable;

/**
 * A tile that marks a place that player should be able to turn.
 * 
 * @author Jiacong Xu
 * @author Nathaniel Hunter
 */
public class TurnTile extends GameObject implements SandglassTile {

	private boolean isFlip;
	
	public TurnTile() {
		super();
		bodyDef.type = BodyDef.BodyType.StaticBody;

		PolygonShape shape = new PolygonShape();
		fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		shape.setAsBox(0.5f, 0.5f);
		fixtureDef.shape = shape;
	}
	
	
	@Override
	public void draw(GameCanvas canvas){
		canvas.draw(getTexture(), getPosition(), getSize());
	}
	
	
	@Override
	public boolean isFlippable() {
		return isFlip;
	}


	@Override
	public boolean isGround() {
		return false;
	}
}
