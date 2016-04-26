package com.ramenstudio.sandglass.game.util;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ramenstudio.sandglass.game.model.GameObject;
import com.ramenstudio.sandglass.game.model.Gate;
import com.ramenstudio.sandglass.game.model.Monster;
import com.ramenstudio.sandglass.game.model.Monster.MonsterLevel;
import com.ramenstudio.sandglass.game.model.Player;
import com.ramenstudio.sandglass.game.model.Resource;
import com.ramenstudio.sandglass.game.model.ShipPiece;
import com.ramenstudio.sandglass.game.model.TurnTile;
import com.ramenstudio.sandglass.game.model.WallTile;

/**
 * Takes a tile map file and parses all the tiles into different layers with
 * collisions.
 * 
 * @author Jiacong Xu
 */
public class LevelLoader {
	public enum LayerKey {
		PLAYER, GROUND, GATE, RESOURCE, MONSTER, SHIP
	}
	/** tiledMap to be used*/
	public TiledMap tiledMap;
	/** center of the map*/
	public Vector2 center;
	/** max Flip per level*/
	public int maxFlip;
	/** zoom factor for level*/
	public float zoom;
	/** bound for each level*/
	public Rectangle bound;

	public Map<LayerKey, Array<GameObject>> loadLevel(int level) {
		return loadLevel("level"+level+".tmx");
	}

	public Map<LayerKey, Array<GameObject>> loadLevel(String filename) {
		tiledMap = new TmxMapLoader().load("Levels/" + filename);
		Map<LayerKey, Array<GameObject>> layerDict = new HashMap<LayerKey, Array<GameObject>>();
		TiledMapTileLayer groundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Ground");
		TiledMapTileLayer objectLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Object");

		Array<GameObject> Tilearr = parseGround(groundLayer, "Collision");
		Array<GameObject> playerTile = parseObject(objectLayer, "type", "player");
		MapLayer centerLayer = (MapLayer) tiledMap.getLayers().get("Center");
		MapObject center = centerLayer.getObjects().get("center");
		MapObject bound = centerLayer.getObjects().get("bound");
		
		// setting fields to pass around
		this.bound = getBounds(bound);
		maxFlip = getFlipNumber(center);
		this.center = getCenter(center);
		zoom = getZoom(center);

		MapLayer monster = (MapLayer) tiledMap.getLayers().get("Monster");
		MapLayer path = (MapLayer) tiledMap.getLayers().get("Path");
		Array<GameObject> mArr = parseMonster(monster, path);
		Array<GameObject> gateTile = parseObject(objectLayer, "type", "gate");
		Array<GameObject> shipTile = parseObject(objectLayer, "type", "ship");
		Array<GameObject> resourceTile = parseObject(objectLayer, "type", "resource");

		tiledMap.getLayers().remove(objectLayer);
		layerDict.put(LayerKey.GROUND, Tilearr);
		layerDict.put(LayerKey.PLAYER, playerTile);
		layerDict.put(LayerKey.MONSTER, mArr);
		layerDict.put(LayerKey.RESOURCE, resourceTile);
		layerDict.put(LayerKey.SHIP, shipTile);
		layerDict.put(LayerKey.GATE, gateTile);
		return layerDict;
	}
	
	private Rectangle getBounds(MapObject bound){
		
		Rectangle rec = ((RectangleMapObject) bound).getRectangle();
		rec.setSize(Math.round(rec.width/128),Math.round(rec.height/128));
		rec.setPosition(Math.round(rec.x/128),Math.round(rec.y/128));
		return rec;
	}

	/**@return the number of flips allowed for the level indicated by n
	 * @param filename level file name*/
	private int getFlipNumber(MapObject center){
		return Integer.parseInt((String)center.getProperties().get("maxFlips"));
	}

	private float getZoom(MapObject center) {
		float longest = Float.parseFloat((String)center.getProperties().get("longest"));
		return longest/9.0f;
	}

	private Vector2 getCenter(MapObject center){
		return new Vector2(Float.parseFloat((String)center.getProperties().get("X")),
				32-Float.parseFloat((String)center.getProperties().get("Y")));
	}

	/**
	 * returns Array of collidable wallTile objects
	 * 
	 * @param cellArr cell Array parsed from tmx file with properties
	 * @param str wanted property value, like CollisionTile
	 * @return ArrayList of walltiles that are classified as edges, i.e. collidable.
	 */
	public Array<GameObject> parseGround(TiledMapTileLayer layer, String str){
		int height = layer.getHeight();
		int width = layer.getWidth();
		Array<GameObject> tArr = new Array<GameObject>();

		for (int i = 0 ; i < height ; i++){
			for (int j = 0 ; j < width; j ++){
				if (layer.getCell(i, j) == null)
					continue;
				TiledMapTile this_tile = layer.getCell(i, j).getTile();
				if (this_tile != null && this_tile.getProperties().containsKey(str)) {
					String propval = (String) this_tile.getProperties().get(str);
					WallTile.WallType type;
					TurnTile tt= new TurnTile();
					
					float boxSize = .15f;

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
						tt.getBodyDef().position.set(new Vector2(i+0.5f+boxSize,j+0.5f-boxSize));
						tArr.add(tt);
						break;
					case "inside_top_right":
						type = WallTile.WallType.BOTLEFT;
						tt.getBodyDef().position.set(new Vector2(i+0.5f-boxSize,j+0.5f-boxSize));
						tArr.add(tt);
						break;
					case "inside_bottom_left":
						type = WallTile.WallType.TOPRIGHT;
						tt.getBodyDef().position.set(new Vector2(i+0.5f+boxSize,j+0.5f+boxSize));
						tArr.add(tt);
						break;
					case "inside_bottom_right":
						type = WallTile.WallType.TOPLEFT;
						tt.getBodyDef().position.set(new Vector2(i+0.5f-boxSize,j+0.5f+boxSize));
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

	public Array<GameObject> parseObject(TiledMapTileLayer layer, String key, String value){
		if (layer == null)
			return null;

		int height = layer.getHeight();
		int width = layer.getWidth();
		Array<GameObject> objArr = new Array<GameObject>();
		for (int i = 0; i < width; i++ ){
			for (int j = 0; j < height; j++){
				if (layer.getCell(i, j)!=null){
					TiledMapTile this_tile = layer.getCell(i, j).getTile();
					if (this_tile.getProperties().containsKey(key)){
						if (((String)this_tile.getProperties().get(key)).equals(value)){
							if (value.equals("player")){
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
							else if (value.equals("resource")){
								Resource resource = new Resource(new Vector2(i+0.5f, j+0.5f));
								objArr.add(resource);
							}
						}
					}
				}
			}
		}
		return objArr;
	}

	public Array<GameObject> parseMonster(MapLayer monLayer, MapLayer pathLayer){
		Array<PolylineMapObject> polys = 
				pathLayer.getObjects().getByType(PolylineMapObject.class);
		Array<GameObject> objArr = new Array<GameObject>(); 

		for (PolylineMapObject p : polys){
			MapObject monster = monLayer.getObjects().get(p.getName());
			Vector2 initPos = new Vector2(Float.parseFloat((String)monster.getProperties().get("X"))/128+0.5f,
					32-Float.parseFloat((String)monster.getProperties().get("Y"))/128+0.5f);
			int id = Integer.parseInt(p.getName());
			MonsterLevel level = MonsterLevel.valueOf((String) monster.getProperties().get("level"));
			float spcf = Float.parseFloat((String) monster.getProperties().get("spcf"));
			Array<Vector2> vertices = new Array<Vector2>();
			float[] vert = p.getPolyline().getVertices();
			for (int i = 0 ; i < vert.length-1 ; i = i + 2){
				Vector2 v = new Vector2((float) Math.round(vert[i]/128)+initPos.x, 
						(float) Math.round(vert[i+1]/128)+initPos.y);
				//		  		System.out.println(v.toString());
				vertices.add(v);
			}
			String angle = (String) monster.getProperties().get("angle");
			Monster mon = new Monster(initPos, id, level, spcf, vertices,angle);
			objArr.add(mon);
		}
		return objArr;
	}
}
