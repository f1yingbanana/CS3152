package com.ramenstudio.sandglass.title.view.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * This is the entire table of levels, their respective icons, and info. This is
 * contained in a scroll pane in the LevelScrollView.
 * @author Jiacong Xu
 */
public class LevelScrollView extends Table {
  public final List<LevelPreviewView> levelPreviewViews = new ArrayList<LevelPreviewView>();
  
  private Skin cachedSkin;
  
  /**
   * Default constructor. Initializes all the UI components with the given skin.
   */
  public LevelScrollView(Skin skin) {
    super();
    
    cachedSkin = skin;
  }
  
  /**
   * Adds a level with given information, and makes it visible.
   * @param level is the order of the level in all the levels. Starting from 1.
   * @param imageStyle is the string defining the preview image for the level.
   * @return the newly created level preview view.
   */
  public LevelPreviewView addLevelView(int level, String imageStyle) {
    final LevelPreviewView preview = new LevelPreviewView(cachedSkin, level, imageStyle);
    row();
    add(preview).size(600, 200).pad(40);
    preview.levelSelectButton.setSize(600, 200);
    levelPreviewViews.add(level - 1, preview);
    
    return preview;
  }
  
  
}
