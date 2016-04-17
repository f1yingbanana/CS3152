/**
 * MonsterController.java
 *
 * This class is an unfinished draft of MonsterController
 *
 * Author: Saerom Choi
 * Based on AIController from Lab 3 by Walker M. WHite
 */
package com.ramenstudio.sandglass.game.controller;
import com.ramenstudio.sandglass.game.model.Monster;
import com.ramenstudio.sandglass.game.model.Monster.Move;

/** 
 * InputController corresponding to AI control.
 * 
 */
public class UnderMonController extends MonsterController {
 
    // Instance Attributes
    /**
     * Creates an AIController for the monster with the given id.
     *
     * @param id The unique monster identifier
     * @param argMonster is the monster this controller represents
     * @param gmodel is the board state used for tracking
     * @param player is the target of this monster
     */
    public UnderMonController(Monster monster) {
        super(monster);
        
        
    }
    /**
     * Returns the action selected by this MonsterController
     * 
     * 
     * @return the action selected by this MonsterController
     */
    @Override
    public void getAction(float dt) {
        // Increment the number of ticks.
        Move move = Move.NONE;
        ticks++;
        // Do not need to rework ourselves every frame. Just every 10 ticks.
        action = move;
    }
    
    @Override
    public void update(float dt){
        super.update(dt);
        monster.setRotation((delegate.getGravity().angle()+90)*(float)Math.PI/180);
    }
}