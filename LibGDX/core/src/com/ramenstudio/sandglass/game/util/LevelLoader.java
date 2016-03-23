package com.ramenstudio.sandglass.game.util;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.ramenstudio.sandglass.game.model.GameObject;
import com.ramenstudio.sandglass.game.model.TurnTile;
import com.ramenstudio.sandglass.game.model.WallTile;

/**
 * Takes a tile map file and parses all the tiles into different layers with
 * collisions.
 * 
 * @author Jiacong Xu
 */
public class LevelLoader {
  public enum Key {
    PLAYER, GROUND, MONSTER, GATE, RESOURCE
  }
  
  public TiledMap tiledMap;
  
  public HashMap<Key, ArrayList<GameObject>> loadLevel(String filename) {
    tiledMap = new TmxMapLoader().load("Levels/" + filename);
    HashMap<Key, ArrayList<GameObject>> layerDict = new HashMap<Key, ArrayList<GameObject>>();
    TiledMapTileLayer groundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Ground");
    TiledMapTileLayer objectLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Objects");
    
    ArrayList<GameObject> Tilearr = parseGround(groundLayer, "Collision");
    ArrayList<GameObject> playerTile = parseObject(objectLayer, "type", "player");
    ArrayList<GameObject> monsterTile = parseObject(objectLayer, "type", "monster");
    ArrayList<GameObject> resourceTile = parseObject(objectLayer, "type", "resource");
    
    layerDict.put(Key.GROUND, Tilearr);
    layerDict.put(Key.PLAYER, playerTile);
    layerDict.put(Key.MONSTER, monsterTile);
    layerDict.put(Key.RESOURCE, resourceTile);
    return layerDict;
  }
  
  
/**
 * returns Array of collidable wallTile objects
 * 
 * @param cellArr cell Array parsed from tmx file with properties
 * @param str wanted property value, like CollisionTile
 * @return ArrayList of walltiles that are classified as edges, i.e. collidable.
 */
  public ArrayList<GameObject> parseGround(TiledMapTileLayer layer, String str){
	  int height = layer.getHeight();
	  int width = layer.getWidth();
	  ArrayList<GameObject> tArr = new ArrayList<GameObject>();
	  
	  for (int i = 0 ; i < height ; i++){
		  for (int j = 0 ; j < width; j ++){
			  TiledMapTile this_tile = layer.getCell(i, j).getTile();
			  if (this_tile.getProperties().containsKey(str)){
				  String propval = (String) this_tile.getProperties().get(str);
				  WallTile.WallType type = WallTile.WallType.HORIZONTAL;
				  TurnTile tt= new TurnTile();
				  switch (propval){
					  
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
						  type = WallTile.WallType.HORIZONTAL;
						  break;
					  case "vert_edge": 
						  type = WallTile.WallType.VERTICAL;
						  break;
					  case "inside_top_left":
						  type = WallTile.WallType.INSIDE_TOPLEFT;
						  tt.getBodyDef().position.set(new Vector2(i-0.25f,j-0.25f));
						  tArr.add(tt);
						  break;
					  case "inside_top_right":
						  type = WallTile.WallType.INSIDE_TOPRIGHT;
						  tt.getBodyDef().position.set(new Vector2(i-0.25f,j+0.25f));
						  tArr.add(tt);
						  break;
					  case "inside_bottom_left":
						  type = WallTile.WallType.INSDIE_BOTLEFT;
						  tt.getBodyDef().position.set(new Vector2(i+0.25f,j-0.25f));
						  tArr.add(tt);
						  break;
					  case "inside_bottom_right":
						  type = WallTile.WallType.INSIDE_BOTRIGHT;
						  tt.getBodyDef().position.set(new Vector2(i+0.25f,j+0.25f));
						  tArr.add(tt);
						  break;
				  }
				  
				  WallTile wt = new WallTile(type);
				  wt.getBodyDef().position.set(new Vector2(i+0.5f, j+0.5f));
				  tArr.add(wt);
			  }
		  }
	  }
	  return tArr;
  }
  
  public ArrayList<GameObject> parseObject(TiledMapTileLayer layer, String key, String value){
	  int height = layer.getHeight();
	  int width = layer.getWidth();
	  ArrayList<GameObject> objArr = new ArrayList<GameObject>();
	  
	  for (int i = 0; i < width; i++ ){
		  for (int j = 0; j < height; j++){
			  TiledMapTile this_tile = layer.getCell(i, j).getTile();
			  if (this_tile.getProperties().containsKey(key)){
				  if (this_tile.getProperties().get(key) == value){
					  GameObject object = new GameObject();
					  object.getBodyDef().position.set(new Vector2(i+0.5f, j+0.5f));
					  objArr.add(object);
				  }
			  }
		  }
	  }
	  return objArr;
  }
}
