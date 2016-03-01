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
   * Processes each vertex and randomly assign a color. Then lerps the color 
   * with respect to distance from edge.
   */
  public class AlternateFade : Postprocessing {
    public Color mainColor = new Color(0.25f, 0.04f, 0.04f, 1);
    public Color suppColor = new Color(0.4275f, 0.2039f, 0.0588f, 1);
    public Color maskColor = Color.black;
    public float maskStrength = 0.05f;

    public override Mesh Postprocess(Mesh generated, List<Shape> paths, List<Shape> holes) {
      Color32[] colors = new Color32[generated.vertices.Length];

      // Loop through each vertex      
      for (int i = 0; i < generated.vertices.Length; i++) {
        float dist = DistToEdge(generated.vertices[i], paths, holes);
        Color32 vertexColor = Random.value > 0.5f? mainColor : suppColor;
        colors[i] = Color32.Lerp(vertexColor, maskColor, dist * maskStrength);
      }

      generated.colors32 = colors;

      return generated;
    }
  }
}
