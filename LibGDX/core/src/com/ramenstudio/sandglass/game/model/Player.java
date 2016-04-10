package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
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
    
    fixtureDefs = new FixtureDef[3];
    
    setTexture(new Texture(Gdx.files.internal("character2.png")));
    setSize(new Vector2(0.8f, 1.5f));
    getBodyDef().position.set(initialPos);
    getBodyDef().type = BodyDef.BodyType.DynamicBody;
    
    FixtureDef fixtureDef = new FixtureDef();
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(0.4f, 0.35f);
    fixtureDef.density = 100.0f;
    fixtureDef.shape = shape;
    fixtureDefs[0] = fixtureDef;
    fixtureDef.friction = 0.0f;
    
    CircleShape c = new CircleShape();
    c.setRadius(0.4f);
    c.setPosition(new Vector2(0, -0.35f));
    FixtureDef fixtureDef2 = new FixtureDef();
    fixtureDef2.shape = c;
    fixtureDefs[1] = fixtureDef2;

    CircleShape c2 = new CircleShape();
    c2.setRadius(0.4f);
    c2.setPosition(new Vector2(0, 0.35f));
    FixtureDef fixtureDef3 = new FixtureDef();
    fixtureDef3.shape = c2;
    fixtureDefs[2] = fixtureDef3;
  }
  
  @Override
  public void draw(GameCanvas canvas) {
	  canvas.draw(getTextureRegion(), getPosition().add(getSize().cpy().scl(-0.5f)), getSize(),
				new Vector2(getSize()).scl(.5f), (float)(getRotation() * 180/Math.PI));
  }
  
  @Override
  public void setBody(Body body) {
    super.setBody(body);
    
  }
}
