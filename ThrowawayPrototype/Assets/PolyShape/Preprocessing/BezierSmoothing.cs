/**
 * PolyShape
 * 
 * Created: 1-Feb-2016
 * Author(s): Flying_Banana
 */

namespace PolyShape {
  using System.Collections.Generic;
  using UnityEngine;

  /**
   * Uses standard bezier curve algorithm to smooth the given path points before
   * generating the mesh.
   */
  public class BezierSmoothing : Preprocessing {
    // The smoothing density. Higher density uses more points for smoothing.
    public int interpolatedPoints = 5;
    public bool smoothPaths = true;
    public bool smoothHoles = true;

    public override void Preprocess(ref List<List<Vector2>> paths, ref List<List<Vector2>> holes) {
      if (smoothPaths) {
        for (int i = 0; i < paths.Count; i++) {
          // We only want loops. Ignore shapes that can't form loops.
          if (paths[i].Count < 3)
            continue;

          Vector2[] ctrPts = GetCurveControlPoints(paths[i].ToArray());

          // Use the control points to interpolate a new spline.
          List<Vector2> splinedPath = new List<Vector2>();

          for (int j = 0; j < paths[i].Count; j++) {
            int afterJ = (j + 1) % paths[i].Count;
            splinedPath.Add(paths[i][j]);

            for (int k = 1; k < interpolatedPoints + 1; k++) {
              float t = k / (interpolatedPoints + 1.0f);
              Vector2 pt = Mathf.Pow(1 - t, 3) * paths[i][j];
              pt += 3 * Mathf.Pow(1 - t, 2) * t * ctrPts[2 * j];
              pt += 3 * (1 - t) * Mathf.Pow(t, 2) * ctrPts[2 * j + 1];
              pt += Mathf.Pow(t, 3) * paths[i][afterJ];

              splinedPath.Add(pt);
            }
          }

          paths[i] = splinedPath;
        }
      }

      if (smoothHoles) {
        for (int i = 0; i < holes.Count; i++) {
          // We only want loops. Ignore shapes that can't form loops.
          if (holes[i].Count < 3)
            continue;

          Vector2[] ctrPts = GetCurveControlPoints(holes[i].ToArray());

          // Use the control points to interpolate a new spline.
          List<Vector2> splinedPath = new List<Vector2>();

          for (int j = 0; j < holes[i].Count; j++) {
            int afterJ = (j + 1) % holes[i].Count;
            splinedPath.Add(holes[i][j]);

            for (int k = 1; k < interpolatedPoints + 1; k++) {
              float t = k / (interpolatedPoints + 1.0f);
              Vector2 pt = Mathf.Pow(1 - t, 3) * holes[i][j];
              pt += 3 * Mathf.Pow(1 - t, 2) * t * ctrPts[2 * j];
              pt += 3 * (1 - t) * Mathf.Pow(t, 2) * ctrPts[2 * j + 1];
              pt += Mathf.Pow(t, 3) * holes[i][afterJ];

              splinedPath.Add(pt);
            }
          }

          holes[i] = splinedPath;
        }
      }
    }

    /**
     * Interpolates the given set of points with bezier curves,
     *
     * Returns an array of control points in the order of segments. Uses wrap-
     * around technique to close the shape.
     */
    Vector2[] GetCurveControlPoints(Vector2[] pts) {
      // We first pad the points so that it "wraps around"
      int n = pts.Length + 5;
      Vector2[] wpts = new Vector2[n];

      for (int i = 2; i < n - 3; i++) {
        wpts[i] = pts[i - 2];
      }

      wpts[0] = pts[pts.Length - 2];
      wpts[1] = pts[pts.Length - 1];
      wpts[n - 3] = pts[0];
      wpts[n - 2] = pts[1];
      wpts[n - 1] = pts[2];

      n = wpts.Length - 1;

      // Calculate first Bezier control points
      // Right hand side vector
      float[] rhs = new float[n];

      // Set right hand side X values
      for (int i = 1; i < n - 1; ++i)
        rhs[i] = 4 * wpts[i].x + 2 * wpts[i + 1].x;

      rhs[0] = wpts[0].x + 2 * wpts[1].x;
      rhs[n - 1] = (8 * wpts[n - 1].x + wpts[n].x) / 2;
      
      // Get first control points X-values
      float[] x = GetFirstControlPoints(rhs);

      // Set right hand side Y values
      for (int i = 1; i < n - 1; ++i)
        rhs[i] = 4 * wpts[i].y + 2 * wpts[i + 1].y;

      rhs[0] = wpts[0].y + 2 * wpts[1].y;
      rhs[n - 1] = (8 * wpts[n - 1].y + wpts[n].y) / 2;
      
      // Get first control points Y-values
      float[] y = GetFirstControlPoints(rhs);

      // Fill output array
      Vector2[] controlPoints = new Vector2[pts.Length * 2];
      
      for (int i = 2; i < n - 2; i++) {
        // First control point
        controlPoints[2 * (i - 2)] = new Vector2(x[i], y[i]);
        // Second control point
        controlPoints[2 * (i - 2) + 1] = new Vector2(2 * wpts[i + 1].x - x[i + 1], 2 * wpts[i + 1].y - y[i + 1]);
      }

      return controlPoints;
    }

    float[] GetFirstControlPoints(float[] rhs) {
      int n = rhs.Length;
      float[] x = new float[n]; // Solution vector.
      float[] tmp = new float[n]; // Temp workspace.

      float b = 2;
      x[0] = rhs[0] / b;
      
      for (int i = 1; i < n; i++) {
        tmp[i] = 1 / b;
        b = (i < n - 1 ? 4.0f : 3.5f) - tmp[i];
        x[i] = (rhs[i] - x[i - 1]) / b;
      }
    
      for (int i = 1; i < n; i++)
        x[n - i - 1] -= tmp[n - i] * x[n - i]; // Backsubstitution.

      return x;
    }
  }
}
