package com.ramenstudio.sandglass;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.*;

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
  
  // The background color
  private Color backgroundColor = Color.SKY;
  
  /**
   * Instantiates a game canvas with the given camera.
   * 
   * @param camera  the orthographic camera reference. The camera itself can be
   *                stored anywhere.
   */
  public GameCanvas(OrthographicCamera camera) {
    mainCamera = camera;
  }
  
  /**
   * @return the current background color for the canvas.
   */
  public Color getBackgroundColor() {
    return backgroundColor;
  }

  /**
   * Sets the background color for the canvas. Without it we get random noise 
   * from the buffer.
   * 
   * @param color is the background color to draw. Default is coral.
   */
  public void setBackgroundColor(Color color) {
    backgroundColor = color;
  }
  
  /**
   * Call first thing in render.
   */
  public void clearCanvas() {
    Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.b, backgroundColor.g, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
  }
}
