using UnityEngine;
using UnityEngine.SceneManagement;

public class Reload : MonoBehaviour {

  void OnTriggerExit2D(Collider2D other) {
    if (other.tag == "Player") {
      ResetGame();
    } else {
      Destroy(other.gameObject);
    }
  }

  public static void ResetGame() {
    Physics2D.gravity = new Vector2(0, -9.81f);
    SceneManager.LoadScene(0);
  }

  void Update() {
    if (Input.GetKeyUp(KeyCode.R)) {
      ResetGame();
    }
  }
}
