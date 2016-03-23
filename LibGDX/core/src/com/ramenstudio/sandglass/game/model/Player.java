package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.Drawable;

/**
 * The player model. This stores information about the player in this level as
 * well as information to draw the player.
 * 
 * @author flyingbanana
 */
public class Player extends GameObject implements Drawable {
  /**
   * Creates the player at the given initial position.
   * 
   * @param initialPos is the position of the player at the time of creation.
   */
  public Player(Vector2 initialPos) {
    super();
    setTexture(new Texture(Gdx.files.internal("badlogic.jpg")));
    setSize(new Vector2(0.8f, 1.5f));
    getBodyDef().position.set(initialPos);
    getBodyDef().type = BodyDef.BodyType.DynamicBody;
    
    FixtureDef fixtureDef = new FixtureDef();
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(0.4f, 0.75f);
    fixtureDef.density = 10.0f;
    fixtureDef.shape = shape;
    fixtureDefs = new FixtureDef[1];
    fixtureDefs[0] = fixtureDef;
  }
  
  @Override
  public void draw(GameCanvas canvas) {
    canvas.draw(getTexture(), getPosition().add(getSize().cpy().scl(-0.5f)), getSize());
  }
  
  @Override
  public void setBody(Body body) {
    super.setBody(body);
    
  }
}
