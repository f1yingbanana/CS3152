using UnityEngine;
using System.Collections.Generic;

[RequireComponent (typeof(Collider2D))]
public class Portal : MonoBehaviour {

  public Portal destination;

  public float turnDeg;

  void OnTriggerEnter2D(Collider2D other) {
    if (destination != null && destination.gameObject.activeInHierarchy && other.tag == "Player") {
      UnityStandardAssets._2D.PlatformerCharacter2D controller = other.GetComponent<UnityStandardAssets._2D.PlatformerCharacter2D>();

      if (!controller.CanFlip()) {
        return;
      }

      other.transform.position = destination.transform.position + (transform.position - other.transform.position);
      Rigidbody2D rb = other.GetComponent<Rigidbody2D>();
      rb.velocity = Quaternion.Euler(0, 0, turnDeg) * rb.velocity;
      controller.TurnWorld(turnDeg);
      controller.MarkAsFlipped();
    }
  }
}
