package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
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
  private Texture playerTexture;
  
  private Vector2 size = new Vector2(0.8f, 1.5f);
  
  /**
   * Creates the player at the given initial position.
   * 
   * @param initialPos is the position of the player at the time of creation.
   */
  public Player(Vector2 initialPos) {
    playerTexture = new Texture(Gdx.files.internal("/Users/saerom/Desktop/2016SP/GameDesign/CS3152/LibGDX/core/assets/badlogic.jpg"));
    bodyDef.position.set(initialPos);
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    
    fixtureDef = new FixtureDef();
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(0.4f, 0.75f, initialPos, 0);
    fixtureDef.density = 10.0f;
    fixtureDef.shape = shape;
  }
  
  @Override
  public void draw(GameCanvas canvas) {
    canvas.draw(playerTexture, getPosition().add(size.cpy().scl(-0.5f)), size);
  }
}
