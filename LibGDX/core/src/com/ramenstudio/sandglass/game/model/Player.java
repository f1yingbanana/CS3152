package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ramenstudio.sandglass.game.view.*;
import com.ramenstudio.sandglass.util.Drawable;

/**
 * The player model. This stores information about the player in this level as
 * well as information to draw the player.
 * 
 * @author flyingbanana
 */
public class Player extends GameObject implements Drawable {

	/** The Player's sprite. */
	private FilmStrip playerSprite;
	
	/**the number of flips the player has in this level*/
	private int flips = 10;
	
	private Vector2 impulse;

	public boolean isImpulse;
	
	private boolean isDeductFlip;
	
	private boolean isTouchMF;
	
	public int DEDUCT_COOL_TIME = 2;

	public boolean isGrounded;

	public boolean isFlashing;
	
	/** Private field to keep track of whether or not to show the player image. */
	private boolean flash = true;
	
	/** Cooldown for flashing image. */
	private static final int FLASH_COOLDOWN = 5;
	
	/** Flashing counter. */
	private int flashCounter = 0;
    
    /** The direction the player is facing (for drawing)
     *	left is -1, right is 1  
     */
//    public float direction;

	/**
	 * Creates the player at the given initial position.
	 * 
	 * @param initialPos is the position of the player at the time of creation.
	 */
	public Player(Vector2 initialPos) {
		super();

		fixtureDefs = new FixtureDef[3];

//		setTexture(player);
		setSize(new Vector2(1.0f, 1.5f));
		getBodyDef().position.set(initialPos);
		getBodyDef().type = BodyDef.BodyType.DynamicBody;

		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.2f, 0.45f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.restitution = 0.2f;
		fixtureDefs[0] = fixtureDef;

		CircleShape c = new CircleShape();
		c.setRadius(0.2f);
		c.setPosition(new Vector2(0, -0.55f));
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = c;
		fixtureDef2.density = 100f;
		fixtureDef2.friction = 0;
		fixtureDefs[1] = fixtureDef2;

		CircleShape c2 = new CircleShape();
		c2.setRadius(0.2f);
		c2.setPosition(new Vector2(0, 0.55f));
		FixtureDef fixtureDef3 = new FixtureDef();
		fixtureDef3.shape = c2;
		fixtureDefs[2] = fixtureDef3;
		
		
//		int NUM_EDGES = 8;
//		float stepSize = 2*(float)Math.PI / NUM_EDGES;
//		
//		float[] vertices = new float[2*NUM_EDGES];
//		float xRadius = size.x / 2.0f-0.1f;
//		float yRadius = size.y / 2.0f;
//		for (int ii = 0; ii < NUM_EDGES; ii++) {
//			double angle =  stepSize * ii;
//			vertices[2*ii  ] = (float)Math.cos(angle) * xRadius;
//			vertices[2*ii+1] = (float)Math.sin(angle) * yRadius;
//		}
//		
//		PolygonShape poly = new PolygonShape();
//		poly.set(vertices);
//		FixtureDef fixtureDef4 = new FixtureDef();
//		fixtureDef4.shape = poly;
//		fixtureDef4.density = 0.1f;
//		fixtureDefs[3] = fixtureDef4;
		
		
	}

	/**@return the number of flips we have left in this level**/
	  public int getFlips(){
		  return flips;
	  }
	  
	  /**increment the number of flips*/
	  public void addFlip(){
		  flips++;
	  }
	  
	  /**decrement the number of flips*/
	  public void subtractFlip(int n){
		  flips-=n;
	  }
	  
	  /**@param s the new number of flips*/
	  public void setFlips(int s){
		  flips = s;
	  }
	
	/**
	 * Sets the image texture for this player
	 * 
	 * This value should be loaded by the PlayerController and set there. 
	 * However, we have to be prepared for this to be null at all times
	 *
	 * @param value the image texture for this ship
	 */
	public void setFilmStrip(FilmStrip value) {
		playerSprite = value;
	}
	
	/**
	 * Gets the frame number for this playerSprite
	 *
	 * @return the frame number the filmstrip is set to
	 */
	public int getFrame() {
		return playerSprite.getFrame();
	}
	
	/**
	 * Sets the frame number for this playerSprite
	 *
	 * @param frame the frame number to set the filmstrip to
	 */
	public void setFrame(int frame) {
		playerSprite.setFrame(frame);
	}

	public FilmStrip getPlayerSprite() {
		return playerSprite;
	}

	public void setPlayerSprite(FilmStrip playerSprite) {
		this.playerSprite = playerSprite;
	}
	
	public void setImpulse(Vector2 impulse){
		this.impulse = impulse;
		isImpulse = true;
	}

	@Override
	public void draw(GameCanvas canvas) {
//		Vector2 size = getSize();
////		if (direction == 1) {
////			size.x *= -1;
////		}
//		if (isTouchMF && !isGrounded) {
//			canvas.draw(playerSprite, Color.RED, getPosition().add(getSize().cpy().scl(-0.5f)), size,
//					new Vector2(getSize()).scl(.5f), (float)(getRotation() * 180/Math.PI));
//		}

		if (isFlashing) {
			if (flash && flashCounter < FLASH_COOLDOWN) {
				flashCounter++;
				canvas.draw(playerSprite, Color.WHITE, getPosition().add(getSize().cpy().scl(-0.5f)), size,
						new Vector2(getSize()).scl(.5f), (float)(getRotation() * 180/Math.PI));
			} else {
				flash = (flashCounter == 0) ? true : false;
				flashCounter--;
			}
		} else {
			canvas.draw(playerSprite, Color.WHITE, getPosition().add(getSize().cpy().scl(-0.5f)), size,
					new Vector2(getSize()).scl(.5f), (float)(getRotation() * 180/Math.PI));
		}
		
	}

	@Override
	public void setBody(Body body) {
		super.setBody(body);
	}

	public Vector2 getImpulse() {
		return impulse;
	}

	public boolean isDeductFlip() {
		return isDeductFlip;
	}

	public void setDeductFlip(boolean isDeductFlip) {
		this.isDeductFlip = isDeductFlip;
	}

	public boolean isTouchMF() {
		return isTouchMF;
	}

	public void setTouchMF(boolean isTouchMF) {
		this.isTouchMF = isTouchMF;
	}
	
	public void dispose() {
		playerSprite.dispose();
	}
}
