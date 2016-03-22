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
	private PolygonShape thePolygonShape;
	private FixtureDef theFixtureDef;
	private float width;
	private float height;
	
	
	public TurnTile() {
		super();
		bodyDef.type = BodyDef.BodyType.StaticBody;

		PolygonShape shape = new PolygonShape();
		thePolygonShape = shape;
		fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		shape.setAsBox(0.5f, 0.5f);
		fixtureDef.shape = shape;
		theFixtureDef = fixtureDef;
		width = .5f;
		height = .5f;
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


	@Override
	public float getWidth() {
		return width;
	}


	@Override
	public float getHeight() {
		return height;
	}
}
