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
import com.badlogic.gdx.graphics.Pixmap;
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
	private static final float MOVE_SPEED = 3.5f;
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
	/** Initial Position*/
	public Vector2 initial;
	/** Velocity */
	private Vector2 velocity;
	/** Goal */
	private Vector2 goal;
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
	public Monster(Vector2 initialPos, MType mType, int level) {
		super();
		initial = initialPos;
		switch (mType){
        case OVER:
            setTexture(new Texture(Gdx.app.getFiles().internal("overmonster.png")));
            fixtureDefs = new FixtureDef[1];
            setSize(new Vector2(0.8f, 1.2f));
            getBodyDef().position.set(initialPos);
            getBodyDef().type = BodyDef.BodyType.DynamicBody;
            
            
            FixtureDef fixtureDef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.4f, 0.6f);
            fixtureDef.density = 1.0f;
            fixtureDef.shape = shape;
            fixtureDefs[0] = fixtureDef;
            fixtureDef.friction = 0;
            break;
        case UNDER:
            if (level ==1) {
                setTexture(new Texture(Gdx.app.getFiles().internal("undermonster1.png")));
                fixtureDefs = new FixtureDef[1];
                setSize(new Vector2(0.8f, 0.8f));
                getBodyDef().position.set(initialPos);
                getBodyDef().type = BodyDef.BodyType.KinematicBody;
                
                
                FixtureDef underFixtureDef = new FixtureDef();
                PolygonShape underShape = new PolygonShape();
                underShape.setAsBox(0.4f, 0.4f);
                underFixtureDef.density = 10.0f;
                underFixtureDef.shape = underShape;
                fixtureDefs[0] = underFixtureDef;
                underFixtureDef.friction = 10;
            }
            else {
                setTexture(new Texture(Gdx.app.getFiles().internal("overmonster.png")));
                fixtureDefs = new FixtureDef[1];
                setSize(new Vector2(0.8f, 1.2f));
                getBodyDef().position.set(initialPos);
                getBodyDef().type = BodyDef.BodyType.KinematicBody;
                
                
                FixtureDef under2fixtureDef = new FixtureDef();
                PolygonShape under2shape = new PolygonShape();
                under2shape.setAsBox(0.4f, 0.6f);
                under2fixtureDef.density = 1.0f;
                under2fixtureDef.shape = under2shape;
                fixtureDefs[0] = under2fixtureDef;
                under2fixtureDef.friction = 0;
                break;
            }
            break;
        default:
            break;
        }
		this.mType = mType;
		this.level = level;
		
		velocity = new Vector2();
		angle  = 90.0f;
		
		isAlive = true;
		
		sound = null;
		sndcue = -1;
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
	 * Returns the goal of this monster
	 *
	 * @return the goal
	 */
	
	public Vector2 getGoal(){
		return goal;
	}
	
	/**
	 * Sets the goal of this monster
	 * 
	 * @param the goal of this monster
	 */
	
	public void setGoal(Vector2 goal){
		this.goal = goal;
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
	
	public boolean isAtGoal(){
	    return position.epsilonEquals(goal, 0.0f);
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
		this.getBody().setLinearVelocity(velocity);
	}

	@Override
	public void draw(GameCanvas canvas){
		canvas.draw(getTextureRegion(), getBody().getPosition().add(getSize().cpy().scl(-0.5f)), 
		        getSize(), new Vector2(getSize()).scl(.5f), (float)(getRotation() * 180/Math.PI));
	}
}
