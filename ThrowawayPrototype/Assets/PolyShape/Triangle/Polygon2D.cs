/**
 * Illuminated
 * 
 * Unity API for Triangle adapted from:
 * http://tothemathmos.com/2013/05/17/advanced-triangulation-in-unity.html
 */

using UnityEngine;
using System.Collections;

public class Polygon2D {

    public int[] triangles;
    public Vector2[] vertices;

    public Polygon2D(int[] triangle, Vector2[] vertices)
    {
        this.triangles = triangle;
        this.vertices = vertices;
    }
}