package com.ramenstudio.sandglass.util;

/**
 * An abstraction for mode that includes some required methods each mode should
 * adhere to.
 * 
 * @author Jiacong Xu
 */
public abstract class AbstractMode {
  /**
   * @return a list of resource paths under the asset folder that this mode
   * needs.
   */
  public abstract String[] getResourcePaths();
  
  /**
   * A screen listener that is set after screen creation to provide helpful
   * methods about screen switching.
   */
  public ScreenListener screenListener;
  
}
