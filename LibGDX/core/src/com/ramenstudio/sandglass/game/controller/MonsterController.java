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


	@Override
	public void dispose() {
		delegate.dispose();
		monster.dispose();
	}
}