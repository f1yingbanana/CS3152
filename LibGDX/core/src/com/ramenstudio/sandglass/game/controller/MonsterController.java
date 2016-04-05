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
import com.ramenstudio.sandglass.game.model.Player;
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

	// Constants for chase algorithms
	/** How close a target must be for us to chase it */
	private static final int CHASE_DIST = 99999;
	/** How close a target must be for us to attack it */
	private static final int ATTACK_DIST = 4;

	// Instance Attributes
	/** The monster being controlled by this AIController */
	public Monster monster;
//	private Monster monster;
	/** The game board; used for pathfinding */
	private GameModel gamemodel;
	/** The monster's current state in the FSM */
	private FSMState state;
	/** The monster's next action  */
	private int move; // A ControlCode
	
	private int umove = -1;
	/** The number of ticks since we started this controller */
	private long ticks;
	/** is player in the same world*/
	private boolean setGoal;
	
	private PhysicsDelegate delegate;
	
	/**
	 * Creates an AIController for the monster with the given id.
	 *
	 * @param id The unique monster identifier
	 * @param argMonster is the monster this controller represents
	 * @param gmodel is the board state used for tracking
	 * @param player is the target of this monster
	 */
	public MonsterController(int id, Vector2 initialPos, MType mType, int level) {
		this.monster = new Monster(id, initialPos, mType, level);
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
                if (ticks%100< 50){
                    move = 1;
                }
                else {
                    move = 0;
                }
            }
            else{
                if (ticks%100 <25){
                    move = 3;
                }
                else if (ticks%100 <50){
                    move = 1;
                }
                else if (ticks%100 < 75){
                    move = 0;
                }
                else{
                    move = 2;
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