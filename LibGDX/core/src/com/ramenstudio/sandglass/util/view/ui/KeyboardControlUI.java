package com.ramenstudio.sandglass.util.view.ui;

/**
 * An UI object that is selectable, event registerable and can shift focus. This
 * is designed so that the entire game can be played with keyboard only.
 * 
 * @author Jiacong Xu
 */
public interface KeyboardControlUI {
  
  // Getters and setters for other instances of focusable objects.
  public KeyboardControlUI getArrowUp();
  public KeyboardControlUI getArrowDown();
  public KeyboardControlUI getArrowLeft();
  public KeyboardControlUI getArrowRight();
  
  public void setArrowUp(KeyboardControlUI element);
  public void setArrowDown(KeyboardControlUI element);
  public void setArrowLeft(KeyboardControlUI element);
  public void setArrowRight(KeyboardControlUI element);
  
  /**
   * Whether this element is focused by the player!
   */
  public void focusChanged(boolean isFocused);
  
  /**
   * This element has been interacted. Perform some action!
   */
  public void interacted();
  
  /**
   * Tags for identification
   */
  public int tag();
  
  public void setTag(int t);
}
