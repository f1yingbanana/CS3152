package com.ramenstudio.sandglass.util.view.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

/**
 * An instance of keyboard controlled button.
 * @author Jiacong Xu
 */
public class KeyboardControlButton extends Label implements KeyboardControlUI {
  
  private Drawable focusBG;

  private LabelStyle cachedStyle;
  
  /*
   * This is the basic implementation of keyboard control. Copy and paste this
   * stack for other classes.
   */
  
  private KeyboardControlUI arrowUp;
  private KeyboardControlUI arrowDown;
  private KeyboardControlUI arrowLeft;
  private KeyboardControlUI arrowRight;
  
  private int tag = 0;
  
  private List<KeyboardUIListener> listeners = new ArrayList<KeyboardUIListener>();
  
  public KeyboardControlButton(CharSequence text, Skin skin, String styleName) {
    super(text, skin, styleName);

    setAlignment(Align.center);
    focusChanged(false);
  }

  @Override
  public KeyboardControlUI getArrowUp() {
    return arrowUp;
  }

  @Override
  public KeyboardControlUI getArrowDown() {
    return arrowDown;
  }

  @Override
  public KeyboardControlUI getArrowLeft() {
    return arrowLeft;
  }

  @Override
  public KeyboardControlUI getArrowRight() {
    return arrowRight;
  }

  
  @Override
  public void setArrowUp(KeyboardControlUI element) {
    arrowUp = element;
  }

  @Override
  public void setArrowDown(KeyboardControlUI element) {
    arrowDown = element;
  }

  @Override
  public void setArrowLeft(KeyboardControlUI element) {
    arrowLeft = element;
  }

  @Override
  public void setArrowRight(KeyboardControlUI element) {
    arrowRight = element;
  }
  
  @Override
  public void interacted() {
    for (KeyboardUIListener l : listeners) {
      l.interacted();
    }
  }
  
  public void addListener(KeyboardUIListener l) {
    listeners.add(l);
  }
  
  public boolean removeListener(KeyboardUIListener l) {
    return listeners.remove(l);
  }
  
  @Override
  public int tag() {
    return tag;
  }
  
  @Override
  public void setTag(int t) {
    tag = t;
  }
  
  // This is the custom appearance stuff!

  @Override
  public void focusChanged(boolean isFocused) {
    // For this project, let's just change our background to something else if 
    // focused.
    
    // First copy the style
    if (cachedStyle == null) {
      cachedStyle = new LabelStyle();
      cachedStyle.font = getStyle().font;
      cachedStyle.fontColor = getStyle().fontColor;
      cachedStyle.background = getStyle().background;
      focusBG = getStyle().background;
      setStyle(cachedStyle);
    }
    
    if (isFocused) {
      getStyle().background = focusBG;
    } else {
      getStyle().background = null;
    }
  }
}
