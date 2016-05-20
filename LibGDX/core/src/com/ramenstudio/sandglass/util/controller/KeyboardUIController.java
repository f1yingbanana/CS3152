package com.ramenstudio.sandglass.util.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ramenstudio.sandglass.util.view.ui.KeyboardControlUI;

/**
 * A controller that takes in player's various inputs and controls the UI.
 * 
 * @author Jiacong Xu
 */
public class KeyboardUIController {
  /**
   * This should be assigned a default first selected element at the start of
   * a given screen.
   */
  private KeyboardControlUI focusedUI;
  
  public boolean enabled = true;
  
  public void update(float dt) {
    if (!enabled) {
      return;
    }
    
    if (getFocusedUI() == null) {
      return;
    }
    // Parse user input
    
    if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
      //System..out.println("UP");
      if (focusedUI.getArrowUp() != null) {
        setFocusedUI(getFocusedUI().getArrowUp());
        SoundController.getInstance().playUIMove();
      }
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
      //System..out.println("DOWN");
      //System..out.println(getFocusedUI().getArrowDown());
      if (getFocusedUI().getArrowDown() != null) {
        setFocusedUI(getFocusedUI().getArrowDown());
        SoundController.getInstance().playUIMove();
      }
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
      if (getFocusedUI().getArrowLeft() != null) {
        setFocusedUI(getFocusedUI().getArrowLeft());
        SoundController.getInstance().playUIMove();
      }
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
      if (getFocusedUI().getArrowRight() != null) {
        setFocusedUI(getFocusedUI().getArrowRight());
        SoundController.getInstance().playUIMove();
      }
    }
    
    if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
      getFocusedUI().interacted();
      SoundController.getInstance().playUIEnter();
    }
  }

  /**
   * @return the focusedUI
   */
  public KeyboardControlUI getFocusedUI() {
    return focusedUI;
  }

  /**
   * @param focusedUI the focusedUI to set
   */
  public void setFocusedUI(KeyboardControlUI focusedUI) {
    if (this.focusedUI != null) {
      //System..out.println(this.focusedUI);
      this.focusedUI.focusChanged(false);
    }
    
    this.focusedUI = focusedUI;
    this.focusedUI.focusChanged(true);
    //System..out.println(focusedUI);
  }
}
