package com.ramenstudio.sandglass.title.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.ramenstudio.sandglass.title.view.ui.LevelPreviewView;

/**
 * The main controller for title screen. This manages the level selection for
 * simplicity.
 */
public class TitleController {

  private static final int totalLevels = 16;
  
  public Integer levelSelected;
  
  public UIController uiController = new UIController();
  
  public TitleController() {
    for (int i = 1; i <= totalLevels; i++) {
      String previewPath = "UI/LevelPreviews/level" + i + ".png";
      LevelPreviewView view = uiController.levelSelectView.levelScrollView.addLevelView(i, previewPath);
      view.levelSelectButton.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          levelSelected = view.level;
        }
      });
    }
    
    uiController.titleView.quitButton.addListener(quitButtonListener);
    uiController.titleView.gameStartButton.addListener(gameStartListener);
  }

  private ChangeListener gameStartListener = new ChangeListener() {
    @Override
    public void changed(ChangeEvent event, Actor actor) {
      levelSelected = 1;
    }
  };
  
  private ChangeListener quitButtonListener = new ChangeListener() {
    @Override
    public void changed(ChangeEvent event, Actor actor) {
      Gdx.app.exit();
    }
  };
  
  public void update(float dt) {
    uiController.update(dt);
  }
  
  public void draw() {
    uiController.draw();
  }
  
  
}
