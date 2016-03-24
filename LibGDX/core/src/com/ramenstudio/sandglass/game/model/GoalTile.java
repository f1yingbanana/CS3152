package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class GoalTile extends GameObject implements GameTile{

	private PolygonShape thePolygonShape;
	private FixtureDef theFixtureDef;
	private float width;
	private float height;
	
	public GoalTile() {
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
	public boolean isFlippable() {
		return false;
	}

	@Override
	public boolean isGround() {
		return false;
	}
	
	@Override
	public boolean isGoal() {
		return true;
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
