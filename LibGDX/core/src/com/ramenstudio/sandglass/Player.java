package com.ramenstudio.sandglass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * The player model. This stores information about the player in this level as
 * well as information to draw the player.
 * 
 * @author Jiacong Xu
 * @author Nathaniel Hunter
 */
public class Player extends GameObject {
  
  public Player() {
	  super();
	  this.setSize(new Vector2(1,1));
	  this.setTexture(new Texture(Gdx.files.internal("badlogic.jpg")));
  }

}
