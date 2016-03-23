package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.ramenstudio.sandglass.game.obstacle.Obstacle;
import com.ramenstudio.sandglass.game.view.GameCanvas;

public abstract class SandglassMonster {
	
	/**
	 * An enum class to encapsulate movements of this monster
	 */
	protected static enum directionCode {
		UP,
		LEFT,
		RIGHT,
		DOWN
	}
	
	/** The underlying Obstacle that encapsulates the hitbox of the monster */
	protected Obstacle hitboxOfMonster;
	
	/** The one and only speed of this monster */
	protected float speedOfMonster;
	
	/** Which direction is the monster facing */
	protected boolean faceRight;
	
	/** If the monster goes on a pre-determined path or chases */
	protected boolean doesChase;
	
	/** Id of this monster */
	protected int id;
	
	/** The angle we want to go to (for momentum) */
	protected float destAngle;
	
	/** Flag for whether this monster is dead or alive */
	protected boolean isAlive;
	
	/** The texture of this monster */
	protected Texture monsterTexture;
	
	public boolean isChasing() {
		return doesChase;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public Texture getTexture() {
		return monsterTexture;
	}
	
	/**
	 * Updates the object's physics state.
	 * 
	 * Use this method to reset cooldowns.
	 * 
	 * @param dt number of second since last animation frame.
	 */
	public abstract void update(float dt);
	
	public abstract void draw(GameCanvas canvas);
}
