package com.ramenstudio.sandglass.game.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.ramenstudio.sandglass.game.model.GameObject;
import com.ramenstudio.sandglass.game.model.Gate;
import com.ramenstudio.sandglass.game.model.Monster;
import com.ramenstudio.sandglass.game.model.Player;
import com.ramenstudio.sandglass.game.model.ShipPiece;
import com.ramenstudio.sandglass.game.model.TurnTile;
import com.ramenstudio.sandglass.game.model.WallTile;
import com.ramenstudio.sandglass.game.model.Monster.MType;
import com.ramenstudio.sandglass.game.model.Player;

/**
 * Takes a tile map file and parses all the tiles into different layers with
 * collisions.
 * 
 * @author Jiacong Xu
 */
public class LevelLoader {
  public enum LayerKey {
    PLAYER, GROUND, UNDER_M, OVER_M, GATE, RESOURCE
  }
  
  public TiledMap tiledMap;
  
  public Map<LayerKey, List<GameObject>> loadLevel(String filename) {
    tiledMap = new TmxMapLoader().load("Levels/" + filename);
    Map<LayerKey, List<GameObject>> layerDict = new HashMap<LayerKey, List<GameObject>>();
    TiledMapTileLayer groundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Ground");
    TiledMapTileLayer objectLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Objects");
   // System.out.println(objectLayer.getHeight() + ", " + objectLayer.getWidth());
    
    ArrayList<GameObject> Tilearr = parseGround(groundLayer, "Collision");
    ArrayList<GameObject> playerTile = parseObject(objectLayer, "type", "player");
    ArrayList<GameObject> umTile = parseObject(objectLayer, "type", "undermonster");
    ArrayList<GameObject> omTile = parseObject(objectLayer, "type", "overmonster");
    ArrayList<GameObject> resourceTile = parseObject(objectLayer, "type", "ship");
    ArrayList<GameObject> gateTile = parseObject(objectLayer, "type", "gate");
    
    tiledMap.getLayers().remove(objectLayer);
    layerDict.put(LayerKey.GROUND, Tilearr);
    layerDict.put(LayerKey.PLAYER, playerTile);
    layerDict.put(LayerKey.UNDER_M, umTile);
    layerDict.put(LayerKey.OVER_M, omTile);
    layerDict.put(LayerKey.RESOURCE, resourceTile);
    layerDict.put(LayerKey.GATE, gateTile);
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
        if (layer.getCell(i, j) == null)
          continue;
        TiledMapTile this_tile = layer.getCell(i, j).getTile();
        if (this_tile != null && this_tile.getProperties().containsKey(str)) {
          String propval = (String) this_tile.getProperties().get(str);
          WallTile.WallType type;
          TurnTile tt= new TurnTile();
          
          switch (propval) {
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
            case "top": 
              type = WallTile.WallType.TOP;
              break;
            case "left": 
              type = WallTile.WallType.LEFT;
              break;
            case "bottom": 
              type = WallTile.WallType.BOT;
              break;
            case "right": 
              type = WallTile.WallType.RIGHT;
              break;
            case "inside_top_left":
              type = WallTile.WallType.BOTRIGHT;
              tt.getBodyDef().position.set(new Vector2(i+0.55f,j+0.45f));
              tArr.add(tt);
              break;
            case "inside_top_right":
              type = WallTile.WallType.BOTLEFT;
              tt.getBodyDef().position.set(new Vector2(i+0.45f,j+0.45f));
              tArr.add(tt);
              break;
            case "inside_bottom_left":
              type = WallTile.WallType.TOPRIGHT;
              tt.getBodyDef().position.set(new Vector2(i+0.55f,j+0.55f));
              tArr.add(tt);
              break;
            case "inside_bottom_right":
              type = WallTile.WallType.TOPLEFT;
              tt.getBodyDef().position.set(new Vector2(i+0.45f,j+0.55f));
              tArr.add(tt);
              break;
            default:
              type = WallTile.WallType.TOP;
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
    if (layer == null)
      return null;
    
    int height = layer.getHeight();
    int width = layer.getWidth();
    ArrayList<GameObject> objArr = new ArrayList<GameObject>();
    
    for (int i = 0; i < width; i++ ){
      for (int j = 0; j < height; j++){
        if (layer.getCell(i, j)!=null){
            TiledMapTile this_tile = layer.getCell(i, j).getTile();
            if (this_tile.getProperties().containsKey(key)){
              if (((String)this_tile.getProperties().get(key)).equals(value)){
                if (value.equals("undermonster")){
                    int level = Integer.parseInt((String) this_tile.getProperties().get("level"));
                    String mType = (String) this_tile.getProperties().get("mType");
                    int span = Integer.parseInt((String) this_tile.getProperties().get("span"));
                    float spcf = Float.parseFloat((String) this_tile.getProperties().get("spcf"));
                    String angle = (String) this_tile.getProperties().get("angle");
                    Monster monster = new Monster(new Vector2(i+0.5f, j+0.5f), 
                            MType.valueOf(mType),level, span, spcf, angle);
                    
                    objArr.add(monster);
                }
                else if (value.equals("overmonster")){
                    int level = Integer.parseInt((String) this_tile.getProperties().get("level"));
                    String mType = (String) this_tile.getProperties().get("mType");
                    int span = Integer.parseInt((String) this_tile.getProperties().get("span"));
                    float spcf = Float.parseFloat((String) this_tile.getProperties().get("spcf"));
                    String angle = (String) this_tile.getProperties().get("angle");
                    String initMove = (String) this_tile.getProperties().get("initMove");
                    Monster monster = new Monster(new Vector2(i, j), 
                            MType.valueOf(mType),level, span, spcf, angle, initMove);
                    objArr.add(monster);
                }
                else if (value.equals("player")){
                    Player player = new Player(new Vector2(i+0.5f, j+0.5f));
                    objArr.add(player); 
                }
                else if (value.equals("gate")){
                	Gate gate = new Gate(new Vector2(i+0.5f, j+0.5f));
                	objArr.add(gate);
                }
                else if (value.equals("ship")){
                    ShipPiece ship = new ShipPiece(new Vector2(i+0.5f, j+0.5f));
                    objArr.add(ship);
                }
              }
            }
          }
      }
    }
    return objArr;
  }
}
