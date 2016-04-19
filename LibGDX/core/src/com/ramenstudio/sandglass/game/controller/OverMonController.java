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
		action = Move.valueOf(monster.initMove);
	}



	public boolean isGrounded() {
		Vector2 footPos = null;
		Vector2 endPos = null;
		Vector2 monsterSize = monster.getSize();
		Vector2 monsterPosition = monster.getBody().getPosition();
		// Handle case of Monster facing North
		if (monster.angle == AngleEnum.NORTH) {
			footPos = monsterPosition.cpy().sub(0,monsterSize.y*0.5f);
			endPos = footPos.cpy().sub(0,1f);
		} else if (monster.angle == AngleEnum.EAST) {
			footPos = monsterPosition.cpy().sub(monsterSize.y*0.5f,0);
			endPos = footPos.cpy().sub(1f,0);
		} else if (monster.angle == AngleEnum.SOUTH) {
			footPos = monsterPosition.cpy().add(0,monsterSize.y*0.5f);
			endPos = footPos.cpy().add(0,1f);
		} else {
			footPos = monsterPosition.cpy().add(monsterSize.y*0.5f,0);
			endPos = footPos.cpy().add(1f,0);
		}
		RayCastHandler handler = new RayCastHandler();
		delegate.rayCast(handler, footPos, endPos);
		oneFrameRayHandler = handler;
		return handler.isGrounded;
	}

	public boolean isWall(){
		Vector2 leftPos = null;
		Vector2 rightPos = null;
		Vector2 leftEnd = null;
		Vector2 rightEnd = null;
		Vector2 monsterPosition = monster.getBody().getPosition();
		float scale = (monster.getSize().cpy().y-monster.getSize().cpy().x)/2;
		Vector2 left = new Vector2(-1f,0f).scl(scale);
		Vector2 right = new Vector2(1f,0f).scl(scale);
		if (monster.angle == AngleEnum.NORTH) {
			leftPos = monsterPosition.cpy().sub(monster.getSize().cpy().scl(0.5f).x,0);
			rightPos = monsterPosition.cpy().add(monster.getSize().cpy().scl(0.5f).x,0);
			leftEnd = leftPos.cpy().add(left.cpy());
			rightEnd = rightPos.cpy().add(right.cpy());
		} else if (monster.angle == AngleEnum.EAST) {
			leftPos = monsterPosition.cpy().add(0,monster.getSize().cpy().scl(0.5f).x);
			rightPos = monsterPosition.cpy().sub(0,monster.getSize().cpy().scl(0.5f).x);
			leftEnd = leftPos.cpy().add(left.cpy().rotate90(-1));
			rightEnd = rightPos.cpy().add(right.cpy().rotate90(-1));
		} else if (monster.angle == AngleEnum.SOUTH) {
			leftPos = monsterPosition.cpy().add(monster.getSize().cpy().scl(0.5f).x,0);
			rightPos = monsterPosition.cpy().sub(monster.getSize().cpy().scl(0.5f).x,0);
			leftEnd = leftPos.cpy().sub(left.cpy());
			rightEnd = rightPos.cpy().sub(right.cpy());
		} else {
			leftPos = monsterPosition.cpy().sub(0,monster.getSize().cpy().scl(0.5f).x);
			rightPos = monsterPosition.cpy().add(0,monster.getSize().cpy().scl(0.5f).x);
			leftEnd = leftPos.cpy().add(left.cpy().rotate90(1));
			rightEnd = rightPos.cpy().add(right.cpy().rotate90(1));
		}
		

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
		Vector2 ms = monster.getSize();
		Vector2 mp = monster.getBody().getPosition();
		switch (monster.angle){
			case EAST:
				if (action==Move.UP && !gdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.NORTH));
					monster.setPosition(mp.cpy().sub(ms.cpy().y*0.6f,-ms.cpy().y*0.5f));
					action = Move.LEFT;
					monster.angle = AngleEnum.NORTH;
				}
				else if (action==Move.UP && wdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.SOUTH));
					monster.setPosition(mp.cpy().add(0.05f,-0.05f));
					action = Move.RIGHT;
					monster.angle = AngleEnum.SOUTH;
				}
				else if (action==Move.DOWN && !gdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.SOUTH));
					monster.setPosition(mp.cpy().sub(ms.cpy().y*0.6f,ms.cpy().y*0.5f));
					action = Move.LEFT;
					monster.angle = AngleEnum.SOUTH;
				}
				else if (action==Move.DOWN && wdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.NORTH));
					monster.setPosition(mp.cpy().add(0.05f,0.05f));
					action = Move.RIGHT;
					monster.angle = AngleEnum.NORTH;
				}
				break;
			case NORTH:
				if (action==Move.LEFT && !gdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.WEST));
					monster.setPosition(mp.cpy().sub(ms.cpy().y*0.5f,ms.cpy().y*0.6f));
					action = Move.DOWN;
					monster.angle = AngleEnum.WEST;
				}
				else if (action==Move.LEFT && wdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.EAST));
					monster.setPosition(mp.cpy().add(0.05f,0.05f));
					action = Move.UP;
					monster.angle = AngleEnum.EAST;
				}
				else if (action==Move.RIGHT && !gdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.EAST));
					monster.setPosition(mp.cpy().add(ms.cpy().y*0.5f,-ms.cpy().y*0.6f));
					action = Move.DOWN;
					monster.angle = AngleEnum.EAST;
				}
				else if (action==Move.RIGHT && wdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.WEST));
					monster.setPosition(mp.cpy().add(-0.05f,0.05f));
					action = Move.UP;
					monster.angle = AngleEnum.WEST;
				}
				break;
			case SOUTH:
				if (action==Move.LEFT && !gdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.WEST));
					monster.setPosition(mp.cpy().sub(ms.cpy().y*0.5f,-ms.cpy().y*0.6f));
					action = Move.UP;
					monster.angle = AngleEnum.WEST;
				}
				else if (action==Move.LEFT && wdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.EAST));
					monster.setPosition(mp.cpy().add(0.05f,-0.05f));
					action = Move.DOWN;
					monster.angle = AngleEnum.EAST;
				}
				else if (action==Move.RIGHT && !gdd){
					monster.setPosition(mp.cpy().add(ms.cpy().y*0.5f,ms.cpy().y*0.6f));
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.EAST));
					action = Move.UP;
					monster.angle = AngleEnum.EAST;
				}
				else if (action==Move.RIGHT && wdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.WEST));
					monster.setPosition(mp.cpy().sub(0.05f,0.05f));
					action = Move.DOWN;
					monster.angle = AngleEnum.WEST;
				}
				break;
			case WEST:
				if (action==Move.UP && !gdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.NORTH));
					monster.setPosition(mp.cpy().add(ms.cpy().y*0.6f,ms.cpy().y*0.5f));
					action = Move.RIGHT;
					monster.angle = AngleEnum.NORTH;
				}
				else if (action==Move.UP && wdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.SOUTH));
					monster.setPosition(mp.cpy().sub(0.05f,0.05f));
					action = Move.LEFT;
					monster.angle = AngleEnum.SOUTH;
				}
				else if (action==Move.DOWN && !gdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.SOUTH));
					monster.setPosition(mp.cpy().add(ms.cpy().y*0.6f,-ms.cpy().y*0.5f));
					action = Move.RIGHT;
					monster.angle = AngleEnum.SOUTH;
				}
				else if (action==Move.DOWN && wdd){
					monster.setRotation(AngleEnum.convertToAngle(AngleEnum.NORTH));
					monster.setPosition(mp.cpy().sub(0.05f,-0.05f));
					action = Move.LEFT;
					monster.angle = AngleEnum.NORTH;
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
	public void getAction(float dt) {
		// Increment the number of ticks.
		Move move = Move.NONE;
		ticks++;
		// Do not need to rework ourselves every frame. Just every 10 ticks.
		if (ticks%100<50){
			move = Move.LEFT;
		}
		else {
			move = Move.RIGHT;
		}
		action = move;
	}

	@Override
	public void update(float dt){
//		System.out.println("======Start of cycle======");
//		System.out.println("Before Rotation: " + monster.angle.toString());
//		System.out.println("is Grounded?" + isGrounded());
//		System.out.println("is Walled? " + isWall());
		//rotateMonster();
		getAction(dt);
		System.out.println(monster.angle.toString());
//		System.out.println("Direction : " + action.toString());
//		System.out.println("After Rotation: " + monster.angle.toString());
		super.update(dt);
	}

}