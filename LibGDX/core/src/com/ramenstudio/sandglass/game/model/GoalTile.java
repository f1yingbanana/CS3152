package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class GoalTile extends GameObject {

	private PolygonShape thePolygonShape;
	private FixtureDef theFixtureDef;
	private float width;
	private float height;
	
	public GoalTile() {
		super();
		bodyDef.type = BodyDef.BodyType.StaticBody;

		PolygonShape shape = new PolygonShape();
		thePolygonShape = shape;
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		shape.setAsBox(0.5f, 0.5f);
		fixtureDef.shape = shape;
		theFixtureDef = fixtureDef;
		width = .5f;
		height = .5f;
	}
	
	public boolean isFlippable() {
		return false;
	}

	public boolean isGround() {
		return false;
	}
	
	public boolean isGoal() {
		return true;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

}
