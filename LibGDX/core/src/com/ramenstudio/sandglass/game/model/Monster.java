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
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.ramenstudio.sandglass.game.controller.AngleEnum;
import com.ramenstudio.sandglass.game.controller.PlayerController.State;
import com.ramenstudio.sandglass.game.view.FilmStrip;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.Drawable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * A model class representing a monster.
 */
public class Monster extends GameObject implements Drawable{

	public enum Move {
		LEFT,
		RIGHT,
		UP,
		DOWN,
		NONE;
	}

	public static enum MType {
		/* For over world */
		OverMonster,
		/*For under world*/
		UnderMonster
	}

	// CONSTANTS FOR monster HANDLING
	/** How far forward this monster can move in a single turn */
	private static final float MOVE_SPEED = 1f;

	// Instance Attributes
	/** A unique identifier; used to decouple classes. */
	private int id;
	/** Monster type*/
	public MType mType;
	/** Initial Position*/
	public Vector2 initial;
	/** Goal */
	private Vector2 goal;
	/** Boolean to track if we are dead yet */
	private boolean isAlive;
	/** Speed coefficient*/
	public float speed_coeff;
	/** is the path loop?*/
	public boolean isLoop;
	/** Starting angle orientation */
	public AngleEnum initAngle;
	/** heading*/
	public AngleEnum angle;
	/** the patrol path of the monster*/
	public Array<Vector2> vertices;
	/** the array of orientations the monster should take on the path */
	public Array<AngleEnum> orientationsOnPath;
	/** the time it takes to get from vertex i to i+1 */
	public Array<Float> timeBetweenVertices;
	/** the total dt time that has passed for this monster. */
	public float totalTime = 0;
	/** the total time of one path */
	public float cycleTime = 0;
	/** the target, which is the player*/
	public Player target;
	/** the level of this monster */
	public MonsterLevel monsterLevel;

	public enum MonsterLevel {
		DEDUCT_FLIPS, KILL, MAKE_FLIP;
	}

	// Variables for animation

	/** The sprite sheet for this monster. */
	private FilmStrip monsterSpriteLight;
	private FilmStrip monsterSpriteDark;

	/** Number of rows in the monster image filmstrip */
	private static final int FILMSTRIP_ROWS_DEDUCT_FLIPS_LIGHT = 1;
	/** Number of columns in the monster image filmstrip */
	private static final int FILMSTRIP_COLS_DEDUCT_FLIPS_LIGHT = 4;
	/** Number of elements in the monster image filmstrip */
	private static final int FILMSTRIP_SIZE_DEDUCT_FLIPS_LIGHT = 4;

	/** Number of rows in the monster image filmstrip */
	private static final int FILMSTRIP_ROWS_DEDUCT_FLIPS_DARK = 1;
	/** Number of columns in the monster image filmstrip */
	private static final int FILMSTRIP_COLS_DEDUCT_FLIPS_DARK = 8;
	/** Number of elements in the monster image filmstrip */
	private static final int FILMSTRIP_SIZE_DEDUCT_FLIPS_DARK = 8;

	/** Number of rows in the monster image filmstrip */
	private static final int FILMSTRIP_ROWS_KILL_LIGHT = 1;
	/** Number of columns in the monster image filmstrip */
	private static final int FILMSTRIP_COLS_KILL_LIGHT = 9;
	/** Number of elements in the monster image filmstrip */
	private static final int FILMSTRIP_SIZE_KILL_LIGHT = 9;

	/** Number of rows in the monster image filmstrip */
	private static final int FILMSTRIP_ROWS_KILL_DARK = 1;
	/** Number of columns in the monster image filmstrip */
	private static final int FILMSTRIP_COLS_KILL_DARK = 10;
	/** Number of elements in the monster image filmstrip */
	private static final int FILMSTRIP_SIZE_KILL_DARK = 10;

	/** Number of rows in the monster image filmstrip */
	private static final int FILMSTRIP_ROWS_MAKE_FLIP_LIGHT = 1;
	/** Number of columns in the monster image filmstrip */
	private static final int FILMSTRIP_COLS_MAKE_FLIP_LIGHT = 1;
	/** Number of elements in the monster image filmstrip */
	private static final int FILMSTRIP_SIZE_MAKE_FLIP_LIGHT = 1;

	/** Number of rows in the monster image filmstrip */
	private static final int FILMSTRIP_ROWS_MAKE_FLIP_DARK = 1;
	/** Number of columns in the monster image filmstrip */
	private static final int FILMSTRIP_COLS_MAKE_FLIP_DARK = 1;
	/** Number of elements in the monster image filmstrip */
	private static final int FILMSTRIP_SIZE_MAKE_FLIP_DARK = 1;

	/** The enum for animation states. */
	public enum State {
		NEUTRAL, WALK 
	}

	/** The current animation state. */
	private State state = State.WALK;
	/** The next animation state. */
	private State next = State.WALK;

	private boolean isUnder = false;
	private int frame = 0;
	private int count = 0;
	private static final int COOLDOWN = 7;

	public boolean isFrozen = false;
	/**
	 * Create monster # id at the given position.
	 *
	 * @param id The unique monster id
	 * @param x The initial x-coordinate of the monster
	 * @param y The initial y-coordinate of the monster
	 */
	public Monster(Vector2 initialPos, int id, MonsterLevel level,
			float spcf, Array<Vector2> vertices, String startAngle) {
		super();
		this.vertices = vertices;
		initAngle = AngleEnum.valueOf(startAngle);
		angle = initAngle;
		speed_coeff = spcf;
		initial = initialPos;
		monsterLevel = level;
		if (level == MonsterLevel.DEDUCT_FLIPS) {
			//			setTexture(new Texture(Gdx.app.getFiles().internal("overmonster.png")));

			Texture monsterTextureLight = new Texture(Gdx.files.internal("DEDUCTFLIP.png"));
			Texture monsterTextureDark = new Texture(Gdx.files.internal("deduct_spritesheet_dark.png"));

			monsterSpriteLight = new FilmStrip(monsterTextureLight,
					FILMSTRIP_ROWS_DEDUCT_FLIPS_LIGHT,
					FILMSTRIP_COLS_DEDUCT_FLIPS_LIGHT,
					FILMSTRIP_SIZE_DEDUCT_FLIPS_LIGHT);
			monsterSpriteDark = new FilmStrip(monsterTextureDark,
					FILMSTRIP_ROWS_DEDUCT_FLIPS_DARK,
					FILMSTRIP_COLS_DEDUCT_FLIPS_DARK,
					FILMSTRIP_SIZE_DEDUCT_FLIPS_DARK);

			fixtureDefs = new FixtureDef[1];
			setSize(new Vector2(0.8f, 0.8f));
			getBodyDef().position.set(initialPos);
			getBodyDef().type = BodyDef.BodyType.StaticBody;
			getBodyDef().gravityScale = 0;

			FixtureDef fixtureDef = new FixtureDef();
			CircleShape shape = new CircleShape();
			shape.setRadius(0.4f);
//			shape.setPosition(new Vector2(0.0f,0.2f));
			fixtureDef.density = 100.0f;
			fixtureDef.shape = shape;
			fixtureDef.isSensor = true;
			fixtureDefs[0] = fixtureDef;
			fixtureDef.friction = 0.0f;
			
//			FixtureDef fixtureDef1 = new FixtureDef();
//			CircleShape shape1 = new CircleShape();
//			shape1.setRadius(0.2f);
//			shape1.setPosition(new Vector2(0.0f,-0.2f));
//			fixtureDef1.density = 100.0f;
//			fixtureDef1.shape = shape1;
//			fixtureDef1.isSensor =true;
//			fixtureDefs[1] = fixtureDef1;
//			fixtureDef1.friction = 0.0f;
		} else if (level == MonsterLevel.KILL){

			Texture monsterTextureLight = new Texture(Gdx.files.internal("KILL.png"));
			Texture monsterTextureDark = new Texture(Gdx.files.internal("DARK_KILL.png"));

			monsterSpriteLight = new FilmStrip(monsterTextureLight,
					FILMSTRIP_ROWS_KILL_LIGHT,
					FILMSTRIP_COLS_KILL_LIGHT,
					FILMSTRIP_SIZE_KILL_LIGHT);
			monsterSpriteDark = new FilmStrip(monsterTextureDark,
					FILMSTRIP_ROWS_KILL_DARK,
					FILMSTRIP_COLS_KILL_DARK,
					FILMSTRIP_SIZE_KILL_DARK);

			fixtureDefs = new FixtureDef[1];
			setSize(new Vector2(0.8f, 0.8f));
			getBodyDef().position.set(initialPos);
			getBodyDef().type = BodyDef.BodyType.StaticBody;
			getBodyDef().gravityScale = 0;

			FixtureDef underFixtureDef = new FixtureDef();
			CircleShape underShape = new CircleShape();
			underShape.setRadius(0.4f);
//			underShape.setAsBox(0.4f, 0.4f);
			underFixtureDef.density = 100.0f;
			underFixtureDef.shape = underShape;
			underFixtureDef.isSensor = true;
			fixtureDefs[0] = underFixtureDef;
			underFixtureDef.friction = 10;
		}
		
		else if (level == MonsterLevel.MAKE_FLIP){
			Texture monsterTextureLight = new Texture(Gdx.files.internal("Makeflip.png"));
			Texture monsterTextureDark = new Texture(Gdx.files.internal("Makeflip2.png"));

			monsterSpriteLight = new FilmStrip(monsterTextureLight,1,1,1);
			monsterSpriteDark = new FilmStrip(monsterTextureDark,1,1,1);

			fixtureDefs = new FixtureDef[1];
			setSize(new Vector2(0.8f, 0.8f));
			getBodyDef().position.set(initialPos);
			getBodyDef().type = BodyDef.BodyType.StaticBody;
			getBodyDef().gravityScale = 0;

			
			CircleShape c = new CircleShape();
			c.setRadius(0.3f);
			c.setPosition(new Vector2(0f,-0.1f));
			PolygonShape underShape = new PolygonShape();
			int NUM_EDGES = 4;
			
			Vector2[] vertex = new Vector2[NUM_EDGES];
			float xRadius = size.x / 2.0f;
			float yRadius = size.y / 8.0f * 3f;
			float xOff = 0f;
			float yOff = -0.1f;
			
			vertex[0] = new Vector2(xOff-xRadius+0.1f, yOff);
			vertex[1] = new Vector2(xOff+xRadius-0.1f, yOff);
			vertex[2] = new Vector2(xOff+xRadius, yOff-yRadius);
			vertex[3] = new Vector2(xOff-xRadius, yOff-yRadius);
			
			underShape.setAsBox(xRadius,yRadius/3f);
//			underShape.setAsBox(0.4f, 0.4f);
			FixtureDef underFixtureDef = new FixtureDef();
			FixtureDef underFixtureDef2 = new FixtureDef();
			underFixtureDef.density = 100.0f;
			underFixtureDef2.density = 100.0f;
			underFixtureDef.isSensor = true;
			underFixtureDef.shape = c;
			underFixtureDef2.shape = underShape;
			underFixtureDef.friction = 10;
			underFixtureDef2.friction = 10;
			fixtureDefs[0] = underFixtureDef;
			//fixtureDefs[1] = underFixtureDef2;
			
		}
		isAlive = true;
		isLoop = (vertices.get(0).epsilonEquals(vertices.get(vertices.size-1),0.01f));


		parametrizeVertices();
	}

	/** 
	 * Private helper method to help parametrize vertices and orientations
	 * during the path of the monster.
	 */
	private void parametrizeVertices() {
		orientationsOnPath = new Array<AngleEnum>();
		timeBetweenVertices = new Array<Float>();

		AngleEnum currentOrientation = initAngle;
		orientationsOnPath.add(initAngle);

		Move previousMove = null;

		if (vertices.size >= 2) {
			Vector2 currentVertex = vertices.get(0);
			Vector2 nextVertex = vertices.get(1);
			previousMove = moveBetweenTwoVertices(currentVertex, nextVertex);
		}
		for (int i = 1; i < vertices.size - 1; i++) {
			Vector2 currentVertex = vertices.get(i%(vertices.size));
			Vector2 nextVertex = vertices.get((i + 1)%(vertices.size));
			Move currentMove = moveBetweenTwoVertices(currentVertex, nextVertex);
			currentOrientation = orientationAfterMove(currentOrientation, 
					previousMove, currentMove);
			orientationsOnPath.add(currentOrientation);
			previousMove = currentMove;
		}
		for (int i = 0; i < vertices.size - 1; i++) {
			Vector2 currentVertex = vertices.get(i);
			Vector2 nextVertex = vertices.get(i+1);
			Vector2 displacement = nextVertex.cpy().sub(currentVertex);
			float lengthOfPath = displacement.len();
			timeBetweenVertices.add(lengthOfPath/(speed_coeff * MOVE_SPEED));
			cycleTime += lengthOfPath/(speed_coeff * MOVE_SPEED);
		}
		if (!isLoop) {
			cycleTime = cycleTime * 2;
		}
	}

	/**
	 * Calculates the move that the monster must take between the
	 * two vertices.
	 * 
	 * @return the Move between the two vertices
	 */
	private Move moveBetweenTwoVertices(Vector2 currentVertex, Vector2 nextVertex) {
		if (Math.abs(nextVertex.x - currentVertex.x) < .5f) {
			if (nextVertex.y - currentVertex.y > .5f) {
				return Move.UP;
			}
			else {
				return Move.DOWN;
			}
		}
		else {
			if (nextVertex.x - currentVertex.x > .5f) {
				return Move.RIGHT;
			}
			else {
				return Move.LEFT;
			}
		}
	}

	private AngleEnum orientationAfterMove(AngleEnum currentAngle, Move currentMove, Move nextMove) {
		if (currentAngle == AngleEnum.NORTH) {
			if (currentMove == Move.LEFT) {
				if (nextMove == Move.UP) {
					return AngleEnum.EAST;
				}
				if (nextMove == Move.DOWN) {
					return AngleEnum.WEST;
				}
			}
			if (currentMove == Move.RIGHT) {
				if (nextMove == Move.UP) {
					return AngleEnum.WEST;
				}
				if (nextMove == Move.DOWN) {
					return AngleEnum.EAST;
				}
			}
		}
		else if (currentAngle == AngleEnum.EAST) {
			if (currentMove == Move.UP) {
				if (nextMove == Move.LEFT) {
					return AngleEnum.NORTH;
				}
				if (nextMove == Move.RIGHT) {
					return AngleEnum.SOUTH;
				}
			}
			if (currentMove == Move.DOWN) {
				if (nextMove == Move.LEFT) {
					return AngleEnum.SOUTH;
				}
				if (nextMove == Move.RIGHT) {
					return AngleEnum.NORTH;
				}
			}
		}
		else if (currentAngle == AngleEnum.SOUTH) {
			if (currentMove == Move.LEFT) {
				if (nextMove == Move.UP) {
					return AngleEnum.WEST;
				}
				if (nextMove == Move.DOWN) {
					return AngleEnum.EAST;
				}
			}
			if (currentMove == Move.RIGHT) {
				if (nextMove == Move.UP) {
					return AngleEnum.EAST;
				}
				if (nextMove == Move.DOWN) {
					return AngleEnum.WEST;
				}
			}
		}
		else {
			if (currentMove == Move.UP) {
				if (nextMove == Move.LEFT) {
					return AngleEnum.NORTH;
				}
				if (nextMove == Move.RIGHT) {
					return AngleEnum.SOUTH;
				}
			}
			if (currentMove == Move.DOWN) {
				if (nextMove == Move.RIGHT) {
					return AngleEnum.SOUTH;
				}
				if (nextMove == Move.LEFT) {
					return AngleEnum.NORTH;
				}
			}
		}

		assert(false);
		return null;
	}

	/** 
	 * Returns the unique monster id number 
	 * 
	 * @return the unique monster id number 
	 */
	public int getId() {
		return id;
	}


	/* Returns the monster ypte
	 *
	 * @return the monster type
	 */
	public MType getType() {
		return mType;
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
		return this.getBody().getPosition().epsilonEquals(goal, 1f);
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
	 * Updates this monster position according to the control code.
	 *
	 * @param controlCode The movement controlCode (from InputController).
	 */
	public void update(float dt) {
		// Animation
		handleAnimation();
		
		if (isFrozen){
			setRotation(target.getRotation());
			return;
		}
		
		totalTime += dt;
		
		
		if (isLoop) {
			float periodicTime = totalTime % cycleTime;
			for (int i = 0; i < timeBetweenVertices.size; i++) {
				periodicTime -= timeBetweenVertices.get(i);
				if (periodicTime < 0.05f) {
					periodicTime += timeBetweenVertices.get(i);
					Vector2 distanceVectorToNext = vertices.get(i+1).cpy().sub(vertices.get(i));
					periodicTime = periodicTime/timeBetweenVertices.get(i);
					distanceVectorToNext.scl(periodicTime);
					Vector2 finalPos = distanceVectorToNext.add(vertices.get(i));
					setPosition(finalPos);
					if (monsterLevel==MonsterLevel.MAKE_FLIP){
						angle = orientationsOnPath.get(i);
						setRotation(AngleEnum.convertToAngle(angle));
					}
					else{
						setRotation(target.getRotation());
					}
					return;
				}
			}
		}
		// If it's not a loop, then the monster should go back along its path.
		else {
			float periodicTime = totalTime % cycleTime;
			for (int i = 0; i < timeBetweenVertices.size; i++) {
				periodicTime -= timeBetweenVertices.get(i);
				if (periodicTime < 0.05f) {
					periodicTime += timeBetweenVertices.get(i);
					Vector2 distanceVectorToNext = vertices.get(i+1).cpy().sub(vertices.get(i));
					periodicTime = periodicTime/timeBetweenVertices.get(i);
					distanceVectorToNext.scl(periodicTime);
					Vector2 finalPos = distanceVectorToNext.add(vertices.get(i));
					setPosition(finalPos);
					if (monsterLevel==MonsterLevel.MAKE_FLIP){
						angle = orientationsOnPath.get(i);
						setRotation(AngleEnum.convertToAngle(angle));
					}
					else{
						setRotation(target.getRotation());
					}
					return;
				}
			}
			for (int i = timeBetweenVertices.size - 1; i >= 0; i--) {
				periodicTime -= timeBetweenVertices.get(i);
				if (periodicTime < 0.05f) {
					periodicTime += timeBetweenVertices.get(i);
					Vector2 distanceVectorToPrevious = vertices.get(i).cpy().sub(vertices.get(i+1));
					periodicTime = periodicTime/timeBetweenVertices.get(i);
					distanceVectorToPrevious.scl(periodicTime);
					Vector2 finalPos = distanceVectorToPrevious.add(vertices.get(i+1));
					setPosition(finalPos);
					if (monsterLevel==MonsterLevel.MAKE_FLIP){
						angle = orientationsOnPath.get(i);
						setRotation(AngleEnum.convertToAngle(angle));
					}
					else{
						setRotation(target.getRotation());
					}
					return;
				}
			}
		}
	}


	private void handleAnimation() {
		if (next == State.WALK) {
			int maxFrame = 0;
			switch (monsterLevel) {
			case DEDUCT_FLIPS:
				if (isUnder) {
					maxFrame = FILMSTRIP_SIZE_DEDUCT_FLIPS_DARK;
				} else {
					maxFrame = FILMSTRIP_SIZE_DEDUCT_FLIPS_LIGHT;
				}
				break;
			case KILL:
				if (isUnder) {
					maxFrame = FILMSTRIP_SIZE_KILL_DARK;
				} else {
					maxFrame = FILMSTRIP_SIZE_KILL_LIGHT;
				}
				break;
			case MAKE_FLIP:
				if (isUnder) {
					maxFrame = FILMSTRIP_SIZE_MAKE_FLIP_DARK;
				} else {
					maxFrame = FILMSTRIP_SIZE_MAKE_FLIP_LIGHT;
				}
				break;
			}
			count++;
			if (count > COOLDOWN) {
				count = 0;
				frame ++;
			} 
			frame = frame%maxFrame;
		}
		
	}
	

	@Override
	public void draw(GameCanvas canvas){
		//		canvas.draw(getTextureRegion(), getBody().getPosition().add(getSize().cpy().scl(-0.5f)), 
		//		        getSize(), new Vector2(getSize()).scl(.5f), (float)(getRotation() * 180/Math.PI));
		FilmStrip monsterSprite = (isUnder)? monsterSpriteDark : monsterSpriteLight;
		monsterSprite.setFrame(frame);
		canvas.draw(monsterSprite, getBody().getPosition().add(getSize().cpy().scl(-0.5f)), 
				getSize(), new Vector2(getSize()).scl(.5f), (float)(getRotation() * 180/Math.PI));
	}

	public void setUnder(boolean under) {
		isUnder  = under;
	}
}
