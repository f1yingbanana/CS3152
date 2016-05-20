package com.ramenstudio.sandglass.title.view.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
  
  private final Image header;
  private final Image footer;
  
  /**
   * Default constructor. Initializes all the UI components with the given skin.
   */
  public LevelScrollView(Skin skin) {
    super();
    
    cachedSkin = skin;
    
    header = new Image();
    footer = new Image();

    add(header).size(800, Gdx.graphics.getHeight() / 2.0f - 150f);
  }
  
  /**
   * Adds a level with given information, and makes it visible.
   * @param level is the order of the level in all the levels. Starting from 1.
   * @return the newly created level preview view.
   */
  public LevelPreviewView addLevelView(int level) {
    final LevelPreviewView preview = new LevelPreviewView(cachedSkin, level);
    row();
    add(preview).size(800, 200).padRight(10).padLeft(10).padTop(50).padBottom(50);
    preview.levelSelectButton.setSize(800, 200);
    levelPreviewViews.add(level - 1, preview);
    
    return preview;
  }
  
  public void doneAdding() {
    row();
    add(footer).size(800, Gdx.graphics.getHeight() / 2.0f - 150f);
  }
  
}
