/**
 * PolyShape
 * 
 * Created: 21-Jan-2016
 * Author(s): Flying_Banana
 */

namespace PolyShape {
  using UnityEngine;
  using UnityEditor;
  using System.Collections.Generic;

  [CustomEditor(typeof(PolyShape))]
  class PolyShapeEditor : Editor {
    // 0 - no action,
    // 1 - add vertex,
    // 2 - insert vertex,
    // 3 - delete vertex.
    int actionType;

    // The vertex that is being affected by the action.
    int actionVertexIndex;

    // For dragging vertices
    int isDirty;

    public override void OnInspectorGUI() {
      PolyShape pShape = target as PolyShape;

      GUIStyle customTitleStyle = EditorStyles.label;
      customTitleStyle.richText = true;
      GUIStyle customButtonStyle = EditorStyles.toolbarButton;
      customButtonStyle.richText = true;

      EditorGUILayout.BeginVertical(EditorStyles.helpBox);

      if (GUILayout.Button("<b><color=purple>Geometry</color></b>", customTitleStyle)) {
        pShape.isGeometryFolded = !pShape.isGeometryFolded;
      }

      if (!pShape.isGeometryFolded) {

        // Paths

        EditorGUILayout.LabelField("<b>Paths</b>", customTitleStyle);

        for (int i = 0; i < pShape.paths.Count; i++) {
          // Create a label here
          Shape shape = pShape.paths[i];

          EditorGUILayout.BeginHorizontal();
          shape.name = GUILayout.TextField(shape.name);

          if (GUILayout.Toggle(pShape.editingShape == shape, "Edit", customButtonStyle, GUILayout.Width(30))) {
            // Begin Edit Mode
            pShape.editingShape = shape;
            Tools.current = Tool.None;
            UnityEditorInternal.InternalEditorUtility.RepaintAllViews();
          } else if (pShape.editingShape == shape) {
            pShape.editingShape = null;
            UnityEditorInternal.InternalEditorUtility.RepaintAllViews();
          }

          if (GUILayout.Button("Del", customButtonStyle, GUILayout.Width(30))) {
            // Delete Element
            Undo.RecordObject(pShape, "Delete path");
            pShape.paths.RemoveAt(i);
            pShape.editingShape = null;
            if (pShape.shouldAutoRecalculate) {
              pShape.RebuildMesh();
            }
            UnityEditorInternal.InternalEditorUtility.RepaintAllViews();
          }

          EditorGUILayout.EndHorizontal();
        }

        if (pShape.paths.Count > 0)
          EditorGUILayout.Space();

        // Create new
        if (GUILayout.Button("New Path")) {
          Undo.RecordObject(pShape, "New path");
          Shape newShape = new Shape();
          pShape.paths.Add(newShape);
          newShape.name = "Path " + pShape.paths.Count;
          pShape.editingShape = newShape;
          UnityEditorInternal.InternalEditorUtility.RepaintAllViews();
        }

        EditorGUILayout.Space();

        // Holes

        EditorGUILayout.LabelField("<b>Holes</b>", customTitleStyle);

        for (int i = 0; i < pShape.holes.Count; i++) {
          // Create a label here
          Shape shape = pShape.holes[i];

          EditorGUILayout.BeginHorizontal();
          shape.name = GUILayout.TextField(shape.name);

          if (GUILayout.Toggle(pShape.editingShape == shape, "Edit", customButtonStyle, GUILayout.Width(30))) {
            // Begin Edit Mode
            pShape.editingShape = shape;
            Tools.current = Tool.None;
            UnityEditorInternal.InternalEditorUtility.RepaintAllViews();
          } else if (pShape.editingShape == shape) {
            pShape.editingShape = null;
            UnityEditorInternal.InternalEditorUtility.RepaintAllViews();
          }

          if (GUILayout.Button("Del", customButtonStyle, GUILayout.Width(30))) {
            // Delete Element
            Undo.RecordObject(pShape, "Delete hole");
            pShape.holes.RemoveAt(i);
            pShape.editingShape = null;
            if (pShape.shouldAutoRecalculate) {
              pShape.RebuildMesh();
            }
            UnityEditorInternal.InternalEditorUtility.RepaintAllViews();
          }

          EditorGUILayout.EndHorizontal();
        }

        // Create new
        if (pShape.holes.Count > 0)
          EditorGUILayout.Space();

        if (GUILayout.Button("New Hole")) {
          Undo.RecordObject(pShape, "New hole");
          Shape newShape = new Shape();
          pShape.holes.Add(newShape);
          newShape.name = "Hole " + pShape.holes.Count;
          pShape.editingShape = newShape;
          UnityEditorInternal.InternalEditorUtility.RepaintAllViews();
        }
      }

      EditorGUILayout.EndVertical();

      // Mesh generation
      EditorGUILayout.BeginVertical(EditorStyles.helpBox);

      if (GUILayout.Button("<b><color=purple>Mesh Editing and Generation</color></b>", customTitleStyle)) {
        pShape.isMeshFolded = !pShape.isMeshFolded;
      }

      if (!pShape.isMeshFolded) {
        // First, we look at Unity settings
        EditorGUILayout.LabelField("<b>Unity Settings</b>", customTitleStyle);

        Color oldEditColor = pShape.editingColor;
        Color oldNormalColor = pShape.normalColor;
        float oldSize = pShape.vertexSize;
        pShape.editingColor = EditorGUILayout.ColorField("Edit color", pShape.editingColor);
        pShape.normalColor = EditorGUILayout.ColorField("Normal color", pShape.normalColor);
        pShape.vertexSize = EditorGUILayout.Slider("Node size", pShape.vertexSize, 0, 0.2f);

        if (pShape.editingColor != oldEditColor || pShape.normalColor != oldNormalColor || pShape.vertexSize != oldSize) {
          UnityEditorInternal.InternalEditorUtility.RepaintAllViews();
        }

        pShape.shouldGenerateCollision = EditorGUILayout.Toggle("Generate collider", pShape.shouldGenerateCollision);

        // Check collider consistency
        if (pShape.shouldGenerateCollision && pShape.GetComponent<PolygonCollider2D>() == null) {
          EditorGUILayout.HelpBox("Collision generation requires a PolygonCollider2D component.", MessageType.Warning);
        }

        pShape.shouldAutoRecalculate = EditorGUILayout.Toggle("Auto-rebuild", pShape.shouldAutoRecalculate);

        if (!pShape.shouldAutoRecalculate) {
          EditorGUILayout.HelpBox("When auto-rebuild is turned off, any changes to the mesh will not be visible until Rebuild Mesh button is pressed.", MessageType.Info);
        }

        EditorGUILayout.Space();

        // Now we configure triangle settings
        EditorGUILayout.LabelField("<b>Triangle Settings</b>", customTitleStyle);

        float oldAngle = pShape.maxAngle;
        float oldArea = pShape.maxArea;
        pShape.maxAngle = EditorGUILayout.Slider("Min angle", pShape.maxAngle, 0, 33);
        pShape.maxArea = Mathf.Max(0.0001f, EditorGUILayout.DelayedFloatField("Max area", pShape.maxArea));

        if (pShape.shouldAutoRecalculate && (pShape.maxAngle != oldAngle || pShape.maxArea != oldArea)) {
          pShape.RebuildMesh();
        }

        if (GUILayout.Button("Rebuild Mesh")) {
          // Tell the shape to rebuild mesh.
          pShape.RebuildMesh();
        }
      }
      EditorGUILayout.EndVertical();


      // Pre and Postprocessing
      EditorGUILayout.BeginVertical(EditorStyles.helpBox);

      if (GUILayout.Button("<b><color=purple>Preprocessing and Postprocessing</color></b>", customTitleStyle)) {
        pShape.isProcessingFolded = !pShape.isProcessingFolded;
      }

      if (!pShape.isProcessingFolded) {
        // Preprocessing
        EditorGUILayout.LabelField("<b>Preprocessing scripts</b>", customTitleStyle);

        for (int i = 0; i < pShape.preprocessing.Count; i++) {
          EditorGUILayout.BeginHorizontal();
          SerializedProperty script = serializedObject.FindProperty("preprocessing").GetArrayElementAtIndex(i);
          EditorGUILayout.PropertyField(script, GUIContent.none);

          if (GUILayout.Button("Up", customButtonStyle, GUILayout.Width(25)) && i != 0) {
            // Delete Element
            Undo.RecordObject(pShape, "Reorder script");
            Preprocessing meshPp = pShape.preprocessing[i];
            pShape.preprocessing.RemoveAt(i);
            pShape.preprocessing.Insert(i - 1, meshPp);

            if (pShape.shouldAutoRecalculate) {
              pShape.RebuildMesh();
            }
          }

          if (GUILayout.Button("Down", customButtonStyle, GUILayout.Width(34)) && i != pShape.preprocessing.Count - 1) {
            // Delete Element
            Undo.RecordObject(pShape, "Reorder script");
            Preprocessing meshPp = pShape.preprocessing[i];
            pShape.preprocessing.RemoveAt(i);
            pShape.preprocessing.Insert(i + 1, meshPp);

            if (pShape.shouldAutoRecalculate) {
              pShape.RebuildMesh();
            }
          }

          if (GUILayout.Button("Del", customButtonStyle, GUILayout.Width(27))) {
            // Delete Element
            Undo.RecordObject(pShape, "Remove script");
            pShape.preprocessing.RemoveAt(i);

            if (pShape.shouldAutoRecalculate) {
              pShape.RebuildMesh();
            }
          }

          EditorGUILayout.EndHorizontal();
        }

        serializedObject.ApplyModifiedProperties();

        // Create new
        if (pShape.preprocessing.Count > 0)
          EditorGUILayout.Space();

        if (GUILayout.Button("Add Script")) {
          Undo.RecordObject(pShape, "Add script");
          pShape.preprocessing.Add(null);
        }

        // Postprocessing

        EditorGUILayout.LabelField("<b>Postprocessing scripts</b>", customTitleStyle);

        for (int i = 0; i < pShape.postprocessing.Count; i++) {
          EditorGUILayout.BeginHorizontal();
          SerializedProperty script = serializedObject.FindProperty("postprocessing").GetArrayElementAtIndex(i);
          EditorGUILayout.PropertyField(script, GUIContent.none);

          if (GUILayout.Button("Up", customButtonStyle, GUILayout.Width(25)) && i != 0) {
            // Delete Element
            Undo.RecordObject(pShape, "Reorder script");
            Postprocessing meshPp = pShape.postprocessing[i];
            pShape.postprocessing.RemoveAt(i);
            pShape.postprocessing.Insert(i - 1, meshPp);

            if (pShape.shouldAutoRecalculate) {
              pShape.RebuildMesh();
            }
          }

          if (GUILayout.Button("Down", customButtonStyle, GUILayout.Width(34)) && i != pShape.postprocessing.Count - 1) {
            // Delete Element
            Undo.RecordObject(pShape, "Reorder script");
            Postprocessing meshPp = pShape.postprocessing[i];
            pShape.postprocessing.RemoveAt(i);
            pShape.postprocessing.Insert(i + 1, meshPp);

            if (pShape.shouldAutoRecalculate) {
              pShape.RebuildMesh();
            }
          }

          if (GUILayout.Button("Del", customButtonStyle, GUILayout.Width(27))) {
            // Delete Element
            Undo.RecordObject(pShape, "Remove script");
            pShape.postprocessing.RemoveAt(i);

            if (pShape.shouldAutoRecalculate) {
              pShape.RebuildMesh();
            }
          }

          EditorGUILayout.EndHorizontal();
        }

        serializedObject.ApplyModifiedProperties();

        // Create new
        if (pShape.postprocessing.Count > 0)
          EditorGUILayout.Space();

        if (GUILayout.Button("Add Script")) {
          Undo.RecordObject(pShape, "Add script");
          pShape.postprocessing.Add(null);
        }
      }

      EditorGUILayout.EndVertical();
    }

    void EndEditing() {
      PolyShape pShape = target as PolyShape;
      pShape.editingShape = null;
    }

    void UndoRedoRedraw() {
      PolyShape pShape = target as PolyShape;
      if (pShape.shouldAutoRecalculate && pShape != null) {
        pShape.RebuildMesh();
      }
    }

    void OnSceneGUI() {
      PolyShape pShape = target as PolyShape;
      
      // Support undo redo - we take off then add because we only want to keep
      // one instance;
      Undo.undoRedoPerformed -= UndoRedoRedraw;
      Selection.selectionChanged -= EndEditing;

      if (Selection.activeGameObject == pShape.gameObject) {
        Undo.undoRedoPerformed += UndoRedoRedraw;
        Selection.selectionChanged += EndEditing;
      }

      // How to incorporate auto rebuild?
      // We can wait until a new point is added or a drag has finished.

      // First we draw all the normal paths - both paths and holes.
      // Paths and holes - no edit means they can be drawn together
      Handles.color = pShape.normalColor;
      List<Shape> combined = new List<Shape>(pShape.paths);
      combined.AddRange(pShape.holes);

      foreach (Shape shape in combined) {
        // Skip if we are editing this shape
        if (shape == pShape.editingShape) {
          continue;
        }

        // Obtain world position for all the vertices
        Vector3[] vertices = shape.Vertices3DArray();

        for (int j = 0; j < vertices.Length; j++) {
          vertices[j] = pShape.transform.TransformPoint(vertices[j]);
        }

        // Draw lines that go through all of them in world (screen) space.
        Handles.DrawAAPolyLine(2, vertices);

        // The endpoints needs to be drawn again. Since this is not an active
        // line, we should always be able to draw them. But for sanity we add
        // the check.
        if (vertices.Length > 2) {
          Vector3[] gap = new Vector3[2];
          gap[0] = vertices[vertices.Length - 1];
          gap[1] = vertices[0];
          Handles.DrawAAPolyLine(2, gap);
        }

        // Also draw a dot for each of the points.
        for (int j = 0; j < vertices.Length; j++) {
          Handles.DotCap(0, vertices[j], Quaternion.identity, HandleUtility.GetHandleSize(vertices[j]) * pShape.vertexSize);
        }
      }

      if (!pShape.paths.Contains(pShape.editingShape) && !pShape.holes.Contains(pShape.editingShape)) {
        return;
      } else if (Tools.current != Tool.None) {
        // User selected some other tool. End editing!
        pShape.editingShape = null;
        return;
      }

      // Finally we depict the cursor as a point. This point disappears around
      // handles (vertices), and we draw lines about the cursor when near lines.

      // We have several cases regarding the cursor: 
      // If there is no point, the cursor snaps to nothing and a single point 
      // is drawn.
      // If there are points, then the cursor snaps to the handle, and we draw
      // nothing.
      // If there are lines, we check distance against each line. If below
      // threshold (zoom-dependent), we draw two dotted lines (insertion).
      // If shift is held down, we want to delete points. Overwrite the nearest
      // point in red.
      // Finally, connect cursor to closest endpoint. Insertion mode.

      // We need:
      // Define a point in action
      // Define the action being performed.
      // Calculate distance between cursor to all points
      // Calculate distance between cursor to all line segments

      // Various event flags, like mouse position, whether shift is down
      Ray mRay = HandleUtility.GUIPointToWorldRay(Event.current.mousePosition);
      Vector2 mouseWorldPos = mRay.GetPoint(mRay.origin.z);
      bool isDeleteMode = Event.current.shift;

      // The path that needs to be edited has to be drawn differently, as well
      // as to support editing.
      Handles.color = pShape.editingColor;
      Vector3[] editingVertices = pShape.editingShape.Vertices3DArray();

      for (int j = 0; j < editingVertices.Length; j++) {
        editingVertices[j] = pShape.transform.TransformPoint(editingVertices[j]);
      }

      // Calculating vertices/edges distance information
      float[] vertexDistances = new float[editingVertices.Length];
      float minVertexDist = Mathf.Infinity;
      int minVertexIndex = 0;
      float[] edgeDistances = new float[Mathf.Max(0, editingVertices.Length - 1)];
      float minEdgeDist = Mathf.Infinity;
      int minEdgeIndex = 0;

      for (int i = 0; i < editingVertices.Length; i++) {
        // Convert this distance to screenspace distance
        Vector2 startPt = HandleUtility.WorldToGUIPoint(editingVertices[i]);
        vertexDistances[i] = Vector2.Distance(Event.current.mousePosition, startPt);

        if (vertexDistances[i] < minVertexDist) {
          minVertexDist = vertexDistances[i];
          minVertexIndex = i;
        }

        // Now we calculate edge distance. We use log iteration approach
        if (i + 1 < editingVertices.Length) {
          Vector2 endPt = HandleUtility.WorldToGUIPoint(editingVertices[i + 1]);
          int iterations = (int)Mathf.Log(Vector2.Distance(startPt, endPt)) + 1;

          for (int j = 1; j <= iterations; j++) {
            float dist1 = Vector2.Distance(startPt, Event.current.mousePosition);
            float dist2 = Vector2.Distance(endPt, Event.current.mousePosition);

            if (dist2 > dist1) {
              endPt = new Vector2((startPt.x + endPt.x) / 2, (startPt.y + endPt.y) / 2);
            } else {
              startPt = new Vector2((startPt.x + endPt.x) / 2, (startPt.y + endPt.y) / 2);
            }
          }

          edgeDistances[i] = Vector2.Distance(startPt, Event.current.mousePosition);

          if (edgeDistances[i] < minEdgeDist) {
            minEdgeDist = edgeDistances[i];
            minEdgeIndex = i;
          }
        }
      }

      // Draw lines that go through all of them in world (screen) space.
      Handles.DrawAAPolyLine(4, editingVertices);

      // The endpoints needs to be drawn as handles. In addition, we want to
      // render the closest end point red if shift is held down.
      for (int j = 0; j < editingVertices.Length; j++) {
        float handleSize = HandleUtility.GetHandleSize(editingVertices[j]) * pShape.vertexSize;
        if (isDeleteMode && j == minVertexIndex) {
          Color prevColor = Handles.color;
          Handles.color = Color.red;
          Handles.DotCap(0, editingVertices[j], Quaternion.identity, handleSize);
          Handles.color = prevColor;
        } else {
          Vector2 newPos = Handles.FreeMoveHandle(editingVertices[j], Quaternion.identity, handleSize, Vector3.zero, Handles.DotCap);
          if (newPos != (Vector2)editingVertices[j]) {
            Undo.RecordObject(pShape, "Move vertex");
            pShape.editingShape.vertices[j] = pShape.transform.InverseTransformPoint(newPos);
            isDirty = 75;
          } else if (isDirty > 0) {
            // We wait until the dragging stopped to rebuild
            isDirty = Mathf.Max(1, isDirty - 1);

            if (isDirty == 1) {
              isDirty = 0;

              if (pShape.shouldAutoRecalculate) {
                pShape.RebuildMesh();
              }
            }
          }
        }
      }

      if (editingVertices.Length > 2) {
        Handles.color = Color.yellow;
        Vector3[] gap = new Vector3[2];
        gap[0] = editingVertices[editingVertices.Length - 1];
        gap[1] = editingVertices[0];
        Handles.DrawAAPolyLine(4, gap);
      }

      // Now we draw the cursor and attached lines. We avoid drawing them if
      // we are deleting points or we are too close to a vertex (say, 5 pixels)
      // There is an associated state with each mouse state. If mouse is normal,
      // we have the following.

      int controlID = GUIUtility.GetControlID(FocusType.Passive);

      if (Event.current.GetTypeForControl(controlID) == EventType.MouseDown) {
        GUIUtility.hotControl = controlID;
        if (isDeleteMode) {
          actionType = 3;
          actionVertexIndex = minVertexIndex;
        } else if (minEdgeDist < 25) {
          // We are dragging along an edge
          actionType = 2;
          actionVertexIndex = minEdgeIndex;
        } else {
          actionType = 1;

          if (editingVertices.Length >= 2) {
            float d1 = Vector2.Distance(mouseWorldPos, editingVertices[0]);
            float d2 = Vector2.Distance(mouseWorldPos, editingVertices[editingVertices.Length - 1]);
            actionVertexIndex = d1 < d2? 0 : editingVertices.Length - 1;
          } else {
            actionVertexIndex = 0;
          }
        }
      } else if (Event.current.GetTypeForControl(controlID) == EventType.MouseUp) {
        GUIUtility.hotControl = 0;
        if (actionType == 1) {
          // Creating a vertex at position
          Undo.RecordObject(pShape, "Insert vertex");
          if (actionVertexIndex == 0) {
            pShape.editingShape.vertices.Insert(0, pShape.transform.InverseTransformPoint(mouseWorldPos));
          } else {
            pShape.editingShape.vertices.Add(pShape.transform.InverseTransformPoint(mouseWorldPos));
          }
        } else if (actionType == 2) {
          // Inserting a vertex between edges
          Undo.RecordObject(pShape, "Insert vertex");
          pShape.editingShape.vertices.Insert(actionVertexIndex + 1, pShape.transform.InverseTransformPoint(mouseWorldPos));
        } else if (actionType == 3) {
          if (isDeleteMode) {
            Undo.RecordObject(pShape, "Delete vertex");
            pShape.editingShape.vertices.RemoveAt(actionVertexIndex);
          }
        }

        if (pShape.shouldAutoRecalculate) {
          if (pShape.shouldGenerateCollision) {
            Undo.RecordObject(pShape.GetComponent<PolygonCollider2D>(), "Generate collision");
          }
          pShape.RebuildMesh();
        }

        actionType = 0;
      } else {
        if (actionType == 0) {
          if (!isDeleteMode && minVertexDist > 10) {
            // If the closest edge is less than a threshold, we attach to there.
            // Otherwise, attach to the closest endpoint.
            Handles.color = Color.yellow;
            if (minEdgeDist < 25) {
              // We are dragging.
              Vector3[] pts = new Vector3[3];
              pts[0] = editingVertices[minEdgeIndex];
              pts[1] = mouseWorldPos;
              pts[2] = editingVertices[minEdgeIndex + 1];
              Handles.DrawAAPolyLine(4, pts);
            } else if (editingVertices.Length >= 2) {
              float d1 = Vector2.Distance(mouseWorldPos, editingVertices[0]);
              float d2 = Vector2.Distance(mouseWorldPos, editingVertices[editingVertices.Length - 1]);
              Vector3[] pts = new Vector3[2];
              pts[0] = mouseWorldPos;

              if (d1 < d2) {
                pts[1] = editingVertices[0];
              } else {
                pts[1] = editingVertices[editingVertices.Length - 1];
              }

              Handles.DrawAAPolyLine(4, pts);
            } else if (editingVertices.Length == 1) {
              Vector3[] pts = new Vector3[2];
              pts[0] = mouseWorldPos;
              pts[1] = editingVertices[0];
              Handles.DrawAAPolyLine(4, pts);
            }

            Color cursorColor = pShape.editingColor;
            cursorColor.a = 0.5f;
            Handles.color = cursorColor;
            Handles.DotCap(0, mouseWorldPos, Quaternion.identity, HandleUtility.GetHandleSize(mouseWorldPos) * pShape.vertexSize);
          }
        } else if (actionType == 1 && editingVertices.Length > 0) {
          // We just want to draw a single line.
          GUIUtility.hotControl = 0;
          Handles.color = Color.yellow;
          Vector3[] pts = new Vector3[2];
          pts[0] = mouseWorldPos;
          pts[1] = editingVertices[actionVertexIndex];
          Handles.DrawAAPolyLine(4, pts);
          Color cursorColor = pShape.editingColor;
          cursorColor.a = 0.5f;
          Handles.color = cursorColor;
          Handles.DotCap(0, mouseWorldPos, Quaternion.identity, HandleUtility.GetHandleSize(mouseWorldPos) * pShape.vertexSize);
        } else if (actionType == 2) {
          // We just want to draw two lines.
          GUIUtility.hotControl = 0;
          Handles.color = Color.yellow;
          Vector3[] pts = new Vector3[3];
          pts[0] = editingVertices[actionVertexIndex];
          pts[1] = mouseWorldPos;
          pts[2] = editingVertices[actionVertexIndex + 1];
          Handles.DrawAAPolyLine(4, pts);
          Color cursorColor = pShape.editingColor;
          cursorColor.a = 0.5f;
          Handles.color = cursorColor;
          Handles.DotCap(0, mouseWorldPos, Quaternion.identity, HandleUtility.GetHandleSize(mouseWorldPos) * pShape.vertexSize);
        }
      }

      // Finally, some useful keys
      if (Event.current.keyCode == KeyCode.Escape) {
        pShape.editingShape = null;
      }
    }
  }
}
