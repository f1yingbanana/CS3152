package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
		setSize(new Vector2(0.6f, 0.925f));
		getBodyDef().position.set(initialPos);
		getBodyDef().type = BodyDef.BodyType.DynamicBody;

		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.2f, 0.2625f);
		fixtureDef.density = 10.0f;
		fixtureDef.shape = shape;
		fixtureDefs[0] = fixtureDef;
		fixtureDef.friction = 0;

		CircleShape c = new CircleShape();
		c.setRadius(0.2f);
		c.setPosition(new Vector2(0, -0.2625f));
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = c;
		fixtureDefs[1] = fixtureDef2;

		CircleShape c2 = new CircleShape();
		c2.setRadius(0.2f);
		c2.setPosition(new Vector2(0, 0.2625f));
		FixtureDef fixtureDef3 = new FixtureDef();
		fixtureDef3.shape = c2;
		fixtureDefs[2] = fixtureDef3;
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

	@Override
	public void draw(GameCanvas canvas) {
		Vector2 size = getSize();
//		if (direction == 1) {
//			size.x *= -1;
//		}
		canvas.draw(playerSprite, getPosition().add(getSize().cpy().scl(-0.5f)), size,
				new Vector2(getSize()).scl(.5f), (float)(getRotation() * 180/Math.PI));
	}

	@Override
	public void setBody(Body body) {
		super.setBody(body);

	}
}
