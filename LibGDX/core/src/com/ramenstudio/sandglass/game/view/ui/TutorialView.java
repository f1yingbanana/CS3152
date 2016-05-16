package com.ramenstudio.sandglass.game.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Displays a single-image tutorial screen.
 * 
 * @author Jiacong Xu
 */
public class TutorialView extends Table {
  
  private enum UIState {
    Showing, Standby, Dismissing, Dismissed
  }
  
  private UIState state = UIState.Showing;
  
  private Color bgcolor = new Color();
  
  private Skin skin;
  
  private float showCounter = 0;
  
  private final float ANIMATION_DURATION = .25f;
  
  public boolean isDismissed = false;
  
  public final Image tutorialImage;
  
  /**
   * Default constructor. Uses the given skin to set up the pause screen UI.
   * @param skin is the style-sheet for the widgets.
   */
  public TutorialView(Skin skin, int level) {
    super();
    
    this.skin = skin;
    
    setFillParent(true);
    setBackground(skin.newDrawable("white", bgcolor));
    
    
    // See if image file exists
    String path = "UI/Tutorials/tutorial" + level + ".png";
    boolean tutorialExists = Gdx.files.internal(path).exists();
    if (!tutorialExists) {
      state = UIState.Dismissed;
      isDismissed = true;
      tutorialImage = null;
    } else {
      tutorialImage = new Image(new Texture(Gdx.files.internal(path)));
      add(tutorialImage).expand();
    }
  }

  @Override
  public void act(float dt) {
    switch (state) {
    case Showing:
      showCounter = Math.min(showCounter + Math.min(dt, 0.1f), ANIMATION_DURATION);
      setBackground(skin.newDrawable("white", bgcolor));
      
      bgcolor.a = 0.6f * showCounter / ANIMATION_DURATION;
      
      if (showCounter >= ANIMATION_DURATION) {
        showCounter = 0;
        state = UIState.Standby;
      }
      
      break;
    case Standby:
      if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        state = UIState.Dismissing;
      }
      break;
    case Dismissing:
      showCounter = Math.min(showCounter + dt, ANIMATION_DURATION);
      setBackground(skin.newDrawable("white", bgcolor));
      
      bgcolor.a = 0.8f * (1 - showCounter / ANIMATION_DURATION);
      
      if (showCounter >= ANIMATION_DURATION) {
        showCounter = 0;
        state = UIState.Dismissed;
        isDismissed = true;
      }
      break;
    default:
      break;
    }
  }
}
