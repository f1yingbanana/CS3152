/**
 * PolyShape
 * 
 * Created: 28-Jan-2016
 * Author(s): Flying_Banana
 */

namespace PolyShape {
  using UnityEngine;
  using System.Collections.Generic;

  /**
   * The parent class for all postprocessing that goes with PolyShape. The 
   * script is called after the mesh has been generated. Extend this class and 
   * implement the Postprocess method to customize the mesh data. This class 
   * also contains some convenient methods for calculations.
   */
  public abstract class Postprocessing : MonoBehaviour {
    // Implement this method to modify the mesh after it is triangulated!
    public abstract Mesh Postprocess(Mesh generated, List<Shape> paths, List<Shape> holes);

    // Helper methods

    // Returns the distance between vertex and the nearest edge.
    protected float DistToEdge(Vector2 vertex, List<Shape> paths, List<Shape> holes) {
      Vector2 startPt;
      Vector2 endPt;
      int iterations = 0;
      float minDist = Mathf.Infinity;

      List<Shape> combined = new List<Shape>(paths);
      combined.AddRange(holes);

      foreach (Shape shape in combined) {
        if (shape.vertices.Count < 3) {
          continue;
        }

        for (int i = 0; i < shape.vertices.Count; i++) {
          startPt = shape.vertices[i];
          endPt = shape.vertices[(i + 1) % shape.vertices.Count];
          iterations = (int)Mathf.Log(Vector2.Distance(startPt, endPt)) + 1;

          for (int j = 1; j <= iterations; j++) {
            float dist1 = Vector2.Distance(startPt, vertex);
            float dist2 = Vector2.Distance(endPt, vertex);

            if (dist2 > dist1) {
              endPt = new Vector2((startPt.x + endPt.x) / 2, (startPt.y + endPt.y) / 2);
            } else {
              startPt = new Vector2((startPt.x + endPt.x) / 2, (startPt.y + endPt.y) / 2);
            }
          }

          float thisDist = Vector2.Distance(startPt, vertex);

          if (thisDist < minDist) {
            minDist = thisDist;
          }
        }
      }

      return minDist;
    }
  }
}
