package com.ramenstudio.sandglass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * The root model class for storing all information in a single level.
 * 
 * @author flyingbanana
 */
public class GameModel implements Drawable {
  private GameCamera mainCamera;
  
  private Player player = new Player();
  
  /**
   * Initializer.
   */
  public GameModel() {
    float ratio = (float)Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
    mainCamera = new GameCamera(new Vector2(9 * ratio, 9));
  }
  
  /**
   * @return the game camera we are currently using.
   */
  public GameCamera getMainCamera() {
    return mainCamera;
  }

  @Override
  public void draw(GameCanvas canvas) {
    // Tells all objects to render themselves with the given canvas.
    player.draw(canvas);
  }
}
