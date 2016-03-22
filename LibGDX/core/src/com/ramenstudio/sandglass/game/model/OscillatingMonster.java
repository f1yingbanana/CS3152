package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.ramenstudio.sandglass.game.obstacle.BoxObstacle;
import com.ramenstudio.sandglass.game.view.GameCanvas;

public class OscillatingMonster extends SandglassMonster{
	
	/** The amount of time before this body can reach its original position */
	protected float oscillationPeriod;
	
	/** The direction of oscillation (up or down) */
	protected boolean isUpAndDown;
	
	/** The age of the monster in seconds */
	protected float ageOfMonster;

	/** Temporary Vector2 to draw for now */
	private Vector2 size = new Vector2(0.8f, 1.5f);
	// TODO: Change size appropriately
	
	
	/**
	 * Creates an instance of an OscillatingMonster
	 * 
	 * @param argTexture is the texture of this oscillating monster
	 * @param argId is the unique identifier for this monster
	 * @param x is the starting x coordinate
	 * @param y is the starting y coordinate
	 * @param argDirectionOfOscillation is whether or not the monster goes up and down or left and right
	 * @param vx determines the starting direction of oscillation, magnitude doesn't matter since speed is set
	 * @param vy determines the starting direction of oscillationg, magnitude doesn't matter since speed is set
	 * @param argSpeed is the one and only speed of this monster
	 */
	public OscillatingMonster(Texture argTexture, int argId, float x, float y, boolean argDirectionOfOscillation,
			float vx, float vy, float argSpeed) {
		hitboxOfMonster = new BoxObstacle(x,y,1,1);
		hitboxOfMonster.setBodyType(BodyDef.BodyType.KinematicBody);
		id = argId;
		hitboxOfMonster.setPosition(new Vector2(x,y));
		hitboxOfMonster.setLinearVelocity(new Vector2(vx,vy).setLength(argSpeed));
		hitboxOfMonster.setAngle(90.0f);
		destAngle = 0.0f;
		doesChase = false;
		speedOfMonster = argSpeed;
		oscillationPeriod = 3.0f;
		isUpAndDown = argDirectionOfOscillation;
		
		// TODO: Give this an actual texture, currently hardcoded to badlogic
		// or maybe don't? Might want to always hard code but definitely not badlogic.jpg
	    monsterTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
	}

	@Override
	public void update(float dt) {
		ageOfMonster += dt;
		Vector2 value = hitboxOfMonster.getLinearVelocity();
		if (ageOfMonster >= oscillationPeriod/2.0f) {
			hitboxOfMonster.setLinearVelocity(value.scl(-1.0f));
			ageOfMonster -= oscillationPeriod/2.0f;
		}
	}
	
	@Override
	public void draw(GameCanvas canvas) {
		canvas.draw(monsterTexture, hitboxOfMonster.getPosition().add(size.cpy().scl(-0.5f)), size);
	}
}
