package com.ramenstudio.sandglass.title.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LevelPreviewView extends Table {
  public final int level;
  
  public final Image previewSprite;
  
  public final Label levelNameLabel;
  
  public final Button levelSelectButton;
  
  /**
   * Constructor: creates a new level preview view with the given skin, level
   * and level preview image name.
   * @param skin
   * @param level
   * @param imageStyle
   */
  public LevelPreviewView(Skin skin, int level, String imagePath) {
    // We just make it such that we have left image followed by right level info
    super();
    
    this.level = level;
    
    // Add preview sprite.
    previewSprite = new Image(new Texture(Gdx.files.internal(imagePath)));
    add(previewSprite).pad(20);
    
    levelNameLabel = new Label("Level " + level, skin);
    add(levelNameLabel).pad(20);
    
    levelSelectButton = new Button(skin);
    addActor(levelSelectButton);
  }
}
