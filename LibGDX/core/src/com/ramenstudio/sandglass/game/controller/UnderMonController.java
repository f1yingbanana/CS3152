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
        int move = 0;
        ticks++;
        // Do not need to rework ourselves every frame. Just every 10 ticks.
            if (monster.getLevel() ==1){
               
                if (ticks%period< period/2){
                    move = 2;
                    //monster.setRotation(AngleEnum.convertToAngle(AngleEnum.SOUTH));
                }
                else {
                    move = 3;
                    //monster.setRotation(AngleEnum.convertToAngle(AngleEnum.NORTH));
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
        action = move;
    }
    
    @Override
    public void update(float dt){
        super.update(dt);
    }
}