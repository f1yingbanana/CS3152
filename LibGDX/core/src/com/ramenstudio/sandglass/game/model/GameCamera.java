package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The game rener camera object that is maintained inside a game object.
 * 
 * @author Jiacong Xu
 */
public class GameCamera extends GameObject {
	
  private OrthographicCamera camera;
  private Viewport viewport;
  
  /**
   * Initializes the camera with the given viewport size.
   * @param size is the size of the view in world units. Can be zoomed later.
   */
  public GameCamera(Vector2 size) {
    camera = new OrthographicCamera(size.x, size.y);
    this.viewport = new FitViewport(size.x,size.y,camera);
  }
  
  /**
   * @return the underlying orthogonal camera this game object holds.
   */
  public OrthographicCamera getCamera() {
    return camera;
  }
  
  public Viewport getViewport() {
	  return viewport;
  }
  
  @Override
  public void setPosition(Vector2 position) {
    // Also moves the camera
    Vector2 oldPos = getPosition();
    super.setPosition(position);
    camera.translate(position.sub(oldPos));
    camera.update();
  }
  
  @Override
  public void setRotation(float rotation) {
    // Also rotates the camera
    float oldRot = getRotation();
    super.setRotation(rotation);
    camera.rotate(rotation - oldRot);
//	super.setRotation(getRotation()+rotation);
//	camera.rotate(rotation);
    camera.update();
  }
}
