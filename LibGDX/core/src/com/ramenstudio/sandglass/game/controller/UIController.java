package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.game.view.ui.GameView;
import com.ramenstudio.sandglass.game.view.ui.LevelCompleteView;
import com.ramenstudio.sandglass.game.view.ui.PauseView;
import com.ramenstudio.sandglass.game.view.ui.TutorialView;
import com.ramenstudio.sandglass.util.view.ui.OptionsView;

/**
 * The root controller for all UI-related functionalities in-game. This keeps
 * the stage and skin.
 * 
 * @author Jiacong Xu
 */
public class UIController extends AbstractController {
  private Stage stage;
  private Skin skin = new Skin(Gdx.files.internal("UI/Skin/uiskin.json"));
  
  public enum UIState {
    TUTORIAL, PLAYING, PAUSED, OPTIONS, LOST, WON
  }
  
  // For caching purposes.
  private UIState uiState;
  
  /**
   * The tutorial UI view that is shown at the beginning of a level.
   */
  public TutorialView tutorialView;
  
  /**
   * The main game UI view.
   */
  public GameView gameView;
  
  /**
   * The paused UI view.
   */
  public PauseView pauseView = new PauseView(skin);
  
  /**
   * The options UI view.
   */
  public OptionsView optionsView = new OptionsView(skin);
  
  /**
   * The game completed UI view.
   */
  public LevelCompleteView levelCompleteView = new LevelCompleteView(skin, true);
  
  /**
   * The game failed UI view.
   */
  public LevelCompleteView levelFailedView = new LevelCompleteView(skin, false);
  
  public UIController(int gameLevel) {
    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);
    
    tutorialView = new TutorialView(skin, gameLevel);
    
    gameView = new GameView(skin, gameLevel);
    
    // Add playing UI.
    stage.addActor(gameView);

    // Add tutorial UI.
    stage.addActor(tutorialView);
    
    // Add paused UI.
    stage.addActor(pauseView);
    
    pauseView.optionsButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        setGameState(UIController.UIState.OPTIONS);
      }
    });
    
    // Add options UI.
    stage.addActor(optionsView);
    
    optionsView.backButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        setGameState(UIController.UIState.PAUSED);
      }
    });
    
    // Add level complete UI
    stage.addActor(levelCompleteView);
    stage.addActor(levelFailedView);
    
    // Finally, let the game play.
    setGameState(UIState.PLAYING);
  }
  
  /**
   * Sets which game state we are in. Uses this to switch visibility of
   * different UIs.
   * 
   * @param state is the current play status of the game.
   */
  public void setGameState(UIState state) {
    if (state == uiState) {
      return;
    }
    
    // Hide all tables
    tutorialView.setVisible(false);
    gameView.setVisible(false);
    pauseView.setVisible(false);
    optionsView.setVisible(false);
    levelCompleteView.setVisible(false);
    levelFailedView.setVisible(false);
    
    switch (state) {
    case TUTORIAL:
      tutorialView.setVisible(true);
    case PLAYING:
      gameView.setVisible(true);
      break;
    case PAUSED:
      pauseView.setVisible(true);
      break;
    case OPTIONS:
      optionsView.setVisible(true);
      break;
    case WON:
      levelCompleteView.setVisible(true);
      break;
    case LOST:
      levelFailedView.setVisible(true);
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

  /**
   * Acquires the input processor for UI in this mode. Must be called after
   * switching modes.
   */
  public void acquireInputProcesser() {
    Gdx.input.setInputProcessor(stage);
  }

@Override
public void dispose() {
	stage.dispose();
	skin.dispose();
}
  
}
