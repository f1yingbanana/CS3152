package com.ramenstudio.sandglass;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * The root view object used for drawing. Canvas needs to track the camera
 * reference to be able to calculate the transforms. This class provides various
 * graphic APIs to draw objects on screen.
 * 
 * @author Jiacong Xu
 */
public class GameCanvas {
  // A reference to the main camera for the game. Camera itself should be stored
  // in GameplayModel.
  private OrthographicCamera mainCamera;
  
  /**
   * Instantiates a game canvas with the given camera.
   * 
   * @param camera  the orthographic camera reference. The camera itself can be
   *                stored anywhere.
   */
  public GameCanvas(OrthographicCamera camera) {
    mainCamera = camera;
  }
  
}
