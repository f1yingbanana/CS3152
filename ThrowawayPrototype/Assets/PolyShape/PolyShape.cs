/**
 * PolyShape
 * 
 * Created: 25-Dec-2015
 * Author(s): Flying_Banana
 */

namespace PolyShape {
  using UnityEngine;
  using System.Collections;
  using System.Collections.Generic;
  using System;

  /**
   * The main script for handling mesh generation
   */
  [RequireComponent (typeof(MeshFilter))]
  public class PolyShape : MonoBehaviour {
  
    public List<Shape> paths = new List<Shape>();
    public List<Shape> holes = new List<Shape>();

    public Shape editingShape;

    // Editor variables
    public bool isGeometryFolded;
    public bool isMeshFolded;
    public bool isProcessingFolded;

    public bool shouldAutoRecalculate = true;
    public bool shouldGenerateCollision = true;

    public Color editingColor = new Color(24/255f, 225/255f, 0, 1);
    public Color normalColor = new Color(150/255f, 178/255f, 196/255f, 1);
    public float vertexSize = 0.04f;

    public float maxAngle = 20;
    public float maxArea = 100;

    public List<Preprocessing> preprocessing = new List<Preprocessing>();
    public List<Postprocessing> postprocessing = new List<Postprocessing>();

    public void GenerateCollision() {
      // Collision is easy. Just grab all the lines and add!
      PolygonCollider2D collider = GetComponent<PolygonCollider2D>();

      collider.pathCount = paths.Count + holes.Count;

      int counter = 0;

      foreach (Shape path in paths) {
        collider.SetPath(counter, path.vertices.ToArray());
        counter++;
      }

      foreach (Shape hole in holes) {
        collider.SetPath(counter, hole.vertices.ToArray());
        counter++;
      }
    }

    // Triggers a mesh rebuild
    public void RebuildMesh() {
      if (Application.isPlaying) {
        Debug.LogWarning("Mesh rebuilding is currently not supported within the player itself, and will not work in any platform except within Unity Editor!");
      }

      if (GetComponent<MeshFilter>() != null) {
        GetComponent<MeshFilter>().mesh = null;
      }

      // Building geometry from user defined paths

      // Preprocess paths
      List<List<Vector2>> pathsCopy = new List<List<Vector2>>();
      List<List<Vector2>> holesCopy = new List<List<Vector2>>();

      foreach (Shape path in paths) {
        pathsCopy.Add(new List<Vector2>(path.vertices));
      }

      foreach (Shape hole in holes) {
        holesCopy.Add(new List<Vector2>(hole.vertices));
      }

      for (int i = 0; i < preprocessing.Count; i++) {
        if (preprocessing[i] != null)
          preprocessing[i].Preprocess(ref pathsCopy, ref holesCopy);
      }

      // Construct graph and generate mesh
      PSLG pslg = new PSLG();

      foreach (List<Vector2> path in pathsCopy) {
        if (path.Count > 2)
          pslg.AddVertexLoop(path);
      }

      foreach (List<Vector2> hole in holesCopy) {
        if (hole.Count > 2)
          pslg.AddHole(hole);
      }

      if (pslg.vertices.Count == 0) {
        return;
      }

      // Calling API to triangulate geometry
      TriangleAPI triangle = new TriangleAPI();
      Polygon2D polygon = triangle.Triangulate(pslg, maxArea, maxAngle);

      // Parsing polygon to Unity Mesh. Generate simple vertex color at the same
      // time.
      Mesh uMesh = new Mesh();
      Vector3[] vertices = new Vector3[polygon.vertices.Length];

      for (int i = 0; i < polygon.vertices.Length; i++) {
        vertices[i] = polygon.vertices[i];
      }

      uMesh.vertices = vertices;
      uMesh.triangles = polygon.triangles;
      uMesh.name = "PolyMesh";

      // Postprocessing if component exists
      for (int i = 0; i < postprocessing.Count; i++) {
        if (postprocessing[i] != null)
          uMesh = postprocessing[i].Postprocess(uMesh, paths, holes);
      }

      GetComponent<MeshFilter>().mesh = uMesh;

      if (shouldGenerateCollision) {
        GenerateCollision();
      }
    }
  }
}