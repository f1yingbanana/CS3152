package com.ramenstudio.sandglass;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;

/**
 * The platform-independent root class that initializes the base MVC for the
 * game.
 * 
 * @author Jiacong Xu
 */
public class GDXRoot extends ApplicationAdapter {
  /** Loads and manages assets. */
  private AssetManager manager;
  /** Drawing context for objects on scene. */
  private GameCanvas canvas;
  
  /**
   * Default initializer that initializes the asset manager.
   */
  public GDXRoot() {
    // Initializes manager.
    manager = new AssetManager();
  }
  
  /** 
   * Called when the Application is first created.
   * 
   * This method immediately loads assets for the loading screen, and prepares
   * the asynchronous loader for all other assets.
   */
  @Override
  public void create() {
    canvas  = new GameCanvas();
    // loading = new LoadingMode(canvas,manager,1);
    
    // Initializes and loads the necessary controllers.
    /*
    controllers = new WorldController[3];
    controllers[0] = new RocketController();
    controllers[1] = new PlatformController();
    controllers[2] = new RagdollController();
    for(int ii = 0; ii < controllers.length; ii++) {
      controllers[ii].preLoadContent(manager);
    }
    
    current = 0;
    loading.setScreenListener(this);
    setScreen(loading);
    */
  }

  /** 
   * Called when the Application is destroyed.
   *
   * pause() is called before this. We should recursively unload all assets.
   */
  @Override
  public void dispose() {
    // Call dispose on our children
  
    // Unload all of the resources
    manager.clear();
    manager.dispose();
    super.dispose();
  }
  
  /**
   * We use this to implement an update loop for the subsequent controllers.
   */
  @Override
  public void render() {
    
  }
}
