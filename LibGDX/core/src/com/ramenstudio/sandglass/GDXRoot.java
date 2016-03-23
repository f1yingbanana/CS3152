package com.ramenstudio.sandglass;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.ramenstudio.sandglass.game.GameMode;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.loading.LoadingMode;
import com.ramenstudio.sandglass.title.TitleMode;
import com.ramenstudio.sandglass.util.ScreenListener;

/**
 * The platform-independent root class that initializes the base MVC for the
 * game.
 * 
 * @author Jiacong Xu
 */
public class GDXRoot extends Game implements ScreenListener {
  /** Loads and manages assets. */
  private AssetManager manager;
  
  /** The three game modes */
  private TitleMode titleMode;
  private GameMode gameMode;
  private LoadingMode loadingMode;
  
  public enum ApplicationMode {
    TITLE, GAME, LOADING
  }
  
  /**
   * Default initializer that initializes the asset manager.
   */
  public GDXRoot() {
    // Initializes manager.
    manager = new AssetManager();
  }
  
  /**
   * We dispose a screen and set the new screen given the mode. We assume the
   * screen we are setting to is correctly configured before this function is
   * called.
   * 
   * @param mode represents which of the three modes of the application we want
   * to switch to.
   */
  public void setApplicationMode(ApplicationMode mode) {
    switch (mode) {
    case TITLE:
      setScreen(titleMode);
      break;
    case GAME:
      setScreen(gameMode);
      break;
    case LOADING:
      setScreen(loadingMode);
      break;
    }
  }
  
  /** 
   * Called when the Application is first created.
   * 
   * This method immediately loads assets for the loading screen, and prepares
   * the asynchronous loader for all other assets.
   */
  @Override
  public void create() {
    // We create all the modes we need. A title mode, a game mode, and a loading
    // mode. We don't need to load anything yet, though.
    titleMode = new TitleMode();
    gameMode = new GameMode();
    loadingMode = new LoadingMode(manager);
    loadingMode.setScreenListener(this);
    
//    String[] textureList = gameMode.getTexturePaths();
//    String[] soundList = gameMode.getSoundPaths();
    
//    loadingMode = new LoadingMode(manager);
//    loadingMode.loadSound(soundList);
//    loadingMode.loadTexture(textureList);
    
    
    // For now, we simply load the game play mode.
    setApplicationMode(ApplicationMode.GAME);
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
  
  @Override
  public void exitScreen(Screen screen, int exitCode) {
	  
	  switch (exitCode){
	  
	  	case 0:
	  		setApplicationMode(ApplicationMode.GAME);
	  		setScreen(gameMode);
	  		break;
	  		
	  	case 1:
	  		setApplicationMode(ApplicationMode.LOADING);
	  		setScreen(loadingMode);
	  		
	  }
    // When the given mode wants to exit the mode. This happens when title chose
    // a level to play on, or when game mode is done, or when loading is done.
  }
}
