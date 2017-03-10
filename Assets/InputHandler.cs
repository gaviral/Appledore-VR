using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InputHandler : MonoBehaviour {
	public KeyCode backButton;

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		if (Input.GetKeyDown (backButton)) {
			// kill the activity if the back button is pressed
			Application.Quit();
		}
	}
}
