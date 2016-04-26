/**
 * MonsterController.java
 *
 * This class is an unfinished draft of MonsterController
 *
 * Author: Saerom Choi
 * Based on AIController from Lab 3 by Walker M. WHite
 */
package com.ramenstudio.sandglass.game.controller;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.ramenstudio.sandglass.game.model.AbstractTile;
import com.ramenstudio.sandglass.game.model.Monster;
import com.ramenstudio.sandglass.game.model.Monster.Move;
import com.ramenstudio.sandglass.game.model.Player;
import com.ramenstudio.sandglass.game.model.TurnTile;
import com.ramenstudio.sandglass.game.view.GameCanvas;

/** 
 * InputController corresponding to AI control.
 * 
 */
public class MonsterController extends AbstractController {


//	public enum AngleEnum {
//        NORTH,
//        EAST,
//        SOUTH,
//        WEST;
//
//        /**
//         * Returns the new compass direction of the
//         * provided direction but rotated 180 degrees/flipped.
//         * 
//         * @param thisEnum is the direction to rotate 180 degrees/flip.
//         * @return the angle rotated 180 degrees/flipped.
//         */
//        public static AngleEnum flipEnum(AngleEnum thisEnum) {
//            if (thisEnum == NORTH) {
//                return SOUTH;
//            }
//            else if (thisEnum == SOUTH) {
//                return NORTH;
//            }
//            else if (thisEnum == WEST) {
//                return EAST;
//            }
//            else {
//                return WEST;
//            }
//        }
//
//        /**
//         * Returns the new compass direction of the
//         * provided direction but rotated 90 degrees counterclockwise.
//         * 
//         * @param thisEnum is the direction to rotate 90 degrees counterclockwise.
//         * @return the angle rotated 90 degrees counterclockwise.
//         */
//        public static AngleEnum flipCounterClockWise(AngleEnum thisEnum) {
//            if (thisEnum == NORTH) {
//                return WEST;
//            }
//            else if (thisEnum == EAST) {
//                return NORTH;
//            }
//            else if (thisEnum == SOUTH) {
//                return EAST;
//            }
//            else {
//                return SOUTH;
//            }
//        }
//
//        /**
//         * Returns the new compass direction of the 
//         * provided direction but rotated 90 degrees clockwise.
//         * 
//         * @param thisEnum is the direction to rotate 90 degrees clockwise.
//         * @return the angle rotated 90 degrees clockwise.
//         */
//        public static AngleEnum flipClockWise(AngleEnum thisEnum) {
//            if (thisEnum == NORTH) {
//                return EAST;
//            }
//            else if (thisEnum == EAST) {
//                return SOUTH;
//            }
//            else if (thisEnum == SOUTH) {
//                return WEST;
//            }
//            else {
//                return NORTH;
//            }
//        }
//
//        /**
//         * Converts the compass direction to an actual angle in radians.
//         * 
//         * @param thisEnum to convert to an angle
//         * @return the angle in radians
//         */
//        public static float convertToAngle(AngleEnum thisEnum) {
//            if (thisEnum == NORTH) {
//                return 0;
//            }
//            else if (thisEnum == WEST) {
//                return (float) (Math.PI/2);
//            }
//            else if (thisEnum == SOUTH) {
//                return (float) (Math.PI);
//            }
//            else {
//                return (float) (3*Math.PI/2);
//            }
//        }
//
//        /**
//         * Whether the given compass direction is vertical.
//         * 
//         * @param   thisEnum to evaluate
//         * @return  true if thisEnum points North or South, false otherwise 
//         */
//        public static boolean isVertical (AngleEnum thisEnum) {
//            if (thisEnum == NORTH || thisEnum == SOUTH) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//    }

	// Instance Attributes
	/** The monster being controlled by this AIController */
	public Monster monster;
	/** The number of ticks since we started this controller */
	public float ticks;
	/** is player in the same world*/	
	public Move action;
	
	public int period;
	
	public World delegate;
	
	/**
	 * Creates an AIController for the monster with the given id.
	 *
	 * @param id The unique monster identifier
	 * @param argMonster is the monster this controller represents
	 * @param gmodel is the board state used for tracking
	 * @param player is the target of this monster
	 */
	public MonsterController(Monster monster) {
		this.monster = monster;
		action = Move.NONE;
	}
	
	
	@Override
	public void update(float dt){
		monster.update(dt);
	}

	@Override
	public void draw(GameCanvas canvas) {
		monster.draw(canvas);
	}

	@Override
	public void objectSetup(World handler) {
	    delegate = handler;
		activatePhysics(handler, monster);
	}
}