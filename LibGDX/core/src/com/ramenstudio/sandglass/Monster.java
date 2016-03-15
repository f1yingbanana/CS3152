/*
 * Monster.java
 * 
 * This is the first draft of model class representing monster. 
 * Monsters behave differently according to its MonsterType, 
 * whose movement will be controlled by MonsterController.java
 *
 * Author: Saerom Choi
 * Based on Ship.java by Walker M. White
 */
package edu.cornell.gdiac.ailab;

import java.util.Random;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.audio.*;

/**
 * A model class representing a monster.
 */
public class Monster {

	protected enum MType {
		/* For over world */
		OVER,
		/* For under world*/
		UNDER
	}
	/** Static random number generator shared across all ships */
	private static final Random random = new Random();

	// CONSTANTS FOR SHIP HANDLING
	/** How far forward this monster can move in a single turn */
	private static final float MOVE_SPEED = 6.5f;
	/** How much this ship can turn in a single turn */
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
	/** The angle we want to go to (for momentum) */
	private float dstAng;
	
	/** Boolean to track if we are dead yet */
	private boolean isAlive;
	// * The number of frames until we can fire again 
	// private int firecool;
	
	/** The sound currently associated with this ship */
	private Sound sound;
	/** The associated sound cue (if ship is making a sound). */
	private long sndcue;

	/**
	 * Create ship # id at the given position.
	 *
	 * @param id The unique ship id
	 * @param x The initial x-coordinate of the ship
	 * @param y The initial y-coordinate of the ship
	 */
	public Ship(int id, float x, float y, MType mType) {
		this.id = id;
		this.mType = mType;
		position = new Vector2(x,y);
		velocity = new Vector2();
		angle  = 90.0f;
		dstAng = 0.0f;
		
		isAlive = true;
		firecool = 0;
		
		sound = null;
		sndcue = -1;
	}
	
	/** 
	 * Returns the unique ship id number 
	 * 
	 * @return the unique ship id number 
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the x-coordinate of the ship position
	 *
	 * @return the x-coordinate of the ship position
	 */
	public float getX() {
		return position.x;
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
	 * Sets the x-coordinate of the ship position
	 *
	 * @param value the x-coordinate of the ship position
	 */
	public void setX(float value) {
		position.x = value;
	}

	/**
	 * Returns the y-coordinate of the ship position
	 *
	 * @return the y-coordinate of the ship position
	 */
	public float getY() {
		return position.y;
	}

	/**
	 * Sets the y-coordinate of the ship position
	 *
	 * @param value the y-coordinate of the ship position
	 */
	public void setY(float value) {
		position.y = value;
	}
	
	/**
	 * Returns the position of this ship.
	 *
	 * This method returns a reference to the underlying ship position vector.
	 * Changes to this object will change the position of the ship.
	 *
	 * @return the position of this ship.
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * Returns the x-coordinate of the ship velocity
	 *
	 * @return the x-coordinate of the ship velocity
	 */
	public float getVX() {
		return velocity.x;
	}

	/**
	 * Sets the x-coordinate of the ship velocity
	 *
	 * @param value the x-coordinate of the ship velocity
	 */
	public void setVX(float value) {
		velocity.x = value;
	}

	/**
	 * Returns the y-coordinate of the ship velocity
	 *
	 * @return the y-coordinate of the ship velocity
	 */
	public float getVY() {
		return velocity.y;
	}

	/**
	 * Sets the y-coordinate of the ship velocity
	 *
	 * @param value the y-coordinate of the ship velocity
	 */
	public void setVY(float value) {
		velocity.y = value;
	}

	/**
	 * Returns the velocity of this ship.
	 *
	 * This method returns a reference to the underlying ship velocity vector.
	 * Changes to this object will change the velocity of the ship.
	 *
	 * @return the velocity of this ship.
	 */	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	/**
	 * Returns the current facing angle of the ship
	 *
	 * This value cannot be changed externally.  It can only
	 * be changed by update()
	 *
	 * @return the current facing angle of the ship
	 */
	public float getAngle() {
		return angle;
	}
	
	/**
	 * Returns whether or not the ship is alive.
	 *
	 * A ship is dead once it has fallen past MAX_FALL_AMOUNT. A dead ship cannot be 
	 * targeted, involved in collisions, or drawn.  For all intents and purposes, it 
	 * does not exist.
	 *
	 * @return whether or not the ship is alive
	 */	 
	public boolean isAlive() {
		return isAlive;
	}
	
	/**
	 * Push the ship so that it starts to fall.
	 * 
	 * This method will not destroy the ship immediately.  It will tumble and fall 
	 * offscreen before dying. To instantly kill a ship, use setAlive().
	 */
	public void destroy() {
		fallAmount = MIN_FALL_AMOUNT;
	}
	
	/**
	 * Sets whether or not the ship is alive.
	 *
	 * This method should only be used if we need to kill the ship immediately.
	 * The preferred method to get rid of a ship is destroy().
	 *
	 * @param value whether or not the ship is alive.
	 */
	public void setAlive(boolean value) {
		isAlive = value;
	}
	
	/**
	 * Plays the given sound.  
	 *
	 * Each ship can only play one sound at a time.  If a sound is currently playing,
	 * it will be stopped.
	 */
	public void play(String sound) {
		if (sndcue != -1) {
			this.sound.stop(sndcue);
		}
		this.sound = SoundController.get(sound);
		sndcue = this.sound.play();
	}

	/**
	 * Updates this ship position (and weapons fire) according to the control code.
	 *
	 * This method updates the velocity and the weapon status, but it does not change
	 * the position or create photons.  The later interact with other objects (position
	 * can cause collisions) so they are processed in a controller.  Method in a model
	 * object should only modify state of that specific object and no others.
	 *
	 * @param controlCode The movement controlCode (from InputController).
	 */
	public void update(int controlCode) {
		// If we are dead do nothing.
		if (!isAlive) {
			return;
		} else if (fallAmount >= MIN_FALL_AMOUNT) {
			// Animate the fall, but quit
			fallAmount += FALL_RATE;
			isAlive = !(fallAmount > MAX_FALL_AMOUNT);
			return;
		}

		// Determine how we are moving.
		boolean movingLeft  = (controlCode & InputController.CONTROL_MOVE_LEFT) != 0;
		boolean movingRight = (controlCode & InputController.CONTROL_MOVE_RIGHT) != 0;
		boolean movingUp    = (controlCode & InputController.CONTROL_MOVE_UP) != 0;
		boolean movingDown  = (controlCode & InputController.CONTROL_MOVE_DOWN) != 0;

		// Process movement command.
		if (movingLeft) {
			dstAng = 0.0f;
			velocity.x = -MOVE_SPEED;
			velocity.y = 0;
		} else if (movingRight) {
			dstAng = 180.0f;
			velocity.x = MOVE_SPEED;
			velocity.y = 0;
		} else if (movingUp) {
			dstAng = 90.0f;
			velocity.y = -MOVE_SPEED;
			velocity.x = 0;
		} else if (movingDown) {
			dstAng = 270.0f;
			velocity.y = MOVE_SPEED;
			velocity.x = 0;
		} else {
			// NOT MOVING, SO SLOW DOWN
			velocity.x *= SPEED_DAMPNING;
			velocity.y *= SPEED_DAMPNING;
			if (Math.abs(velocity.x) < EPSILON_CLAMP) {
				velocity.x = 0.0f;
			}
			if (Math.abs(velocity.y) < EPSILON_CLAMP) {
				velocity.y = 0.0f;
			}
		}

		updateRotation();
	}

	/**
	 * Update the ship rotation so that angle gets closer to dstAng
	 *
	 * This allows us to have some delay in rotation, even though
	 * movement is always left-right/up-down.  The result is a much
	 * smoother animation.
	 */
	private void updateRotation() {
		// Change angle to get closer to dstAng
		if (angle > dstAng) {
			float angleDifference = angle - dstAng;
			if (angleDifference <= TURN_SPEED) {
				angle = dstAng;
			} else {
				if (angleDifference == HALF_CIRCLE) {
					angleDifference += random.nextFloat()*RAND_FACTOR-RAND_OFFSET;
				}
				if (angleDifference > HALF_CIRCLE) {
					angle += TURN_SPEED;
				} else {
					angle -= TURN_SPEED;
				}
			}
			velocity.setZero();
		} else if (angle < dstAng) {
			float angleDifference = dstAng - angle;
			if (angleDifference <= TURN_SPEED) {
				angle = dstAng;
			} else {
				if (angleDifference == HALF_CIRCLE) {
					angleDifference += random.nextFloat()*RAND_FACTOR-RAND_OFFSET;
				}
				if (angleDifference > HALF_CIRCLE) {
					angle -= TURN_SPEED;
				} else {
					angle += TURN_SPEED;
				}
			}
			velocity.setZero();
		}
		
		// Get rid of overspins.
		while (angle > FULL_CIRCLE) {
			angle -= FULL_CIRCLE;
		}
		while (angle < 0.0f) {
			angle += FULL_CIRCLE;
		}
	}
}