package com.ramenstudio.sandglass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * The player model. This stores information about the player in this level as
 * well as information to draw the player.
 * 
 * @author flyingbanana
 */
public class Player extends GameObject implements Drawable {
  private Texture playerTexture;
  
  private Vector2 size = new Vector2(1, 1);
  
  public Player() {
    playerTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
  }
  
  @Override
  public void draw(GameCanvas canvas) {
    canvas.draw(playerTexture, getPosition(), size);
  }
}
