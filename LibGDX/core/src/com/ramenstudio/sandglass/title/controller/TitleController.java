package com.ramenstudio.sandglass.title.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.ramenstudio.sandglass.title.view.ui.LevelPreviewView;

/**
 * The main controller for title screen. This manages the level selection for
 * simplicity.
 */
public class TitleController {

  public Integer levelSelected;
  
  public UIController uiController = new UIController();
  
  public TitleController() {
    for (int i = 1; i < 10; i++) {
      LevelPreviewView view = uiController.levelSelectView.levelScrollView.addLevelView(i, "settings-100");
      view.levelSelectButton.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          levelSelected = view.level;
        }
      });
    }
  }
  
  public void update(float dt) {
    uiController.update(dt);
  }
  
  public void draw() {
    uiController.draw();
  }
  
  
}
