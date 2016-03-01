using UnityEngine;
using UnityEngine.SceneManagement;
using System.Collections;

public class Goal : MonoBehaviour {

  void OnTriggerEnter2D(Collider2D other) {
    if (other.tag == "Player") {
      Reload.ResetGame();
    }
  }
}
