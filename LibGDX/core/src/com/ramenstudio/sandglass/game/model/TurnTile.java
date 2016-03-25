package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * A tile that marks a place that player should be able to turn.
 * 
 * @author Jiacong Xu
 * @author Nathaniel Hunter
 */
public class TurnTile extends AbstractTile {
  
  public TurnTile() {
    super();
    size.x = 0.05f;
    size.y = 0.05f;
    getBodyDef().type = BodyDef.BodyType.StaticBody;
    fixtureDefs = new FixtureDef[1];
    PolygonShape shape = new PolygonShape();
    fixtureDefs[0] = new FixtureDef();
    fixtureDefs[0].isSensor = true;
    shape.setAsBox(0.05f, 0.05f);
    fixtureDefs[0].shape = shape;
  }
}
