package com.ramenstudio.sandglass.util;

import com.ramenstudio.sandglass.game.view.GameCanvas;

/**
 * Specifies how the object can be rendered on a given canvas.
 * 
 * @author Jiacong Xu
 */
public interface Drawable {
  /**
   * Draw this object on the canvas.
   * 
   * @param canvas  the current active game canvas.
   */
  public void draw(GameCanvas canvas);
}
