package com.ramenstudio.sandglass;

/**
 * The root model class for storing all information in a single level.
 * 
 * @author flyingbanana
 */
public class GameplayModel implements Drawable {
  private GameCamera mainCamera = new GameCamera();
  
  /**
   * @return the game camera we are currently using.
   */
  public GameCamera getMainCamera() {
    return mainCamera;
  }

  @Override
  public void draw(GameCanvas canvas) {
    // Tells all objects to render themselves with the given canvas.
  }
}
