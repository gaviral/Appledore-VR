using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using UnityStandardAssets.Characters.FirstPerson;

public class CommandReceiver : MonoBehaviour {

	public float movementSpeed = 2f;
	public string parserClass = "com.vroneinc.vrone.CommandParser";

	private PlayerController playerController;
	private static readonly float ZERO_TOLERANCE = 0.005f;

	// Use this for initialization
	void Start () {
		// TODO this might have to change to whatever script will have a Move() function for the character
		playerController = GetComponent<PlayerController> ();
	}

	void FixedUpdate() {
		// read commands from the android part
		AndroidJavaClass pluginClass = new AndroidJavaClass(parserClass);
		int x = pluginClass.CallStatic<char>("getCurrentX");
		int y = pluginClass.CallStatic<char>("getCurrentY");
		bool stickButtonPressed = pluginClass.CallStatic<bool> ("getStickButton");
		bool leftButtonPressed = pluginClass.CallStatic<bool> ("getLeftButton");
		bool rightButtonPressed = pluginClass.CallStatic<bool> ("getRightButton");
		bool forwardButtonPressed = pluginClass.CallStatic<bool> ("getForwardButton");

		// normalize the x and y from being 0-1023 to being from -1 to 1
		float xNorm = (((float)x) / 1023 - 0.5f) * 2f;
		float yNorm = (((float)y) / 1023 - 0.5f) * 2f;
		UpdateMovement (xNorm, yNorm);

		// Delegate button presses
		if (stickButtonPressed) {
			OnStickButtonPressed ();
		}
		if (leftButtonPressed) {
			OnLeftButtonPressed ();
		}
		if (rightButtonPressed) {
			OnRightButtonPressed ();
		}
		if (forwardButtonPressed) {
			OnForwardButtonPressed ();
		}
			
	}

	void UpdateMovement(float x, float y) {
		Debug.Log ("x value: " + x);
		Debug.Log ("y value: " + y);

		Vector2 dir = new Vector2 (x, y);
		float speed = dir.magnitude * movementSpeed;
		if (dir.magnitude > ZERO_TOLERANCE) {
			playerController.Move (dir, speed);
		}
	}

	void OnStickButtonPressed() {
		Debug.Log ("Stick button pressed");
		playerController.Jump ();
	}

	void OnLeftButtonPressed() {
		Debug.Log ("Left button pressed");
		playerController.Jump ();
	}

	void OnRightButtonPressed() {
		Debug.Log ("Right button pressed");
		playerController.Jump ();
	}

	void OnForwardButtonPressed() {
		Debug.Log ("Forward button pressed");
		playerController.Jump ();
	}
}
