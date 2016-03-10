package com.ramenstudio.sandglass;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
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
  private Color bgColor = Color.SKY;
  
  // Drawing context to handle textures AND POLYGONS as sprites
  private PolygonSpriteBatch spriteBatch = new PolygonSpriteBatch();
  
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
    return bgColor;
  }

  /**
   * Sets the background color for the canvas. Without it we get random noise 
   * from the buffer. The alpha channel is ignored.
   * 
   * @param color is the background color to draw. Default is coral.
   */
  public void setBackgroundColor(Color color) {
    bgColor = color;
  }
  
  /**
   * Call first thing in render to avoid left-over buffer garbage.
   */
  public void clear() {
    Gdx.gl.glClearColor(bgColor.r, bgColor.b, bgColor.g, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
  }
  
  /**
   * Begins a new drawing pass.
   */
  public void begin() {
    spriteBatch.setProjectionMatrix(mainCamera.combined);
    spriteBatch.begin();
  }
  
  /**
   * Flushes all the drawing to the GPU.
   */
  public void end() {
    spriteBatch.end();
  }
  
  /**
   * Draws the given texture at origin with given size.
   * @param image is the texture we will render.
   * @param origin is the bottom left corner of the image.
   * @param size is how big the image should be in world units.
   */
  public void draw(Texture image, Vector2 origin, Vector2 size) {
    spriteBatch.draw(image, origin.x, origin.y, size.x, size.y);
  }
}
