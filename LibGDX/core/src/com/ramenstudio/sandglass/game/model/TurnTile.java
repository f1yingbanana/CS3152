package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.Drawable;

/**
 * A tile that marks a place that player should be able to turn.
 * 
 * @author Jiacong Xu
 * @author Nathaniel Hunter
 */
public class TurnTile extends GameObject implements Drawable {
  
  public TurnTile() {
    super();
    
    getBodyDef().type = BodyDef.BodyType.StaticBody;
    
    fixtureDefs = new FixtureDef[1];
    PolygonShape shape = new PolygonShape();
    fixtureDefs[0] = new FixtureDef();
    fixtureDefs[0].isSensor = true;
    shape.setAsBox(0.5f, 0.5f);
    fixtureDefs[0].shape = shape;
  }
  
  @Override
  public void draw(GameCanvas canvas){
    canvas.draw(getTexture(), getPosition(), getSize());
  }
}
