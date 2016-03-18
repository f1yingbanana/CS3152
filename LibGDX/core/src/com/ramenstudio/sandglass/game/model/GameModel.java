package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.Drawable;

/**
 * The root model class for storing all information in a single level.
 * 
 * @author flyingbanana
 */
public class GameModel implements Drawable {
  /**
   * Initializer.
   */
  public GameModel() {
  }
  
  @Override
  public void draw(GameCanvas canvas) {
    // Tells all objects to render themselves with the given canvas.
  }
}
