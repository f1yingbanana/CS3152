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
import com.ramenstudio.sandglass.game.model.GameModel;
import com.ramenstudio.sandglass.game.model.Monster;
import com.ramenstudio.sandglass.game.model.Monster.MType;
import com.ramenstudio.sandglass.game.view.GameCanvas;

/** 
 * InputController corresponding to AI control.
 * 
 */
public class MonsterController extends AbstractController {
	/**
	 * Enumeration to encode the finite state machine.
	 */
	private static enum FSMState {
		/** The monster just spawned */
		SPAWN,
		/** The monster is patrolling around without a target */
		WANDER,
		/** The monster has a target, but must get closer */
		CHASE,
		/** The monster has a target and is attacking it */
		ATTACK;
	}

	private enum AngleEnum {
        NORTH,
        EAST,
        SOUTH,
        WEST;

        /**
         * Returns the new compass direction of the
         * provided direction but rotated 180 degrees/flipped.
         * 
         * @param thisEnum is the direction to rotate 180 degrees/flip.
         * @return the angle rotated 180 degrees/flipped.
         */
        private static AngleEnum flipEnum(AngleEnum thisEnum) {
            if (thisEnum == NORTH) {
                return SOUTH;
            }
            else if (thisEnum == SOUTH) {
                return NORTH;
            }
            else if (thisEnum == WEST) {
                return EAST;
            }
            else {
                return WEST;
            }
        }

        /**
         * Returns the new compass direction of the
         * provided direction but rotated 90 degrees counterclockwise.
         * 
         * @param thisEnum is the direction to rotate 90 degrees counterclockwise.
         * @return the angle rotated 90 degrees counterclockwise.
         */
        private static AngleEnum flipCounterClockWise(AngleEnum thisEnum) {
            if (thisEnum == NORTH) {
                return WEST;
            }
            else if (thisEnum == EAST) {
                return NORTH;
            }
            else if (thisEnum == SOUTH) {
                return EAST;
            }
            else {
                return SOUTH;
            }
        }

        /**
         * Returns the new compass direction of the 
         * provided direction but rotated 90 degrees clockwise.
         * 
         * @param thisEnum is the direction to rotate 90 degrees clockwise.
         * @return the angle rotated 90 degrees clockwise.
         */
        private static AngleEnum flipClockWise(AngleEnum thisEnum) {
            if (thisEnum == NORTH) {
                return EAST;
            }
            else if (thisEnum == EAST) {
                return SOUTH;
            }
            else if (thisEnum == SOUTH) {
                return WEST;
            }
            else {
                return NORTH;
            }
        }

        /**
         * Converts the compass direction to an actual angle in radians.
         * 
         * @param thisEnum to convert to an angle
         * @return the angle in radians
         */
        private static float convertToAngle(AngleEnum thisEnum) {
            if (thisEnum == NORTH) {
                return 0;
            }
            else if (thisEnum == WEST) {
                return (float) (Math.PI/2);
            }
            else if (thisEnum == SOUTH) {
                return (float) (Math.PI);
            }
            else {
                return (float) (3*Math.PI/2);
            }
        }

        /**
         * Whether the given compass direction is vertical.
         * 
         * @param   thisEnum to evaluate
         * @return  true if thisEnum points North or South, false otherwise 
         */
        private static boolean isVertical (AngleEnum thisEnum) {
            if (thisEnum == NORTH || thisEnum == SOUTH) {
                return true;
            } else {
                return false;
            }
        }
    }
	// Constants for chase algorithms
	/** How close a target must be for us to chase it */
	private static final int CHASE_DIST = 99999;
	/** How close a target must be for us to attack it */
	private static final int ATTACK_DIST = 4;

	// Instance Attributes
	/** The monster being controlled by this AIController */
	public Monster monster;
	/** The monster's current state in the FSM */
	private FSMState state;
	/** The monster's next action  */
	private int move; // A ControlCode
	/** The number of ticks since we started this controller */
	private long ticks;
	/** is player in the same world*/
	private boolean setGoal;
	/** how long does it take to change direction?*/
	private int period;
	
	private PhysicsDelegate delegate;
	
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
		period = monster.span;
		move = -1;
	}
	
	
	/**
	 * Returns the action selected by this MonsterController
	 * 
	 * 
	 * @return the action selected by this MonsterController
	 */
	public int getAction() {
		// Increment the number of ticks.
		ticks++;

		// Do not need to rework ourselves every frame. Just every 10 ticks.
		switch (monster.getType()){
        case OVER:
//            if (ticks<100){
//                return -1;
//            }
//            if (ticks%100 == 0){
//                move = 1 - move;
//                umove = 2 + move;
//            }
//            return umove;
            break;
        case UNDER:
            if (monster.getLevel() ==1){
                if (ticks%period< period/2){
                    move = 1;
                    monster.setRotation(AngleEnum.convertToAngle(AngleEnum.SOUTH));
                }
                else {
                    move = 0;
                    monster.setRotation(AngleEnum.convertToAngle(AngleEnum.NORTH));
                }
            }
            else{
                if (ticks%period < period/4){
                    move = 3;
                    monster.setRotation(AngleEnum.convertToAngle(AngleEnum.EAST));
                }
                else if (ticks%period <period/2){
                    move = 1;
                    monster.setRotation(AngleEnum.convertToAngle(AngleEnum.SOUTH));
                }
                else if (ticks%period < 3*period/4){
                    move = 0;
                    monster.setRotation(AngleEnum.convertToAngle(AngleEnum.NORTH));
                }
                else{
                    move = 2;
                    monster.setRotation(AngleEnum.convertToAngle(AngleEnum.WEST));
                }
            }
            
        default:
            break;
		}
		return move;
		
		
		
//		if ((monster.getId() + ticks) % 10 == 0) {
//			// Process the FSM
//			changeStateIfApplicable();
//			// Pathfinding
//			markGoalTiles();
//			move = getMoveAlongPathToGoalTile();
//		}
	}
	

	/** The following three are path-finding methods*/
	
	private int getMoveAlongPathToGoalTile() {
		// TODO Auto-generated method stub
		return 0;
	}




	private void markGoalTiles() {
		// TODO Auto-generated method stub
		setGoal = false;
		
		if (monster.getType() == Monster.MType.OVER){
			switch (state){
			case ATTACK:
				break;
			case CHASE:
				break;
			case SPAWN:
				break;
			case WANDER:
				break;
			default:
				break;
		
			}
		}
//		else {
//		    switch (monster.getLevel()){
//		    case 1:
//		        monster.setGoal();
//		        break;
//		    case 2:
//		        break;
//		    }
//		    
//		}
		
	}




	private void changeStateIfApplicable() {
		// TODO Auto-generated method stub
		switch (state){
		case ATTACK:
			break;
		case CHASE:
			break;
		case SPAWN:
			break;
		case WANDER:
			break;
		default:
			break;
		}
	}



	@Override
	public void update(float dt) {
		int action = getAction();
		monster.update(action);
	}

	@Override
	public void draw(GameCanvas canvas) {
		monster.draw(canvas);
	}

	@Override
	public void objectSetup(PhysicsDelegate handler) {
		delegate = handler;
		activatePhysics(handler, monster);
		monster.getBody().setFixedRotation(true);
	}
}