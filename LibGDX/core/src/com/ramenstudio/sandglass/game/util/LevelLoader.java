package com.ramenstudio.sandglass.game.util;

import java.util.ArrayList;
import java.util.Dictionary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.ramenstudio.sandglass.game.model.GameObject;

/**
 * Takes a tile map file and parses all the tiles into different layers with
 * collisions.
 * 
 * @author Jiacong Xu
 */
public class LevelLoader {
  public enum Layer {
    BACKGROUND, FOREGROUND, PLAYER, CAMERA, OBSTACLE
  }
  
  public TiledMap tiledMap;
  
  public Dictionary<Layer, ArrayList<GameObject>> loadLevel(String filename) {
    tiledMap = new TmxMapLoader().load("Levels/" + filename);
    
    return null;
  }
}
