package com.ramenstudio.sandglass.title.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.ramenstudio.sandglass.util.Constants;
import com.ramenstudio.sandglass.util.view.ui.KeyboardControlButton;

public class LevelPreviewView extends Table {
  public final int level;
  
  public final Image previewSprite;

  public final Label levelNumberLabel;
  
  public final Label levelNameLabel;
  
  public final KeyboardControlButton levelSelectButton;
  
  public final Label scoreLabel;
  
  public final Image focusCircle;

  private final Drawable unselectedPreview;
  private final Drawable selectedPreview;
  
  /**
   * Constructor: creates a new level preview view with the given skin, level
   * and level preview image name.
   * @param skin
   * @param level
   * @param imageStyle
   */
  public LevelPreviewView(Skin skin, int level) {
    // We just make it such that we have left image followed by right level info
    super();
    
    this.level = level;
    
    // Add preview sprite.
    levelSelectButton = new KeyboardControlButton("", skin, "whiteNormal");
    addActor(levelSelectButton);
    
    // For identifying which level we are selecting
    levelSelectButton.setTag(level);

    String imageOnPath = "UI/LevelPreviews/LevelSelected-" + level + ".png";
    String imageOffPath = "UI/LevelPreviews/Level-" + level + ".png";

    unselectedPreview = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(imageOffPath))));
    selectedPreview = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(imageOnPath))));
    
    previewSprite = new Image(unselectedPreview);
    
    add(previewSprite).pad(20);

    String levelName = "UNTITLED";
    
    if (Constants.LEVEL_NAMES.length >= level) {
      levelName = Constants.LEVEL_NAMES[level - 1].toUpperCase();
    }
    levelNameLabel = new Label(levelName, skin);
    levelNumberLabel = new Label("LEVEL " + level, skin, "whiteNormal");
    
    Table texts = new Table();
    texts.add(levelNumberLabel).right().padLeft(240).padTop(65);
    texts.row();
    texts.add(levelNameLabel).size(280, 60).left().padLeft(20);
    
    add(texts).width(360);
    
    focusCircle = new Image(new Texture(Gdx.files.internal("UI/LevelSelectHighlight.png")));
    focusCircle.setVisible(false);
    focusCircle.setSize(400f, 400f);
    addActor(focusCircle);
    
    scoreLabel = new Label("", skin, "gold");
    scoreLabel.setAlignment(Align.center);
    scoreLabel.setVisible(false);
    scoreLabel.setSize(200f, 60f);
    
    addActor(scoreLabel);
  }
  
  @Override
  public void act(float dt) {
    // Reposition
    focusCircle.setPosition(previewSprite.getX() - 100f, previewSprite.getOriginY() - 100f);
    scoreLabel.setPosition(previewSprite.getX(), previewSprite.getY() - 75f);
    
    setFocus(levelSelectButton.isFocused());
    
    updateScore();
  }
  
  /**
   * Sets whether this object should be focused on.
   * @param inFocus
   */
  public void setFocus(boolean inFocus) {
    
    focusCircle.setVisible(inFocus);
    scoreLabel.setVisible(inFocus);
    
    if (inFocus) {
      previewSprite.setDrawable(selectedPreview);
    } else {
      previewSprite.setDrawable(unselectedPreview);
    }
  }
  
  private void updateScore() {
    Preferences prefs = Gdx.app.getPreferences(Constants.PREF_FILE_PATH);
    
    String score = "--:--";
    
    int total = prefs.getInteger("highScore" + level);
    
    if (total != 0) {
      int minutes = total / 60;
      int seconds = total % 60;
      String secText = "" + seconds;
      
      if (seconds < 10) {
        secText = "0" + secText;
      }
      
      score = "" + minutes + ":" + secText;
    }
    
    scoreLabel.setText(score);
  }
}
