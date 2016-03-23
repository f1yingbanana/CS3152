/**
 * MonsterController.java
 *
 * This class is an unfinished draft of MonsterController
 *
 * Author: Saerom Choi
 * Based on AIController from Lab 3 by Walker M. WHite
 */
package com.ramenstudio.sandglass.game.controller;

import java.util.*;

import com.ramenstudio.sandglass.game.model.GameModel;
import com.ramenstudio.sandglass.game.model.Monster;
import com.ramenstudio.sandglass.game.model.Player;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.game.model.SandglassMonster;

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
	private SandglassMonster monster;
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
	
	/**
	 * Creates an AIController for the monster with the given id.
	 *
	 * @param id The unique monster identifier
	 * @param argMonster is the monster this controller represents
	 * @param gmodel is the board state used for tracking
	 * @param player is the target of this monster
	 */
	public MonsterController(int id, SandglassMonster argMonster, GameModel gmodel, Player player) {
		this.id = id;
		monster = argMonster;
		gamemodel = gmodel;
		target = player;
	}

//	/**
//	 * Returns the action selected by this InputController
//	 *
//	 * The returned int is a bit-vector of more than one possible input 
//	 * option. This is why we do not use an enumeration of Control Codes;
//	 * Java does not (nicely) provide bitwise operation support for enums. 
//	 *
//	 * This function tests the environment and uses the FSM to chose the next
//	 * action of the monster. This function SHOULD NOT need to be modified.  It
//	 * just contains code that drives the functions that you need to implement.
//	 *
//	 * @return the action selected by this InputController
//	 */
	
	
//	/**
//	 * Returns the action selected by this MonsterController
//	 * 
//	 * 
//	 * @return the action selected by this MonsterController
//	 */
//	public int getAction() {
//		// Increment the number of ticks.
//		ticks++;
//
//		// Do not need to rework ourselves every frame. Just every 10 ticks.
//		if ((monster.getId() + ticks) % 10 == 0) {
//			// Process the FSM
//			changeStateIfApplicable();
//			// Pathfinding
//			markGoalTiles();
//			move = getMoveAlongPathToGoalTile();
//		}
//
//		int action = move;
//		return action;
//	}

	@Override
	public void update(float dt) {
		monster.update(dt);
	}

	@Override
	public void draw(GameCanvas canvas) {
		monster.draw(canvas);
	}

	@Override
	public void objectSetup(PhysicsDelegate handler) {
		// TODO Auto-generated method stub
		
	}
}