package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
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

	/** The density of this player model. */
	private static final float PLAYER_DENSITY = 10.0f;
	/** The friction for this player model. */
	private static final float PLAYER_FRICTION = 2.0f;
	
	/** Texture for the player */
	private static final String PLAYER_TEXTURE = "character2.png";
	/** Number of rows in the ship image filmstrip */
	private static final int PLAYER_ROWS = 1;
	/** Number of columns in this ship image filmstrip */
	private static final int PLAYER_COLS = 8;
	/** Number of elements in this ship image filmstrip */
	private static final int PLAYER_SIZE = 8;

	/** Reference to ship's sprite for drawing */
	private FilmStrip playerSprite;

	// Ship Frame Sprite numbers
	/** The frame number for the tightest bank for a left turn */
	public static final int PLAYER_IMG_LEFT = 0;
	/** The frame number for a ship that is not turning */
	public static final int PLAYER_IMG_FLAT = 9;
	/** The frame number for the tightest bank for a right turn */
	public static final int PLAYER_IMG_RIGHT = 17;

	/**
	 * Creates the player at the given initial position.
	 * 
	 * @param initialPos is the position of the player at the time of creation.
	 */
	public Player(Vector2 initialPos) {
		super();
		
		Texture image = new Texture(Gdx.files.internal(PLAYER_TEXTURE));
//		playerSprite = new FilmStrip(image,PLAYER_ROWS,PLAYER_COLS,PLAYER_SIZE);
		
		setTexture(image);
		setSize(new Vector2(.8f, 1.5f));
		bodyDef.position.set(initialPos);
		bodyDef.type = BodyDef.BodyType.DynamicBody;

		fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.4f, 0.75f);
		fixtureDef.density = PLAYER_DENSITY;
		fixtureDef.shape = shape;
	}

	/**
	 * Returns the image filmstrip for this ship
	 * 
	 * This value should be loaded by the GameMode and set there. However, we
	 * have to be prepared for this to be null at all times
	 *
	 * @return the image texture for this ship
	 */
	public FilmStrip getFilmStrip() {
		return playerSprite;
	}

	/**
	 * Sets the image texture for this ship
	 * 
	 * This value should be loaded by the GameMode and set there. However, we
	 * have to be prepared for this to be null at all times
	 *
	 * param value the image texture for this ship
	 */
	public void setFilmStrip(FilmStrip value) {
		playerSprite = value;
		playerSprite.setFrame(PLAYER_IMG_FLAT);
	}

	@Override
	public void draw(GameCanvas canvas) {
		canvas.draw(getTextureRegion(), getPosition().add(getSize().cpy().scl(-0.5f)), getSize(),
				new Vector2(getSize()).scl(.5f), (float)(getRotation() * 180/Math.PI));
		
//		canvas.draw(playerSprite,Color.BLACK, 0, 0, getPosition().x, 
//				getPosition().y, (float) (getRotation()*180/Math.PI), 10000f, 100000f);

	}
}
