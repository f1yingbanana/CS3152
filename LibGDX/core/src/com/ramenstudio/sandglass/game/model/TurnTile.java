package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ramenstudio.sandglass.game.controller.AngleEnum;

/**
 * A tile that marks a place that player should be able to turn.
 * 
 * @author Jiacong Xu
 * @author Nathaniel Hunter
 */
public class TurnTile extends AbstractTile {

	/** The placement of this TurnTile relative to the corner it is on.
	 * 0: Top left
	 * 1: Top right
	 * 2: Bottom right
	 * 3: Bottom left
	 */
	private int index;

	public TurnTile() {
		super();
		float boxSize = .15f;
		size.x = boxSize;
		size.y = boxSize;
		getBodyDef().type = BodyDef.BodyType.StaticBody;
		fixtureDefs = new FixtureDef[1];
		PolygonShape shape = new PolygonShape();
		fixtureDefs[0] = new FixtureDef();
		fixtureDefs[0].isSensor = true;
		shape.setAsBox(boxSize, boxSize);
		fixtureDefs[0].shape = shape;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int i) {
		index = i;
	}
	
	/** Determines whether or not the player is walking towards the TurnTile. */
	public boolean validTurn(Vector2 velocity, AngleEnum heading) {
		switch(index) {
		case 0:
			if ((velocity.x < 0 && heading == AngleEnum.NORTH) || 
					(velocity.y > 0 && heading == AngleEnum.WEST)) {
				return true;
			}
			break;
		case 1:
			if ((velocity.x > 0 && heading == AngleEnum.NORTH) || 
					(velocity.y > 0 && heading == AngleEnum.EAST)) {
				return true;
			}
			break;
		case 2:
			if ((velocity.x > 0 && heading == AngleEnum.SOUTH) || 
					(velocity.y < 0 && heading == AngleEnum.EAST)) {
				return true;
			}
			break;
		case 3:
			if ((velocity.x < 0 && heading == AngleEnum.SOUTH) || 
					(velocity.y < 0 && heading == AngleEnum.WEST)) {
				return true;
			}
			break;
		}
		return false;
	}
	
}
