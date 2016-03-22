package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ramenstudio.sandglass.game.controller.GameController;
import com.ramenstudio.sandglass.game.controller.PhysicsDelegate;
import com.ramenstudio.sandglass.game.view.GameCanvas;

public class NormalTile extends GameObject implements SandglassTile {
	
	private boolean isFlip;

	public NormalTile(PhysicsDelegate physicsDelegate,
			World world, float positionX, float positionY, float boxHalfWidth,
			float boxHalfHeight, boolean argFlip) {
		isFlip = argFlip;
		
	    BodyDef box1Def = new BodyDef();
	    box1Def.position.set(new Vector2(positionX, positionY));
	    Body box1 = world.createBody(box1Def);
	    PolygonShape boxShape1 = new PolygonShape();
	    boxShape1.setAsBox(boxHalfWidth, boxHalfHeight);
	    box1.createFixture(boxShape1, 0);
	}
	
	@Override
	public boolean isFlippable() {
		return isFlip;
	}

	
	@Override
	public void draw(GameCanvas canvas) {
		canvas.draw(getTexture(), getPosition(), getSize());
	}

	@Override
	public boolean isGround() {
		return true;
	}
}
