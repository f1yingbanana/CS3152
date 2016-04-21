package com.ramenstudio.sandglass.title.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramenstudio.sandglass.title.view.ui.*;
import com.ramenstudio.sandglass.util.view.ui.OptionsView;

/**
 * This is the root controller for the title screen, which consists of mainly
 * UI stuff.
 * 
 * @author Jiacong Xu
 */
public class UIController {
  
  public enum UIState {
    TITLE, OPTIONS, LEVEL_SELECT
  }
  
  private UIState state;
  
  private Stage stage;
  private Skin skin = new Skin(Gdx.files.internal("UI/Skin/uiskin.json"));
  
  public TitleView titleView = new TitleView(skin);
  
  public OptionsView optionsView = new OptionsView(skin);
  
  public LevelSelectView levelSelectView = new LevelSelectView(skin);
  
  public UIController() {
    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);
    stage.addActor(titleView);
    stage.addActor(optionsView);
    stage.addActor(levelSelectView);
    
    // Set up callbacks
    titleView.gameStartButton.addListener(gameStartListener);
    titleView.optionsButton.addListener(optionsListener);
    optionsView.backButton.addListener(backListener);
    levelSelectView.backButton.addListener(backListener);
    
    setUIState(UIState.TITLE);
  }
  
  // Button listeners
  private ChangeListener gameStartListener = new ChangeListener() {
    @Override
    public void changed(ChangeEvent event, Actor actor) {
      setUIState(UIState.LEVEL_SELECT);
    }
  };

  private ChangeListener backListener = new ChangeListener() {
    @Override
    public void changed(ChangeEvent event, Actor actor) {
      setUIState(UIState.TITLE);
    }
  };

  private ChangeListener optionsListener = new ChangeListener() {
    @Override
    public void changed(ChangeEvent event, Actor actor) {
      setUIState(UIState.OPTIONS);
    }
  };
  
  
  public void setUIState(UIState uiState) {
    if (uiState == state) {
      return;
    }
    
    state = uiState;
    
    titleView.setVisible(false);
    optionsView.setVisible(false);
    levelSelectView.setVisible(false);
    
    switch (state) {
    case LEVEL_SELECT:
      levelSelectView.setVisible(true);
      break;
    case OPTIONS:
      optionsView.setVisible(true);
      break;
    case TITLE:
      titleView.setVisible(true);
      break;
    }
  }
  
  
  public void update(float dt) {
    stage.act(dt);
  }
  
  public void draw() {
    stage.draw();
  }
}
