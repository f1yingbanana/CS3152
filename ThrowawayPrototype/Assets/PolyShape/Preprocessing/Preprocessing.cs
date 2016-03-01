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
   * The parent class for all preprocessing that goes with PolyShape. The 
   * script is called before the vertices are passed into Triangle for mesh
   * generation. Extend this class and implement the Preprocess method to
   * customize the vertices before they are passed in for mesh generation.
   */
  public abstract class Preprocessing : MonoBehaviour {
    // Implement this method to modify the paths before they are passed into
    // Triangle. The list of vectors represent a loop with each vertex connected
    // to the one before and after it. The first and last vertices are also
    // connected. Modify the paths and holes to change the vertex information
    // passed into Triangle.
    public abstract void Preprocess(ref List<List<Vector2>> paths, ref List<List<Vector2>> holes);
  }
}
