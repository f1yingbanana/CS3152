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
import com.ramenstudio.sandglass.game.model.Monster;
import com.ramenstudio.sandglass.game.model.Monster.Move;

/** 
 * InputController corresponding to AI control.
 * 
 */
public class OverMonController extends MonsterController {
 
    // Instance Attributes
    /** is player in the same world*/
    public boolean setGoal;
    /**
     * Creates an AIController for the monster with the given id.
     *
     * @param id The unique monster identifier
     * @param argMonster is the monster this controller represents
     * @param gmodel is the board state used for tracking
     * @param player is the target of this monster
     */
    public OverMonController(Monster monster) {
        super(monster);
    }
    

    
    public boolean isGrounded() {
        
        
        Vector2 g = delegate.getGravity().nor();
        Vector2 footPos = monster.getBody().getPosition().sub(monster.getSize().x*0.5f,monster.getSize().y*0.5f);
        Vector2 endPos = footPos.cpy().add(g.cpy().scl(1.0f));

        Vector2 footPos2 = monster.getBody().getPosition().sub(-monster.getSize().x,monster.getSize().y*0.5f);
        Vector2 endPos2 = footPos2.cpy().add(g.cpy().scl(1.0f));
        System.out.println("MONSTER: "+ monster.getBody().getPosition().toString());
        System.out.println("LEFTCORNER: "+ endPos2.toString());
        System.out.println("RIGHTCORNER:"+ endPos.toString());
        RayCastHandler handler = new RayCastHandler();
        delegate.rayCast(handler, footPos, endPos);
        RayCastHandler handler2 = new RayCastHandler();
        delegate.rayCast(handler2, footPos2, endPos2);
        oneFrameRayHandler = handler;
        return handler.isGrounded || handler2.isGrounded;
    }
    
    public boolean isWall(){
            Vector2 left = delegate.getGravity().nor().rotate90(1);
            Vector2 right = delegate.getGravity().nor().rotate90(-1);
            Vector2 leftPos = monster.getBody().getPosition().add(-monster.getSize().scl(0.5f).x,0);
            Vector2 leftEnd = leftPos.cpy().sub(left.cpy().scl(1.4f));
            Vector2 rightPos = monster.getBody().getPosition().add(monster.getSize().scl(0.5f).x,0);
            Vector2 rightEnd = rightPos.cpy().add(right.cpy().scl(1.4f));
    
            RayCastHandler handler = new RayCastHandler();
            RayCastHandler handler2 =new RayCastHandler();
            delegate.rayCast(handler, leftPos, leftEnd);
            delegate.rayCast(handler2, rightPos, rightEnd);
            oneFrameRayHandler = handler;
            return handler.isGrounded || handler2.isGrounded;
    }
    
    public void rotateMonster(){
        boolean gdd = isGrounded();
        boolean wdd = isWall();
        switch (monster.angle){
        case EAST:
            if (action==Move.UP && !gdd){
                monster.setRotation(AngleEnum.convertToAngle(AngleEnum.NORTH));
                action = Move.LEFT;
            }
            else if (action==Move.UP && wdd){
                monster.setRotation(AngleEnum.convertToAngle(AngleEnum.SOUTH));
                action = Move.RIGHT;
            }
            else if (action==Move.DOWN && !gdd){
                monster.setRotation(AngleEnum.convertToAngle(AngleEnum.SOUTH));
                action = Move.LEFT;
            }
            else if (action==Move.DOWN)
            break;
        case NORTH:
            break;
        case SOUTH:
            break;
        case WEST:
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
    public void getAction(float dt) {
        // Increment the number of ticks.
        Move move = Move.NONE;
        ticks++;
        // Do not need to rework ourselves every frame. Just every 10 ticks.
            if (setGoal){
                    String gdd = isGrounded() ? "is Grounded" : "not Grounded";
                    String wdd = isWall()? "is Walled" : "not Walled";
                    System.out.println(gdd);
                    System.out.println(wdd);
                    move = Move.LEFT;
                if (!isGrounded()){
                    move = Move.DOWN;
                }
            }
        action = move;  
    }
    
    @Override
    public void update(float dt){
        rotateMonster();
        super.update(dt);
    }
    
}