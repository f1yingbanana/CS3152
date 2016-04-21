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
import com.ramenstudio.sandglass.game.controller.MonsterController.AngleEnum;
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
	private static final float MOVE_SPEED = 6.5f;
		
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

	/**
	 * Create monster # id at the given position.
	 *
	 * @param id The unique monster id
	 * @param x The initial x-coordinate of the monster
	 * @param y The initial y-coordinate of the monster
	 */
    
   
	public Monster(Vector2 initialPos, MType mType, int id, int level,
			float spcf, Array<Vector2> vertices, String startAngle) {
		super();
		this.vertices = vertices;
		initAngle = AngleEnum.valueOf(startAngle);
		angle = initAngle;
		speed_coeff = spcf;
		initial = initialPos;
        if (mType == Monster.MType.OverMonster){
        	System.out.println("this is over monster");
            setTexture(new Texture(Gdx.app.getFiles().internal("overmonster.png")));
            fixtureDefs = new FixtureDef[3];
            setSize(new Vector2(0.8f, 1.2f));
            getBodyDef().position.set(initialPos);
            getBodyDef().type = BodyDef.BodyType.StaticBody;
            
            FixtureDef fixtureDef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.4f, 0.35f);
            fixtureDef.density = 100.0f;
            fixtureDef.shape = shape;
            fixtureDefs[0] = fixtureDef;
            fixtureDef.friction = 0.0f;
            fixtureDef.isSensor = true;
            
            
            CircleShape c = new CircleShape();
            c.setRadius(0.3f);
            c.setPosition(new Vector2(0, -0.35f));
            FixtureDef fixtureDef2 = new FixtureDef();
            fixtureDef2.shape = c;
            fixtureDef2.isSensor = true;
            fixtureDefs[1] = fixtureDef2;
            

            CircleShape c2 = new CircleShape();
            c2.setRadius(0.3f);
            c2.setPosition(new Vector2(0, 0.35f));
            FixtureDef fixtureDef3 = new FixtureDef();
            fixtureDef3.shape = c2;
            fixtureDef3.isSensor = true;
            fixtureDefs[2] = fixtureDef3;
        }
        else{
                setTexture(new Texture(Gdx.app.getFiles().internal("undermonster1.png")));
                fixtureDefs = new FixtureDef[1];
                setSize(new Vector2(0.8f, 0.8f));
                getBodyDef().position.set(initialPos);
                getBodyDef().type = BodyDef.BodyType.DynamicBody;
                getBodyDef().gravityScale=0;
                
                FixtureDef underFixtureDef = new FixtureDef();
                PolygonShape underShape = new PolygonShape();
                underShape.setAsBox(0.4f, 0.4f);
                underFixtureDef.density = 100.0f;
                underFixtureDef.shape = underShape;
                fixtureDefs[0] = underFixtureDef;
                underFixtureDef.friction = 10;
        }
		this.mType = mType;
		isAlive = true;
		System.out.println("monster is created");
		
		System.out.println("Monster id: " + id );
		for (int i = 0 ; i < vertices.size ; i ++ ) {
			System.out.println(vertices.get(i).toString());
		}
		isLoop = (vertices.get(0).epsilonEquals(vertices.get(vertices.size-1),0.01f));
		
		
		parametrizeVertices();
		
		System.out.println(isLoop);
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
			Vector2 currentVertex = vertices.get(i%(vertices.size - 1));
			Vector2 nextVertex = vertices.get((i + 1)%(vertices.size - 1));
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
					return AngleEnum.SOUTH;
				}
				if (nextMove == Move.RIGHT) {
					return AngleEnum.NORTH;
				}
			}
			if (currentMove == Move.DOWN) {
				if (nextMove == Move.RIGHT) {
					return AngleEnum.NORTH;
				}
				if (nextMove == Move.LEFT) {
					return AngleEnum.SOUTH;
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
	    return this.getBody().getPosition().epsilonEquals(goal, 0.0f);
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
					return;
				}
			}
		}
	}
	

	@Override
	public void draw(GameCanvas canvas){
		canvas.draw(getTextureRegion(), getBody().getPosition().add(getSize().cpy().scl(-0.5f)), 
		        getSize(), new Vector2(getSize()).scl(.5f), (float)(getRotation() * 180/Math.PI));
	}
}
