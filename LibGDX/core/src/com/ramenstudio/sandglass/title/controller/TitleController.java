package com.ramenstudio.sandglass.title.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.ramenstudio.sandglass.title.view.ui.LevelPreviewView;
import com.ramenstudio.sandglass.util.view.ui.KeyboardUIListener;

/**
 * The main controller for title screen. This manages the level selection for
 * simplicity.
 */
public class TitleController {

  private static final int totalLevels = 23;
  
  public Integer levelSelected;
  
  public UIController uiController = new UIController();
  
  public TitleController() {
    LevelPreviewView last = null;
    for (int i = 1; i <= totalLevels; i++) {
      String previewPath = "UI/LevelPreviews/level" + i + ".png";
      LevelPreviewView view = uiController.levelSelectView.levelScrollView.addLevelView(i, previewPath);
      
      if (last != null) {
        last.levelSelectButton.setArrowDown(view.levelSelectButton);
        view.levelSelectButton.setArrowUp(last.levelSelectButton);
      }
      
      view.levelSelectButton.setArrowLeft(uiController.levelSelectView.backButton);
      
      view.levelSelectButton.addListener(new KeyboardUIListener() {
        @Override
        public void interacted() {
          levelSelected = view.level;
        }
      });
      
      last = view;
    }
    
    uiController.titleView.quitButton.addListener(quitButtonListener);
    uiController.titleView.gameStartButton.addListener(gameStartListener);
  }

  private KeyboardUIListener gameStartListener = new KeyboardUIListener() {
    @Override
    public void interacted() {
      levelSelected = 1;
    }
  };
  
  private KeyboardUIListener quitButtonListener = new KeyboardUIListener() {
    @Override
    public void interacted() {
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
