/*
 * Monster.java
 * 
 * This is the first draft of model class representing monster. 
 * Monsters behave differently according to its MonsterType, 
 * whose movement will be controlled by MonsterController.java
 *
 * Author: Saerom Choi
 * Based on monster.java by Walker M. White
 */

package com.ramenstudio.sandglass.game.model;

import java.util.Random;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.Drawable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.graphics.Texture;

/**
 * A model class representing a monster.
 */
public class Monster extends GameObject implements Drawable{

	public static enum MType {
		/* For over world */
		OVER,
		/*For under world*/
		UNDER
	}
	/** Static random number generator shared across all monsters */
	private static final Random random = new Random();

	// CONSTANTS FOR monster HANDLING
	/** How far forward this monster can move in a single turn */
	private static final float MOVE_SPEED = 6.5f;
	/** How much this monster can turn in a single turn */
	private static final float TURN_SPEED = 15.0f;
	/** For animating turning movement */
	private static final float RAND_FACTOR = (2.0f / 128.0f);
	private static final float RAND_OFFSET = (1.0f / 128.0f);
	private static final float FULL_CIRCLE = 360.0f;
	private static final float HALF_CIRCLE = 180.0f;
	/** Time increment used by shader */
	private static final float SPEED_DAMPNING = 0.75f;
	private static final float EPSILON_CLAMP = 0.01f;
	
	// Instance Attributes
	/** A unique identifier; used to decouple classes. */
	private int id;
	/** Monster type*/
	private MType mType;
	/** Monster Level*/
	private int level;
	/** Position */
	private Vector2 position;
	/** Velocity */
	private Vector2 velocity;
	/** The current angle of orientation (in degrees) */
	private float angle;
	
	/** Boolean to track if we are dead yet */
	private boolean isAlive;
	
	/** The sound currently associated with this monster */
	private Sound sound;
	/** The associated sound cue (if monster is making a sound). */
	private long sndcue;

	/**
	 * Create monster # id at the given position.
	 *
	 * @param id The unique monster id
	 * @param x The initial x-coordinate of the monster
	 * @param y The initial y-coordinate of the monster
	 */
	public Monster(int id, Vector2 initialPos, MType mType, int level) {
		super();
		fixtureDefs = new FixtureDef[3];
	    
	    setTexture(new Texture(Gdx.files.internal("character2.png")));
	    setSize(new Vector2(0.8f, 1.5f));
	    getBodyDef().position.set(initialPos);
	    getBodyDef().type = BodyDef.BodyType.DynamicBody;
	    
	    FixtureDef fixtureDef = new FixtureDef();
	    PolygonShape shape = new PolygonShape();
	    shape.setAsBox(0.4f, 0.35f);
	    fixtureDef.density = 10.0f;
	    fixtureDef.shape = shape;
	    fixtureDefs[0] = fixtureDef;
	    fixtureDef.friction = 0;
	    
	    CircleShape c = new CircleShape();
	    c.setRadius(0.4f);
	    c.setPosition(new Vector2(0, -0.35f));
	    FixtureDef fixtureDef2 = new FixtureDef();
	    fixtureDef2.shape = c;
	    fixtureDefs[1] = fixtureDef2;

	    CircleShape c2 = new CircleShape();
	    c2.setRadius(0.4f);
	    c2.setPosition(new Vector2(0, 0.35f));
	    FixtureDef fixtureDef3 = new FixtureDef();
	    fixtureDef3.shape = c2;
	    fixtureDefs[2] = fixtureDef3;

		
		this.id = id;
		this.mType = mType;
		
		
		switch (mType){
		case OVER:
				setTexture(new Texture(Gdx.files.internal("overmonster.png")));
			break;
		case UNDER:
			if (level ==1) {
				setTexture(new Texture(Gdx.files.internal("undermonster1.png")));
			}
			else {
			}
			break;
		default:
			break;
		}
		
		
		velocity = new Vector2();
		angle  = 90.0f;
		
		isAlive = true;
		
		sound = null;
		sndcue = -1;
		
		fixtureDefs = new FixtureDef[3];
	}
	
	/** 
	 * Returns the monster level 
	 * 
	 * @return the monster level 
	 */
	public int getLevel(){
		return level;
	}
	
	/** 
	 * Returns the unique monster id number 
	 * 
	 * @return the unique monster id number 
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the x-coordinate of the monster position
	 *
	 * @return the x-coordinate of the monster position
	 */
	public float getX() {
		return position.x;
	}

	/**
	 * Returns the y-coordinate of the monster position
	 *
	 * @return the y-coordinate of the monster position
	 */
	public float getY() {
		return position.y;
	}
	
	/**
	 * Returns the monster ypte
	 *
	 * @return the monster type
	 */
	public MType getType() {
		return mType;
	}
	
	/**
	 * Returns the position of this monster.
	 *
	 * This method returns a reference to the underlying monster position vector.
	 * Changes to this object will change the position of the monster.
	 *
	 * @return the position of this monster.
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * Returns the x-coordinate of the monster velocity
	 *
	 * @return the x-coordinate of the monster velocity
	 */
	public float getVX() {
		return velocity.x;
	}

	/**
	 * Returns the y-coordinate of the monster velocity
	 *
	 * @return the y-coordinate of the monster velocity
	 */
	public float getVY() {
		return velocity.y;
	}

	/**
	 * Returns the velocity of this monster.
	 *
	 * This method returns a reference to the underlying monster velocity vector.
	 * Changes to this object will change the velocity of the monster.
	 *
	 * @return the velocity of this monster.
	 */	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	/**
	 * Returns the current facing angle of the monster
	 *
	 * This value cannot be changed externally.  It can only
	 * be changed by update()
	 *
	 * @return the current facing angle of the monster
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * Sets the x-coordinate of the monster position
	 *
	 * @param value the x-coordinate of the monster position
	 */
	public void setX(float value) {
		position.x = value;
	}

	/**
	 * Sets the y-coordinate of the monster position
	 *
	 * @param value the y-coordinate of the monster position
	 */
	public void setY(float value) {
		position.y = value;
	}

	/**
	 * Sets the x-coordinate of the monster velocity
	 *
	 * @param value the x-coordinate of the monster velocity
	 */
	public void setVX(float value) {
		velocity.x = value;
	}
	
	/**
	 * Sets the y-coordinate of the monster velocity
	 *
	 * @param value the y-coordinate of the monster velocity
	 */
	public void setVY(float value) {
		velocity.y = value;
	}
	
	/**
	 * Returns whether or not the monster is alive.
	 *
	 * A monster is dead once it has fallen past MAX_FALL_AMOUNT. A dead monster cannot be 
	 * targeted, involved in collisions, or drawn.  For all intents and purposes, it 
	 * does not exist.
	 *
	 * @return whether or not the monster is alive
	 */	 
	public boolean isAlive() {
		return isAlive;
	}
	
	/**
	 * Sets whether or not the monster is alive.
	 *
	 * This method should only be used if we need to kill the monster immediately.
	 * The preferred method to get rid of a monster is destroy().
	 *
	 * @param value whether or not the monster is alive.
	 */
	public void setAlive(boolean value) {
		isAlive = value;
	}
	
	/**
	 * Push the monster so that it starts to fall.
	 * 
	 * This method will not destroy the monster immediately.  It will tumble and fall 
	 * offscreen before dy	ing. To instantly kill a monster, use setAlive().
	 */
	public void destroy() {
		// TODO: Implement
	}
	
	/**
	 * Plays the appropriate sound for this monster's current 
	 */
	public void play() {
		
	}

	/**
	 * Updates this monster position (and weapons fire) according to the control code.
	 *
	 * This method updates the velocity and the weapon status, but it does not change
	 * the position or create photons.  The later interact with other objects (position
	 * can cause collisions) so they are processed in a controller.  Method in a model
	 * object should only modify state of that specific object and no others.
	 * 
	 * 0 is for up, 1 is for down, 2 is for left, 3 is for right
	 *
	 * @param controlCode The movement controlCode (from InputController).
	 */
	public void update(int move) {
		if (!isAlive) {
			return;
		}
		
		switch (move){
		case 0:
			velocity.y = MOVE_SPEED;
			velocity.x = 0;
			break;
		case 1:
			velocity.y = -MOVE_SPEED;
			velocity.x = 0;
			break;
		case 2:
			velocity.x = -MOVE_SPEED;
			velocity.y = 0;
			break;
		case 3:
			velocity.x = MOVE_SPEED;
			velocity.y = 0;
			break;
		default:
			break;
		}		
	}

	@Override
	public void draw(GameCanvas canvas){
		canvas.draw(getTextureRegion(), getPosition().add(getSize().cpy().scl(-0.5f)), getSize(),
				new Vector2(getSize()).scl(.5f), (float)(getRotation() * 180/Math.PI));
	}
}
