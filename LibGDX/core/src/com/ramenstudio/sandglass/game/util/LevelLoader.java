package com.ramenstudio.sandglass.game.util;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.ramenstudio.sandglass.game.model.GameObject;
import com.ramenstudio.sandglass.game.model.WallTile;

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
  
  
/**
 * returns Array of collidable wallTile objects
 * 
 * @param cellArr cell Array parsed from tmx file with properties
 * @param str wanted property value, like CollisionTile
 * @return ArrayList of walltiles that are classified as edges, i.e. collidable.
 */
  public ArrayList<WallTile> parseTile(ArrayList<Cell> cellArr, String str){
	  ArrayList<WallTile> wtArr = new ArrayList<WallTile>();
	  for (Cell c: cellArr){
		  if (c.property.containsKey(str)){
			  String propVal = c.property.get(str);
			  WallTile.WallType type = WallTile.WallType.TOP;
			  
			  switch (propVal){
				  
				  case "top_left": 
					  type = WallTile.WallType.TOPLEFT;
					  break;
				  case "bottom_left": 
					  type = WallTile.WallType.BOTLEFT;
					  break;
				  case "top_right": 
					  type = WallTile.WallType.TOPRIGHT;
					  break;
				  case "bottom_right": 
					  type = WallTile.WallType.BOTRIGHT;
					  break;
				  case "horiz_edge": 
					  type = WallTile.WallType.TOP;
					  break;
				  case "vert_edge": 
					  type = WallTile.WallType.LEFT;
					  break;
			  }
			  
			  WallTile wt = new WallTile(type);
			  wtArr.add(wt);
		  }
	  }
	  return wtArr;
  }
  
  private class Cell{
	  Hashtable<String, String> property;
  }
}
