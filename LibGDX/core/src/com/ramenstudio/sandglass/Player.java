package com.ramenstudio.sandglass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * The player model. This stores information about the player in this level as
 * well as information to draw the player.
 * 
 * @author flyingbanana
 * @author Nathaniel Hunter
 */
public class Player extends GameObject implements Drawable {
  
  public Player() {
	  super();
	  this.setTexture(new Texture(Gdx.files.internal("badlogic.jpg")));
	  this.setSize(new Vector2(1,1));
  }
}
