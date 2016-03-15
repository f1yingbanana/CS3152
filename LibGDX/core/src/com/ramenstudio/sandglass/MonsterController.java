/*
 * MonsterController.java
 *
 * This class is an unfinished draft of MonsterController
 *
 * Author: Saerom Choi
 * Based on AIController from Lab 3 by Walker M. WHite
 */
package edu.cornell.gdiac.ailab;

import java.util.*;

/** 
 * InputController corresponding to AI control.
 * 
 * REMEMBER: As an implementation of InputController you will have access to
 * the control code constants in that interface.  You will want to use them.
 */
public class MonsterController implements InputController {
	/**
	 * Enumeration to encode the finite state machine.
	 */
	private static enum FSMState {
		/** The ship just spawned */
		SPAWN,
		/** The ship is patrolling around without a target */
		WANDER,
		/** The ship has a target, but must get closer */
		CHASE,
		/** The ship has a target and is attacking it */
		ATTACK
	}
	protected enum MType {
		/* For over world */
		OVER,
		/* For under world*/
		UNDER
	}
	// Constants for chase algorithms
	/** How close a target must be for us to chase it */
	private static final int CHASE_DIST  = 9;
	/** How close a target must be for us to attack it */
	private static final int ATTACK_DIST = 4;

	// Instance Attributes
	/** The ship being controlled by this AIController */
	private Monster ship;
	/** The game board; used for pathfinding */
	private GameModel gamemodel;
	/** The monster's current state in the FSM */
	private FSMState state;
	/** The target ship (to chase or attack). */
	private Player target; 
	/** The ship's next action  */
	private int move; // A ControlCode
	/** The number of ticks since we started this controller */
	private long ticks;
	
	private boolean setGoal;
	
	/**
	 * Creates an AIController for the ship with the given id.
	 *
	 * @param id The unique ship identifier
	 * @param board The game board (for pathfinding)
	 * @param ships The list of ships (for targetting)
	 */
	public AIController(int id, MType mType, GmaeModel gmodel) {
	}

	/**
	 * Returns the action selected by this InputController
	 *
	 * The returned int is a bit-vector of more than one possible input 
	 * option. This is why we do not use an enumeration of Control Codes;
	 * Java does not (nicely) provide bitwise operation support for enums. 
	 *
	 * This function tests the environment and uses the FSM to chose the next
	 * action of the ship. This function SHOULD NOT need to be modified.  It
	 * just contains code that drives the functions that you need to implement.
	 *
	 * @return the action selected by this InputController
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

		// If we're attacking someone and we can shoot him now, then do so.
		if (state == FSMState.ATTACK && canShootTarget()) {
			action |= CONTROL_FIRE;
		}

		return action;
	}
	
	// FSM Code for Targeting (MODIFY ALL THE FOLLOWING METHODS)

	/**
	 * Change the state of the ship.
	 *
	 * A Finite State Machine (FSM) is just a collection of rules that,
	 * given a current state, and given certain observations about the
	 * environment, chooses a new state. For example, if we are currently
	 * in the ATTACK state, we may want to switch to the CHASE state if the
	 * target gets out of range.
	 */
	private void changeStateIfApplicable() {
		// Add initialization code as necessary
		//#region PUT YOUR CODE HERE
	
		//#endregion

		// Next state depends on current state.
		switch (state) {
		case SPAWN: 
			//#region PUT YOUR CODE HERE
			
			state = FSMState.WANDER;
			//#endregion
			break;

		case WANDER:
			//#region PUT YOUR CODE HERE
			selectTarget();
			if (target!=null){
				if (Math.abs(target.getX() - ship.getX()) + 
						Math.abs(target.getY()-ship.getY())<=board.boardToScreen(CHASE_DIST)){
					state = FSMState.CHASE;	
				}
				
				else {
					target = null;
				}
			}
			//#endregion			
			break;

		case CHASE: 
			//#region PUT YOUR CODE HERE 
			if (canShootTarget()){
				state = FSMState.ATTACK;
			}
			//#endregion			
			break;

		case ATTACK: 
			//#region PUT YOUR CODE HERE
			if (target.isActive() && !canShootTarget()){
				state = FSMState.CHASE;
			}
			else if (!target.isActive()){
				state = FSMState.WANDER;
			}
			//#endregion			
			break;

		default:
			// Unknown or unhandled state, should never get here
			assert (false);
			state = FSMState.WANDER; // If debugging is off
			break;
		}
	}

	/**
	 * Acquire a target to attack (and put it in field target).
	 *
	 * Insert your checking and target selection code here. Note that this
	 * code does not need to reassign <c>target</c> every single time it is
	 * called. Like all other methods, make sure it works with any number
	 * of players (between 0 and 32 players will be checked). Also, it is a
	 * good idea to make sure the ship does not target itself or an
	 * already-fallen (e.g. inactive) ship.
	 */
	private void selectTarget() {
		//#region PUT YOUR CODE HERE
		if (target!=null && target.isActive()){
			return;
		}		
		ship_avail = new ArrayList<Ship>();		
		Iterator<Ship> shipIter = fleet.iterator();		
		while (shipIter.hasNext()){
			Ship current = shipIter.next();
			if (current.isActive() && current.getId() != ship.getId()){
				ship_avail.add(current);
			}
		}
		
		//println("available ships number : " + ship_avail.size());
		if (ship_avail.size()>0){
			int index = (int) (Math.random()*ship_avail.size()) % ship_avail.size();
			target = ship_avail.get(index);
			//println("ship " + ship.getId() + ": target is "+ target.getId());
		}
		
		else {
			target=null;
		}
		
		//#endregion			
	}

	/**
	 * Returns true if we can hit a target from here.
	 *
	 * Insert code to return true if a shot fired from the given (x,y) would
	 * be likely to hit the target. We can hit a target if it is in a straight
	 * line from this tile and within attack range. The implementation must take
	 * into consideration whether or not the source tile is a Power Tile.
	 *
	 * @param x The x-index of the source tile
	 * @param y The y-index of the source tile
	 * @return true if we can hit a target from here.
	 */
	private boolean canShootTargetFrom(int x, int y) {
		//println("target location = " + board.screenToBoard(target.getX()) + ", " + board.screenToBoard(target.getY()));
		//#region
		

		if ((Math.abs(board.screenToBoard(target.getX()) - x) < ATTACK_DIST 
				&& board.screenToBoard(target.getY()) == y) || 
				(Math.abs(board.screenToBoard(target.getY()) - y) < ATTACK_DIST 
						&& board.screenToBoard(target.getX()) == x)){
			return true;
		}
		
		if (board.isPowerTileAt(x, y) && Math.abs(board.screenToBoard(target.getX())-x) 
				== Math.abs(board.screenToBoard(target.getY())-y)){
			return true;
		}
		
		return false;
		//#endregion not sure
	}

	/**
	 * Returns true if we can both fire and hit our target
	 *
	 * If we can fire now, and we could hit the target from where we are, 
	 * we should hit the target now.
	 *
	 * @return true if we can both fire and hit our target
	 */
	private boolean canShootTarget() {
		//#region
		int shipX = board.screenToBoard(ship.getX());
		int shipY = board.screenToBoard(ship.getY());
		return canShootTargetFrom(shipX, shipY);
		//#endregion
	}

	// Pathfinding Code (MODIFY ALL THE FOLLOWING METHODS)

	/** 
	 * Mark all desirable tiles to move to.
	 *
	 * This method implements pathfinding through the use of goal tiles.
	 * It searches for all desirable tiles to move to (there may be more than
	 * one), and marks each one as a goal. Then, the pathfinding method
	 * getMoveAlongPathToGoalTile() moves the ship towards the closest one.
	 *
	 * POSTCONDITION: There is guaranteed to be at least one goal tile
     * when completed.
     */
	private void markGoalTiles() {
		// Clear out previous pathfinding data.
		board.clearMarks(); 
		setGoal = false; // Until we find a goal
		
		// Add initialization code as necessary
		
		//#region PUT YOUR CODE HERE
		
		//#endregion
		
		switch (state) {
		case SPAWN: 
			break;

		case WANDER: // Do not pre-empt with FSMState in a case
			// Insert code to mark tiles that will cause us to move around;
			// set setGoal to true if we marked any tiles.
			// NOTE: this case must work even if the ship has no target
			// (and changeStateIfApplicable should make sure we are never
			// in a state that won't work at the time)
			
			//#region PUT YOUR CODE HERE
			
			
			int x = (int)(Math.random()*board.getWidth());
			int y = (int)(Math.random()*board.getHeight());
			
			if (board.isSafeAt(x, y)){
				board.setGoal(x, y);
				setGoal = true;
			}
			break;

		case CHASE: 
			//#region PUT YOUR CODE HERE
			int x1 = board.screenToBoard(target.getX());
			int y1 = board.screenToBoard(target.getY());
			
			board.setGoal(x1, y1);
			setGoal=true;
			//#endregion
			break;

		case ATTACK: 
			//#region PUT YOUR CODE HERE
			for (int new_x=0; new_x<board.getWidth();new_x++){
				for (int new_y = 0 ; new_y<board.getHeight();new_y++){
					if (canShootTargetFrom(new_x,new_y)){
						board.setGoal(new_x,new_y);
						setGoal=true;
					}
				}
			}
			//#endregion
			break;
		}

		// If we have no goals, mark current position as a goal
		// so we do not spend time looking for nothing:
		if (!setGoal) {
			int sx = board.screenToBoard(ship.getX());
			int sy = board.screenToBoard(ship.getY());
			board.setGoal(sx, sy);
			setGoal = true;
		}
	}
	
	/**
 	 * Returns a movement direction that moves towards a goal tile.
 	 *
 	 * This is one of the longest parts of the assignment. Implement
	 * breadth-first search (from 2110) to find the best goal tile
	 * to move to. However, just return the movement direction for
	 * the next step, not the entire path.
	 * 
	 * The value returned should be a control code.  See PlayerController
	 * for more information on how to use control codes.
	 *
 	 * @return a movement direction that moves towards a goal tile.
 	 */
	private int getMoveAlongPathToGoalTile() {
		if (current.prev == null){
			return CONTROL_NO_ACTION;
		}		
		while (current.prev.prev!=null){			
			current = current.prev;			
		}		
		if (current.prev.x > current.x){
			return CONTROL_MOVE_LEFT;
		}
		else if (current.prev.x < current.x){
			return CONTROL_MOVE_RIGHT;
		}
		if (current.prev.y > current.y){
			return CONTROL_MOVE_UP;
		}
		else if (current.prev.y < current.y){
			return CONTROL_MOVE_DOWN;
		}
		
		return CONTROL_NO_ACTION;
		//#endregion
	}
}