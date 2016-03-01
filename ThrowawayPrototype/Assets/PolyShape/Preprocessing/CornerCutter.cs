/**
 * PolyShape
 * 
 * Created: 2-Feb-2016
 * Author(s): Flying_Banana
 */

namespace PolyShape {
  using System.Collections.Generic;
  using UnityEngine;

  /**
   * Uses Chaikin's Corner Cutter algorithm to smooth the shape before building
   * the mesh.
   */
  public class CornerCutter : Preprocessing {
    // The smoothing iteration.
    public int iterations = 3;

    public bool smoothPaths = true;
    public bool smoothHoles = true;

    public override void Preprocess(ref List<List<Vector2>> paths, ref List<List<Vector2>> holes) {
      if (smoothPaths) {
        for (int i = 0; i < paths.Count; i++) {
          // We only want loops. Ignore shapes that can't form loops.
          int n = paths[i].Count;

          if (n < 3)
            continue;

          for (int j = 0; j < iterations; j++) {
            List<Vector2> smoothed = new List<Vector2>();

            for (int k = 0; k < n; k++) {
              // We cut corners! 1/4 of this corner then 3/4 of that one...
              Vector2 c1 = paths[i][k];
              Vector2 c2 = paths[i][(k + 1) % n];
              smoothed.Add(3 * c1 / 4 + c2 / 4);
              smoothed.Add(c1 / 4 + 3 * c2 / 4);
            }

            paths[i] = smoothed;
            n = smoothed.Count;
          }
        }
      }

      if (smoothHoles) {
        for (int i = 0; i < holes.Count; i++) {
          int n = holes[i].Count;

          if (n < 3)
            continue;

          for (int j = 0; j < iterations; j++) {
            List<Vector2> smoothed = new List<Vector2>();

            for (int k = 0; k < n; k++) {
              Vector2 c1 = holes[i][k];
              Vector2 c2 = holes[i][(k + 1) % n];
              smoothed.Add(3 * c1 / 4 + c2 / 4);
              smoothed.Add(c1 / 4 + 3 * c2 / 4);
            }

            holes[i] = smoothed;
            n = smoothed.Count;
          }
        }
      }
    }
  }
}
