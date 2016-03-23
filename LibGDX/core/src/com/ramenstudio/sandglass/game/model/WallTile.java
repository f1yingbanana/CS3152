package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * A wall tile that has a specific collision fixture set up.
 * 
 * @author Jiacong Xu
 */
public class WallTile extends GameObject {
  // Thickness of the border.
  private static final float THICKNESS = 0.1f;
  private static final float TILE_WIDTH = 1.0f;
  
  public enum WallType {
    TOPLEFT, HORIZONTAL, TOPRIGHT, VERTICAL, BOTLEFT, BOTRIGHT, 
    INSIDE_TOPLEFT, INSDIE_BOTLEFT, INSIDE_TOPRIGHT, INSIDE_BOTRIGHT
  }
  
  /**
   * Default constructor. Creates a tile with collision corresponding to the 
   * given wall type.
   * 
   * @param type specifies fixture geometry.
   */
  public WallTile(WallType type) {
    getBodyDef().type = BodyDef.BodyType.StaticBody;
    
    PolygonShape shape = new PolygonShape();
    Vector2 c = new Vector2();
    
    switch (type) {
    case TOPLEFT:
      fixtureDefs = new FixtureDef[2];

      fixtureDefs[0] = new FixtureDef();
      c.x = (TILE_WIDTH - THICKNESS) / 4;
      shape.setAsBox((TILE_WIDTH + THICKNESS) / 4, THICKNESS / 2, c, 0);
      fixtureDefs[0].shape = shape;
      
      fixtureDefs[1] = new FixtureDef();
      c.x = 0;
      c.y = -(TILE_WIDTH - THICKNESS) / 4;
      shape = new PolygonShape();
      shape.setAsBox(THICKNESS / 2, (TILE_WIDTH + THICKNESS) / 4, c, 0);
      fixtureDefs[1].shape = shape;
      break;
    case BOTLEFT:
      fixtureDefs = new FixtureDef[2];

      fixtureDefs[0] = new FixtureDef();
      c.x = (TILE_WIDTH - THICKNESS) / 4;
      shape.setAsBox((TILE_WIDTH + THICKNESS) / 4, THICKNESS / 2, c, 0);
      fixtureDefs[0].shape = shape;
      
      fixtureDefs[1] = new FixtureDef();
      c.x = 0;
      c.y = (TILE_WIDTH - THICKNESS) / 4;
      shape = new PolygonShape();
      shape.setAsBox(THICKNESS / 2, (TILE_WIDTH + THICKNESS) / 4, c, 0);
      fixtureDefs[1].shape = shape;
      break;
    case BOTRIGHT:
      fixtureDefs = new FixtureDef[2];

      fixtureDefs[0] = new FixtureDef();
      c.x = -(TILE_WIDTH - THICKNESS) / 4;
      shape.setAsBox((TILE_WIDTH + THICKNESS) / 4, THICKNESS / 2, c, 0);
      fixtureDefs[0].shape = shape;
      
      fixtureDefs[1] = new FixtureDef();
      c.x = 0;
      c.y = (TILE_WIDTH - THICKNESS) / 4;
      shape = new PolygonShape();
      shape.setAsBox(THICKNESS / 2, (TILE_WIDTH + THICKNESS) / 4, c, 0);
      fixtureDefs[1].shape = shape;
      break;
    case TOPRIGHT:
      fixtureDefs = new FixtureDef[2];

      fixtureDefs[0] = new FixtureDef();
      c.x = -(TILE_WIDTH - THICKNESS) / 4;
      shape.setAsBox((TILE_WIDTH + THICKNESS) / 4, THICKNESS / 2, c, 0);
      fixtureDefs[0].shape = shape;
      
      fixtureDefs[1] = new FixtureDef();
      c.x = 0;
      c.y = -(TILE_WIDTH - THICKNESS) / 4;
      shape = new PolygonShape();
      shape.setAsBox(THICKNESS / 2, (TILE_WIDTH + THICKNESS) / 4, c, 0);
      fixtureDefs[1].shape = shape;
      break;
    case HORIZONTAL:
      fixtureDefs = new FixtureDef[1];
      
      fixtureDefs[0] = new FixtureDef();
      shape.setAsBox(TILE_WIDTH / 2, THICKNESS / 2);
      fixtureDefs[0].shape = shape;
      break;
//    case BOT:
//      fixtureDefs = new FixtureDef[1];
//      
//      fixtureDefs[0] = new FixtureDef();
//      shape.setAsBox(TILE_WIDTH / 2, THICKNESS / 2);
//      fixtureDefs[0].shape = shape;
//      break;
    case VERTICAL:
      fixtureDefs = new FixtureDef[1];
      
      fixtureDefs[0] = new FixtureDef();
      shape.setAsBox(THICKNESS / 2, TILE_WIDTH / 2);
      fixtureDefs[0].shape = shape;
      break;
//    case RIGHT:
//      fixtureDefs = new FixtureDef[1];
//      
//      fixtureDefs[0] = new FixtureDef();
//      shape.setAsBox(THICKNESS / 2, TILE_WIDTH / 2);
//      fixtureDefs[0].shape = shape;
//      break;
    }
  }
}
