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

  [Serializable]
  public class Shape {
    public List<Vector2> vertices = new List<Vector2>();

    public string name = "New shape";

    public Vector3[] Vertices3DArray() {
      Vector3[] newList = new Vector3[vertices.Count];

      for (int i = 0; i < vertices.Count; i++) {
        newList[i] = vertices[i];
      }

      return newList;
    }
  }
}
