package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramenstudio.sandglass.game.model.GameState;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.game.view.ui.GameView;
import com.ramenstudio.sandglass.game.view.ui.PauseView;

/**
 * The root controller for all UI-related functionalities in-game. This keeps
 * the stage and skin.
 * 
 * @author Jiacong Xu
 */
public class UIController extends AbstractController {
  private Stage stage;
  private Skin skin = new Skin(Gdx.files.internal("UI/Skin/uiskin.json"));
  
  // For caching purposes.
  private GameState gameState;
  
  public GameView gameView = new GameView(skin);
  public PauseView pauseView = new PauseView(skin);
  
  // Outlets for paused table
  
  public UIController() {
    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);
    
    // Add playing UI.
    stage.addActor(gameView);
    
    // Add paused UI.
    stage.addActor(pauseView);
    
    setGameState(GameState.PLAYING);
  }
  
  /**
   * Sets which game state we are in. Uses this to switch visibility of
   * different UIs.
   * 
   * @param state is the current play status of the game.
   */
  public void setGameState(GameState state) {
    if (state == gameState) {
      return;
    }
    
    // Hide all tables
    gameView.setVisible(false);
    pauseView.setVisible(false);
    
    switch (state) {
    case LOST:
      break;
    case PAUSED:
      pauseView.setVisible(true);
      break;
    case PLAYING:
      gameView.setVisible(true);
      break;
    case WON:
      break;
    }
  }
  
  @Override
  public void update(float dt) {
    stage.act(dt);
  }
  
  @Override
  public void draw(GameCanvas canvas) {
    stage.draw();
  }

}
