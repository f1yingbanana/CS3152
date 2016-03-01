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
   * A more complex post-processing script. We show that post-processing can
   * even change the mesh vertices. This first reconstruct the mesh so triangles
   * share no vertices. Then Each triangle is assigned color variations.
   */
  public class DistinctTriangle : Postprocessing {
    public Color mainColor = new Color(0, 0.71f, 1, 1);
    public float hueVariation = 0.05f;
    public float satuationVariation = 0.1f;
    public float valueVariation = 0.2f;

    public float gradientVariation = 0.2f;

    public override Mesh Postprocess(Mesh generated, List<Shape> paths, List<Shape> holes) {
      // We want to flatten out all vertices so triangles share no vertex.
      // This is the key to achieve distinct colors for each triangle.
      Vector3[] vertices = new Vector3[generated.triangles.Length];
      Color32[] colors = new Color32[generated.triangles.Length];
      int[] triangles = new int[generated.triangles.Length];

      // Loop through each triangle and reconstruct the vertices.
      for (int i = 0; i < generated.triangles.Length; i += 3) {
        vertices[i] = generated.vertices[generated.triangles[i]];
        vertices[i + 1] = generated.vertices[generated.triangles[i + 1]];
        vertices[i + 2] = generated.vertices[generated.triangles[i + 2]];
        triangles[i] = i;
        triangles[i + 1] = i + 1;
        triangles[i + 2] = i + 2;

        // For color, we first find a variation of the main color. HSV is better
        // a model for such a task.
        float h, s, v;
        Color.RGBToHSV(mainColor, out h, out s, out v);
        h += (Random.value - 0.5f) * hueVariation;
        s += (Random.value - 0.5f) * satuationVariation;
        v += (Random.value - 0.5f) * valueVariation;
        Color32 vertexColor = Color.HSVToRGB(h, s, v);
        colors[i] = vertexColor;

        // We want the other two vertices to have slightly different colors.
        // We can simply darken the vertex color. That is, reduce its value.
        colors[i + 1] = Color.HSVToRGB(h, s, v - gradientVariation * Random.value);
        colors[i + 2] = Color.HSVToRGB(h, s, v - gradientVariation * Random.value);
      }

      generated.vertices = vertices;
      generated.triangles = triangles;
      generated.colors32 = colors;

      return generated;
    }
  }
}
