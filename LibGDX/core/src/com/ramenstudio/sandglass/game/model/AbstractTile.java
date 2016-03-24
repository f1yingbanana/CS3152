package com.ramenstudio.sandglass.game.model;

/**
 * An abstract tile class that implements most of the tile logics.
 * @author Jiacong Xu
 */
public abstract class AbstractTile extends GameObject {
  protected boolean isFlippable;
  protected boolean isGround;
  
  /**
   * @return whether the tile is flippable.
   */
  public boolean isFlippable() {
    return isFlippable;
  }
  
  /**
   * @return whether the tile is ground.
   */
  public boolean isGround() {
    return isGround;
  }
}
