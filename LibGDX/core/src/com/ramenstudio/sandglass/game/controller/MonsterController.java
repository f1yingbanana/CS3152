/**
 * MonsterController.java
 *
 * This class is an unfinished draft of MonsterController
 *
 * Author: Saerom Choi
 * Based on AIController from Lab 3 by Walker M. WHite
 */
package com.ramenstudio.sandglass.game.controller;

import com.ramenstudio.sandglass.game.model.GameModel;
import com.ramenstudio.sandglass.game.model.Monster;
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
	private Monster monster;
//	private Monster monster;
	/** The game board; used for pathfinding */
	private GameModel gamemodel;
	/** The monster's current state in the FSM */
	private FSMState state;
	/** The target monster (to chase or attack). */
	private Player target; 
	/** The monster's next action  */
	private int move; // A ControlCode
	/** The number of ticks since we started this controller */
	private long ticks;
	/** is player in the same world*/
	private boolean setGoal;
	/** Unique monster identifier */
	private int id;
	private PhysicsDelegate delegate;
	
	/**
	 * Creates an AIController for the monster with the given id.
	 *
	 * @param id The unique monster identifier
	 * @param argMonster is the monster this controller represents
	 * @param gmodel is the board state used for tracking
	 * @param player is the target of this monster
	 */
	public MonsterController(int id, Monster monster, GameModel gmodel, Player player) {
		this.id = id;
		this.monster = monster;
		gamemodel = gmodel;
		target = player;
		
		switch (monster.getType()) {
		case OVER:
			break;
		case UNDER:
			if (monster.getLevel()==1){
				
			}
			else if (monster.getLevel()==2){
				
			}
			break;
		default:
			break;
		}
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
		if ((monster.getId() + ticks) % 10 == 0) {
			// Process the FSM
			changeStateIfApplicable();
			// Pathfinding
			markGoalTiles();
			move = getMoveAlongPathToGoalTile();
		}

		int action = move;
		return action;
	}


	/** The following three are path-finding methods*/
	
	private int getMoveAlongPathToGoalTile() {
		// TODO Auto-generated method stub
		return 0;
	}




	private void markGoalTiles() {
		// TODO Auto-generated method stub
		
	}




	private void changeStateIfApplicable() {
		// TODO Auto-generated method stub
		
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